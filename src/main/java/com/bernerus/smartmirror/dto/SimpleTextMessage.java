package com.bernerus.smartmirror.dto;

/**
 * Created by andreas on 03/03/16.
 */
public class SimpleTextMessage {
  final String message;

  public SimpleTextMessage(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }
}
