import * as React from "react";
import styled from "styled-components";

interface Props {
}

interface State {
    date: Date;
}

const TimeText = styled.h1`
    font-size: 12em;
    position: relative;
    top: -40px;
    left: -15px;
    font-family: 'Helvetica Neue', sans-serif;
    font-weight: 200;
`;


export class Clock extends React.Component<Props, State> {

    constructor(props: Props) {
        super(props);
        this.state = {
            date: new Date(),
        };
        const tick = this.tick.bind(this);
        setInterval(tick, 1000);
    }

    tick() {
        this.setState({
            date: new Date(),
        })
    }

    render() {
        return (
            <div>
                <TimeText>{this.state.date.toLocaleTimeString('sv-SE').substring(0, 8)}</TimeText>
            </div>
        );
    }
}