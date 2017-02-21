package com.bernerus.smartmirror.controller.zwave.http;

import com.bernerus.smartmirror.model.zwave.HttpClientConfig;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

/**
 * Created by andreas on 21/02/17.
 */
public class RazberryHttpClient {

  private static final Logger LOG = LoggerFactory.getLogger(RazberryHttpClient.class);

  protected final String deviceId; // = "11-0-51-rgb";

  private HttpClientConfig httpClientConfig;

  private String httpPrefix;
  private HttpHeaders standardHeaders;

  private RestTemplate restTemplate;

  public RazberryHttpClient(HttpClientConfig clientConfig, String deviceId) {
    this.httpClientConfig = clientConfig;
    this.deviceId = deviceId;
    this.restTemplate = new RestTemplate();

    httpPrefix = "http://" + httpClientConfig.getHost() + ":" + httpClientConfig.getPort();

    String plainCreds = httpClientConfig.getUsername() + ":" + httpClientConfig.getPassword();
    byte[] plainCredsBytes = plainCreds.getBytes();
    byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
    String base64Creds = new String(base64CredsBytes);

    standardHeaders = new HttpHeaders();
    standardHeaders.add("Authorization", "Basic " + base64Creds);
    standardHeaders.add("Accept", "application/json");
    standardHeaders.add("User-Agent", "java");
  }

  private <T> HttpEntity<T> createRequest() {
    return new HttpEntity<>(standardHeaders);
  }

  protected <T> HttpEntity<T> get(String url, Class<T> clazz) {
    try {
      return restTemplate.exchange(httpPrefix + url, HttpMethod.GET, createRequest(), clazz);
    } catch (Exception e) {
      LOG.error("Kass", e);
      return null;
    }
  }

}
