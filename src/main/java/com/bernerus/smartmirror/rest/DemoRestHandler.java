package com.bernerus.smartmirror.rest;

import com.bernerus.smartmirror.api.VTTransportList;
import com.bernerus.smartmirror.controller.WeatherController;
import com.bernerus.smartmirror.dto.yr.YrWeather;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DemoRestHandler {
  @Autowired
  WeatherController weatherController;

  @RequestMapping("/say/{greeting}")
  public @ResponseBody String greeting(@PathVariable String greeting){
    return greeting;
  }

  @RequestMapping("/yr")
  public @ResponseBody YrWeather getYrNow(){
    return weatherController.getWeather();
  }

}
//http://www.yr.no/place/Sweden/V%C3%A4stra_G%C3%B6taland/Gothenburg/forecast.xml