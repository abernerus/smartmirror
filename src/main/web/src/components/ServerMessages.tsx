import * as React from "react";
import styled from "styled-components";
import {AppState, withAppState} from "../context"
import {TextMessage, WsMessageType} from "../api";

interface Props {
    appState: AppState,
}

interface State {
    message: TextMessage[];
}

const ServerText = styled.span`
    font-size: 12px;
`;


const component = class ServerMessages extends React.Component<Props, State> {

    constructor(props: Props) {
        super(props);
        this.state = {
            message: [{id: null, content: "Starting...", type: WsMessageType.TEXT}],
        };
    }

    shouldComponentUpdate(nextProps:Props, nextState:State) {
        return nextProps.appState.textMessage.id !== this.props.appState.textMessage.id;
    }

    static getDerivedStateFromProps(nextProps: Props, prevState: State) {
        let textMessage: TextMessage = nextProps.appState.textMessage;
        if (textMessage !== null && (textMessage.id == null || ServerMessages.lastMessageIdDiffer(textMessage, prevState.message))) {
            const messages = prevState.message;
            if (messages.length > 5) {
                messages.shift();
            }
            return {
                message: [...messages, textMessage]
            };
        }
        return null;
    }

    private static lastMessageIdDiffer(textMessage: TextMessage, prevMessages: TextMessage[]) {
        if (prevMessages.length > 0) {
            return textMessage.id != prevMessages[prevMessages.length - 1].id;
        }
        return true;
    }

    render() {
        const messages = this.state.message.map((msg, i) => <ServerText key={i + "-" + msg.id}>{msg.content}<br/></ServerText>);
        return (
            <div>
                <ServerText>{messages}</ServerText>
            </div>
        );
    }
}

export default withAppState(component)