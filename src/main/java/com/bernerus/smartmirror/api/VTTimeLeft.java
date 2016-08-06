package com.bernerus.smartmirror.api;

/**
 * Created by andreas on 06/08/16.
 */
public class VTTimeLeft {
  private Long timeLeft;
  private HurryStatus hurryStatus;

  public VTTimeLeft(Long timeLeft, HurryStatus hurryStatus) {
    this.timeLeft = timeLeft;
    this.hurryStatus = hurryStatus;
  }

  public VTTimeLeft() {
  }

  public Long getTimeLeft() {
    return timeLeft;
  }

  public void setTimeLeft(Long timeLeft) {
    this.timeLeft = timeLeft;
  }

  public HurryStatus getHurryStatus() {
    return hurryStatus;
  }

  public void setHurryStatus(HurryStatus hurryStatus) {
    this.hurryStatus = hurryStatus;
  }
}
