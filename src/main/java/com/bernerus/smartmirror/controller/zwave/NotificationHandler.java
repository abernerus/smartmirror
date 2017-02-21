package com.bernerus.smartmirror.controller.zwave;

import com.bernerus.smartmirror.model.zwave.RazberryWsNotification;

/**
 * Created by andreas on 21/02/17.
 */
public interface NotificationHandler {
  void handleMessage(RazberryWsNotification notification);
}
