package com.bernerus.smartmirror.model.websocket;

import java.util.function.BiConsumer;

/**
 * Wrapper for a bi consumer to be sent from WsHandler to Custom Scheduled Executors to access a method for sending websocket messages
 */
public class WsMessageSender {
  private BiConsumer<MessageType, Object> consumer;

  public WsMessageSender(BiConsumer<MessageType, Object> consumer) {
    this.consumer = consumer;
  }

  public void send(MessageType messageType, Object o) {
    this.consumer.accept(messageType, o);
  }
}
