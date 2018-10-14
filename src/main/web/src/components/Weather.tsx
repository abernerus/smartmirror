import * as React from "react";
import styled from "styled-components";
import {AppState, withAppState} from "../context"
import {WeatherMessageContent, WeatherTime} from "../api";

interface Props {
    appState: AppState
}

interface State {

}

const WidgetWrapper = styled.div`
    margin-top:50px;
    margin-right: -5px;
`;

const FromTime = styled.div`
    font-size:1.25em;
    text-align: right;
    margin-bottom: 15px;
`;
const ToTime = styled.div`
    font-size:1.25em;
    text-align: right;
    clear: both;
`;
const WeatherWrapper = styled.div`
    float: right;
    padding-right: 0;
`;
const WeatherData = styled.div`
    float:left;
    width: 100px;
`;
const Temperature = styled.div`
    font-size:4em;
    text-align:left;
`;
const Precipitation = styled.div`
    font-size:1.5em;
    padding-left: 10px;
`;
const WeatherIcon = styled.img`
    width: 120px;
    float:left;
`;

const component = class WeatherWidget extends React.Component<Props, State> {

    constructor(props: Props) {
        super(props);
    }

    shouldComponentUpdate(nextProps: Props, nextState: State) {
        return nextProps.appState.weatherMessage.id !== this.props.appState.weatherMessage.id;
    }

    getDateTimeString(dateTime: WeatherTime) {
        let hour = dateTime.hour + "";
        if (dateTime.hour < 10) {
            hour = "0" + dateTime.hour;
        }

        let min = dateTime.minute + "";
        if (dateTime.minute < 10) {
            min = "0" + dateTime.minute;
        }

        return hour + ":" + min;
    }

    render() {
        console.log("Rendering weather widget");
        const weatherDataList: WeatherMessageContent = this.props.appState.weatherMessage.content;
        if (weatherDataList == null) {
            return "Loading...";
        }
        const weather = weatherDataList.weatherDatas
            .filter((wd, i) => i < 3)
            .map((weather, i) => {
                const key = 'weather-' + i;
                const iconLocation = "../../img/weather/" + weather.symbol.number + ".png";
                let toTimeStyle = {color: "#000", fontSize: "0em"};
                if (i == 2) {
                    toTimeStyle = {color: "#fff", fontSize: "1.25em"}
                }

                return (
                    <div key={key}>
                        <FromTime>
                            {this.getDateTimeString(weather.fromDateTime)}
                        </FromTime>
                        <WeatherWrapper className="clearfix">
                            <WeatherIcon src={iconLocation}/>
                            <WeatherData>
                                <Temperature>{weather.temperature}°</Temperature>
                                <Precipitation>{weather.precipitation.value} mm</Precipitation>
                            </WeatherData>
                        </WeatherWrapper>
                        <ToTime style={toTimeStyle}>
                            {this.getDateTimeString(weather.toDateTime)}
                        </ToTime>
                    </div>
                );
            });

        return (
            <WidgetWrapper>
                {weather}
            </WidgetWrapper>
        );
    }
};

export default withAppState(component)