//package com.bernerus.smartmirror.controller;
//
//import com.bernerus.smartmirror.dto.NowPlaying;
//import com.bernerus.smartmirror.dto.lastfm.LastFmRecentTracks;
//import com.bernerus.smartmirror.dto.lastfm.LastFmResult;
//import com.bernerus.smartmirror.dto.lastfm.LastFmTrack;
//import com.bernerus.smartmirror.model.ApplicationState;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.List;
//
///**
// * Created by andreas on 03/03/16.
// */
//@Service
//public class LastFmController {
//  private static final Logger log = LoggerFactory.getLogger(LastFmController.class);
//  private final String LAST_FM_API_KEY = "252dc026a57846110647f5cfecee77c3";
//  private final String LAST_FM_USER = "wimpbeef";
//  private final String BASE_URL = "http://ws.audioscrobbler.com/2.0/";
//  private final String RECENT_TRACKS = "user.getrecenttracks";
//
//  @Autowired
//  RestTemplate restTemplate;
//
//  @Autowired
//  ApplicationState applicationState;
//
//
//  public NowPlaying getNowPlaying() {
//    if (!applicationState.screenSleeps()) {
//      log.debug("Requesting LastFm Last playing");
//
//      try {
//        LastFmResult lastFmResult = restTemplate.getForObject(BASE_URL + "?method=" + RECENT_TRACKS + "&user=" + LAST_FM_USER + "&api_key=" + LAST_FM_API_KEY + "&limit=1&format=json", LastFmResult.class);
//
//        List<LastFmTrack> tracks = lastFmResult.getRecenttracks().getTrack();
//        if(!tracks.isEmpty()) {
//          LastFmTrack nowPlaying = tracks.get(0);
//          return new NowPlaying(nowPlaying.getName(), nowPlaying.getArtist().getText(), nowPlaying.getAlbum().getText(), nowPlaying.getNowPlayingAttribute() != null);
//        }
//      } catch (Exception e) {
//        log.error("Error getting last played", e);
//      }
//    }
//    return new NowPlaying("N/A", "N/A", "N/A", false);
//  }
//}
