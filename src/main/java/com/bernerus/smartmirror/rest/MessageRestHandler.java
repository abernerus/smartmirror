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
public class MessageRestHandler {
  private static final Logger LOG = LoggerFactory.getLogger(MessageRestHandler.class);


  @Autowired
  WebSocketHandler webSocketHandler;

  @RequestMapping("/message/set/{message}")
  public @ResponseBody String setMessage(@PathVariable String message) {
    LOG.info("Received new message: " + message);
    webSocketHandler.sendTextMessage(message);
    return "Message Set";
  }

}
