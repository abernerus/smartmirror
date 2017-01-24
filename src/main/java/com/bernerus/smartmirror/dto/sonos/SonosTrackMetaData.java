package com.bernerus.smartmirror.dto.sonos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * Created by andreas on 24/01/17.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SonosTrackMetaData {
  @XmlElement
  private String albumArtURI;
  @XmlElement(name="class")
  private String clazz;
  @XmlElement
  private String title;
  @XmlElement
  private String creator;
  @XmlElement
  private String albumArtist;
  @XmlElement
  private String album;
  @XmlElement
  private String tiid;

  public String getAlbumArtURI() {
    return albumArtURI;
  }

  public String getClazz() {
    return clazz;
  }

  public String getTitle() {
    return title;
  }

  public String getCreator() {
    return creator;
  }

  public String getAlbumArtist() {
    return albumArtist;
  }

  public String getAlbum() {
    return album;
  }

  public String getTiid() {
    return tiid;
  }
}
