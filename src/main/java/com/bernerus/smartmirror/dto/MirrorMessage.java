package com.bernerus.smartmirror.dto;

/**
 * Created by andreas on 01/05/16.
 */
public class MirrorMessage {
  final String mirrorMessage;

  public MirrorMessage(String mirrorMessage) {
    this.mirrorMessage = mirrorMessage;
  }

  public String getMirrorMessage() {
    return mirrorMessage;
  }
}
