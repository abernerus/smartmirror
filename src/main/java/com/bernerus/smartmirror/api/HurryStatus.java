package com.bernerus.smartmirror.api;

/**
 * Created by andreas on 26/01/16.
 */
public enum HurryStatus {
  DEPARTED(-1), GOING_TO_MISS_IT(0), RUN(1), WALK(2), CHILL_WALK(3), DO_NOT_LEAVE_YET(4);
  private int value;

  HurryStatus(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }

  public void setValue(int value) {
    this.value = value;
  }
}
