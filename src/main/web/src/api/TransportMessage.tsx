import {WsMessage} from "./WsMessage";

export interface TransportMessage extends WsMessage<Transports> {

}

export interface Transports {
    transports: [Transport]
}

export interface Transport {
    name: string
    timeLeftList: TransportTimeLeft[]
}

export interface TransportTimeLeft {
    timeLeft:number
    hurryStatus: string
}