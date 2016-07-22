package com.bernerus.smartmirror.dto.lastfm;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by andreas on 22/07/16.
 */
public class LastFmTrack {
  private String name;
  private LastFmTextContainer artist;
  private LastFmTextContainer album;

  @JsonProperty(value = "@attr")
  private LastFmPlayingAttribute nowPlayingAttribute;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public LastFmTextContainer getArtist() {
    return artist;
  }

  public void setArtist(LastFmTextContainer artist) {
    this.artist = artist;
  }

  public LastFmTextContainer getAlbum() {
    return album;
  }

  public void setAlbum(LastFmTextContainer album) {
    this.album = album;
  }

  public LastFmPlayingAttribute getNowPlayingAttribute() {
    return nowPlayingAttribute;
  }

  public void setNowPlayingAttribute(LastFmPlayingAttribute nowPlayingAttribute) {
    this.nowPlayingAttribute = nowPlayingAttribute;
  }
}
