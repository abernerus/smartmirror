package com.bernerus.smartmirror.model.tvservice;

/**
 * Created by andreas on 2017-06-16.
 */
public enum TvServiceCommand {
  STATUS("-s"), TURN_OFF("-o"), TURN_ON("-p");

  private final String value;

  TvServiceCommand(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
