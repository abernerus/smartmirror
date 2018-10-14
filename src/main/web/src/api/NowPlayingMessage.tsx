import {WsMessage} from "./WsMessage";

export interface NowPlayingMessage extends WsMessage<NowPlaying> {

}

export interface NowPlaying {
    duration: number
    timePassed: number
    album: string
    artist: string
    title: string
    paused: boolean
}

