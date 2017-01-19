package com.bernerus.smartmirror.dto.sonos.proxy;

/**
 * Created by andreas on 04/01/17.
 */
public class TrackInfo {
  private String album;
  private String artist;
  private String title;
  private String uri;
  private String playlist_position;
  private String duration;
  private String position;
  private String album_art;
  private String metadata;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    TrackInfo trackInfo = (TrackInfo) o;

    if (album != null ? !album.equals(trackInfo.album) : trackInfo.album != null) return false;
    if (artist != null ? !artist.equals(trackInfo.artist) : trackInfo.artist != null) return false;
    if (title != null ? !title.equals(trackInfo.title) : trackInfo.title != null) return false;
    if (uri != null ? !uri.equals(trackInfo.uri) : trackInfo.uri != null) return false;
    if (playlist_position != null ? !playlist_position.equals(trackInfo.playlist_position) : trackInfo.playlist_position != null) return false;
    if (duration != null ? !duration.equals(trackInfo.duration) : trackInfo.duration != null) return false;
    if (position != null ? !position.equals(trackInfo.position) : trackInfo.position != null) return false;
    if (album_art != null ? !album_art.equals(trackInfo.album_art) : trackInfo.album_art != null) return false;
    return metadata != null ? metadata.equals(trackInfo.metadata) : trackInfo.metadata == null;

  }

  @Override
  public int hashCode() {
    int result = album != null ? album.hashCode() : 0;
    result = 31 * result + (artist != null ? artist.hashCode() : 0);
    result = 31 * result + (title != null ? title.hashCode() : 0);
    result = 31 * result + (uri != null ? uri.hashCode() : 0);
    result = 31 * result + (playlist_position != null ? playlist_position.hashCode() : 0);
    result = 31 * result + (duration != null ? duration.hashCode() : 0);
    result = 31 * result + (position != null ? position.hashCode() : 0);
    result = 31 * result + (album_art != null ? album_art.hashCode() : 0);
    result = 31 * result + (metadata != null ? metadata.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "TrackInfo{" +
      "album='" + album + '\'' +
      ", artist='" + artist + '\'' +
      ", title='" + title + '\'' +
      ", uri='" + uri + '\'' +
      ", playlist_position='" + playlist_position + '\'' +
      ", duration='" + duration + '\'' +
      ", position='" + position + '\'' +
      ", album_art='" + album_art + '\'' +
      ", metadata='" + metadata + '\'' +
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

  public String getUri() {
    return uri;
  }

  public void setUri(String uri) {
    this.uri = uri;
  }

  public String getPlaylist_position() {
    return playlist_position;
  }

  public void setPlaylist_position(String playlist_position) {
    this.playlist_position = playlist_position;
  }

  public String getDuration() {
    return duration;
  }

  public void setDuration(String duration) {
    this.duration = duration;
  }

  public String getPosition() {
    return position;
  }

  public void setPosition(String position) {
    this.position = position;
  }

  public String getAlbum_art() {
    return album_art;
  }

  public void setAlbum_art(String album_art) {
    this.album_art = album_art;
  }

  public String getMetadata() {
    return metadata;
  }

  public void setMetadata(String metadata) {
    this.metadata = metadata;
  }
}
