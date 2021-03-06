package com.bernerus.smartmirror.controller.vasttrafik;

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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * Created by andreas on 03/03/16.
 */
@Service
public class VasttrafikController {
    public static final String SORHALLSTORGET_ID = "9021014002238000";
    public static final String LINDHOLMEN_ID = "9021014004490000";
    public static final String LINDHOLMSPIREN_ID = "9021014004493000";
    public static final String ERIKSBERG_FARJELAGE_ID = "9021014002239000";
    public static final String KLIPPAN_ID = "9021014003890000";
    public static final String MUNKEBACKSMOTET_ID = "9021014004840000";
    public static final String ATTEHOGSGATAN_ID = "9021014007750000";
    public static final String HARLANDA_ID = "9021014003310000";
    public static final String SVINGELN_ID = "9021014006480000";
    public static final String KORSVAGEN_ID = "9021014003980000";
    public static final String LUNDENSKOLAN_ID = "9021014005090000";
    public static final String VAGNHALLEN_GARDA_ID = "9021014007460000";
    public static final String REDBERGSPLATSEN_ID = "9021014005460000";
    public static final String MINUTES = "61";
    private static final Logger LOG = LoggerFactory.getLogger(VasttrafikController.class);
    private final VasttrafikTokenStore tokenStore = VasttrafikTokenStore.getInstance();

    private final RestTemplate restTemplate;

    private final ApplicationState applicationState;

    public VasttrafikController(RestTemplate restTemplate, ApplicationState applicationState) {
        this.restTemplate = restTemplate;
        this.applicationState = applicationState;
    }

    public String killToken() {
        tokenStore.killToken();
        return "token destroyed";
    }

    public VTTransportList getUpcomingTransports() {
        if(!applicationState.screenSleeps()) {
            VTTransportList transportList = new VTTransportList();
            List<Departure> allDepartures = new ArrayList<>();
            LOG.debug("REQUESTING VASTTRAFIK");
            Future<List<Departure>> list1 = getTransports(LUNDENSKOLAN_ID, VAGNHALLEN_GARDA_ID);
            //Future<List<Departure>> list2 = getTransports(LUNDENSKOLAN_ID, REDBERGSPLATSEN_ID);

            try {
                LOG.debug("GETTING RESPONSES FROM VT...");
                allDepartures.addAll(list1.get());
                //allDepartures.addAll(list2.get());
                LOG.debug("VASTTRAFIK RESPONSES RECEIVED");

                Map<String, List<Departure>> groupedDepartures = allDepartures.stream().collect(Collectors.groupingBy(Departure::getName));
                LOG.debug("Found totally {} departures", allDepartures.size());

                long nowMs = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

                groupedDepartures.keySet().forEach(key -> {
                    List<Departure> departures = groupedDepartures.get(key);
                    LOG.debug("Found {} departures for {}", departures.size(), key);

                    List<Long> timeLeftList = departures.stream()
                            .map(departure -> {
                                LocalDateTime arriveDateTime = LocalDateTime.parse(departure.getDate() + " " + getTheTime(departure), formatter);
                                long arriveMs = arriveDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                                return (arriveMs - nowMs) / 1000 / 60;
                            }).collect(Collectors.toList());

                    VTTransport transport = new VTTransport(departures.get(0).getName(), departures.get(0).getStop(), timeLeftList);
                    transportList.getTransports().add(transport);

                });

                (transportList.getTransports()).sort(Comparator.comparing(VTTransport::getLeastTimeLeft));

                return transportList;
            } catch(InterruptedException | ExecutionException e) {
                LOG.error("Failed to query Vasttrafik!");
            }
        }
        return null;
    }

    private String getTheTime(Departure departure) {
        return departure.getRtTime() != null ? departure.getRtTime() : departure.getTime();
    }

    @Async
    Future<List<Departure>> getTransports(String fromId, String toId) {
        return getTransports(fromId, toId, 1);
    }

    @Async
    Future<List<Departure>> getTransports(String fromId, String toId, int attempt) {
        VTToken token = tokenStore.getToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_XML));
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
        LOG.debug("VT URL:{}", url);
        try {
            ResponseEntity<DepartureBoard> responseEntity = restTemplate.exchange(url, HttpMethod.GET, request, DepartureBoard.class);
            LOG.debug(responseEntity.getBody().getServertime());
            return new AsyncResult<>(responseEntity.getBody().getDeparture());
        } catch(RestClientException e) {
            if(attempt < 3) {
                LOG.warn("Exception from vasttrafik! Retrying ({}/3)", attempt);
                return getTransports(fromId, toId, attempt + 1);
            }
            LOG.error("Exception from vasttrafik! Tried three times, returning empty list...", e);
            return new AsyncResult<>(new ArrayList<>());
        }
    }
}
