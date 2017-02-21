package com.bernerus.smartmirror.model.zwave;

/**
 * Created by andreas on 21/02/17.
 */
public class RazberryWsNotification {
  //{"type":"me.z-wave.notifications.add","data":"{\"id\":1487701837539,\"timestamp\":\"2017-02-21T18:30:37.539Z\",\"level\":\"device-info\",\"message\":{\"dev\":\"Hall Movement\",\"l\":\"off\"},\"type\":\"device-OnOff\",\"source\":\"ZWayVDev_zway_12-0-113-7-8-A\",\"redeemed\":false}"}

  private String type;
  private RazberryNotificationData data;

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public RazberryNotificationData getData() {
    return data;
  }

  public void setData(RazberryNotificationData data) {
    this.data = data;
  }

  @Override
  public String toString() {
    return "RazberryWsNotification{" +
      "type='" + type + '\'' +
      ", data=" + data +
      '}';
  }
}
