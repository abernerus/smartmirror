package com.bernerus.smartmirror.model;

import com.bernerus.smartmirror.controller.VasttrafikTokenController;
import com.bernerus.smartmirror.dto.VTToken;
import org.springframework.stereotype.Component;

/**
 * Created by andreas on 24/01/16.
 */
@Component
public class ApplicationState {
  private boolean screenSleeps = false;

  public boolean isScreenSleeps() {
    return screenSleeps;
  }

  public void setScreenSleeps(boolean screenSleeps) {
    this.screenSleeps = screenSleeps;
  }
}
