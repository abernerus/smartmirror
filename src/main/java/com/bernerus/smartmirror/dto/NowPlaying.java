package com.bernerus.smartmirror.dto;

import java.util.Objects;

/**
 * Created by andreas on 22/07/16.
 */
public class NowPlaying {
  private String songTitle;
  private String artistName;
  private String albumName;
  private Boolean playing;

  public NowPlaying() {
  }

  public NowPlaying(String songTitle, String artistName, String albumName, Boolean playing) {
    this.songTitle = songTitle;
    this.artistName = artistName;
    this.albumName = albumName;
    this.playing = playing;
  }

  public String getSongTitle() {
    return songTitle;
  }

  public void setSongTitle(String songTitle) {
    this.songTitle = songTitle;
  }

  public String getArtistName() {
    return artistName;
  }

  public void setArtistName(String artistName) {
    this.artistName = artistName;
  }

  public String getAlbumName() {
    return albumName;
  }

  public void setAlbumName(String albumName) {
    this.albumName = albumName;
  }

  public Boolean getPlaying() {
    return playing;
  }

  public void setPlaying(Boolean playing) {
    this.playing = playing;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    NowPlaying that = (NowPlaying) o;
    return Objects.equals(songTitle, that.songTitle) &&
      Objects.equals(artistName, that.artistName) &&
      Objects.equals(albumName, that.albumName) &&
      Objects.equals(playing, that.playing);
  }

  @Override
  public int hashCode() {
    return Objects.hash(songTitle, artistName, albumName, playing);
  }

  @Override
  public String toString() {
    return "NowPlaying{" +
      "songTitle='" + songTitle + '\'' +
      ", artistName='" + artistName + '\'' +
      ", albumName='" + albumName + '\'' +
      ", playing=" + playing +
      '}';
  }
}
