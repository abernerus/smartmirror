package com.bernerus.smartmirror.model.websocket;

/**
 * Created by andreas on 2017-03-15.
 */
public class MirrorWebSocketMessage<T> {
  private final MessageType messageType;
  private final T message;

  public MirrorWebSocketMessage(MessageType messageType, T message) {
    this.messageType = messageType;
    this.message = message;
  }

  public MessageType getMessageType() {
    return messageType;
  }

  public T getMessage() {
    return message;
  }
}
