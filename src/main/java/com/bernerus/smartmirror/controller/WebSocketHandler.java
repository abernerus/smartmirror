package com.bernerus.smartmirror.controller;

import com.bernerus.smartmirror.api.VTTransportList;
import com.bernerus.smartmirror.dto.MirrorMessage;
import com.bernerus.smartmirror.dto.SimpleTextMessage;
import com.bernerus.smartmirror.dto.Temperature;
import com.bernerus.smartmirror.dto.sonos.TrackInfo;
import com.bernerus.smartmirror.dto.yr.YrWeather;
import com.bernerus.smartmirror.model.asana.AsanaTasks;
import com.bernerus.smartmirror.model.websocket.MessageType;
import com.bernerus.smartmirror.model.websocket.MirrorWebSocketMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by andreas on 03/03/16.
 */
@Controller
public class WebSocketHandler extends TextWebSocketHandler {
  private static org.slf4j.Logger log = LoggerFactory.getLogger(WebSocketHandler.class);
  private ObjectMapper mapper = new ObjectMapper();

  private ScheduledExecutorService vasttrafikExecutor;
  private ScheduledExecutorService weatherExecutor;
  private ScheduledExecutorService spotifyProxyExecutor;
  private ScheduledExecutorService asanaProxyExecutor;

  @Autowired
  private VasttrafikController vasttrafikController;

  @Autowired
  private WeatherController weatherController;

  @Autowired
  private SonosController sonosController;

  @Autowired
  private AsanaController asanaController;

  private Map<String, WebSocketSession> sessions = new HashMap<>();

  private VTTransportList previouslyFetchedUpcomingTransports = null;
  private LocalDateTime previouslyFetchedUpcomingTransportsTime = LocalDateTime.MIN;

  private YrWeather previouslyFetchedWeather = null;
  private LocalDateTime previouslyFetchedWeatherTime = LocalDateTime.MIN;

  private String mirrorMessage = "";
  private TrackInfo lastPlaying;
  private AsanaTasks lastTasks = null;

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws IOException {
    log.info("Opened new session in instance " + this);
    lastPlaying = null;
    this.sessions.put(session.getId(), session);
    this.subscribeForDepartures();
    this.subscribeForWeather();
    this.subscribeForNowPlaying();
    this.subscribeForAsanaTasks();
    this.sendMessage(new MirrorWebSocketMessage<>(MessageType.TEXT, new MirrorMessage(this.mirrorMessage)));
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

    if (sessions.size() == 0) {
      log.warn("All sessions closed.");
      if (vasttrafikExecutor != null) {
        log.info("Killing vasttrafikExecutor");
        vasttrafikExecutor.shutdown();
      }
      if (weatherExecutor != null) {
        log.info("Killing weatherExecutor");
        weatherExecutor.shutdown();
      }
      if (spotifyProxyExecutor != null) {
        log.info("Killing spotifyExecutor");
        spotifyProxyExecutor.shutdown();
        lastPlaying = null;
      }
      if (asanaProxyExecutor != null) {
        log.info("Killing asanaExecutur");
        asanaProxyExecutor.shutdown();
        lastTasks = null;
      }
    }
  }

  public void requestTransportsNow() {
    if (isEmpty(previouslyFetchedUpcomingTransports) || previouslyFetchedUpcomingTransportsTime.plusSeconds(30).isBefore(LocalDateTime.now())) {
      previouslyFetchedUpcomingTransports = vasttrafikController.getUpcomingTransports();
      if (previouslyFetchedUpcomingTransports != null) {
        previouslyFetchedUpcomingTransportsTime = LocalDateTime.now();
      }
    } else {
      log.info("Recently fetched västtrafik data. Returning it instead of fetching again.");
    }
    sendMessage(new MirrorWebSocketMessage<>( MessageType.TRANSPORTS, previouslyFetchedUpcomingTransports));
  }

  private boolean isEmpty(VTTransportList transportList) {
    return transportList == null || transportList.getTransports() == null || transportList.getTransports().isEmpty();
  }

  private void subscribeForDepartures() {
    sendTextMessage("messageSocket opened! Transport subscription set up.");
    if (vasttrafikExecutor == null || (vasttrafikExecutor.isShutdown() || vasttrafikExecutor.isTerminated())) {
      log.info("Creating new vasttrafikExecutor with vasttrafik poll task!");
      vasttrafikExecutor = Executors.newScheduledThreadPool(1);
      vasttrafikExecutor.scheduleAtFixedRate(this::requestTransportsNow, 0, 1, TimeUnit.MINUTES);
    } else {
      log.info("Executor already running...");
      requestTransportsNow();
    }
  }

