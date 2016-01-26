package com.bernerus.smartmirror.api;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static com.bernerus.smartmirror.controller.VasttrafikRestController.ATTEHOGSGATAN_ID;
import static com.bernerus.smartmirror.controller.VasttrafikRestController.HARLANDA_ID;
import static com.bernerus.smartmirror.controller.VasttrafikRestController.MUNKEBACKSMOTET_ID;

/**
 * Created by andreas on 24/01/16.
 */
public class VTTransport {
  private String name;
  private String arrivalTime;
  private long timeLeft;
  private HurryStatus hurryStatus;

  public VTTransport(String name, String arrivalDate, String arrivalTime, String stopName) {
    this.name = name;
    this.arrivalTime = arrivalTime;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    LocalDateTime arriveDateTime = LocalDateTime.parse(arrivalDate + " " + arrivalTime, formatter);

    long arriveMs = arriveDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    long nowMs = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

    this.timeLeft = (arriveMs - nowMs) / 1000 / 60;

    if(timeLeft <= 0) {
      setMyHurryInt(HurryStatus.DEPARTED);
    }
    if(stopName.equals("Munkebäcksmotet, Göteborg")) {
      if(timeLeft > 0 && timeLeft < 5) {
        setMyHurryInt(HurryStatus.GOING_TO_MISS_IT);
      } else if(timeLeft >= 5 && timeLeft < 8) {
        setMyHurryInt(HurryStatus.RUN);
      } else if(timeLeft >=8 && timeLeft < 10) {
        setMyHurryInt(HurryStatus.WALK);
      } else if(timeLeft >= 10 && timeLeft < 12) {
        setMyHurryInt(HurryStatus.CHILL_WALK);
      } else {
        setMyHurryInt(HurryStatus.DO_NOT_LEAVE_YET);
      }
    } else if(stopName.equals("Ättehögsgatan, Göteborg")) {
      if(timeLeft > 0 && timeLeft < 3) {
        setMyHurryInt(HurryStatus.GOING_TO_MISS_IT);
      } else if(timeLeft >= 3 && timeLeft < 4) {
        setMyHurryInt(HurryStatus.RUN);
      } else if(timeLeft >=5 && timeLeft < 6) {
        setMyHurryInt(HurryStatus.WALK);
      } else if(timeLeft >= 6 && timeLeft < 8) {
        setMyHurryInt(HurryStatus.CHILL_WALK);
      } else {
        setMyHurryInt(HurryStatus.DO_NOT_LEAVE_YET);
      }
    } else if(stopName.equals("Härlanda, Göteborg")) {
      if(timeLeft > 0 && timeLeft < 6) {
        setMyHurryInt(HurryStatus.GOING_TO_MISS_IT);
      } else if(timeLeft >= 6 && timeLeft < 9) {
        setMyHurryInt(HurryStatus.RUN);
      } else if(timeLeft >=9 && timeLeft < 11) {
        setMyHurryInt(HurryStatus.WALK);
      } else if(timeLeft >= 11 && timeLeft < 14) {
        setMyHurryInt(HurryStatus.CHILL_WALK);
      } else {
        setMyHurryInt(HurryStatus.DO_NOT_LEAVE_YET);
      }
    }
  }
  private void setMyHurryInt(HurryStatus status) {
    this.hurryStatus = status;
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

  public void setTimeLeft(long timeLeft) {
    this.timeLeft = timeLeft;
  }

  public HurryStatus getHurryStatus() {
    return hurryStatus;
  }

  public void setHurryStatus(HurryStatus hurryStatus) {
    this.hurryStatus = hurryStatus;
  }
}
