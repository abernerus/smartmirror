package com.bernerus.smartmirror;

import com.bernerus.smartmirror.dto.lastfm.LastFmResult;
import com.bernerus.smartmirror.util.YrXmlParser;
import org.apache.commons.io.IOUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

/**
 * Created by andreas on 30/06/16.
 */
public class TestTest {
  final String json = "{\"recenttracks\":{\"track\":[{\"artist\":{\"#text\":\"Dada Life\",\"mbid\":\"5b3cc848-942f-4363-8e89-b63a13c3fd0e\"},\"name\":\"Tic Tic Tic\",\"streamable\":\"0\",\"mbid\":\"\",\"album\":{\"#text\":\"Tic Tic Tic\",\"mbid\":\"\"},\"url\":\"https://www.last.fm/music/Dada+Life/_/Tic+Tic+Tic\",\"image\":[{\"#text\":\"\",\"size\":\"small\"},{\"#text\":\"\",\"size\":\"medium\"},{\"#text\":\"\",\"size\":\"large\"},{\"#text\":\"\",\"size\":\"extralarge\"}],\"@attr\":{\"nowplaying\":\"true\"}},{\"artist\":{\"#text\":\"Black Stone Cherry\",\"mbid\":\"1801bd47-46ae-49ff-bfcd-6e01b562d880\"},\"name\":\"Such a Shame\",\"streamable\":\"0\",\"mbid\":\"eb24257a-879e-4cf0-b9c2-ad8fe41a7ea4\",\"album\":{\"#text\":\"Between The Devil & The Deep Blue Sea\",\"mbid\":\"dccebe05-6f27-4742-a741-3df7314c1544\"},\"url\":\"https://www.last.fm/music/Black+Stone+Cherry/_/Such+a+Shame\",\"image\":[{\"#text\":\"https://lastfm-img2.akamaized.net/i/u/34s/9ae3044bc25544cda62bf3f4dffa32e6.png\",\"size\":\"small\"},{\"#text\":\"https://lastfm-img2.akamaized.net/i/u/64s/9ae3044bc25544cda62bf3f4dffa32e6.png\",\"size\":\"medium\"},{\"#text\":\"https://lastfm-img2.akamaized.net/i/u/174s/9ae3044bc25544cda62bf3f4dffa32e6.png\",\"size\":\"large\"},{\"#text\":\"https://lastfm-img2.akamaized.net/i/u/300x300/9ae3044bc25544cda62bf3f4dffa32e6.png\",\"size\":\"extralarge\"}],\"date\":{\"uts\":\"1469218633\",\"#text\":\"22 Jul 2016, 20:17\"}}],\"@attr\":{\"user\":\"wimpbeef\",\"page\":\"1\",\"perPage\":\"1\",\"totalPages\":\"15130\",\"total\":\"15130\"}}}";

  private final String LAST_FM_API_KEY = "252dc026a57846110647f5cfecee77c3";
  private final String LAST_FM_USER = "wimpbeef";
  private final String BASE_URL = "http://ws.audioscrobbler.com/2.0/";
  private final String RECENT_TRACKS = "user.getrecenttracks";

  @Test
  public void testXml() throws Exception {
    ClassLoader classLoader = getClass().getClassLoader();

    String xml = IOUtils.toString(classLoader.getResourceAsStream("testweather.xml"), "UTF-8");
    YrXmlParser.readYrXml(xml);
  }

  @Ignore
  @Test
  public void testJson() {
    RestTemplate restTemplate = new RestTemplate();

    LastFmResult jsonResult = restTemplate.getForObject(BASE_URL + "?method=" + RECENT_TRACKS + "&user=" + LAST_FM_USER + "&api_key=" + LAST_FM_API_KEY + "&limit=1&format=json", LastFmResult.class);

//    JsonObject jsonRoot = new Gson().fromJson(jsonResult, JsonObject.class);
//    JsonObject recenttracks = jsonRoot.getAsJsonObject("recenttracks");
//    JsonArray tarcks = recenttracks.getAsJsonArray("track");
//    JsonObject nowPlayingJson = tarcks.get(0).getAsJsonObject();
//
//    String songName = nowPlayingJson.getAsJsonPrimitive("name").getAsString();
//    String artistName = nowPlayingJson.getAsJsonObject("artist").getAsJsonPrimitive("#text").getAsString();
//    String albumName = nowPlayingJson.getAsJsonObject("album").getAsJsonPrimitive("#text").getAsString();


//    NowPlaying nowPlaying = new NowPlaying(songName, artistName, albumName);
    System.out.println(jsonResult);
  }
}
