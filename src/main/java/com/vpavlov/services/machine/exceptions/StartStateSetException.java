package com.vpavlov.services.machine.exceptions;

import com.vpavlov.machine.State;

public class StartStateSetException extends Exception {

    private final String startState;


    public StartStateSetException(String startState) {
        this.startState = startState;
    }

    public String getStartStateTitle() {
        return startState;
    }

}
