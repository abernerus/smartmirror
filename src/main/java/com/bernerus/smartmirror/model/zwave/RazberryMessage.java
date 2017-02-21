package com.bernerus.smartmirror.model.zwave;

/**
 * Created by andreas on 21/02/17.
 */
public class RazberryMessage {
  //{\"dev\":\"Hall Movement\",\"l\":\"off\"}
  private String dev;
  private String l;

  public String getDev() {
    return dev;
  }

  public void setDev(String dev) {
    this.dev = dev;
  }

  public String getL() {
    return l;
  }

  public void setL(String l) {
    this.l = l;
  }

  @Override
  public String toString() {
    return "RazberryMessage{" +
      "dev='" + dev + '\'' +
      ", l='" + l + '\'' +
      '}';
  }
}
