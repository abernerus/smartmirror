package com.bernerus.smartmirror.dto.sonos;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.Objects.nonNull;

/**
 * Created by andreas on 04/01/17.
 */
public class TrackInfo {
  private static final Logger LOG = LoggerFactory.getLogger(TrackInfo.class);
  private long duration;
  private long timePassed;
  private String album;
  private String artist;
  private String title;

  public TrackInfo(String album, String artist, String title, String trackDuration, String relTime) {
    this.album = decode(album);
    this.artist = decode(artist);
    this.title = decode(title);

    this.duration = convertToMillis(trackDuration, "trackDuration");
    this.timePassed = convertToMillis(relTime, "relTime");
  }

  private long convertToMillis(String stringTime, String timeType) {
    String[] split = StringUtils.split(stringTime, ":", 3);
    try {
      long hoursMs = Long.parseLong(split[0]) * 60 * 60 * 1000;
      long minutesMs = Long.parseLong(split[1]) * 60 * 1000;
      long secondsMs = Long.parseLong(split[2]) * 1000;
      return hoursMs + minutesMs + secondsMs;

    } catch (NumberFormatException nfe) {
      LOG.warn("Could not decode timeString '{}' for {}", stringTime, timeType);
      return 0;
    }
  }

  private String decode(String s) {
    if(nonNull(s)){
      return s.replaceAll("&apos;", "'").replaceAll("&amp;", "&");
    }
    return "";
  }

  @Override public boolean equals(Object o) {
    if (this == o)
      return true;

    if (o == null || getClass() != o.getClass())
      return false;

    TrackInfo trackInfo = (TrackInfo) o;

    return new EqualsBuilder()
      .append(duration, trackInfo.duration)
      .append(timePassed, trackInfo.timePassed)
      .append(album, trackInfo.album)
      .append(artist, trackInfo.artist)
      .append(title, trackInfo.title)
      .isEquals();
  }

  @Override public int hashCode() {
    return new HashCodeBuilder(17, 37)
      .append(duration)
      .append(timePassed)
      .append(album)
      .append(artist)
      .append(title)
      .toHashCode();
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

  public long getDuration() {
    return duration;
  }

  public void setDuration(long duration) {
    this.duration = duration;
  }

  public long getTimePassed() {
    return timePassed;
  }

  public void setTimePassed(long timePassed) {
    this.timePassed = timePassed;
  }

  public static TrackInfo empty() {
    return new TrackInfo(null, null, null, "00:00:00", "00:00:00");
  }
}
