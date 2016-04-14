package com.bernerus.smartmirror.controller;

import com.bernerus.smartmirror.api.VTTransportList;
import com.bernerus.smartmirror.dto.SimpleTextMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by andreas on 03/03/16.
 */
public class VasttrafikWebSocketHandler extends TextWebSocketHandler {
  private static org.slf4j.Logger log = LoggerFactory.getLogger(VasttrafikWebSocketHandler.class);
  private WebSocketSession session;

  ObjectMapper mapper = new ObjectMapper();
  ScheduledExecutorService executor;

  @Autowired
  VasttrafikController vasttrafikController;

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws IOException {
    log.info("Opened new session in instance " + this);
    this.session = session;
    this.subscribeForDepartures();
  }

  @Override
  public void handleTextMessage(WebSocketSession session, org.springframework.web.socket.TextMessage message) throws Exception {
    String echoMessage = message.getPayload() + " tjoho";
    log.info(echoMessage);
    sendTextMessage(echoMessage);
  }

  @Override
  public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
    session.close(CloseStatus.SERVER_ERROR);
    this.session.close(CloseStatus.SERVER_ERROR);
    throw new RuntimeException(exception);
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    super.afterConnectionClosed(session, status);
    executor.shutdown();
    log.warn("Session closed");
  }

  private void subscribeForDepartures() {
    sendTextMessage("messageSocket opened! Transport subscription set up.");
    executor = Executors.newScheduledThreadPool(1);
    Runnable pollTask = () -> {
      try {
        VTTransportList upcomingTransports = vasttrafikController.getUpcomingTransports();
        session.sendMessage(new org.springframework.web.socket.TextMessage(mapper.writeValueAsString(upcomingTransports)));
      } catch (IOException e) {
        log.error(e.getMessage());
      }
    };
    executor.scheduleAtFixedRate(pollTask, 0, 1, TimeUnit.MINUTES);
  }

  private void sendTextMessage(final String message) {
    sendMessage(new SimpleTextMessage(message));
  }

  private void sendMessage(Object o) {
    try {
      session.sendMessage(new TextMessage(mapper.writeValueAsString(o)));
    } catch (IOException e) {
      log.error(e.getMessage());
      e.printStackTrace();
    }
  }
}
