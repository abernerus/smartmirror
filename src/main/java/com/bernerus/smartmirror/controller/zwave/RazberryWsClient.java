package com.bernerus.smartmirror.controller.zwave;

import com.bernerus.smartmirror.config.RazberryActive;
import com.bernerus.smartmirror.controller.zwave.http.RazberryRgbHttpClient;
import com.bernerus.smartmirror.model.zwave.HttpClientConfig;
import com.bernerus.smartmirror.model.zwave.RGBColor;
import com.bernerus.smartmirror.model.zwave.RazberryWsNotification;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.annotation.PostConstruct;
import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by andreas on 21/02/17.
 */
@Component
@ClientEndpoint
@Conditional(RazberryActive.class)
public class RazberryWsClient extends TextWebSocketHandler {
  private static final Logger LOG = LoggerFactory.getLogger(RazberryWsClient.class);
  private static final String BIG_BEDBOX_SENSOR = "ZWayVDev_zway_5-0-48-1";
  private static final String BEDBOX_RGB_LIGHTS = "ZWayVDev_zway_11-0-51-rgb";

  private Session userSession = null;
  private Map<String, NotificationHandler> handlers = new HashMap<>();
  private DefaultNotificationHandler defaultNotificationHandler = new DefaultNotificationHandler(this::logNoHandler);

  private RazberryRgbHttpClient bedboxRgbHttpClient;

  private HttpClientConfig clientConfig;

  @Autowired
  public RazberryWsClient(Environment env) {
    String host = env.getProperty("razberry.host");
    String port = env.getProperty("razberry.port");
    String username = env.getProperty("razberry.user");
    String password = env.getProperty("razberry.password");
    this.clientConfig = new HttpClientConfig(host, port, username, password);

    this.bedboxRgbHttpClient = new RazberryRgbHttpClient(clientConfig, BEDBOX_RGB_LIGHTS);

    handlers.put(BIG_BEDBOX_SENSOR, new BinaryNotificationHandler(this::bigBedBoxOpen, this::bigBedBoxClose));
  }

  @PostConstruct
  public void init() {
    try {
      WebSocketContainer container = ContainerProvider.getWebSocketContainer();
      URI endpointURI = URI.create("ws://" + clientConfig.getHost() + ":" + clientConfig.getPort());
      container.connectToServer(this, endpointURI);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private void logNoHandler(RazberryWsNotification razberryWsNotification) {
    LOG.warn("No handler specified for {}", razberryWsNotification.getData().getSource());
  }

  private void bigBedBoxOpen(RazberryWsNotification razberryWsNotification) {
    LOG.info("Turning bed lights on!");
    bedboxRgbHttpClient.setBedBoxColor(RGBColor.white());

  }

  private void bigBedBoxClose(RazberryWsNotification razberryWsNotification) {
    LOG.info("Turning bed lights off!");
    bedboxRgbHttpClient.setBedBoxColor(RGBColor.black());

  }

  @OnOpen
  public void onOpen(final Session userSession) {
    LOG.info("WS open to Razberry");
    this.userSession = userSession;
  }

  @OnClose
  public void onClose(final Session userSession, final CloseReason reason) {
    LOG.info("WS closed to Razberry");
    this.userSession = null;
  }

  @OnMessage
  public void onMessage(final String message) {
    String json = cleanupMessage(message);
    LOG.info("WS message: " + json);
    ObjectMapper mapper = new ObjectMapper();
    try {
      RazberryWsNotification notification = mapper.readValue(json, RazberryWsNotification.class);
      final String deviceId = notification.getData().getSource();
      handlers.getOrDefault(deviceId, defaultNotificationHandler).handleMessage(notification);
    } catch (IOException e) {
      LOG.error("Could not parse ws message", e);
    }
  }

  private String cleanupMessage(String message) {
    String clean = message.replaceAll("\\\\\"", "\"");
    clean = clean.replaceAll("\"\\{", "{");
    clean = clean.replaceAll("}\"", "}");
    return clean;
  }
}
