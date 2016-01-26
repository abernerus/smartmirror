package com.bernerus.smartmirror.controller;

import com.bernerus.smartmirror.api.VTTransport;
import com.bernerus.smartmirror.api.VTTransportList;
import com.bernerus.smartmirror.dto.VTToken;
import com.bernerus.smartmirror.model.VasttrafikTokenStore;
import generated.Departure;
import generated.DepartureBoard;
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
import se.vasttrafik.api.location.LocationList;
import se.vasttrafik.api.location.StopLocation;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by andreas on 25/12/15.
 */
@Controller
public class VasttrafikRestController {
  private static final Logger log = LoggerFactory.getLogger(VasttrafikRestController.class);
  public static final String MUNKEBACKSMOTET_ID = "9021014004840000";
  public static final String ATTEHOGSGATAN_ID = "9021014007750000";
  public static final String HARLANDA_ID = "9021014003310000";
  public static final String SVINGELN_ID = "9021014006480000";

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

    String url = "https://api.vasttrafik.se/bin/rest.exe/v2/location.name?format=xml&input=" + destination;
    log.info(url);
    ResponseEntity<LocationList> response = restTemplate.exchange(url, HttpMethod.GET, request, LocationList.class);

    log.info(response.toString());

    StopLocation location = (StopLocation) response.getBody().getStopLocationOrCoordLocation().get(0);

    model.addAttribute("name", location.getId());
    return "greeting";
  }

  @RequestMapping("/upcoming")
  public @ResponseBody VTTransportList getUpcomingTransports() {

    List<Departure> allDepartures = new ArrayList<>();
    allDepartures.addAll(getTransports(MUNKEBACKSMOTET_ID, SVINGELN_ID));
    allDepartures.addAll(getTransports(ATTEHOGSGATAN_ID, SVINGELN_ID));
    allDepartures.addAll(filterBusToLindholmen(getTransports(HARLANDA_ID, SVINGELN_ID)));

    //Sort per time
    Collections.sort(allDepartures, (departure1, departure2) -> departure1.getTime().compareTo(departure2.getTime()));

    VTTransportList transportList = new VTTransportList();
    for(Departure departure : allDepartures) {
      log.info(departure.getName() + " " + departure.getTime());
      VTTransport transport = new VTTransport(departure.getName(), departure.getDate(), departure.getTime(), departure.getStop());
      if(transport.getTimeLeft() > 0) {
        transportList.getTransports().add(transport);
      }
    }

    return transportList;
  }

  private List<Departure> filterBusToLindholmen(List<Departure> departures) {
    ArrayList<Departure> filteredList = new ArrayList<>();
    for(Departure departure : departures) {
      if(departure.getDirection().startsWith("Lindholmen")) {
        filteredList.add(departure);
      }
    }
    return filteredList;
  }

  private List<Departure> getTransports(String fromId, String toId) {
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

    String url = "https://api.vasttrafik.se/bin/rest.exe/v2/departureBoard" +
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
    ResponseEntity<DepartureBoard> response = restTemplate.exchange(url, HttpMethod.GET, request, DepartureBoard.class);
    log.info(response.getBody().getServertime());
    return response.getBody().getDeparture();
  }

  private String urlEncode(String s) {
    try {
      return URLEncoder.encode(s, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }

}
