package com.bernerus.smartmirror.rest;

import com.bernerus.smartmirror.config.EyeSensorActive;
import com.bernerus.smartmirror.controller.TvServiceController;
import com.bernerus.smartmirror.controller.WebSocketHandler;
import com.bernerus.smartmirror.model.ApplicationState;
import com.bernerus.smartmirror.model.tvservice.TvServiceCommand;
import com.bernerus.smartmirror.model.tvservice.TvServiceCommandResponse;
import com.bernerus.smartmirror.model.tvservice.TvServiceStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static com.bernerus.smartmirror.model.tvservice.TvServiceCommand.TURN_OFF;
import static com.bernerus.smartmirror.model.tvservice.TvServiceCommand.TURN_ON;

/**
 * Created by andreas on 25/12/15.
 */
@Controller
@Conditional(EyeSensorActive.class)
public class DisplayRestHandler {
  private static final Logger LOG = LoggerFactory.getLogger(DisplayRestHandler.class);
  LocalDateTime lastMovement;

  ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
  ScheduledFuture<?> future;

  @Autowired
  WebSocketHandler webSocketHandler;

  @Autowired
  TvServiceController tvServiceController;

  @Autowired
  ApplicationState applicationState;

  @PostConstruct
  public void postConstruct() {
    //At startup, report movement to force screen to be turned on and status set to not sleeping!
    this.reportMovement();
  }

  @RequestMapping("/reportmovement")
  @ResponseBody
  public TvServiceCommandResponse reportMovement() {
    LOG.debug("Movement detected!");
    this.lastMovement = LocalDateTime.now();
    TvServiceCommandResponse response = callMirror(TURN_ON);
    cancelFuture("Cancelling scheduled mirror shutdown!");
    int timeoutMinutes = 30; //Default time before mirror turn off
    int currentHour = LocalDateTime.now().getHour();
    if (currentHour >= 23 || (currentHour >= 0 && currentHour < 5)) {
      timeoutMinutes = 5;
    }
    LOG.info("Mirror monitor shutdown scheduled to " + LocalDateTime.now().plusMinutes(timeoutMinutes));
    future = executor.schedule(() -> callMirror(TURN_OFF), timeoutMinutes, TimeUnit.MINUTES);

    return response;
  }

  @RequestMapping("/reportnomovement")
  @ResponseBody
  public TvServiceCommandResponse reportNoMovement() {
    LOG.debug("Forcing no moment timeout!");
    TvServiceCommandResponse response = callMirror(TURN_OFF);
    cancelFuture("Cancelling scheduled mirror shutdown, since screen has been forced off just now!");
    return response;
  }

  @RequestMapping("/screenstatus")
  @ResponseBody
  public TvServiceStatus screenstatus() {
    return tvServiceController.getStatus();
  }

  @RequestMapping("/screenstatus/toggle")
  @ResponseBody
  public TvServiceCommandResponse toggleScreenStatus() {
    if (tvServiceController.getStatus().equals(TvServiceStatus.ON)) {
      return callMirror(TURN_OFF);
    } else {
      return callMirror(TURN_ON);
    }
  }


  private TvServiceCommandResponse callMirror(final TvServiceCommand command) {
    return callMirror(command, false);
  }

  private TvServiceCommandResponse callMirror(final TvServiceCommand command, final boolean force) {
    if (TvServiceCommand.STATUS.equals(command)) {
      return tvServiceController.getStatusRaw();
    }

    TvServiceStatus mirrorStatus = tvServiceController.getStatus();
    if (mirrorStatus.equals(TvServiceStatus.ON) && TURN_ON.equals(command) && !force) {
      LOG.info("Got on command but mirror is already on. Ignoring...");
      return TvServiceCommandResponse.success("Ignored");
    } else if (mirrorStatus.equals(TvServiceStatus.OFF) && TURN_OFF.equals(command) && !force) {
      LOG.info("Got off command but mirror is already off. Ignoring...");
      return TvServiceCommandResponse.success("Ignored");
    }

    TvServiceCommandResponse response;
    if (TURN_OFF.equals(command)) {
      response = tvServiceController.turnOff();
      updateScreenSleepsApplicationState(response, true);
    } else {
      response = tvServiceController.turnOn();
      updateScreenSleepsApplicationState(response, false);
      CompletableFuture.runAsync(() -> webSocketHandler.requestTransportsNow());
      CompletableFuture.runAsync(() -> webSocketHandler.requestWeatherNow());
    }
    return response;
  }

  private void updateScreenSleepsApplicationState(TvServiceCommandResponse response, boolean screenSleeps) {
    if (response.isSuccessful()) {
      applicationState.setScreenSleeps(screenSleeps);
    }
  }

  private void cancelFuture(String message) {
    if (future != null && !future.isDone()) {
      LOG.info(message);
      future.cancel(false);
    }
  }

}
