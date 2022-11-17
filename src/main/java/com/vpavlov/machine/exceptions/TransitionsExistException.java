package com.vpavlov.machine.exceptions;

import com.vpavlov.machine.State;

import java.util.Map;

public class TransitionsExistException extends Exception {


    private final State fromState;

    private final Map<String, State> transitions;

    public TransitionsExistException(State fromState, Map<String, State> transitions) {
        this.fromState = fromState;
        this.transitions = transitions;
    }

    public State getFromState() {
        return fromState;
    }

    public Map<String, State> getTransitions() {
        return transitions;
    }
}
