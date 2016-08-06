package com.bernerus.smartmirror.controller;

import com.bernerus.smartmirror.api.VTTransport;
import com.bernerus.smartmirror.api.VTTransportList;
import com.bernerus.smartmirror.dto.VTToken;
import com.bernerus.smartmirror.model.ApplicationState;
import com.bernerus.smartmirror.model.VasttrafikTokenStore;
import generated.Departure;
import generated.DepartureBoard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * Created by andreas on 03/03/16.
 */
@Service
public class VasttrafikController {
  public static final String MUNKEBACKSMOTET_ID = "9021014004840000";
  public static final String ATTEHOGSGATAN_ID = "9021014007750000";
  public static final String HARLANDA_ID = "9021014003310000";
  public static final String SVINGELN_ID = "9021014006480000";
  public static final String KORSVAGEN_ID = "9021014003980000";
  public static final String MINUTES = "30";
  private static final Logger log = LoggerFactory.getLogger(VasttrafikController.class);
  private final VasttrafikTokenStore tokenStore = VasttrafikTokenStore.getInstance();

  @Autowired
  RestTemplate restTemplate;

  @Autowired
  ApplicationState applicationState;

  public String killToken() {
    tokenStore.killToken();
    return "token destroyed";
  }

  public VTTransportList getUpcomingTransports() {
    if (!applicationState.screenSleeps()) {
      VTTransportList transportList = new VTTransportList();
      List<Departure> allDepartures = new ArrayList<>();
      log.debug("REQUESTING VASTTRAFIK");
      Future<List<Departure>> list1 = getTransports(MUNKEBACKSMOTET_ID, SVINGELN_ID);
      Future<List<Departure>> list2 = getTransports(ATTEHOGSGATAN_ID, SVINGELN_ID);
      Future<List<Departure>> list2b = getTransports(ATTEHOGSGATAN_ID, KORSVAGEN_ID);
      Future<List<Departure>> list3 = getTransports(HARLANDA_ID, SVINGELN_ID);

      try {
        log.debug("GETTING RESPONSES FROM VT...");
        allDepartures.addAll(list1.get());
        allDepartures.addAll(list2.get());
        allDepartures.addAll(list2b.get());
        allDepartures.addAll(filterBusToLindholmen(list3.get()));
        log.debug("VASTTRAFIK RESPONSES RECEIVED");

        Map<String, List<Departure>> groupedDepartures = allDepartures.stream().collect(Collectors.groupingBy(Departure::getName));
        log.debug("Found totally {} departures", allDepartures.size());

        long nowMs = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        groupedDepartures.keySet().forEach(key -> {
          List<Departure> departures = groupedDepartures.get(key);
          log.debug("Found {} departures for {}", departures.size(), key);

          List<Long> timeLeftList = departures.stream().map(departure -> {
            LocalDateTime arriveDateTime = LocalDateTime.parse(departure.getDate() + " " + getTheTime(departure), formatter);
            long arriveMs = arriveDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            return (arriveMs - nowMs) / 1000 / 60;
          }).collect(Collectors.toList());

          VTTransport transport = new VTTransport(departures.get(0).getName(), departures.get(0).getStop(), timeLeftList);
          transportList.getTransports().add(transport);

        });

        Collections.sort(transportList.getTransports(), (departure1, departure2) -> departure1.getLeastTimeLeft().compareTo(departure2.getLeastTimeLeft()));

        return transportList;
      } catch (InterruptedException | ExecutionException e) {
        log.error("Failed to query Vasttrafik!");
      }
    }
    return null;
  }

  private String getTheTime(Departure departure) {
    return departure.getRtTime() != null ? departure.getRtTime() : departure.getTime();
  }

  private List<Departure> filterBusToLindholmen(List<Departure> departures) {
    ArrayList<Departure> filteredList = new ArrayList<>();
    for (Departure departure : departures) {
      if (departure.getDirection().startsWith("Lindholmen")) {
        filteredList.add(departure);
      }
    }
    return filteredList;
  }

  @Async
  private Future<List<Departure>> getTransports(String fromId, String toId) {
    return getTransports(fromId, toId, 1);
  }

  @Async
  private Future<List<Departure>> getTransports(String fromId, String toId, int attempt) {
    VTToken token = tokenStore.getToken();
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
      "&timeSpan=" + MINUTES +
      "&direction=" + toId +
      "&format=xml";
    log.debug(String.format("VT URL:%s", url));
    try {
      ResponseEntity<DepartureBoard> responseEntity = restTemplate.exchange(url, HttpMethod.GET, request, DepartureBoard.class);
      log.debug(responseEntity.getBody().getServertime());
      return new AsyncResult<>(responseEntity.getBody().getDeparture());
    } catch (RestClientException e) {
      if(attempt < 3) {
        log.info("Exception from vasttrafik! Retrying ({}/3)", attempt);
        return getTransports(fromId, toId, attempt + 1);
      }
      log.error("Exception from vasttrafik! Tried three times, returning empty list...", e);
      return new AsyncResult<>(new ArrayList<>());
    }
  }

  private String urlEncode(String s) {
    try {
      return URLEncoder.encode(s, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }
}
