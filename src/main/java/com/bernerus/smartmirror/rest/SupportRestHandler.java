package com.bernerus.smartmirror.rest;

import com.bernerus.smartmirror.controller.weather.WeatherController;
import com.bernerus.smartmirror.dto.yr.YrWeather;
import com.bernerus.smartmirror.model.ApplicationState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SupportRestHandler {
  @Autowired
  WeatherController weatherController;

  @Autowired
  ApplicationState applicationState;

  @RequestMapping("/say/{greeting}")
  public @ResponseBody String greeting(@PathVariable String greeting){
    return greeting;
  }

  @RequestMapping("/yr")
  public @ResponseBody YrWeather getYrNow(){
    return weatherController.getWeather();
  }

  @RequestMapping("/simulate/screen/off")
  public @ResponseBody String simulateScreenSleeps(){
    applicationState.setScreenSleeps(true);
    return "Screes sleeps = " + applicationState.screenSleeps();
  }

  @RequestMapping("/simulate/screen/on")
  public @ResponseBody String simulateScreenWakes(){
    applicationState.setScreenSleeps(false);
    return "Screes sleeps = " + applicationState.screenSleeps();
  }

}
//http://www.yr.no/place/Sweden/V%C3%A4stra_G%C3%B6taland/Gothenburg/forecast.xml