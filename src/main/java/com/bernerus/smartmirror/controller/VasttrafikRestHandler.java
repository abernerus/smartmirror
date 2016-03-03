package com.bernerus.smartmirror.controller;

import com.bernerus.smartmirror.api.VTTransportList;
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

  @RequestMapping("/getid/{destinationName}")
  public @ResponseBody
  String getId(@PathVariable String destinationName, Model model) {
    return vasttrafikController.getId(destinationName, model);
  }

  @RequestMapping("/upcoming")
  public @ResponseBody VTTransportList getUpcomingTransports() {
    return vasttrafikController.getUpcomingTransports();
  }

}
