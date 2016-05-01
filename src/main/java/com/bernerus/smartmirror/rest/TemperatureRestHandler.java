package com.bernerus.smartmirror.rest;

import com.bernerus.smartmirror.controller.VasttrafikController;
import com.bernerus.smartmirror.controller.VasttrafikWebSocketHandler;
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
import org.springframework.web.bind.annotation.PathVariable;
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
public class TemperatureRestHandler {
  private static final Logger LOG = LoggerFactory.getLogger(TemperatureRestHandler.class);

  @Autowired
  VasttrafikWebSocketHandler vasttrafikWebSocketHandler;

  @RequestMapping("/reporttemperature/{temperature}")
  public @ResponseBody String reportTemperature(@PathVariable String temperature) {
    LOG.info("Received new temperature! (" + temperature + "°C)");
    vasttrafikWebSocketHandler.sendTemperatureToConsumers(Float.parseFloat(temperature));
    return "OK";
  }

}
