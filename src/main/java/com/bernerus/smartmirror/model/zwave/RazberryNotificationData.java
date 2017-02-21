package com.bernerus.smartmirror.model.zwave;

/**
 * Created by andreas on 21/02/17.
 */
public class RazberryNotificationData {
  //"{\"id\":1487701837539,\"timestamp\":\"2017-02-21T18:30:37.539Z\",\"level\":\"device-info\",\"message\":{\"dev\":\"Hall Movement\",\"l\":\"off\"},\"type\":\"device-OnOff\",\"source\":\"ZWayVDev_zway_12-0-113-7-8-A\",\"redeemed\":false}"}
  private String id;
  private String timestamp;
  private String level;
  private RazberryMessage message;
  private String type;
  private String source;
  private boolean redeemed;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }

  public String getLevel() {
    return level;
  }

  public void setLevel(String level) {
    this.level = level;
  }

  public RazberryMessage getMessage() {
    return message;
  }

  public void setMessage(RazberryMessage message) {
    this.message = message;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public boolean isRedeemed() {
    return redeemed;
  }

  public void setRedeemed(boolean redeemed) {
    this.redeemed = redeemed;
  }

  @Override
  public String toString() {
    return "RazberryNotificationData{" +
      "id='" + id + '\'' +
      ", timestamp='" + timestamp + '\'' +
      ", level='" + level + '\'' +
      ", message=" + message +
      ", type='" + type + '\'' +
      ", source='" + source + '\'' +
      ", redeemed=" + redeemed +
      '}';
  }
}