  private void subscribeForWeather() {
    sendTextMessage("messageSocket opened! Weather subscription set up.");
    if (weatherExecutor == null || (weatherExecutor.isShutdown() || weatherExecutor.isTerminated())) {
      log.info("Creating new weatherExecutor with yr.no poll task!");
      weatherExecutor = Executors.newScheduledThreadPool(1);
      weatherExecutor.scheduleAtFixedRate(this::requestWeatherNow, 0, 1, TimeUnit.HOURS);
    } else {
      log.info("weatherExecutor already running...");
      requestWeatherNow();
    }
  }

  public void requestWeatherNow() {
    if (previouslyFetchedWeather == null || previouslyFetchedWeatherTime.plusMinutes(45).isBefore(LocalDateTime.now())) {
      previouslyFetchedWeather = weatherController.getWeather();
      if (previouslyFetchedWeather != null) {
        previouslyFetchedWeatherTime = LocalDateTime.now();
      }
    } else {
      log.info("Recently fetched weather data. Returning it instead of fetching again.");
    }
    sendMessage(new MirrorWebSocketMessage<>( MessageType.WEATHER, previouslyFetchedWeather));
  }

  private void subscribeForNowPlaying() {
    sendTextMessage("messageSocket opened! Now Playing subscription set up.");
    if (spotifyProxyExecutor == null || (spotifyProxyExecutor.isShutdown() || spotifyProxyExecutor.isTerminated())) {
      log.info("Creating new spotifyExecutorExecutor with now playing poll task!");
      spotifyProxyExecutor = Executors.newScheduledThreadPool(1);
      spotifyProxyExecutor.scheduleAtFixedRate(this::requestNowPlaying, 0, 5, TimeUnit.SECONDS);
    } else {
      log.info("spotifyProxyExecutor already running...");
      requestNowPlaying();
    }
  }

  private void subscribeForAsanaTasks() {
    sendTextMessage("messageSocket opened! Asana tasks subscription set up.");
    if (asanaProxyExecutor == null || (asanaProxyExecutor.isShutdown() || asanaProxyExecutor.isTerminated())) {
      log.info("Creating new asanaProxyExecutor with Asana tasks poll task!");
      asanaProxyExecutor = Executors.newScheduledThreadPool(1);
      asanaProxyExecutor.scheduleAtFixedRate(this::requestAsanaTasks, 0, 5, TimeUnit.SECONDS);
    } else {
      log.info("asanaProxyExecutor already running...");
      requestAsanaTasks();
    }
  }


  private void requestNowPlaying() {
    TrackInfo nowPlaying = sonosController.getNowPlaying();
    log.debug(nowPlaying.toString());
    if (!nowPlaying.equals(lastPlaying)) {
      lastPlaying = nowPlaying;
      sendMessage(new MirrorWebSocketMessage<>(MessageType.NOW_PLAYING, lastPlaying));
    }
  }

  private void requestAsanaTasks() {
    AsanaTasks tasks = asanaController.getAsanaTasks();
    if(!tasks.equals(lastTasks)) {
      lastTasks = tasks;
      log.debug(tasks.toString());
      sendMessage(new MirrorWebSocketMessage<>(MessageType.TASKS, tasks.getData()));
    }
  }

  private void sendTextMessage(final String message) {
    sendMessage(new MirrorWebSocketMessage<>(MessageType.TEXT, new SimpleTextMessage(message)));
  }

  private void sendMessage(MirrorWebSocketMessage o) {
    sessions.values().stream().filter(WebSocketSession::isOpen).forEach(session -> {
      try {
        session.sendMessage(new TextMessage(mapper.writeValueAsString(o)));
      } catch (IOException e) {
        log.error(e.getMessage(), e);
      }
    });
  }

  public void sendTemperatureToConsumers(float temperature) {
    sendMessage(new MirrorWebSocketMessage<>(MessageType.TEMPERATURE, new Temperature(temperature)));
  }

  public void sendMessageToConsumers(String message) {
    this.mirrorMessage = message;
    sendMessage(new MirrorWebSocketMessage<>( MessageType.TEXT, new MirrorMessage(message)));
  }
}
