package com.bernerus.smartmirror.dto.spotify.proxy;

/**
 * Created by andreas on 10/08/16.
 */
public class NowPlaying {
  //{"artist":"Legenda Aurea","track":"Remembrance","album":"Aeon","uri":"spotify:track:3sz6nNih9aCuh3yNTsVrst","duration":"5min 32s (332 seconds)",
  // "nowAt":"1min 11s","playerState":"paused","volume":"27","shuffle":"true","repeat":"false"}
  private String artist;
  private String track;
  private String album;
  private String uri;
  private String duration;
  private String nowAt;
  private String playerState;
  private String volume;
  private String shuffle;
  private String repeat;

  public NowPlaying() {
  }

  public NowPlaying(String songTitle, String artistName, String albumName, Boolean playing) {
    this.track = songTitle;
    this.artist = artistName;
    this.album = albumName;
    this.playerState = playing ? "playing" : "paused";
  }

  public String getArtist() {
    return artist;
  }

  public void setArtist(String artist) {
    this.artist = artist;
  }

  public String getTrack() {
    return track;
  }

  public void setTrack(String track) {
    this.track = track;
  }

  public String getAlbum() {
    return album;
  }

  public void setAlbum(String album) {
    this.album = album;
  }

  public String getUri() {
    return uri;
  }

  public void setUri(String uri) {
    this.uri = uri;
  }

  public String getDuration() {
    return duration;
  }

  public void setDuration(String duration) {
    this.duration = duration;
  }

  public String getNowAt() {
    return nowAt;
  }

  public void setNowAt(String nowAt) {
    this.nowAt = nowAt;
  }

  public String getPlayerState() {
    return playerState;
  }

  public void setPlayerState(String playerState) {
    this.playerState = playerState;
  }

  public String getVolume() {
    return volume;
  }

  public void setVolume(String volume) {
    this.volume = volume;
  }

  public String getShuffle() {
    return shuffle;
  }

  public void setShuffle(String shuffle) {
    this.shuffle = shuffle;
  }

  public String getRepeat() {
    return repeat;
  }

  public void setRepeat(String repeat) {
    this.repeat = repeat;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    NowPlaying that = (NowPlaying) o;

    if (uri != null ? !uri.equals(that.uri) : that.uri != null) return false;
    return playerState != null ? playerState.equals(that.playerState) : that.playerState == null;

  }

  @Override
  public int hashCode() {
    int result = uri != null ? uri.hashCode() : 0;
    result = 31 * result + (playerState != null ? playerState.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "NowPlaying{" +
      "artist='" + artist + '\'' +
      ", track='" + track + '\'' +
      ", album='" + album + '\'' +
      ", uri='" + uri + '\'' +
      ", playerState='" + playerState + '\'' +
      '}';
  }
}
