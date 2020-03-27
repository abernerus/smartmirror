package com.bernerus.smartmirror.controller.asana;

import com.bernerus.smartmirror.controller.AbstractScheduledExecutor;
import com.bernerus.smartmirror.model.asana.AsanaTasks;
import com.bernerus.smartmirror.model.websocket.MessageType;
import com.bernerus.smartmirror.model.websocket.WsMessageSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AsanaScheduledExecutor extends AbstractScheduledExecutor {
    private static final Logger LOG = LoggerFactory.getLogger(AsanaScheduledExecutor.class);
    private AsanaTasks lastTasks = null;
    private AsanaController asanaController;

    public AsanaScheduledExecutor(WsMessageSender messageSender, AsanaController asanaController) {
        super(messageSender);
        this.asanaController = asanaController;
    }

    @Override
    protected int getInitialScheduleIntervalSeconds() {
        return 10;
    }

    @Override
    public void onStart() {
        LOG.info("Started Asana Scheduler");
    }

    @Override
    public void onStop() {
        LOG.info("Stopped Asana Scheduler");
        lastTasks = null;
    }

    @Override
    protected void run() {
//        AsanaTasks tasks = asanaController.getAsanaTasks();
//        if (!tasks.equals(lastTasks)) {
//            lastTasks = tasks;
//            LOG.debug(tasks.toString());
//            messageSender.send(MessageType.TASKS, tasks.getData());
//        }

    }
}
