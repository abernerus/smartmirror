package com.bernerus.smartmirror.controller;

import com.bernerus.smartmirror.dto.yr.YrWeather;
import com.bernerus.smartmirror.model.ApplicationState;
import com.bernerus.smartmirror.util.YrXmlParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import org.apache.commons.io.IOUtils;

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
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_XML));
    headers.add("Cache-Control", "no-cache");
    headers.add("Pragma", "no-cache");
    headers.add("Connection", "keep-alive");
    headers.add("Content-Type", "text/plain");
    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(new LinkedMultiValueMap<>(), headers);

    //Temporarly disabled
    String result = restTemplate.getForObject("http://www.yr.no/place/Sweden/Västra_Götaland/Gothenburg/forecast.xml", String.class);
    //String result = getWeatherFromFile();

//    ResponseEntity<String> responseEntity = restTemplate.exchange("http://www.yr.no/place/Sweden/Västra_Götaland/Gothenburg/forecast.xml", HttpMethod.GET, request, String.class);
    YrWeather weather = YrXmlParser.readYrXml(result);
    return weather;
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
