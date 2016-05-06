package com.bernerus.smartmirror.rest;

import com.bernerus.smartmirror.api.VTTransportList;
import com.bernerus.smartmirror.controller.VasttrafikController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by andreas on 25/12/15.
 */
@Controller
public class VasttrafikRestHandler {

  @Autowired
  VasttrafikController vasttrafikController;

  @RequestMapping("/killtoken")
  public @ResponseBody String killToken() {
    return vasttrafikController.killToken();
  }

  @RequestMapping("/upcoming")
  public @ResponseBody VTTransportList getUpcomingTransports() {
    return vasttrafikController.getUpcomingTransports();
  }

}
