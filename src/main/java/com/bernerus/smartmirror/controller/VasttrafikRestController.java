package com.bernerus.smartmirror.controller;

import com.bernerus.smartmirror.api.VTTransport;
import com.bernerus.smartmirror.api.VTTransportList;
import com.bernerus.smartmirror.dto.VTToken;
import com.bernerus.smartmirror.model.VasttrafikTokenStore;
import generated.Arrival;
import generated.ArrivalBoard;
import generated.LocationList;
import generated.StopLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by andreas on 25/12/15.
 */
@Controller
public class VasttrafikRestController {
  private static final Logger log = LoggerFactory.getLogger(VasttrafikRestController.class);
  private static final String MUNKEBACKSMOTET_ID = "9021014004840000";
  private static final String ATTEHOGSGATAN_ID = "9021014007750000";
  private static final String HARLANDA_ID = "9021014003310000";
  private static final String SVINGELN_ID = "9021014006480000";
  private static final String LINDHOLMEN_ID = "9021014004490000";

  private final VasttrafikTokenStore tokenStore = VasttrafikTokenStore.getInstance();



  @RequestMapping("/killtoken")
  public @ResponseBody String killToken() {
    tokenStore.killToken();
    return "token destroyed";
  }

  @RequestMapping("/getid/{destinationName}")
  public @ResponseBody String getId(@PathVariable String destinationName, Model model) {
    VTToken token = tokenStore.getToken();

    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_XML));
    headers.add("Authorization", "Bearer " + token.getAccessKey());
    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(new LinkedMultiValueMap<>(), headers);
    String destination = urlEncode(destinationName);

    String url = "https://api.vasttrafik.se/bin/rest.exe/v2/location.name?format=xml&input="+destination;
    log.info(url);
    ResponseEntity<LocationList> response = restTemplate.exchange(url, HttpMethod.GET, request, LocationList.class);

    log.info(response.toString());

    StopLocation location = (StopLocation) response.getBody().getStopLocationOrCoordLocation().get(0);

    model.addAttribute("name", location.getId());
    return "greeting";
  }

  @RequestMapping("/upcoming")
  public @ResponseBody VTTransportList getUpcomingTransports() {

    List<Arrival> allArrivals = new ArrayList<>();
    allArrivals.addAll(getTransports(MUNKEBACKSMOTET_ID, SVINGELN_ID));
    allArrivals.addAll(getTransports(ATTEHOGSGATAN_ID, SVINGELN_ID));
    allArrivals.addAll(getTransports(HARLANDA_ID, LINDHOLMEN_ID));

    //Sort per time
    Collections.sort(allArrivals, (arrival1, arrival2) -> arrival1.getTime().compareTo(arrival2.getTime()));

    VTTransportList transportList = new VTTransportList();
    for(Arrival arrival : allArrivals) {
      log.info(arrival.getName() + " " + arrival.getTime());
      transportList.getTransports().add(new VTTransport(arrival.getName(), arrival.getDate(), arrival.getTime()));
    }

    return transportList;
  }

  private List<Arrival> getTransports(String fromId, String toId) {
    VTToken token = tokenStore.getToken();
    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_XML));
    headers.add("Authorization", "Bearer " + token.getAccessKey());
    headers.add("Cache-Control", "no-cache");
    headers.add("Pragma", "no-cache");
    headers.add("Connection", "keep-alive");
    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(new LinkedMultiValueMap<>(), headers);
    LocalDateTime currentTime = LocalDateTime.now();

    String url = "https://api.vasttrafik.se/bin/rest.exe/v2/arrivalBoard" +
      "?id=" + fromId +
      "&date=" + currentTime.getYear() + "-" + currentTime.getMonthValue() + "-" + currentTime.getDayOfMonth() +
      "&time=" + currentTime.getHour() + ":" + currentTime.getMinute() +
      "&useVas=0" +
      "&useLDTrain=0" +
      "&useRegTrain=0" +
      "&timeSpan=30" +
      "&direction=" + toId +
      "&format=xml";
    log.info(url);
    ResponseEntity<ArrivalBoard> response = restTemplate.exchange(url, HttpMethod.GET, request, ArrivalBoard.class);
    log.info(response.getBody().getServertime());
    return response.getBody().getArrival();
  }

  private String urlEncode(String s) {
    try {
      return URLEncoder.encode(s, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }

}
