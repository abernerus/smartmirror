package com.bernerus.smartmirror.dto.yr;

/**
 * Created by andreas on 30/06/16.
 */
public class YrWeatherSymbol {

  private final String name;
  private final Integer number;
  private final String var;

  public YrWeatherSymbol(String name, Integer number, String var) {

    this.name = name;
    this.number = number;
    this.var = var;
  }

  public String getName() {
    return name;
  }

  public Integer getNumber() {
    return number;
  }

  public String getVar(){ return var; }
}
