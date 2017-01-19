package com.bernerus.smartmirror.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;

/**
 * Created by andreas on 31/12/16.
 */
@EnableScheduling
@Controller
public class Scheduler {
  private static final Logger log = LoggerFactory.getLogger(Scheduler.class);

  @PostConstruct
  public void init() {
    log.info("Started scheduler");
  }

  //0 15 10 15 * ?
  @Scheduled(cron = "${lights.morning.cron}")
  public void scheduleTaskUsingCronExpression() {
    long now = System.currentTimeMillis() / 1000;
    log.info("Running morning script - " + now);
  }
}
