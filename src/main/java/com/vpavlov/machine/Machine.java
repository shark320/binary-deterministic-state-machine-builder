package com.vpavlov.machine;

import com.vpavlov.machine.exceptions.StartStateSetException;
import com.vpavlov.machine.exceptions.TransitionsExistException;

import java.util.*;

public class Machine {

    private final Map<String, State> states = new HashMap<>();

    private final Set<State> finalStates = new HashSet<>();

    private State currentState = null;

    private State startState = null;

    private final Alphabet alphabet;

    public Machine(Alphabet alphabet) {
        this.alphabet = alphabet;
    }

    public void addState(String title, int serialNumber) {
        states.put(title, new State(title));
    }

    public void addFinalState(String title) {
        finalStates.add(states.get(title));
    }

    public void removeFinalState(String title) {
        finalStates.remove(states.get(title));
    }

    public boolean addTransition(String symbol, String from, String to) {
        State fromState = states.get(from);
        State toState = states.get(to);
        if (fromState == null || toState == null || !alphabet.contains(symbol)) {
            //no states with specified titles or invalid symbol
            return false;
        }

        fromState.addTransitionOut(symbol, toState);
        toState.addTransitionIn(symbol, fromState);


        return true;
    }

    public boolean addTransitions(Collection<String> symbols, String from, String to) throws TransitionsExistException {
        State fromState = states.get(from);
        State toState = states.get(to);
        if (fromState == null || toState == null || !alphabet.containsAll(symbols)) {
            //no states with specified titles or invalid symbol
            return false;
        }

        Map<String, State> exitingTransitions = getExistingTransitions(symbols, from);
        if (!exitingTransitions.isEmpty()) {
            throw new TransitionsExistException(fromState, exitingTransitions);
        }

        for (String symbol : symbols) {
            fromState.addTransitionOut(symbol, toState);
            toState.addTransitionIn(symbol, fromState);
        }
        return true;
    }

    public boolean addAndReplaceTransitions(Collection<String> symbols, String from, String to){
        State fromState = states.get(from);
        State toState = states.get(to);
        if (fromState == null || toState == null || !alphabet.containsAll(symbols)) {
            //no states with specified titles or invalid symbol
            return false;
        }
        for (String symbol : symbols) {
            fromState.addTransitionOut(symbol, toState);
            toState.addTransitionIn(symbol, fromState);
        }
        return true;
    }

    public Map<String, State> getExistingTransitions(Collection<String> symbols, String from) {
        State fromState = states.get(from);
        Map<String, State> exitingTransitions = new HashMap<>();
        for (String symbol : symbols) {
            State existingState = fromState.getTransitionOut(symbol);
            if (existingState != null) {
                exitingTransitions.put(symbol, existingState);
            }
        }

        return exitingTransitions;
    }

    public boolean isCompleteStates(){
        for (State state : states.values()) {
            for (String symbol : alphabet.getSymbols()){
                if (state.getTransitionOut(symbol)==null){
                    return false;
                }
            }
        }

        return true;
    }

    public boolean isStartStateSet(){
        return startState != null;
    }

    public boolean isFinalStatesSet(){
        return !finalStates.isEmpty();
    }

    public void setStartState(String title) throws StartStateSetException {
        if (this.startState == null) {
            this.startState = states.get(title);
            this.currentState = this.startState;
        } else {
            throw new StartStateSetException(startState);
        }
    }

    public State overrideStartNode(String title){
        State previous = startState;
        startState = states.get(title);
        currentState = startState;
        return previous;
    }


    public Map<String, State> getStates(){
        return states;
    }

    public void removeStartState() {
        this.startState = null;
        this.currentState = null;
    }

    public State getCurrentState() {
        return currentState;
    }

    public State getStartState() {
        return startState;
    }

    public State transition(String symbol) {
        State current = states.get(currentState);
        State newState = current.getTransition(symbol);
        currentState = newState;
        return currentState;
    }

    public void removeTransitions(String from, String to, Collection<String> symbols) {
        State fromState = states.get(from);
        State toState = states.get(to);
        for (String symbol : symbols) {
            fromState.removeTransitionOut(symbol);
            toState.removeTransitionIn(symbol, fromState);
        }
    }

    public void removeState(String symbol) {
        State state = states.get(symbol);
        state.removeAllTransitions();
        states.remove(symbol);
    }


}
