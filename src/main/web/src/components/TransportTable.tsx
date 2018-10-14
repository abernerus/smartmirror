import * as React from "react";
import styled from "styled-components";
import {AppState, withAppState} from "../context"
import {Transports} from "../api";

interface Props {
    appState: AppState
}

interface State {

}

const TransportsTable = styled.table`
    font-size: 40px;
    width:100%;
`;

const TransportRow = styled.tr`
    
    
`;

/**
 * This has 2 purposes. It sets (in render) always 3 columns, so we always have minumum 3 columns! It also creates the line below Västtrafik title
 */
const HiddenRow = styled.tr`
    border-bottom: 1px solid #fff;
    font-size: 1px;
    color: #000;
`;

const NameCol = styled.td`
    width:40%;
`;

const TimeCol = styled.td`
    width:19%;
`;

const ExtraTimeCol = styled.td`
    width:3%;
    font-size: 24px;
    text-align: right;
    color: #ddd;
`;

const MinSpan = styled.div`
    display:inline-block;
    width: 50px;
    text-align:right;
`;

const MAX_TIME_COLS = 3;

const component = class TransportTable extends React.Component<Props, State> {

    constructor(props: Props) {
        super(props);
    }

    shouldComponentUpdate(nextProps: Props, nextState: State) {
        return nextProps.appState.transportMessage.id !== this.props.appState.transportMessage.id;
    }

    render() {
        console.log("Rendering transports");
        const content: Transports = this.props.appState.transportMessage.content;
        if (content == null || content == undefined) {
            return "Laddar...";
        }
        const transportRows = content.transports.map((transport, i) => {
            const {name, timeLeftList} = transport;
            const rowNumber = i;
            const timeLeftCols = timeLeftList
                .filter(ttl => ttl.timeLeft > 2)
                .map((tll, i) => {
                    if (i < MAX_TIME_COLS) {
                        return <TimeCol key={rowNumber + "-" + i}><MinSpan>{tll.timeLeft}</MinSpan> min</TimeCol>;
                    } else if (i == MAX_TIME_COLS) {
                        //4:th column
                        return <ExtraTimeCol key={rowNumber + "-" + i}>+{timeLeftList.length - MAX_TIME_COLS}</ExtraTimeCol>
                    }
                    return null;
                }).filter(col => col != null);

            if(timeLeftCols.length == 0) {
                return null;
            }

            return (
                <TransportRow key={i}>
                    <NameCol>{name}</NameCol>
                    {timeLeftCols}
                </TransportRow>
            );

        }).filter(row => row !== null);

        if (transportRows.length == 0) {
            transportRows.push(<TransportRow key={"loading"}><NameCol>Inga transporter inom 60 minuter</NameCol></TransportRow>)
        }

        return (
            <div>
                <h1>Västtrafik</h1>
                <TransportsTable>
                    <tbody>
                    <HiddenRow>
                        <td>name</td>
                        <td>t1</td>
                        <td>t2</td>
                        <td>t3</td>
                        <td>t+</td>
                    </HiddenRow>
                    {transportRows}
                    </tbody>
                </TransportsTable>
            </div>
        );
    }
}

export default withAppState(component)