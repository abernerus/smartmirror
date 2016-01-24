package com.bernerus.smartmirror.model;

import com.bernerus.smartmirror.controller.VasttrafikTokenController;
import com.bernerus.smartmirror.dto.VTToken;

/**
 * Created by andreas on 24/01/16.
 */
public class VasttrafikTokenStore {
  private static VasttrafikTokenStore instance = null;

  private VTToken token;

  private VasttrafikTokenStore() {
    // Exists only to defeat instantiation.
  }

  public static VasttrafikTokenStore getInstance() {
    if(instance == null) {
      instance = new VasttrafikTokenStore();
    }
    return instance;
  }

  public VTToken getToken() {
    if(token == null || token.getExpires() < System.currentTimeMillis()) {
      token = VasttrafikTokenController.refresh();
    }
    return token;
  }

  public void killToken() {
    token = null;
  }
}
