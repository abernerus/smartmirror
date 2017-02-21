package com.bernerus.smartmirror.controller.zwave;

import com.bernerus.smartmirror.model.zwave.RazberryWsNotification;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Created by andreas on 21/02/17.
 */
public class BinaryNotificationHandler implements NotificationHandler{
  private static final String ON = "on";
  private static final String OFF = "off";
  private Consumer<RazberryWsNotification> onOnEvent;
  private Consumer<RazberryWsNotification> onOffEvent;

  public BinaryNotificationHandler(Consumer<RazberryWsNotification> onOnEvent, Consumer<RazberryWsNotification> onOffEvent) {
    this.onOnEvent = onOnEvent;
    this.onOffEvent = onOffEvent;
  }

  @Override
  public void handleMessage(RazberryWsNotification notification) {
    if(notification.getData().getMessage().getL().equals(ON)) {
      onOnEvent.accept(notification);
    } else {
      onOffEvent.accept(notification);
    }
  }
}
