package com.bernerus.smartmirror.controller.sonos;

import com.bernerus.smartmirror.dto.sonos.SonosStatus;
import com.bernerus.smartmirror.dto.sonos.TrackInfo;
import com.bernerus.smartmirror.model.websocket.MessageType;
import com.bernerus.smartmirror.model.websocket.WsMessageSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class SonosScheduledExecutor {
  private static final long MAX_PAUSE_TIME_MS = 6 * 60 * 1000; // 6 mins in millis
  private static final Logger LOG = LoggerFactory.getLogger(SonosScheduledExecutor.class);
  private final WsMessageSender messageSender;

  private ScheduledExecutorService scheduledExecutor;
  private ScheduledFuture<?> scheduledFuture;

  private final SonosController sonosController;

  private Long pausedTime = null;
  private int pauseCheck = 0;
  private boolean somethingPlaying = false;
  private TrackInfo lastPlaying;
  private int sonosScheduleInterval = 5;

  public SonosScheduledExecutor(WsMessageSender messageSender, SonosController sonosController) {
    this.messageSender = messageSender;
    this.sonosController = sonosController;
    lastPlaying = null;
    scheduledExecutor = Executors.newScheduledThreadPool(1);
  }

  public void stop() {
    LOG.info("Stopping sonos scheduled executor");
    scheduledFuture.cancel(true);
    scheduledExecutor.shutdown();
    lastPlaying = null;
  }

  public void start() {
    LOG.info("Creating new sonosExecutorExecutor with now playing poll task!");
    scheduleSonosTask(5);
  }

  private void scheduleSonosTask(int period) {
    try {
      if (scheduledFuture != null) {
        scheduledFuture.cancel(false);
      }
      scheduledExecutor.awaitTermination(2, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      LOG.warn("Could not terminate scheduledExecutor", e);
    }
    sonosScheduleInterval = period;
    LOG.debug("Rescheduling sonos to {} seconds", sonosScheduleInterval);
    scheduledFuture = scheduledExecutor.scheduleAtFixedRate(this::requestNowPlaying, 1, sonosScheduleInterval, TimeUnit.SECONDS);
  }

  private void requestNowPlaying() {
    try {
      TrackInfo nowPlaying = sonosController.getNowPlaying();
      LOG.debug(nowPlaying.toString());
      SonosStatus status = getStatus(nowPlaying);
      LOG.debug("Status = {}", status);
      switch (status) {
        case START_PLAYING:
          somethingPlaying = true;
          LOG.debug("Start playing, setting scheduler period to 1 second");
          scheduleSonosTask(1);
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
          LOG.debug("Stopped playing, setting scheduler period to 5 seconds");
          scheduleSonosTask(5);
          messageSender.send(MessageType.NOW_PLAYING, null);
          break;
        case IDLE:
        default:
          // Do nothing;
      }
    } catch (Throwable e) {
      // Catching everything to not halt the scheduler
      LOG.error("Error requesting sonos data!", e);
    }
  }

  private SonosStatus getStatus(TrackInfo nowPlaying) {
    if (!nowPlaying.equals(lastPlaying)) {
      if (!somethingPlaying || sonosScheduleInterval > 1) {
        return SonosStatus.START_PLAYING;
      }
      return SonosStatus.PLAYING;
    } else if (nowPlaying.getTitle() != null) {
      if (pausedTime == null && sonosScheduleInterval < 5 && pauseCheck < 3) {
        // check paused for 3 seconds before reporting paused. Sonos only has seconds res on relTime so we can get same second even though it is not actually paused
        return SonosStatus.PAUSE_CHECK;
      } else if (pausedTime == null) {
        return SonosStatus.PAUSED;
      } else if (somethingPlaying && System.currentTimeMillis() > pausedTime + MAX_PAUSE_TIME_MS) {
        // Paused for more than MAX_PAUSE_TIME_MS
        return SonosStatus.STOP_PLAYING;
      }
    }
    return SonosStatus.IDLE;
  }
}
