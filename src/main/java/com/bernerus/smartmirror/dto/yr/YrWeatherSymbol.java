package com.bernerus.smartmirror.dto.yr;

/**
 * Created by andreas on 30/06/16.
 */
public class YrWeatherSymbol {

  private final String name;
  private final Integer number;

  public YrWeatherSymbol(String name, Integer number) {

    this.name = name;
    this.number = number;
  }

  public String getName() {
    return name;
  }

  public Integer getNumber() {
    return number;
  }
}
