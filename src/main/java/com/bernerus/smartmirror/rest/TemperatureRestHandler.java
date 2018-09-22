package com.bernerus.smartmirror.rest;

import com.bernerus.smartmirror.controller.WebSocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by andreas on 25/12/15.
 *
 */
@Controller
public class TemperatureRestHandler {
  private static final Logger LOG = LoggerFactory.getLogger(TemperatureRestHandler.class);

  @Autowired
  WebSocketHandler webSocketHandler;

  @RequestMapping("/reporttemperature/{temperature}")
  public @ResponseBody String reportTemperature(@PathVariable String temperature) {
    LOG.debug("Received new temperature! (" + temperature + "°C)");
    webSocketHandler.sendTemperatureToConsumers(Float.parseFloat(temperature));
    return "OK";
  }

}
