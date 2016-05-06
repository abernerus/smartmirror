package com.bernerus.smartmirror.model;

import org.springframework.stereotype.Component;

/**
 * Created by andreas on 24/01/16.
 */
@Component
public class ApplicationState {
  private boolean screenSleeps = true;

  public boolean screenSleeps() {
    return screenSleeps;
  }

  public void setScreenSleeps(boolean screenSleeps) {
    this.screenSleeps = screenSleeps;
  }
}
