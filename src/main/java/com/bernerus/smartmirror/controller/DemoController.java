package com.bernerus.smartmirror.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DemoController {
  @RequestMapping("/say/{greeting}")
  public @ResponseBody String greeting(@PathVariable String greeting){
    return greeting;
  }
}