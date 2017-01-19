package com.bernerus.smartmirror.dto.mirror;

/**
 * Created by andreas on 10/08/16.
 */
public class MirrorStatus {
  private boolean screenSleeps;

  public MirrorStatus() {
  }

  public MirrorStatus(boolean screenSleeps) {
    this.screenSleeps = screenSleeps;
  }

  public boolean isScreenSleeps() {
    return screenSleeps;
  }

  public void setScreenSleeps(boolean screenSleeps) {
    this.screenSleeps = screenSleeps;
  }
}
