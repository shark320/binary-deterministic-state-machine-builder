package com.vpavlov.machine.exceptions;

import com.vpavlov.machine.State;

public class StartStateSetException extends Exception {

    private final State startState;


    public StartStateSetException(State startState) {
        this.startState = startState;
    }

    public State getStartState() {
        return startState;
    }

}
