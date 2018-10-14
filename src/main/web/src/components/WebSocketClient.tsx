import * as React from "react";
import {AppState, withAppState} from "../context";
import {TextMessage, WsMessage, WsMessageType} from "../api";

interface Props {
    appState: AppState
}

interface State {
}

interface JsonWsMessage {
    data: string;
}

const MAX_RETRIES = 10;

const client = class WebSocketClient extends React.Component<Props, State> {
    private retries : number = 0;
    private readonly wsUri: string;

    constructor(props: Props) {
        super(props);
        console.log("Constructing ws client...");
        console.log(window.location.host);
        this.wsUri = "ws://" + window.location.host + "/transportsHandler";
        this.createWsClient();
    }

    createWsClient() {
        console.log("Opening ws to: ", this.wsUri);
        const ws = new WebSocket(this.wsUri);
        ws.onopen = this.onOpen.bind(this);
        ws.onmessage = this.onMessage.bind(this);
        ws.onclose = this.onClose.bind(this);
        ws.onerror = this.onError.bind(this);
    }

    onOpen() {
        this.sendMessage("Socket has been opened!");
    }
    onError() {
        this.sendMessage("Socket has been opened!");
    }

    private sendMessage(message: string) {
        const textMessage: TextMessage = {id: "onOpen", content: message, type: WsMessageType.TEXT}
        this.props.appState.setStateFromWsMessage(textMessage);
    }

    onMessage(jsonMessage: JsonWsMessage) {
        const message: WsMessage<any> = JSON.parse(jsonMessage.data);
        if (message) {
            this.props.appState.setStateFromWsMessage(message);
        }
    }

    onClose() {
        this.sendMessage("Socket has been closed!");
        //Try to reopen connection!
        this.retries++;
        let delay = this.retries * 1000;
        console.log("Retrying in: ", delay);
        setTimeout(() => this.reconnect(), delay);
    }

    reconnect() {
        this.createWsClient();
    }


    render() {
        return (
            <span/>
        );
    }
};

export const WebSocketClient = withAppState(client);