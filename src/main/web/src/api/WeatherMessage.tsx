import {WsMessage} from "./WsMessage";

export interface WeatherMessage extends WsMessage<WeatherMessageContent> {

}

export interface WeatherMessageContent {
    iconType: string
    weatherDatas: [WeatherData]
}

export interface WeatherData {
    fromDateTime: WeatherTime
    toDateTime: WeatherTime
    precipitation: WeatherPrecipitation
    symbol: WeatherSymbol
    temperature: string
}

export interface WeatherPrecipitation {
    value: string
    minvalue: string
    maxvalue: string
}

export interface WeatherSymbol {
    name: string
    number: number
    var: string
}

export interface WeatherTime {
    dayOfMonth: number
    dayOfWeek: string
    dayOfYear: number
    hour: number
    minute: number
    month: string
    monthValue: number
    nano: number
    second: number
    year: number
}

