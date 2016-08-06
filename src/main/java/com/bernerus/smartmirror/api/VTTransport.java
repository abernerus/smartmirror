package com.bernerus.smartmirror.api;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by andreas on 24/01/16.
 */
public class VTTransport {
  private String name;
  private List<VTTimeLeft> timeLeftList;

  public VTTransport(String name, String stopName, List<Long> timeLeftList) {
    this.name = name;
    this.timeLeftList = timeLeftList.stream().map(l -> {
      return new VTTimeLeft(l, calculateHurryStatus(stopName, l));
    }).collect(Collectors.toList());

    Collections.sort(this.timeLeftList, (departure1, departure2) -> departure1.getTimeLeft().compareTo(departure2.getTimeLeft()));
  }

  private HurryStatus calculateHurryStatus(String stopName, long timeLeft) {
    if (timeLeft <= 0) {
      return HurryStatus.DEPARTED;
    }
    HurryStatus hurry = HurryStatus.DO_NOT_LEAVE_YET;
    if (stopName.equals("Munkebäcksmotet, Göteborg")) {
      if (timeLeft > 0 && timeLeft < 5) {
        hurry = HurryStatus.GOING_TO_MISS_IT;
      } else if (timeLeft >= 5 && timeLeft < 8) {
        hurry = HurryStatus.RUN;
      } else if (timeLeft >= 8 && timeLeft < 10) {
        hurry = HurryStatus.WALK;
      } else if (timeLeft >= 10 && timeLeft < 12) {
        hurry = HurryStatus.CHILL_WALK;
      }
    } else if (stopName.equals("Ättehögsgatan, Göteborg")) {
      if (timeLeft > 0 && timeLeft < 3) {
        hurry = HurryStatus.GOING_TO_MISS_IT;
      } else if (timeLeft >= 3 && timeLeft < 4) {
        hurry = HurryStatus.RUN;
      } else if (timeLeft >= 5 && timeLeft < 6) {
        hurry = HurryStatus.WALK;
      } else if (timeLeft >= 6 && timeLeft < 8) {
        hurry = HurryStatus.CHILL_WALK;
      }
    } else if (stopName.equals("Härlanda, Göteborg")) {
      if (timeLeft > 0 && timeLeft < 6) {
        hurry = HurryStatus.GOING_TO_MISS_IT;
      } else if (timeLeft >= 6 && timeLeft < 9) {
        hurry = HurryStatus.RUN;
      } else if (timeLeft >= 9 && timeLeft < 11) {
        hurry = HurryStatus.WALK;
      } else if (timeLeft >= 11 && timeLeft < 14) {
        hurry = HurryStatus.CHILL_WALK;
      }
    }
    return hurry;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<VTTimeLeft> getTimeLeftList() {
    return timeLeftList;
  }

  public void setTimeLeftList(List<VTTimeLeft> timeLeftList) {
    this.timeLeftList = timeLeftList;
  }

  public Long getLeastTimeLeft(){
    if(this.timeLeftList.size() > 0) {
      return this.timeLeftList.get(0).getTimeLeft();
    }
    return Long.MAX_VALUE;
  }
}
