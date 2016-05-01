package com.bernerus.smartmirror.dto;

/**
 * Created by andreas on 01/05/16.
 */
public class Temperature {
  final float temperature;

  public Temperature(float value) {
    this.temperature = value;
  }

  public float getTemperature() {
    return temperature;
  }
}
