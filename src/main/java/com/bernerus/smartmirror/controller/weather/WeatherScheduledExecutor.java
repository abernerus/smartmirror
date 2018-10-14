package com.bernerus.smartmirror.controller.weather;

import com.bernerus.smartmirror.controller.AbstractScheduledExecutor;
import com.bernerus.smartmirror.dto.yr.YrWeather;
import com.bernerus.smartmirror.model.websocket.MessageType;
import com.bernerus.smartmirror.model.websocket.MirrorWebSocketMessage;
import com.bernerus.smartmirror.model.websocket.WsMessageSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public class WeatherScheduledExecutor extends AbstractScheduledExecutor {
    private static final Logger LOG = LoggerFactory.getLogger(WeatherScheduledExecutor.class);

    private WeatherController weatherController;

    private YrWeather previouslyFetchedWeather = null;
    private LocalDateTime previouslyFetchedWeatherTime = LocalDateTime.MIN;

    public WeatherScheduledExecutor(WsMessageSender messageSender, WeatherController weatherController) {
        super(messageSender);
        this.weatherController = weatherController;
    }

    @Override
    protected int getInitialScheduleIntervalSeconds() {
        return 60 * 60; //1h
    }

    @Override
    public void onStart() {
        LOG.info("Started Weather Scheduler");
    }

    @Override
    public void onStop() {
        LOG.info("Stopped Weather Scheduler");
    }

    @Override
    protected void run() {
        if (previouslyFetchedWeather == null || previouslyFetchedWeatherTime.plusMinutes(45).isBefore(LocalDateTime.now())) {
            previouslyFetchedWeather = weatherController.getWeather();
            if (previouslyFetchedWeather != null) {
                previouslyFetchedWeatherTime = LocalDateTime.now();
            }
        } else {
            LOG.debug("Recently fetched weather data. Returning it instead of fetching again.");
        }
        messageSender.send(MessageType.WEATHER, previouslyFetchedWeather);
    }


}
