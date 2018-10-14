import {WsMessage} from "./WsMessage";

export interface TasksMessage extends WsMessage<[Task]>{

}

export interface Task {
    id: string
    name: string
}