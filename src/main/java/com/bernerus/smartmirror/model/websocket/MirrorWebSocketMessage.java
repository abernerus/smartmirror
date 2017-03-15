package com.bernerus.smartmirror.model.websocket;

/**
 * Created by andreas on 2017-03-15.
 */
public class MirrorWebSocketMessage<T> {
  private final MessageType type;
  private final T content;

  public MirrorWebSocketMessage(MessageType type, T content) {
    this.type = type;
    this.content = content;
  }

  public MessageType getType() {
    return type;
  }

  public T getContent() {
    return content;
  }
}
