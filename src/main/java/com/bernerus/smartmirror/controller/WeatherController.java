package com.bernerus.smartmirror.controller;

import com.bernerus.smartmirror.dto.yr.YrWeather;
import com.bernerus.smartmirror.model.ApplicationState;
import com.bernerus.smartmirror.util.YrXmlParser;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by andreas on 03/03/16.
 */
@Service
public class WeatherController {
  private static final Logger log = LoggerFactory.getLogger(WeatherController.class);

  @Autowired
  RestTemplate restTemplate;

  @Autowired
  ApplicationState applicationState;


  public YrWeather getWeather() {
    if (!applicationState.screenSleeps()) {
      String result = restTemplate.getForObject("http://www.yr.no/place/Sweden/Västra_Götaland/Gothenburg/forecast.xml", String.class);
      //String result = getWeatherFromFile();

      log.info("Requesting YrWeather");
      YrWeather weather = YrXmlParser.readYrXml(result);
      log.info("Received weather times: " + weather.getWeatherDatas().size());
      return weather;
    }
    return null;
  }

  private String getWeatherFromFile() {
    String result = "";
    try {
      ClassLoader classLoader = getClass().getClassLoader();
      result = IOUtils.toString(classLoader.getResourceAsStream("testweather.xml"), "UTF-8");
    } catch (IOException e) {
      e.printStackTrace();
    }
    return result;
  }

  private String urlEncode(String s) {
    try {
      return URLEncoder.encode(s, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }
}
