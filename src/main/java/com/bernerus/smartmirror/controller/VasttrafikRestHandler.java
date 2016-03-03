package com.bernerus.smartmirror.controller;

import com.bernerus.smartmirror.api.VTTransportList;
import com.bernerus.smartmirror.model.VasttrafikTokenStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
  public static final String MUNKEBACKSMOTET_ID = "9021014004840000";
  public static final String ATTEHOGSGATAN_ID = "9021014007750000";
  public static final String HARLANDA_ID = "9021014003310000";
  public static final String SVINGELN_ID = "9021014006480000";
  private static final Logger log = LoggerFactory.getLogger(VasttrafikRestHandler.class);
  private final VasttrafikTokenStore tokenStore = VasttrafikTokenStore.getInstance();

  @Autowired
  VasttrafikController vasttrafikController;

  @RequestMapping("/killtoken")
  public
  @ResponseBody
  String killToken() {
    return vasttrafikController.killToken();
  }

  @RequestMapping("/getid/{destinationName}")
  public
  @ResponseBody
  String getId(
    @PathVariable
    String destinationName, Model model) {
    return vasttrafikController.getId(destinationName, model);
  }

  @RequestMapping("/upcoming")
  public
  @ResponseBody
  VTTransportList getUpcomingTransports() {
    return vasttrafikController.getUpcomingTransports();
  }

}
