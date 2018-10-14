package com.bernerus.smartmirror.controller.vasttrafik;

import com.bernerus.smartmirror.api.VTTransportList;
import com.bernerus.smartmirror.controller.AbstractScheduledExecutor;
import com.bernerus.smartmirror.model.asana.AsanaTasks;
import com.bernerus.smartmirror.model.websocket.MessageType;
import com.bernerus.smartmirror.model.websocket.MirrorWebSocketMessage;
import com.bernerus.smartmirror.model.websocket.WsMessageSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public class VasttrafikScheduledExecutor extends AbstractScheduledExecutor {
    private static final Logger LOG = LoggerFactory.getLogger(VasttrafikScheduledExecutor.class);

    private VasttrafikController vasttrafikController;

    private VTTransportList previouslyFetchedUpcomingTransports = null;
    private LocalDateTime previouslyFetchedUpcomingTransportsTime = LocalDateTime.MIN;

    public VasttrafikScheduledExecutor(WsMessageSender messageSender, VasttrafikController vasttrafikController) {
        super(messageSender);
        this.vasttrafikController = vasttrafikController;
    }

    @Override
    protected int getInitialScheduleIntervalSeconds() {
        return 60;
    }

    @Override
    public void onStart() {
        LOG.info("Started Västtrafik Scheduler");
    }

    @Override
    public void onStop() {
        LOG.info("Stopped Västtrafik Scheduler");
    }

    @Override
    protected void run() {
        if (isEmpty(previouslyFetchedUpcomingTransports) || previouslyFetchedUpcomingTransportsTime.plusSeconds(30).isBefore(LocalDateTime.now())) {
            previouslyFetchedUpcomingTransports = vasttrafikController.getUpcomingTransports();
            if (previouslyFetchedUpcomingTransports != null) {
                previouslyFetchedUpcomingTransportsTime = LocalDateTime.now();
            }
        } else {
            LOG.debug("Recently fetched västtrafik data. Returning it instead of fetching again.");
        }
        messageSender.send(MessageType.TRANSPORTS, previouslyFetchedUpcomingTransports);
    }

    private boolean isEmpty(VTTransportList transportList) {
        return transportList == null || transportList.getTransports() == null || transportList.getTransports().isEmpty();
    }
}
