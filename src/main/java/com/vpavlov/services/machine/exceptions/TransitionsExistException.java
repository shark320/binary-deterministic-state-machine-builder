package com.vpavlov.services.machine.exceptions;

import java.util.Map;

/**
 * Machine exception for exist transitions. Uses to indicate that transitions from a node already exist.
 *
 * @author vpavlov
 * @see com.vpavlov.machine.Machine
 */
public class TransitionsExistException extends Exception {

    /**
     * The state title from which transitions are
     */
    private final String fromState;

    /**
     * The transitions from the state
     */
    private final Map<String, String> transitions;

    /**
     * Constructor
     * @param fromState the state title from which transitions are
     * @param transitions the transitions from the state
     */
    public TransitionsExistException(String fromState, Map<String, String> transitions) {
        this.fromState = fromState;
        this.transitions = transitions;
    }

    /**
     * From state title getter
     * @return from state title
     */
    public String getFrom() {
        return fromState;
    }

    /**
     * Transitions getter
     * @return transitions
     */
    public Map<String, String> getTransitions() {
        return transitions;
    }
}
