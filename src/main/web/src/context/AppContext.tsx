import * as React from 'react';
import {ReactNode} from 'react';
import {AppState} from "./AppState";
import {NowPlayingMessage, Task, WsMessage, WsMessageType} from "../api";

const AppContext = React.createContext<AppState>(null);

interface ProviderProps {
    children: ReactNode
}

export class AppContextProvider extends React.Component<ProviderProps, AppState> {
    constructor(props: any) {
        super(props);

        this.state = {
            textMessage: {id: null, content:null, type: WsMessageType.TEXT},
            transportMessage: {id: null, content: null, type: WsMessageType.TRANSPORTS},
            nowPlayingMessage: {id: null, content: null, type: WsMessageType.NOW_PLAYING},
            weatherMessage: {id: null, content: null, type: WsMessageType.WEATHER},
            tasksMessage: {id: null, content: null, type: WsMessageType.TASKS},
            setStateFromWsMessage: this.setStateFromWsMessage.bind(this),
        };
    }

    setStateFromWsMessage(wsMessage: WsMessage<any>) {
        let stateUpdate = {};
        switch (wsMessage.type) {
            case WsMessageType.NOW_PLAYING:
                const nowPlayingMessage: NowPlayingMessage = wsMessage;
                this.addPausedFlagToContent(nowPlayingMessage, false);
                stateUpdate = {nowPlayingMessage: nowPlayingMessage};
                break;
            case WsMessageType.NOW_PLAYING_PAUSED:
                const nowPausedMessage: NowPlayingMessage = wsMessage;
                this.addPausedFlagToContent(nowPausedMessage, true);
                stateUpdate = {nowPlayingMessage: nowPausedMessage};
                break;
            case WsMessageType.TASKS:
                stateUpdate = {tasksMessage: wsMessage};
                break;
            case WsMessageType.TEMPERATURE:
                break;
            case WsMessageType.TEXT:
                stateUpdate = {textMessage: wsMessage};
                break;
            case WsMessageType.TRANSPORTS:
                stateUpdate = {transportMessage: wsMessage};
                break;
            case WsMessageType.WEATHER:
                stateUpdate = {weatherMessage: wsMessage};
                break;
        }
        this.setState(stateUpdate);
    }

    private addPausedFlagToContent(nowPlayingMessage: NowPlayingMessage, b: boolean) {
        if(nowPlayingMessage.content) {
            nowPlayingMessage.content.paused = b;
        }
    }

    render() {
        return (
            <AppContext.Provider value={this.state}>
                {this.props.children}
            </AppContext.Provider>
        );
    }
}

type Omit<T, K extends keyof T> = Pick<T, Exclude<keyof T, K>>;

export function withAppState<P extends { appState: AppState }, R = Omit<P, 'appState'>>(
    Component: React.ComponentClass<P> | React.StatelessComponent<P>): React.SFC<R> {
    return function BoundComponent(props: R) {
        return (
            <AppContext.Consumer>
                {value => value !== undefined ? <Component {...props} appState={value}/> : <span>Loading...</span>}
            </AppContext.Consumer>
        );
    };
}
