import * as React from "react";
import {Col, Grid, Row} from "react-bootstrap";
import styled from "styled-components";
import {AppContextProvider} from './context';
import {Clock} from "./components/Clock";
import {DateRow} from "./components/DateRow";
import WeatherWidget from "./components/Weather";
import ServerMessages from "./components/ServerMessages";
import {WebSocketClient} from "./components/WebsocketClient";
import TransportTable from "./components/TransportTable";
import NowPlayingWidget from "./components/NowPlaying";
import TodoList from "./components/TodoList";

const BottomSection = styled.div`
    position: fixed;
    bottom: 100px;
    left: 21px;
    right: 21px;
`;

interface AppProps {
}

interface AppState {

}

export class App extends React.Component<AppProps, AppState> {

    constructor(props: AppProps) {
        super(props);
    }


    render() {
        return (
            <AppContextProvider>
                <WebSocketClient/>

                <Grid fluid>
                    <DateRow/>
                    <Row>
                        <Col md={9}>
                            <Row>
                                <Col md={12}>
                                    <Clock/>
                                </Col>
                                <Col md={12}>
                                    <NowPlayingWidget/>
                                </Col>
                                <Col md={12}>
                                    <TodoList/>
                                </Col>
                            </Row>
                        </Col>
                        <Col md={3}>
                            <WeatherWidget />
                        </Col>
                    </Row>
                </Grid>
                <BottomSection>
                    <Grid fluid>
                        <Row>
                            <Col md={6}>

                            </Col>
                            <Col mdOffset={6} md={6}>
                                {/*<ServerMessages/>*/}
                            </Col>
                        </Row>
                        <Row>
                            <Col md={12}>
                                <TransportTable/>
                            </Col>
                        </Row>

                    </Grid>
                </BottomSection>
            </AppContextProvider>
        );
    }
}
