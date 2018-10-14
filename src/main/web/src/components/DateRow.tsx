import * as React from "react";
import {Col, Row} from "react-bootstrap";

interface Props {
}

interface State {
    date: Date;
    weekNumber: number
}

export class DateRow extends React.Component<Props, State> {

    constructor(props: Props) {
        super(props);
        this.state = {
            date: new Date(),
            weekNumber: DateRow.getCurrentWeek()
        };
        const weekTick = this.weekTick.bind(this);
        setInterval(weekTick, 5 * 60 * 1000);
    }

    static getCurrentWeek() {
        const d = new Date();
        let onejan = new Date(d.getFullYear(),0,1);
        let today = new Date(d.getFullYear(), d.getMonth(), d.getDate());
        let dayOfYear = ((today.getTime() - onejan.getTime() +1)/86400000);
        return Math.ceil(dayOfYear/7)
    }

    weekTick() {
        this.setState({
            date: new Date(),
            weekNumber: DateRow.getCurrentWeek()
        })
    }

    render() {
        return (
            <Row>
                <Col md={6}>
                    <span>{this.state.date.toISOString().substr(0, 10)}</span>
                </Col>
                <Col md={6} style={{textAlign: "right"}}>
                    <span>Vecka {this.state.weekNumber}</span>
                </Col>
            </Row>
        );
    }
}