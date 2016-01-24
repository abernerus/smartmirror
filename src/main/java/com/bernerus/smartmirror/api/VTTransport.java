package com.bernerus.smartmirror.api;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Created by andreas on 24/01/16.
 */
public class VTTransport {
  private String name;
  private String arrivalTime;
  private long timeLeft;

  public VTTransport(String name, String arrivalDate, String arrivalTime) {
    this.name = name;
    this.arrivalTime = arrivalTime;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    LocalDateTime arriveDateTime = LocalDateTime.parse(arrivalDate + " " + arrivalTime, formatter);

    long arriveMs = arriveDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    long nowMs = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

    this.timeLeft = (arriveMs - nowMs) / 1000 / 60;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getArrivalTime() {
    return arrivalTime;
  }

  public void setArrivalTime(String arrivalTime) {
    this.arrivalTime = arrivalTime;
  }

  public long getTimeLeft() {
    return timeLeft;
  }
}
