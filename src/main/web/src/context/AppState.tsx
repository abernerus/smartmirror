import {WsMessage, TransportMessage, NowPlayingMessage, WeatherMessage, TextMessage, TasksMessage} from "../api"



export interface AppState {
    textMessage: TextMessage
    transportMessage: TransportMessage
    nowPlayingMessage: NowPlayingMessage
    weatherMessage: WeatherMessage
    tasksMessage: TasksMessage
    setStateFromWsMessage: (wsMessage: WsMessage<any>) => void
}