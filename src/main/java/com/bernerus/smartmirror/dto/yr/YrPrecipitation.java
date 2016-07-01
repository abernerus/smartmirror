package com.bernerus.smartmirror.dto.yr;

/**
 * Created by andreas on 30/06/16.
 */
public class YrPrecipitation {
  private final String value;
  private final String minvalue;
  private final String maxvalue;

  public YrPrecipitation(String value, String minvalue, String maxvalue) {
    this.value = value;
    this.minvalue = minvalue;
    this.maxvalue = maxvalue;
  }

  public String getValue() {
    return value;
  }

  public String getMinvalue() {
    return minvalue;
  }

  public String getMaxvalue() {
    return maxvalue;
  }
}
