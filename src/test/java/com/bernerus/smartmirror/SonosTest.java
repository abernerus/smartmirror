package com.bernerus.smartmirror;

import com.bernerus.smartmirror.controller.SonosController;
import com.bernerus.smartmirror.dto.sonos.SonosGetPositionInfoResponse;
import org.junit.Test;

import javax.xml.bind.JAXBContext;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Created by andreas on 23/01/17.
 */
public class SonosTest {
  private static final String sonosFullResponse = "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\"><s:Body><u:GetPositionInfoResponse xmlns:u=\"urn:schemas-upnp-org:service:AVTransport:1\"><Track>3</Track><TrackDuration>0:06:44</TrackDuration><TrackMetaData>&lt;DIDL-Lite xmlns:dc=&quot;http://purl.org/dc/elements/1.1/&quot; xmlns:upnp=&quot;urn:schemas-upnp-org:metadata-1-0/upnp/&quot; xmlns:r=&quot;urn:schemas-rinconnetworks-com:metadata-1-0/&quot; xmlns=&quot;urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/&quot;&gt;&lt;item id=&quot;-1&quot; parentID=&quot;-1&quot;&gt;&lt;res protocolInfo=&quot;http-get:*:audio/x-spotify:*&quot; duration=&quot;0:06:44&quot;&gt;x-sonos-spotify:spotify%3atrack%3a4zJLpLsfY52ih6y8u4mhKH?sid=9&amp;amp;flags=0&amp;amp;sn=1&lt;/res&gt;&lt;upnp:albumArtURI&gt;https://i.scdn.co/image/5515df20c6ead399b0ef14169679e40b6ed7dbd7&lt;/upnp:albumArtURI&gt;&lt;upnp:class&gt;object.item.audioItem.musicTrack&lt;/upnp:class&gt;&lt;dc:title&gt;Wherever I May Roam&lt;/dc:title&gt;&lt;dc:creator&gt;Metallica&lt;/dc:creator&gt;&lt;r:albumArtist&gt;Metallica&lt;/r:albumArtist&gt;&lt;upnp:album&gt;Metallica&lt;/upnp:album&gt;&lt;r:tiid&gt;1504078762&lt;/r:tiid&gt;&lt;/item&gt;&lt;/DIDL-Lite&gt;</TrackMetaData><TrackURI>x-sonos-spotify:spotify%3atrack%3a4zJLpLsfY52ih6y8u4mhKH?sid=9&amp;flags=0&amp;sn=1</TrackURI><RelTime>0:01:37</RelTime><AbsTime>NOT_IMPLEMENTED</AbsTime><RelCount>2147483647</RelCount><AbsCount>2147483647</AbsCount></u:GetPositionInfoResponse></s:Body></s:Envelope>";
  private static final String sonosResponseSmall = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><GetPositionInfoResponse> <Track>3</Track> <TrackDuration>0:04:09</TrackDuration><TrackMetaData>&amp;lt;DIDL-Lite xmlndc=&amp;quot;http://purl.org/dc/elements/1.1/&amp;quot; xmlnupnp=&amp;quot;urn:schemas-upnp-org:metadata-1-0/upnp/&amp;quot; xmlnr=&amp;quot;urn:schemas-rinconnetworks-com:metadata-1-0/&amp;quot; xmlns=&amp;quot;urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/&amp;quot;&amp;gt;&amp;lt;item id=&amp;quot;-1&amp;quot; parentID=&amp;quot;-1&amp;quot;&amp;gt;&amp;lt;res protocolInfo=&amp;quot;http-get:*:audio/x-spotify:*&amp;quot; duration=&amp;quot;0:04:09&amp;quot;&amp;gt;x-sonos-spotify:spotify%3atrack%3a5uRN4EmPA7BWs5f4xsIl0Z?sid=9&amp;amp;flags=0&amp;amp;sn=1&amp;lt;/res&amp;gt;&amp;lt;upnp:albumArtURI&amp;gt;http//i.scdn.co/image/23f0e53e68858fe25ab574eca5821849496b45d4&amp;lt;/upnp:albumArtURI&amp;gt;&amp;lt;upnp:class&amp;gt;object.item.audioItem.musicTrack&amp;lt;/upnp:class&amp;gt;&amp;lt;dc:title&amp;gt;Looks That Kill&amp;lt;/dc:title&amp;gt;&amp;lt;dc:creator&amp;gt;Mötley Crüe&amp;lt;/dc:creator&amp;gt;&amp;lt;r:albumArtist&amp;gt;Mötley Crüe&amp;lt;/r:albumArtist&amp;gt;&amp;lt;upnp:album&amp;gt;Shout At The Devil&amp;lt;/upnp:album&amp;gt;&amp;lt;r:tiid&amp;gt;2090580731&amp;lt;/r:tiid&amp;gt;&amp;lt;/item&amp;gt;&amp;lt;/DIDL-Lite&amp;gt;</TrackMetaData> <TrackURI>x-sonos-spotify:spotify%3atrack%3a5uRN4EmPA7BWs5f4xsIl0Z?sid=9&amp;flags=0&amp;sn=1</TrackURI> <RelTime>0:02:51</RelTime> <AbsTime>NOT_IMPLEMENTED</AbsTime><RelCount>2147483647</RelCount><AbsCount>2147483647</AbsCount></GetPositionInfoResponse>";

  @Test
  public void testUnmarshal() throws Exception {
    SonosController controller = new SonosController();
    String trimmedResponse = controller.fixSonosSoapXml(sonosFullResponse);

    JAXBContext getPositionInfoResponseContext = JAXBContext.newInstance(SonosGetPositionInfoResponse.class);
    InputStream stream = new ByteArrayInputStream(sonosResponseSmall.getBytes(StandardCharsets.UTF_8));

    SonosGetPositionInfoResponse sonosResponseObj = (SonosGetPositionInfoResponse) getPositionInfoResponseContext.createUnmarshaller().unmarshal(stream);

    sonosResponseObj.getTrack();
  }
}
