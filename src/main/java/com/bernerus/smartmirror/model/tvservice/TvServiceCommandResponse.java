package com.bernerus.smartmirror.model.tvservice;

import java.util.Collections;
import java.util.List;

/**
 * Created by andreas on 2017-06-16.
 */
public class TvServiceCommandResponse {
  private final boolean successful;
  private final List<String> responseLine;

  private TvServiceCommandResponse(boolean successful, List<String> responseLine) {
    this.successful = successful;
    this.responseLine = responseLine;
  }

  public static TvServiceCommandResponse failed(String errorMessage) {
    return new TvServiceCommandResponse(false, Collections.singletonList(errorMessage));
  }

  public static TvServiceCommandResponse success(String responseMessage) {
    return new TvServiceCommandResponse(true, Collections.singletonList(responseMessage));
  }

  public static TvServiceCommandResponse success(List<String> responseMessages) {
    return new TvServiceCommandResponse(true, responseMessages);
  }

  public boolean isSuccessful() {
    return successful;
  }

  public List<String> getResponseLine() {
    return responseLine;
  }
}
