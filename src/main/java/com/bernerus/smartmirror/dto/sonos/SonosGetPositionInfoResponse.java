package com.bernerus.smartmirror.dto.sonos;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by andreas on 23/01/17.
 */
@XmlRootElement(name="GetPositionInfoResponse")
public class SonosGetPositionInfoResponse {
  @XmlElement(name="TrackDuration")
  private String trackDuration;

  @XmlElement(name="AbsTime")
  private String absTime;

  @XmlElement(name="TrackMetaData")
  private SonosTrackMetaData trackMetaData;

  @XmlElement(name="TrackURI")
  private String trackURI;

  @XmlElement(name="AbsCount")
  private String absCount;

  @XmlElement(name="RelTime")
  private String relTime;

  @XmlElement(name="RelCount")
  private String relCount;

  @XmlElement(name="Track")
  private String track;

  public String getTrackDuration() {
    return trackDuration;
  }

  public String getAbsTime() {
    return absTime;
  }

  public SonosTrackMetaData getTrackMetaData() {
    return trackMetaData;
  }

  public String getTrackURI() {
    return trackURI;
  }

  public String getAbsCount() {
    return absCount;
  }

  public String getRelTime() {
    return relTime;
  }

  public String getRelCount() {
    return relCount;
  }

  public String getTrack() {
    return track;
  }
}
