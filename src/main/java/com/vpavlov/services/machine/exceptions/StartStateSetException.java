package com.vpavlov.services.machine.exceptions;

/**
 * Machine exception for start state setting. Uses to indicate that start state is already set.
 *
 * @author vpavlov
 * @see com.vpavlov.machine.Machine
 */
public class StartStateSetException extends Exception {

    /**
     * The set start state title in the machine
     */
    private final String startState;

    /**
     * Constructor
     *
     * @param startState the set start state title in the machine
     */
    public StartStateSetException(String startState) {
        this.startState = startState;
    }

    /**
     * The start state title getter
     *
     * @return the set start state
     */
    public String getStartStateTitle() {
        return startState;
    }

}
