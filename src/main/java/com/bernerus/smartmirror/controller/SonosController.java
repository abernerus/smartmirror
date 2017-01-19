package com.bernerus.smartmirror.controller;

import com.bernerus.smartmirror.dto.sonos.proxy.TrackInfo;
import com.bernerus.smartmirror.model.ApplicationState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@Service
public class SonosController {
  private static final Logger log = LoggerFactory.getLogger(SonosController.class);

  @Value("${sonos.proxy.host}")
  private String sonosProxyHost;

  @Value("${sonos.proxy.port}")
  private String sonosProxyPort;

  private String baseUrl;

  @PostConstruct
  private void init() {
    baseUrl = "http://" + sonosProxyHost + ":" + sonosProxyPort;
  }

  @Autowired
  RestTemplate restTemplate;

  @Autowired
  ApplicationState applicationState;

  public TrackInfo getNowPlaying() {
    if (!applicationState.screenSleeps()) {
      log.debug("Requesting Spotify data from proxy @ {}:{}", sonosProxyHost, sonosProxyPort);
      try {
        TrackInfo result = restTemplate.getForObject(baseUrl + "/info", TrackInfo.class);

        return result;
      } catch (Exception e) {
        log.error("Error getting last played", e);
      }
    }
    return new TrackInfo();
  }
}
