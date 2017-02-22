package com.bernerus.smartmirror.controller;

import com.bernerus.smartmirror.config.SonosActive;
import com.bernerus.smartmirror.dto.sonos.SonosGetPositionInfoResponse;
import com.bernerus.smartmirror.dto.sonos.SonosTrackMetaData;
import com.bernerus.smartmirror.dto.sonos.TrackInfo;
import com.bernerus.smartmirror.model.ApplicationState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Conditional(SonosActive.class)
public class SonosController {
  private static final Logger LOG = LoggerFactory.getLogger(SonosController.class);

  @Value("${sonos.host}")
  private String sonosHost;

  @Value("${sonos.port}")
  private String sonosPort;

  private String baseUrl;

  @Autowired
  ApplicationState applicationState;

  //private static String  baseUrl = "http://192.168.1.200:1400";
  private JAXBContext getPositionInfoResponseContext;

  @PostConstruct
  public void init() {
    baseUrl = "http://" + sonosHost + ":" + sonosPort;
    LOG.info("SONOS: " + baseUrl);

    try {
      getPositionInfoResponseContext = JAXBContext.newInstance(SonosGetPositionInfoResponse.class);
    } catch (JAXBException e) {
      LOG.error("Could not create context, Doh!", e);
    }
  }

  public TrackInfo getNowPlaying() {
    if (!applicationState.screenSleeps()) {
      LOG.debug("Requesting Spotify data from playbar @ {}:{}", sonosHost, sonosPort);
      try {
        String content = execute(infoUrl, infoSoapAction, infoSoapBody);
        InputStream stream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
        SonosGetPositionInfoResponse sonosResponse = (SonosGetPositionInfoResponse) getPositionInfoResponseContext.createUnmarshaller().unmarshal(stream);
        SonosTrackMetaData track = sonosResponse.getTrackMetaData();
        return new TrackInfo(track.getAlbum(), track.getAlbumArtist(), track.getTitle());
      } catch (Exception e) {
        LOG.error("Error getting last played", e);
      }
    }
    return TrackInfo.empty();
  }

  private void playSpotify(String code, Integer volume, Integer sleep, boolean forever) {
    ExecutorService executor = Executors.newSingleThreadExecutor();
    executor.submit(() -> {
      try {
        resetQueue();
        enqueueSpotify(code);
        setVolume(volume);
        play();
        if (!forever) {
          Thread.sleep(sleep);
          pause();
        }
      } catch (IOException | InterruptedException e) {
        e.printStackTrace();
      }

    });

  }


  private void setVolume(Integer volume) throws IOException {
    String body = setVolumeSoapBody.replace("${volume}", volume.toString());

    execute(setVolumeUrl, setVolumeSoapAction, body);
  }

  private void enqueueSpotify(String code) throws IOException {
    String body = enqueueSoapBody.replace("${code}", code);

    execute(enqueueUrl, enqueueSoapAction, body);
  }

  private void resetQueue() throws IOException {
    execute(resetQueueUrl, resetQueueSoapAction, resetQueueSoapBody);
  }

  private void play() throws IOException {
    execute(playUrl, playSoapAction, playSoapBody);
  }

  private void pause() {
    ExecutorService executor = Executors.newSingleThreadExecutor();
    executor.submit(() -> {
      try {
        execute(pauseUrl, pauseSoapAction, pauseSoapBody);
        String body = setVolumeSoapBody.replace("${volume}", "20");
        execute(setVolumeUrl, setVolumeSoapAction, body);
      } catch (IOException e) {
        e.printStackTrace();
      }

    });

  }

  private String execute(String url, String soapAction, String body) throws IOException {
    body = getSoapBody(body);
    String endpoint = baseUrl + url;
    URL _url = new URL(endpoint);

    HttpURLConnection conn = (HttpURLConnection) _url.openConnection();


    conn.setRequestProperty("SOAPACTION", soapAction);
    conn.setRequestProperty("CONTENT-TYPE", "text/xml; charset=\"utf-8\"");
    conn.setRequestProperty("CONNECTION", "close");


    conn.setReadTimeout(10000 /* milliseconds */);
    conn.setConnectTimeout(15000 /* milliseconds */);
    conn.setRequestMethod("POST");
    conn.setDoInput(true);

    conn.setDoOutput(true);
    conn.connect();

    if (body != null) {
      OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
      out.write(body);
      out.close();

    }
    int response = conn.getResponseCode();
    InputStream is = null;
    is = conn.getInputStream();

    String contentAsString = fixSonosSoapXml(readFully(is));

    LOG.debug(contentAsString);
    return contentAsString;
  }

  private String readFully(InputStream inputStream)
    throws IOException {
    return new String(readBytesFully(inputStream), "UTF-8");
  }

  private static String getSoapBody(String innerBody) {
    return soapBodyStart + innerBody + soapBodyEnd;
  }

