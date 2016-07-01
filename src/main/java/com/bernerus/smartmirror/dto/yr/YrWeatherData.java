package com.bernerus.smartmirror.dto.yr;

import java.time.LocalDateTime;

/**
 * Created by andreas on 30/06/16.
 */
public class YrWeatherData {
  private LocalDateTime fromDateTime;
  private LocalDateTime toDateTime;
  private YrWeatherSymbol symbol;
  private YrPrecipitation precipitation;
  private String temperature;

  public YrWeatherData(LocalDateTime fromDateTime, LocalDateTime toDateTime) {
    this.fromDateTime = fromDateTime;
    this.toDateTime = toDateTime;
  }

  public LocalDateTime getFromDateTime() {
    return fromDateTime;
  }

  public void setFromDateTime(LocalDateTime fromDateTime) {
    this.fromDateTime = fromDateTime;
  }

  public LocalDateTime getToDateTime() {
    return toDateTime;
  }

  public void setToDateTime(LocalDateTime toDateTime) {
    this.toDateTime = toDateTime;
  }

  public void setSymbol(YrWeatherSymbol symbol) {
    this.symbol = symbol;
  }

  public YrWeatherSymbol getSymbol() {
    return symbol;
  }

  public void setPrecipitation(YrPrecipitation precipitation) {
    this.precipitation = precipitation;
  }

  public YrPrecipitation getPrecipitation() {
    return precipitation;
  }

  public void setTemperature(String temperature) {
    this.temperature = temperature;
  }

  public String getTemperature() {
    return temperature;
  }
}
