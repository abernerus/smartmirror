package com.bernerus.smartmirror.controller;

import com.bernerus.smartmirror.model.tvservice.TvServiceCommand;
import com.bernerus.smartmirror.model.tvservice.TvServiceCommandResponse;
import com.bernerus.smartmirror.model.tvservice.TvServiceStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by andreas on 2017-06-16.
 */
@Controller
public class TvServiceController {
    private static final Logger LOG = LoggerFactory.getLogger(TvServiceController.class);
    private static final String TVSERVICE = "tvservice";
    @Autowired
    RestTemplate restTemplate;
    @Value("${mirror.host}")
    private String smartMirrorHost;
    @Value("${mirror.port}")
    private String smartMirrorPort;
    @Value("${mirror.external}")
    private Boolean externalMirror;

    public TvServiceStatus getStatus() {
        TvServiceCommandResponse response = callTVService(TvServiceCommand.STATUS);
        return parseStatusResponse(response);
    }

    public TvServiceCommandResponse getStatusRaw() {
        return callTVService(TvServiceCommand.STATUS);
    }

    public TvServiceCommandResponse turnOff() {
        return callTVService(TvServiceCommand.TURN_OFF);
    }

    public TvServiceCommandResponse turnOn() {
        return callTVService(TvServiceCommand.TURN_ON);
    }

    private TvServiceStatus parseStatusResponse(TvServiceCommandResponse response) {
        return TvServiceStatus.fromBoolean(response.getResponseLine().stream().noneMatch(lineContainsOff()));
    }

    private Predicate<String> lineContainsOff() {
        return line -> line.contains("[TV is off]");
    }

    private TvServiceCommandResponse callTVService(TvServiceCommand command) {
        if (externalMirror) {
            return callExternalTvService(command);
        }
        return callLocalTVService(command);
    }

    private TvServiceCommandResponse callLocalTVService(TvServiceCommand command) {
        BufferedReader tvServiceReader = null;
        try {
            Process tvServiceProcess = new ProcessBuilder(TVSERVICE, command.getValue()).start();
            tvServiceReader = new BufferedReader(new InputStreamReader(tvServiceProcess.getInputStream()));
            List<String> output = tvServiceReader.lines().collect(Collectors.toList());
            tvServiceProcess.waitFor();
            return TvServiceCommandResponse.success(output);
        } catch (IOException | InterruptedException e) {
            final String error = "Could not execute command";
            LOG.error(error);
            return TvServiceCommandResponse.failed(error);
        } finally {
            if (tvServiceReader != null) {
                try {
                    tvServiceReader.close();
                } catch (IOException e) {
                    LOG.error("Could not close tvServiceReader...");
                }
            }
        }
    }

    private TvServiceCommandResponse callExternalTvService(TvServiceCommand command) {
        String commandString;
        switch (command) {
            case STATUS:
                commandString = "status";
                break;
            case TURN_OFF:
                commandString = "off";
                break;
            default:
            case TURN_ON:
                commandString = "on";
                break;
        }

        String url = "http://" + smartMirrorHost + ":" + smartMirrorPort + "/" + commandString;
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.TEXT_PLAIN));
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(new LinkedMultiValueMap<>(), headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
        LOG.info(response.toString());
        String responseBody = response.getBody();
        return TvServiceCommandResponse.success(responseBody);
    }
}
