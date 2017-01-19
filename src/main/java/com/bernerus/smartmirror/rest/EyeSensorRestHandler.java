package com.bernerus.smartmirror.rest;

import com.bernerus.smartmirror.controller.WebSocketHandler;
import com.bernerus.smartmirror.dto.mirror.MirrorStatus;
import com.bernerus.smartmirror.dto.spotify.proxy.NowPlaying;
import com.bernerus.smartmirror.model.ApplicationState;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by andreas on 25/12/15.
 */
@Controller
public class EyeSensorRestHandler {
  private static final Logger LOG = LoggerFactory.getLogger(EyeSensorRestHandler.class);
  LocalDateTime lastMovement;

  ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
  ScheduledFuture<?> future;

  @Autowired
  WebSocketHandler webSocketHandler;

  @Autowired
  RestTemplate restTemplate;

  @Autowired
  ApplicationState applicationState;


  @Value("${mirror.host}")
  private String smartMirrorHost;

  @Value("${mirror.port}")
  private String smartMirrorPort;

  @PostConstruct
  public void postConstruct() {
    //At startup, report movement to force screen to be turned on and status set to not sleeping!
    this.reportMovement();
  }

  @RequestMapping("/reportmovement")
  public
  @ResponseBody
  String reportMovement() {
    LOG.debug("Movement detected!");
    this.lastMovement = LocalDateTime.now();
    String response = callMirror("on");

    cancelFuture("Cancelling scheduled mirror shutdown!");
    int timeoutMinutes = 30; //Default time before mirror turn off
    int currentHour = LocalDateTime.now().getHour();
    if (currentHour >= 23 || (currentHour >= 0 && currentHour < 5)) {
      timeoutMinutes = 5;
    }
    LOG.info("Mirror monitor shutdown scheduled to " + LocalDateTime.now().plusMinutes(timeoutMinutes));
    future = executor.schedule(() -> callMirror("off"), timeoutMinutes, TimeUnit.MINUTES);

    return response;
  }

  @RequestMapping("/reportnomovement")
  public
  @ResponseBody
  String reportNoMovement() {
    LOG.debug("Forcing no moment timeout!");
    String response = callMirror("off");
    cancelFuture("Cancelling scheduled mirror shutdown, since screen has been forced off just now!");
    return response;
  }

  @RequestMapping("/forcemovement")
  public
  @ResponseBody
  String forceMovement() {
    LOG.debug("Forcing moment!");
    return callMirror("on", true);
  }

  @RequestMapping("/screenstatus")
  public
  @ResponseBody
  String screenstatus() {
    return String.format("Sleeps = %b", applicationState.screenSleeps());
  }

  private String callMirror(final String onOrOff) {
    return callMirror(onOrOff, false);
  }

  private String callMirror(final String onOrOff, final boolean force) {
    MirrorStatus mirrorStatus = restTemplate.getForObject("http://" + smartMirrorHost + ":" + smartMirrorPort + "/status", MirrorStatus.class);

    if (!mirrorStatus.isScreenSleeps() && "on".equals(onOrOff) && !force) {
      LOG.info("Detected movement but screen is already on. Ignoring...");
      return "Ignored";
    }

    if ("off".equals(onOrOff)) {
      applicationState.setScreenSleeps(true);
    } else {
      applicationState.setScreenSleeps(false);
      CompletableFuture.runAsync(() -> webSocketHandler.requestTransportsNow());
      CompletableFuture.runAsync(() -> webSocketHandler.requestWeatherNow());
    }

    String url = "http://" + smartMirrorHost + ":" + smartMirrorPort + "/" + onOrOff;
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.TEXT_PLAIN));
    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(new LinkedMultiValueMap<>(), headers);
    ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
    LOG.info(response.toString());
    return response.getBody();
  }

  private void cancelFuture(String message) {
    if (future != null && !future.isDone()) {
      LOG.info(message);
      future.cancel(false);
    }
  }

}
