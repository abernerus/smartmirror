package com.bernerus.smartmirror.dto;

/**
 * Created by andreas on 24/01/16.
 */
public class VTToken {
  private long expires;
  private String accessKey;

  public VTToken(long expires, String accessKey) {
    this.expires = expires;
    this.accessKey = accessKey;
  }

  public long getExpires() {
    return expires;
  }

  public String getAccessKey() {
    return accessKey;
  }
}
