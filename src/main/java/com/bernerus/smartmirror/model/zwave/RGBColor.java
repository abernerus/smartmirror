package com.bernerus.smartmirror.model.zwave;

/**
 * Created by andreas on 21/02/17.
 */
public class RGBColor {
  private final int red;
  private final int green;
  private final int blue;

  public RGBColor(int red, int green, int blue) {
    this.red = red;
    this.green = green;
    this.blue = blue;
  }

  public static RGBColor white() {
    return new RGBColor(255, 255, 255);
  }

  public static RGBColor black() {
    return new RGBColor(0, 0, 0);
  }

  public int getRed() {
    return red;
  }

  public int getGreen() {
    return green;
  }

  public int getBlue() {
    return blue;
  }
}
