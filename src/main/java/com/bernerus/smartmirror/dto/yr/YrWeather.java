package com.bernerus.smartmirror.dto.yr;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andreas on 30/06/16.
 */
public class YrWeather {
  private String iconType;

  private List<YrWeatherData> weatherDatas = new ArrayList<>();

  public List<YrWeatherData> getWeatherDatas() {
    return weatherDatas;
  }

  public String getIconType() {
    return iconType;
  }

  public void setIconType(String iconType) {
    this.iconType = iconType;
  }
}
