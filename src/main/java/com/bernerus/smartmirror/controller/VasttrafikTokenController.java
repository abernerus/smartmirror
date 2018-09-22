package com.bernerus.smartmirror.controller;


import com.bernerus.smartmirror.dto.VTToken;
import com.bernerus.smartmirror.dto.VTTokenResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by andreas on 25/12/15.
 */
@Controller
public final class VasttrafikTokenController {
  private static final Logger log = LoggerFactory.getLogger(VasttrafikTokenController.class);

  private VasttrafikTokenController() {
  }

  public static VTToken refresh() {

    try {
      SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(
        new SSLContextBuilder()
          .loadTrustMaterial(null, new TrustSelfSignedStrategy()).build());

      //.loadKeyMaterial(keyStore, "password".toCharArray()

      HttpClient httpClient = HttpClients.custom().setSSLSocketFactory(socketFactory).build();

      HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
      requestFactory.setReadTimeout(15000);
      requestFactory.setConnectTimeout(15000);

      // Spring Rest!
      try {
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        // Set auth headers & Västtrafiks grant_type
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Authorization", "Basic aGMzTHJ3UW5rWm5rRlFHZlhDTDFkMUlFTmZZYTpmZ0xFUEZsN3FFY2VwZmp3WUlTcHExSlFJZG9h");
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "client_credentials");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        //HTTPS!
        String url = "https://api.vasttrafik.se:443/token";
        ResponseEntity<VTTokenResponse> response = restTemplate.exchange(url, HttpMethod.POST, request, VTTokenResponse.class);

        //Spring has automagically parsed response to VTTokenResponse
        VTTokenResponse vtTokenResponse = response.getBody();


        long expires = System.currentTimeMillis() + (Long.parseLong(vtTokenResponse.getExpires_in()) * 1000);

        //Map it to  a more useful object
        VTToken token = new VTToken(expires, vtTokenResponse.getAccess_token());
        log.debug("New token received with key: " + token.getAccessKey());
        return token;
      } catch (RestClientException e) {
        log.error("Could not get new token :( Retrying!", e);
        return VasttrafikTokenController.refresh();
      }


    } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
      e.printStackTrace();
    }

    return null;
  }


}
