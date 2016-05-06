package com.bernerus.smartmirror.controller;

import com.bernerus.smartmirror.api.VTTransportList;
import com.bernerus.smartmirror.dto.SimpleTextMessage;
import com.bernerus.smartmirror.dto.Temperature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by andreas on 03/03/16.
 */
public class VasttrafikWebSocketHandler extends TextWebSocketHandler {
  private static org.slf4j.Logger log = LoggerFactory.getLogger(VasttrafikWebSocketHandler.class);
  ObjectMapper mapper = new ObjectMapper();
  ScheduledExecutorService executor;
  @Autowired
  VasttrafikController vasttrafikController;
  private Map<String, WebSocketSession> sessions = new HashMap<>();

  VTTransportList previouslyFetchedUpcomingTransports = null;
  LocalDateTime previouslyFetchedUpcomingTransportsTime = LocalDateTime.MIN;

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws IOException {
    log.info("Opened new session in instance " + this);
    this.sessions.put(session.getId(), session);
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
    throw new RuntimeException(exception);
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    super.afterConnectionClosed(session, status);
    String sessionId = session.getId();
    this.sessions.remove(sessionId);
    log.warn("Session with id " + sessionId + " was closed!");

    if (sessions.size() == 0 && executor != null) {
      log.warn("All sessions closed. Killing executor");
      executor.shutdown();
    }
  }

  public void requestTransportsNow() {
    if(isEmpty(previouslyFetchedUpcomingTransports) || previouslyFetchedUpcomingTransportsTime.plusSeconds(30).isBefore(LocalDateTime.now())) {
      previouslyFetchedUpcomingTransportsTime = LocalDateTime.now();
      previouslyFetchedUpcomingTransports = vasttrafikController.getUpcomingTransports();
    } else {
      log.info("Recently fetched data. Returning it instead of fetching again.");
    }
    sendMessage(previouslyFetchedUpcomingTransports);
  }

  private boolean isEmpty(VTTransportList transportList) {
    return transportList == null || transportList.getTransports() == null || transportList.getTransports().isEmpty();
  }

  private void subscribeForDepartures() {
    sendTextMessage("messageSocket opened! Transport subscription set up.");
    if (executor == null || (executor.isShutdown() || executor.isTerminated())) {
      log.info("Creating new executor with vasttrafik poll task!");
      executor = Executors.newScheduledThreadPool(1);
      executor.scheduleAtFixedRate(this::requestTransportsNow, 0, 1, TimeUnit.MINUTES);
    } else {
      log.info("Executor already running...");
      requestTransportsNow();
    }
  }

  private void sendTextMessage(final String message) {
    sendMessage(new SimpleTextMessage(message));
  }

  private void sendMessage(Object o) {
    sessions.values().stream().filter(WebSocketSession::isOpen).forEach(session -> {
      try {
        session.sendMessage(new TextMessage(mapper.writeValueAsString(o)));
      } catch (IOException e) {
        log.error(e.getMessage(), e);
      }
    });
  }

  public void sendTemperatureToConsumers(float temperature) {
    sendMessage(new Temperature(temperature));
  }
}
