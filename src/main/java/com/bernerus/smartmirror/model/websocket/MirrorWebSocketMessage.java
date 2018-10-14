package com.bernerus.smartmirror.model.websocket;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.UUID;

/**
 * Created by andreas on 2017-03-15.
 */
public class MirrorWebSocketMessage<T> {
    private final UUID id;
    private final MessageType type;
    private final T content;

    public MirrorWebSocketMessage(MessageType type, T content) {
        this.id = UUID.randomUUID();
        this.type = type;
        this.content = content;
    }

    public MessageType getType() {
        return type;
    }

    public T getContent() {
        return content;
    }

    public UUID getId() {
        return id;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("type", type)
                .append("content", content)
                .toString();
    }
}
