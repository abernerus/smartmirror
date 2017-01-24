package com.bernerus.smartmirror.dto.sonos;

/**
 * Created by andreas on 04/01/17.
 */
public class TrackInfo {
  private String album;
  private String artist;
  private String title;

  public TrackInfo(String album, String artist, String title) {
    this.album = decode(album);
    this.artist = decode(artist);
    this.title = decode(title);
  }

  private String decode(String s) {
    return s.replaceAll("&apos;", "'").replaceAll("&amp;", "&");
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    TrackInfo trackInfo = (TrackInfo) o;

    if (album != null ? !album.equals(trackInfo.album) : trackInfo.album != null) return false;
    if (artist != null ? !artist.equals(trackInfo.artist) : trackInfo.artist != null) return false;
    return title != null ? title.equals(trackInfo.title) : trackInfo.title == null;
  }

  @Override
  public int hashCode() {
    int result = album != null ? album.hashCode() : 0;
    result = 31 * result + (artist != null ? artist.hashCode() : 0);
    result = 31 * result + (title != null ? title.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "TrackInfo{" +
      "album='" + album + '\'' +
      ", artist='" + artist + '\'' +
      ", title='" + title + '\'' +
      '}';
  }

  public String getAlbum() {
    return album;
  }

  public void setAlbum(String album) {
    this.album = album;
  }

  public String getArtist() {
    return artist;
  }

  public void setArtist(String artist) {
    this.artist = artist;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public static TrackInfo empty() {
    return new TrackInfo(null, null, null);
  }
}
