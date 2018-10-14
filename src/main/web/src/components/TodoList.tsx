import * as React from "react";
import styled from "styled-components";
import {AppState, withAppState} from "../context"
import {Task} from "../api";
import {Glyphicon} from "react-bootstrap";

interface Props {
    appState: AppState
}

interface State {

}

const TodoWidget = styled.div`
     margin-top:50px;
    min-height: 110px;
`;
const TodoListHeader = styled.span`
    font-size: 3em;
    margin-left: 0;
    font-weight: lighter;
    line-height: 1.2em;
`;
const Todo = styled.div`
    
`;
const TodoName = styled.span`
    font-size: 1.6em;
    line-height: 1.6em;
`;

const TodoIcon = styled(Glyphicon)`
    font-size: 2em;
    float: left;
    margin-right: 20px;
`;

const component = class TodoList extends React.Component<Props, State> {

    constructor(props: Props) {
        super(props);
    }

    shouldComponentUpdate(nextProps: Props, nextState: State) {
        return nextProps.appState.tasksMessage.id !== this.props.appState.tasksMessage.id;
    }

    render() {
        console.log("Rendering todo list");
        const content: [Task] = this.props.appState.tasksMessage.content;
        if(content == null) {
            return null;
        }
        const tasks = content.map(task => {
            return (
                <Todo key={task.id}>
                    <TodoIcon glyph={"unchecked"}/>
                    <TodoName>{task.name}</TodoName>
                </Todo>
            );
        });
        return (
            <TodoWidget className="clearfix">
                <TodoListHeader>Todo:</TodoListHeader><br/>
                {tasks}
            </TodoWidget>
        );
    }
}

export default withAppState(component)