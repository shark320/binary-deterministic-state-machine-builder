package com.vpavlov.machine;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Class represents a deterministic state machine state
 *
 * @author vpavlov
 * @version 20.11.2022
 */
public class State {

    /**
     * Title of the state
     */
    private String title;

    /**
     * Transitions from the state
     */
    private final Map<String, State> transitionsOut;

    /**
     * Transitions to the state
     */
    private final Map<String, Set<State>> transitionsIn;

    /**
     * Constructor
     *
     * @param title title of the state
     */
    public State(String title) {
        this.title = title;
        this.transitionsOut = new HashMap<>();
        this.transitionsIn = new HashMap<>();
    }

    /**
     * Deep copy constructor (without transitions copy)
     *
     * @param state state to copy
     */
    public State(State state) {
        this.transitionsOut = new HashMap<>();
        this.transitionsIn = new HashMap<>();
        this.title = state.title;
    }

    /**
     * Add new transitions from the state
     *
     * @param symbol transitions symbol
     * @param next   the state to which the transition is
     */
    public void addTransitionOut(String symbol, State next) {
        transitionsOut.put(symbol, next);
    }

    /**
     * Add new transition to the state
     *
     * @param symbol   transition symbol
     * @param previous the state from which the transition is
     */
    public void addTransitionIn(String symbol, State previous) {
        Set<State> fromStates = transitionsIn.computeIfAbsent(symbol, k -> new HashSet<>());
        fromStates.add(previous);
    }

    /**
     * Remove transition from the state with specified symbol
     *
     * @param symbol the symbol of transition to be removed
     */
    public void removeTransitionOut(String symbol) {
        System.out.printf("<%s> Removing transitionOut %s\n", this.title, symbol);
        transitionsOut.remove(symbol);
    }

    /**
     * Remove transition to the state with specified symbol and specified from state
     *
     * @param symbol    the symbol of transition to be removed
     * @param fromState the state from which the transition is
     */
    public void removeTransitionIn(String symbol, State fromState) {
        System.out.printf("<%s> Removing transitionIn %s\n", this.title, symbol);
        Set<State> fromStates = transitionsIn.get(symbol);
        fromStates.remove(fromState);
        if (fromStates.isEmpty()) {
            transitionsIn.remove(symbol);
        }
    }

    /**
     * The state title getter
     *
     * @return the state title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Remove all transitions from the state
     */
    public void removeTransitionsOut() {
        for (String symbol : transitionsOut.keySet()) {
            System.out.printf("<%s> Removing propagation for transitionOut %s\n", this.title, symbol);
            transitionsOut.get(symbol).removeTransitionIn(symbol, this);
        }
        transitionsOut.clear();
    }

    /**
     * Remove all transitions ot the state
     */
    public void removeTransitionsIn() {
        for (String symbol : transitionsIn.keySet()) {
            System.out.printf("<%s> Removing propagation for transitionIn %s\n", this.title, symbol);
            Set<State> fromStates = transitionsIn.get(symbol);
            for (State state : fromStates) {
                state.removeTransitionOut(symbol);
            }
        }
        transitionsIn.clear();
    }

    /**
     * Gets the state to of the transition from the state with specified symbol
     *
     * @param symbol the transition symbol
     * @return the state to of the transition from the state with specified symbol
     */
    public State getTransitionOut(String symbol) {
        return transitionsOut.get(symbol);
    }

    /**
     * Transitions to the state getter
     *
     * @return transitions to the state
     */
    public Map<String, Set<State>> getTransitionsIn() {
        return transitionsIn;
    }

    /**
     * Removes all transitions to and from the state
     */
    public void removeAllTransitions() {
        removeTransitionsOut();
        removeTransitionsIn();
    }

    /**
     * Rename the state
     *
     * @param newTitle new title
     */
    public void rename(String newTitle) {
        this.title = newTitle;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("State [").append(title).append("]:\n");
        sb.append("----------------------------------------------------------------\n");
        sb.append("Transitions In:\n");
        sb.append("----------------------------------------------------------------\n");
        for (String symbol : transitionsIn.keySet()) {
            for (State fromState : transitionsIn.get(symbol)) {
                sb.append(String.format("<%s> --%s--> <%s>\n", fromState.title, symbol, this.title));
            }
        }
        sb.append("----------------------------------------------------------------\n");
        sb.append("Transitions Out:\n");
        sb.append("----------------------------------------------------------------\n");
        for (String symbol : transitionsOut.keySet()) {
            State toState = transitionsOut.get(symbol);
            sb.append(String.format("<%s> --%s--> <%s>\n", this.title, symbol, toState.title));
        }

        return sb.toString();
    }
}
