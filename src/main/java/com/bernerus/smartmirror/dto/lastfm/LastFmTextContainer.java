package com.bernerus.smartmirror.dto.lastfm;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by andreas on 22/07/16.
 */
public class LastFmTextContainer {
  @JsonProperty(value = "#text")
  private String text;
  private String mbid;


  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public String getMbid() {
    return mbid;
  }

  public void setMbid(String mbid) {
    this.mbid = mbid;
  }
}
