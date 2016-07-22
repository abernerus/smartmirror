package com.bernerus.smartmirror.dto.lastfm;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by andreas on 22/07/16.
 */
public class LastFmPlayingAttribute {
  @JsonProperty("nowplaying")
  private boolean nowPlaying;

  public boolean isNowPlaying() {
    return nowPlaying;
  }

  public void setNowPlaying(boolean nowPlaying) {
    this.nowPlaying = nowPlaying;
  }
}
