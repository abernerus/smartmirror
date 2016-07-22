package com.bernerus.smartmirror.dto.lastfm;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andreas on 22/07/16.
 */
public class LastFmRecentTracks {
  private List<LastFmTrack> track;

  public List<LastFmTrack> getTrack() {
    if(track == null) {
      return new ArrayList<>();
    }
    return track;
  }

  public void setTrack(List<LastFmTrack> track) {
    this.track = track;
  }
}
