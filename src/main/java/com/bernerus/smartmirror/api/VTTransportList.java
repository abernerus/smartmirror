package com.bernerus.smartmirror.api;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andreas on 24/01/16.
 */
public class VTTransportList {
  private List<VTTransport> transports = new ArrayList<>();

  public List<VTTransport> getTransports() {
    return transports;
  }

  public void setTransports(List<VTTransport> transports) {
    this.transports = transports;
  }


}
