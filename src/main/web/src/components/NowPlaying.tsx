import * as React from "react";
import {CSSProperties} from "react";
import styled from "styled-components";
import {AppState, withAppState} from "../context"
import {Col, Glyphicon, ProgressBar, Row} from "react-bootstrap";
import {NowPlaying} from "../api";

interface Props {
    appState: AppState
}

interface State {

}

const NowPlayingInfo = styled.div` 
    display:inline-block;
    font-size: 2em;
    position: relative;
    left: 10px;
    white-space: nowrap;
`;
const NowPlayingIcon = styled.div`
    display:inline-block;
    line-height: 0;
    font-size: 10em;
`;
const ProgressBarWrapper = styled.div`
    height:5px; 
    width: 100%; 
    margin-top: 10px; 
    background-color: #555;
`;

const component = class NowPlayingWidget extends React.Component<Props, State> {

    constructor(props: Props) {
        super(props);
    }

    shouldComponentUpdate(nextProps: Props, nextState: State) {
        return nextProps.appState.nowPlayingMessage.id !== this.props.appState.nowPlayingMessage.id;
    }

    render() {
        const nowPlaying: NowPlaying = this.props.appState.nowPlayingMessage.content;
        if (nowPlaying == null) {
            return null;
        }
        let icon: string = "music";
        let iconNudge: CSSProperties = {position: "relative", top: "0"};
        if (nowPlaying.paused) {
            icon = "pause";
            iconNudge = {position: "relative", top: "-14px"}
        }
        return (
            <div>
                <Row>
                    <Col md={2}>
                        <NowPlayingIcon>
                            <Glyphicon glyph={icon} style={iconNudge}/>
                        </NowPlayingIcon>
                    </Col>
                    <Col md={10}>
                        <NowPlayingInfo>
                            <span className="song-title">{nowPlaying.title}</span><br/>
                            <span className="album-title">{nowPlaying.album}</span><br/>
                            <span className="artist-name">{nowPlaying.artist}</span>
                        </NowPlayingInfo>
                    </Col>
                </Row>
                <Row>
                    <Col md={12} style={{paddingRight:40}}>
                        <ProgressBarWrapper>
                            <ProgressBar min={0} max={nowPlaying.duration} now={nowPlaying.timePassed}/>
                        </ProgressBarWrapper>
                    </Col>
                </Row>
            </div>
        );
    }
};

export default withAppState(component)