package com.bernerus.smartmirror.controller.sonos;

import com.bernerus.smartmirror.controller.AbstractScheduledExecutor;
import com.bernerus.smartmirror.dto.sonos.SonosStatus;
import com.bernerus.smartmirror.dto.sonos.TrackInfo;
import com.bernerus.smartmirror.model.websocket.MessageType;
import com.bernerus.smartmirror.model.websocket.WsMessageSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SonosScheduledExecutor extends AbstractScheduledExecutor {
    private static final Logger LOG = LoggerFactory.getLogger(SonosScheduledExecutor.class);
    private static final long MAX_PAUSE_TIME_MS = 6 * 60 * 1000; // 6 mins in millis
//    private static final long MAX_PAUSE_TIME_MS = 7000; // For testing

    private final SonosController sonosController;

    private Long pausedTime = null;
    private int pauseCheck = 0;
    private boolean somethingPlaying = false;
    private TrackInfo lastPlaying;

    public SonosScheduledExecutor(WsMessageSender messageSender, SonosController sonosController) {
        super(messageSender);
        this.sonosController = sonosController;
        lastPlaying = null;
    }

    @Override
    protected int getInitialScheduleIntervalSeconds() {
        return 5;
    }

    @Override
    protected void onStop() {
        lastPlaying = null;
        pauseCheck = 0;
        somethingPlaying = false;
        pausedTime = null;
    }

    @Override
    protected void onStart() {

    }

    @Override
    protected void run() {
            TrackInfo nowPlaying = sonosController.getNowPlaying();
            LOG.debug(nowPlaying.toString());
            SonosStatus status = getStatus(nowPlaying);
            LOG.debug("Status = {}", status);
            switch (status) {
                case START_PLAYING:
                    somethingPlaying = true;
                    LOG.debug("Start playing, setting scheduler period to 1 second");
                    rescheduleExecutor(1);
                case PLAYING:
                    pauseCheck = 0;
                    pausedTime = null;
                    lastPlaying = nowPlaying;
                    messageSender.send(MessageType.NOW_PLAYING, lastPlaying);
                    break;
                case PAUSE_CHECK:
                    pauseCheck++;
                    break;
                case PAUSED:
                    pausedTime = System.currentTimeMillis();
                    LOG.debug("Sending paused message");
                    messageSender.send(MessageType.NOW_PLAYING_PAUSED, lastPlaying);
                    break;
                case STOP_PLAYING:
                    somethingPlaying = false;
                    LOG.info("Stopped playing, setting scheduler period to 5 seconds");
                    rescheduleExecutor(5);
                    messageSender.send(MessageType.NOW_PLAYING_PAUSED, null);
                    break;
                case IDLE:
                default:
                    // Do nothing;
            }
    }

    private SonosStatus getStatus(TrackInfo nowPlaying) {
        if (nowPlaying.getDuration() > 0) {
            if (!nowPlaying.equals(lastPlaying)) {
                if (!somethingPlaying || getScheduleInterval() > 1) {
                    return SonosStatus.START_PLAYING;
                }
                return SonosStatus.PLAYING;
            } else {
                if (pausedTime == null && getScheduleInterval() < 5 && pauseCheck < 3) {
                    // check paused for 3 seconds before reporting paused. Sonos only has seconds res on relTime so we can get same second even though it is not actually paused
                    return SonosStatus.PAUSE_CHECK;
                } else if (pausedTime == null) {
                    return SonosStatus.PAUSED;
                } else if (somethingPlaying && System.currentTimeMillis() > pausedTime + MAX_PAUSE_TIME_MS) {
                    // Paused for more than MAX_PAUSE_TIME_MS
                    return SonosStatus.STOP_PLAYING;
                }
            }
        } else if(somethingPlaying) {
            return SonosStatus.STOP_PLAYING;
        }
        return SonosStatus.IDLE;
    }
}
