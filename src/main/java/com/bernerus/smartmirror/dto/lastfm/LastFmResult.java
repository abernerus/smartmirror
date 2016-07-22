package com.bernerus.smartmirror.dto.lastfm;

/**
 * Created by andreas on 22/07/16.
 */
public class LastFmResult {
  private LastFmRecentTracks recenttracks;

  public LastFmRecentTracks getRecenttracks() {
    if (recenttracks == null) {
      return new LastFmRecentTracks();
    }
    return recenttracks;
  }

  public void setRecenttracks(LastFmRecentTracks recenttracks) {
    this.recenttracks = recenttracks;
  }
}
