package com.bernerus.smartmirror.controller;

import com.bernerus.smartmirror.controller.asana.AsanaController;
import com.bernerus.smartmirror.controller.asana.AsanaScheduledExecutor;
import com.bernerus.smartmirror.controller.sonos.SonosController;
import com.bernerus.smartmirror.controller.sonos.SonosScheduledExecutor;
import com.bernerus.smartmirror.controller.vasttrafik.VasttrafikController;
import com.bernerus.smartmirror.controller.vasttrafik.VasttrafikScheduledExecutor;
import com.bernerus.smartmirror.controller.weather.WeatherController;
import com.bernerus.smartmirror.controller.weather.WeatherScheduledExecutor;
import com.bernerus.smartmirror.dto.Temperature;
import com.bernerus.smartmirror.model.websocket.MessageType;
import com.bernerus.smartmirror.model.websocket.MirrorWebSocketMessage;
import com.bernerus.smartmirror.model.websocket.WsMessageSender;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Created by andreas on 03/03/16.
 */
@Controller
public class WebSocketHandler extends TextWebSocketHandler {
    private static org.slf4j.Logger LOG = LoggerFactory.getLogger(WebSocketHandler.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private SonosScheduledExecutor sonosScheduledExecutor;
    private AsanaScheduledExecutor asanaScheduledExecutor;
    private VasttrafikScheduledExecutor vasttrafikScheduledExecutor;
    private WeatherScheduledExecutor weatherScheduledExecutor;

    private Map<String, WebSocketSession> sessions = new HashMap<>();

    private static final int MAX_RETRY_COUNT = 10;


    @Autowired
    public WebSocketHandler(VasttrafikController vasttrafikController, AsanaController asanaController, SonosController sonosController,
                            WeatherController weatherController) {
        this.weatherScheduledExecutor = new WeatherScheduledExecutor(new WsMessageSender(this::send), weatherController);;
        this.vasttrafikScheduledExecutor = new VasttrafikScheduledExecutor(new WsMessageSender(this::send), vasttrafikController);
        this.asanaScheduledExecutor = new AsanaScheduledExecutor(new WsMessageSender(this::send), asanaController);
        this.sonosScheduledExecutor = new SonosScheduledExecutor(new WsMessageSender(this::send), sonosController);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        LOG.info("Opened new session in instance ! {}", this);
        this.sessions.put(session.getId(), session);
        this.subscribeForDepartures();
        this.subscribeForWeather();
        this.subscribeForNowPlaying();
        this.subscribeForAsanaTasks();
        this.sendTextMessage("Subscriptions set up!");
    }

    @Override
    public void handleTextMessage(WebSocketSession session, org.springframework.web.socket.TextMessage message) throws Exception {
        String echoMessage = message.getPayload() + " tjoho";
        LOG.info(echoMessage);
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
        LOG.info("Session with id {} was closed!", sessionId);

        if (sessions.isEmpty()) {
            LOG.info("All sessions closed.");
            weatherScheduledExecutor.stop();
            vasttrafikScheduledExecutor.stop();
            sonosScheduledExecutor.stop();
            asanaScheduledExecutor.stop();
        }
    }

    private void subscribeForDepartures() {
        vasttrafikScheduledExecutor.start();
        sendTextMessage("messageSocket opened! Transport subscription set up.");
    }

    private void subscribeForWeather() {
        weatherScheduledExecutor.start();
        sendTextMessage("messageSocket opened! Weather subscription set up.");
    }

    private void subscribeForNowPlaying() {
        sonosScheduledExecutor.start();
        sendTextMessage("messageSocket opened! Now Playing subscription set up.");
    }

    private void subscribeForAsanaTasks() {
        asanaScheduledExecutor.start();
        sendTextMessage("messageSocket opened! Asana tasks subscription set up.");

    }

    public void requestTransportsNow() {
        vasttrafikScheduledExecutor.requestNow();
    }

    public void requestWeatherNow() {
        weatherScheduledExecutor.requestNow();
    }

    public void sendTextMessage(final String message) {
        sendMessage(new MirrorWebSocketMessage<>(MessageType.TEXT, message));
    }

    private <T> void send(MessageType type, T content) {
        sendMessage(new MirrorWebSocketMessage<>(type, content));
    }

    private void sendMessage(MirrorWebSocketMessage message) {
        sessions.values().stream()
                .filter(WebSocketSession::isOpen)
                .forEach(session -> CompletableFuture.runAsync(() -> sendMessageForSession(message, session, 0)));
    }

    private void sendMessageForSession(MirrorWebSocketMessage message, WebSocketSession session, int retryCount) {
        try {
            session.sendMessage(new TextMessage(OBJECT_MAPPER.writeValueAsString(message)));
        } catch (JsonProcessingException e) {
            LOG.error("Cannot parse message. Nothing sent.", e); //Should not happen unless bad code
        } catch (Exception e) {
            if (retryCount > MAX_RETRY_COUNT) {
                LOG.error("Could not send message after {} retries. Error message: {}", MAX_RETRY_COUNT, e.getMessage(), e);
            } else {
                sendMessageForSessionAfterDelay(message, session, retryCount);
            }
        }
    }

    private void sendMessageForSessionAfterDelay(MirrorWebSocketMessage message, WebSocketSession session, int retryCount) {
        try {
            LOG.info("Retrying to send message with id: '{}' of type '{}' due to connection issue, retry: {}", message.getId(), message.getType(), retryCount + 1);
            Thread.sleep(100);
            sendMessageForSession(message, session, retryCount + 1);
        } catch (InterruptedException e) {
            LOG.error("Got interrupted while waiting to retry to send message");
            throw new RuntimeException(e);
        }
    }

    public void sendTemperatureToConsumers(float temperature) {
        sendMessage(new MirrorWebSocketMessage<>(MessageType.TEMPERATURE, new Temperature(temperature)));
    }

}
