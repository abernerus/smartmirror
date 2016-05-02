package com.bernerus.smartmirror.rest;

import com.bernerus.smartmirror.controller.VasttrafikController;
import com.bernerus.smartmirror.controller.VasttrafikWebSocketHandler;
import com.bernerus.smartmirror.model.ApplicationState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by andreas on 25/12/15.
 *
 */
@Controller
public class EyeSensorRestHandler {
  private static final Logger LOG = LoggerFactory.getLogger(EyeSensorRestHandler.class);
  LocalDateTime lastMovement;

  ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
  ScheduledFuture<?> future;

  @Autowired
  VasttrafikWebSocketHandler vasttrafikWebSocketHandler;

  @Autowired
  RestTemplate restTemplate;

  @Autowired
  ApplicationState applicationState;

  @RequestMapping("/reportmovement")
  public @ResponseBody String reportMovement() {
    LOG.debug("Movement detected!");
    this.lastMovement = LocalDateTime.now();
    String response = callMirror("on");

    if(future != null) {
      LOG.info("Cancelling scheduled mirror shutdown!");
      future.cancel(false);
    }
    int timeoutMinutes = 30; //Default time before mirror turn off
    int currentHour = LocalDateTime.now().getHour();
    if(currentHour >= 23 || (currentHour >= 0 && currentHour < 5)) {
      timeoutMinutes = 5;
    }
    LOG.info("Mirror monitor shutdown scheduled to " + LocalDateTime.now().plusMinutes(timeoutMinutes));
    future = executor.schedule(() -> callMirror("off"), timeoutMinutes, TimeUnit.MINUTES);

    return response;
  }

  private String callMirror(final String onOrOff) {
    //Doesn't seem to work, dont get it....
//    if(applicationState.isScreenSleeps() == false && "on".equals(onOrOff)) {
//      return "Ignored";
//    }

    if("off".equals(onOrOff)) {
      applicationState.setScreenSleeps(true);
    } else {
      applicationState.setScreenSleeps(false);
      vasttrafikWebSocketHandler.requestTransportsNow();
    }

    String url = "http://192.168.0.18:5000/" + onOrOff;
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.TEXT_PLAIN));
    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(new LinkedMultiValueMap<>(), headers);
    ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
    LOG.info(response.toString());
    return response.getBody();
  }

}
