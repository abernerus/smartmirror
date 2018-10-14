
export enum WsMessageType {
    TRANSPORTS = "TRANSPORTS",
    TEMPERATURE = "TEMPERATURE",
    TEXT = "TEXT",
    WEATHER = "WEATHER",
    NOW_PLAYING = "NOW_PLAYING",
    NOW_PLAYING_PAUSED = "NOW_PLAYING_PAUSED",
    TASKS = "TASKS",
}

export interface WsMessage<T> {
    id: string;
    type: WsMessageType;
    content: T;
}