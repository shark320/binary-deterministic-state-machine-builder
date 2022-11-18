package com.vpavlov.services.machine.exceptions;

import com.vpavlov.machine.State;

import java.util.Map;

public class TransitionsExistException extends Exception {


    private final String fromState;

    private final Map<String, String> transitions;

    public TransitionsExistException(String fromState, Map<String, String> transitions) {
        this.fromState = fromState;
        this.transitions = transitions;
    }

    public String getFrom() {
        return fromState;
    }

    public Map<String, String> getTransitions() {
        return transitions;
    }
}
