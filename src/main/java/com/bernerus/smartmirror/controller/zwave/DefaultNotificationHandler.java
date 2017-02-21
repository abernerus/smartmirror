package com.bernerus.smartmirror.controller.zwave;

import com.bernerus.smartmirror.model.zwave.RazberryWsNotification;

import java.util.function.Consumer;

/**
 * Created by andreas on 21/02/17.
 */
public class DefaultNotificationHandler implements NotificationHandler {
  private Consumer<RazberryWsNotification> defaultHandler;

  public DefaultNotificationHandler(Consumer<RazberryWsNotification> defaultHandler) {
    this.defaultHandler = defaultHandler;
  }

  @Override
  public void handleMessage(RazberryWsNotification notification) {
    defaultHandler.accept(notification);
  }
}
