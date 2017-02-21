package com.bernerus.smartmirror.model.zwave;

/**
 * Created by andreas on 21/02/17.
 */
public class HttpClientConfig {
  private String host;
  private String port;
  private String username;
  private String password;

  public HttpClientConfig(String host, String port, String username, String password) {
    this.host = host;
    this.port = port;
    this.username = username;
    this.password = password;
  }

  public String getHost() {
    return host;
  }

  public String getPort() {
    return port;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }
}
