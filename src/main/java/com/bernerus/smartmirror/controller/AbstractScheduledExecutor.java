package com.bernerus.smartmirror.controller;

import com.bernerus.smartmirror.model.websocket.WsMessageSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static java.util.Optional.ofNullable;

public abstract class AbstractScheduledExecutor {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractScheduledExecutor.class);
    protected final WsMessageSender messageSender;
    private int scheduleInterval = getInitialScheduleIntervalSeconds();
    private ScheduledExecutorService scheduledExecutor;
    private ScheduledFuture<?> scheduledFuture;


    public AbstractScheduledExecutor(WsMessageSender messageSender) {
        this.messageSender = messageSender;
    }

    public void stop() {
        ofNullable(scheduledFuture).ifPresent(future -> future.cancel(true));
        ofNullable(scheduledExecutor).ifPresent(ExecutorService::shutdown);
        onStop();
    }

    public void start() {
        if (scheduledExecutor == null || (scheduledExecutor.isShutdown() || scheduledExecutor.isTerminated())) {
            LOG.info("Creating new {} with now playing poll task!", this.getClass().getSimpleName());
            scheduledExecutor = Executors.newScheduledThreadPool(1);
            rescheduleExecutor(scheduleInterval);
        } else {
            LOG.info("{} already running...", this.getClass().getSimpleName());
            rescheduleExecutor(scheduleInterval);
        }
        onStart();
    }

    protected void rescheduleExecutor(int period) {
        ofNullable(scheduledFuture).ifPresent(future -> {
            future.cancel(false);
            try {
                future.get();
            } catch (CancellationException e) {
                LOG.info("Cancelled OK!");
            } catch (InterruptedException | ExecutionException e) {
                LOG.error("Scheduled Future for {} interrupted!", getClass().getSimpleName(), e);
            }
        });

        scheduleInterval = period;
        LOG.info("Rescheduling {} to {} seconds", this.getClass().getSimpleName(), scheduleInterval);
        scheduledFuture = scheduledExecutor.scheduleAtFixedRate(this::runAndCatchErrors, 0, scheduleInterval, TimeUnit.SECONDS);
    }

    private void runAndCatchErrors() {
        try {
            this.run();
        } catch (Throwable e) {
            // Catching everything to not halt the scheduler
            LOG.error("Error in scheduled executor run method!", e);
        }
    }

    public void requestNow(){
        run();
    }

    protected int getScheduleInterval() {
        return scheduleInterval;
    }

    protected abstract int getInitialScheduleIntervalSeconds();

    protected abstract void onStop();

    protected abstract void onStart();

    protected abstract void run();
}