  private static byte[] readBytesFully(InputStream inputStream)
    throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    byte[] buffer = new byte[1024];
    int length = 0;
    while ((length = inputStream.read(buffer)) != -1) {
      baos.write(buffer, 0, length);
    }
    return baos.toByteArray();
  }

  public String fixSonosSoapXml(final String soapXml) {
    String trimmedXml = soapXml;
    trimmedXml = trimmedXml.replaceAll("&amp;", "&");
    trimmedXml = trimmedXml.replaceAll("&lt;", "<");
    trimmedXml = trimmedXml.replaceAll("&gt;", ">");
    trimmedXml = trimmedXml.replaceAll("&quot;", "\"");
    trimmedXml = trimmedXml.replaceAll("<[a-z]+:", "<");
    trimmedXml = trimmedXml.replaceAll("</[a-z]+:", "</");
    trimmedXml = trimmedXml.replaceAll("</?Envelope[^>]*>", "");
    trimmedXml = trimmedXml.replaceAll("</?DIDL-Lite[^>]*>", "");
    trimmedXml = trimmedXml.replaceAll("</?Body>", "");
    trimmedXml = trimmedXml.replaceFirst("<item[^>]+>", "");
    trimmedXml = trimmedXml.replaceFirst("</item>", "");
    trimmedXml = trimmedXml.replaceAll("&([^;&]+(?!(?:\\\\w|;)))", "&amp;$1");
    return trimmedXml;
  }

  private static String infoSoapAction = "urn:schemas-upnp-org:service:AVTransport:1#GetPositionInfo";
  private static String infoSoapBody = "<u:GetPositionInfo xmlns:u=\"urn:schemas-upnp-org:service:AVTransport:1\"><InstanceID>0</InstanceID><Speed>1</Speed></u:GetPositionInfo>";
  private static String infoUrl = "/MediaRenderer/AVTransport/Control";


  private static String playSoapAction = "urn:schemas-upnp-org:service:AVTransport:1#Play";
  private static String playSoapBody = "<u:Play xmlns:u=\"urn:schemas-upnp-org:service:AVTransport:1\"><InstanceID>0</InstanceID><Speed>1</Speed></u:Play>";
  private static String playUrl = "/MediaRenderer/AVTransport/Control";

  private static String pauseSoapAction = "urn:schemas-upnp-org:service:AVTransport:1#Pause";
  private static String pauseSoapBody = "<u:Pause xmlns:u=\"urn:schemas-upnp-org:service:AVTransport:1\"><InstanceID>0</InstanceID><Speed>1</Speed></u:Pause>";
  private static String pauseUrl = "/MediaRenderer/AVTransport/Control";

  private static String enqueueSoapAction = "urn:schemas-upnp-org:service:AVTransport:1#AddURIToQueue";
  private static String enqueueSoapBody = "<u:AddURIToQueue xmlns:u=\"urn:schemas-upnp-org:service:AVTransport:1\"> <InstanceID> 0</InstanceID><EnqueuedURI>x-sonos-spotify:spotify%3atrack%3a${code}?sid=9&amp;flags=8224&amp;sn=1 </EnqueuedURI><EnqueuedURIMetaData>[truncated]&lt;DIDL-Lite xmlns:dc=&quot;http://purl.org/dc/elements/1.1/&quot; xmlns:upnp=&quot;urn:schemas-upnp-org:metadata-1-0/upnp/&quot; xmlns:r=&quot;urn:schemas-rinconnetworks-com:metadata-1-0/&quot; xmlns=&quot;urn:schemas-upnp-or</EnqueuedURIMetaData> <DesiredFirstTrackNumberEnqueued> 0</DesiredFirstTrackNumberEnqueued> <EnqueueAsNext> 1</EnqueueAsNext></u:AddURIToQueue>";
  private static String enqueueUrl = "/MediaRenderer/AVTransport/Control";

  private static String resetQueueSoapAction = "urn:schemas-sonos-com:service:Queue:1#RemoveAllTracks";
  private static String resetQueueSoapBody = "<u:RemoveAllTracks xmlns:u=\"urn:schemas-sonos-com:service:Queue:1\"> <QueueID> 0 </QueueID> <UpdateID> 0 </UpdateID> </u:RemoveAllTracks>";
  private static String resetQueueUrl = "/MediaRenderer/Queue/Control";

  private static String setVolumeSoapAction = "urn:schemas-upnp-org:service:RenderingControl:1#SetVolume";
  private static String setVolumeSoapBody = "<u:SetVolume xmlns:u=\"urn:schemas-upnp-org:service:RenderingControl:1\"><InstanceID>0</InstanceID><Channel>Master</Channel><DesiredVolume>${volume}</DesiredVolume></u:SetVolume></s:Body></s:Envelope>";
  private static String setVolumeUrl = "/MediaRenderer/RenderingControl/Control";


  private static String soapBodyStart = "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\"><s:Body>";
  private static String soapBodyEnd = "</s:Body></s:Envelope>";


}
