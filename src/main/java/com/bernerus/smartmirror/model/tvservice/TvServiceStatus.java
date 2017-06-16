package com.bernerus.smartmirror.model.tvservice;

/**
 * Created by andreas on 2017-06-16.
 */
public enum TvServiceStatus {
  ON, OFF;

  public static TvServiceStatus fromBoolean(boolean status) {
    return status ? ON : OFF;
  }
}
