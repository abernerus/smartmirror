package com.bernerus.smartmirror.rest;

import com.bernerus.smartmirror.controller.SonosController;
import com.bernerus.smartmirror.dto.sonos.proxy.TrackInfo;
import com.bernerus.smartmirror.model.ApplicationState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by andreas on 04/01/17.
 */
@Controller
public class SonosRestHandler {
  @Autowired
  SonosController sonosController;

  @Autowired
  ApplicationState applicationState;

  @RequestMapping("/sonos/track")
  public @ResponseBody
  TrackInfo greeting() {
    return sonosController.getNowPlaying();
  }
}
