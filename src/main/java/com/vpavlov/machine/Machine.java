package com.vpavlov.machine;

import com.vpavlov.services.machine.exceptions.StartStateSetException;
import com.vpavlov.services.machine.exceptions.TransitionsExistException;
import com.vpavlov.services.api.TitleGenerator;

import java.util.*;

public class Machine {

    private final Map<String, State> states;

    private final Set<State> finalStates;

    private State currentState = null;

    private State startState = null;

    private final Alphabet alphabet;

    private int statesCount = 0;

    private final TitleGenerator titleGenerator;

    public Machine(Alphabet alphabet,TitleGenerator titleGenerator) {
        this.states = new HashMap<>();
        this.finalStates = new HashSet<>();
        this.titleGenerator = titleGenerator;
        this.alphabet = alphabet;
    }

    public Machine(Machine machine){
        this.currentState = null;
        this.alphabet = machine.alphabet;
        this.titleGenerator = machine.titleGenerator;
        this.statesCount = machine.statesCount;
        this.states = copyStates(machine.states);
        this.startState = states.get(machine.startState.getTitle());
        this.finalStates = copyFinalStates(machine.finalStates);
        copyTransitions(machine.states);
        System.out.printf("Source machine:\n%s", machine);
        System.out.printf("Copied machine:\n%s", this);
    }

    private Set<State> copyFinalStates(Set<State> finalStates) {
        Set<State> newFinalStates = new HashSet<>();
        for (State state : finalStates) {
            newFinalStates.add(states.get(state.getTitle()));
        }

        return newFinalStates;
    }

    private void copyTransitions(Map<String, State> states) {
        for(State state : states.values()){
            State newToState = this.states.get(state.getTitle());
            copyTransitionsIn(newToState, state.getTransitionsIn());
        }
    }

    private void copyTransitionsIn(State toState ,Map<String, Set<State>> transitions) {
        for(String symbol : transitions.keySet()){
            Set<State> states = transitions.get(symbol);
            for (State fromState : states){
                State newFromState = this.states.get(fromState.getTitle());
                toState.addTransitionIn(symbol, newFromState);
                newFromState.addTransitionOut(symbol, toState);
            }
        }
    }

    private Map<String, State> copyStates(Map<String, State> states) {
        Map<String, State> statesCopies = new HashMap<String, State>();
        for (State state : states.values()) {
            statesCopies.put(state.getTitle(), new State(state));
        }

        return statesCopies;
    }

    public void addState() {
        String title = titleGenerator.generateTitle(statesCount);
        states.put(title, new State(title));
        ++statesCount;
    }

    public void addState(String title){
        states.put(title, new State(title));
        ++statesCount;
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

        Map<String, String> exitingTransitions = getExistingTransitions(symbols, from);
        if (!exitingTransitions.isEmpty()) {
            throw new TransitionsExistException(fromState.getTitle(), exitingTransitions);
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

    public Map<String, String> getExistingTransitions(Collection<String> symbols, String from) {
        State fromState = states.get(from);
        Map<String, String> exitingTransitions = new HashMap<>();
        for (String symbol : symbols) {
            State existingState = fromState.getTransitionOut(symbol);
            if (existingState != null) {
                exitingTransitions.put(symbol, existingState.getTitle());
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
            throw new StartStateSetException(startState.getTitle());
        }
    }

    public String overrideStartNode(String title){
        State previous = startState;
        startState = states.get(title);
        currentState = startState;
        return previous.getTitle();
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

    public void removeState(String title) {
        --statesCount;
        State state = states.get(title);
        state.removeAllTransitions();
        states.remove(title);
        renameStates(title);
    }

    private void renameStates(String removedTitle ) {
        Set<State> renamedStates = new HashSet<>();
        for (State state : states.values()) {
            if (state.getTitle().compareTo(removedTitle) > 0) {
                String newTitle = String.valueOf((char) (state.getTitle().charAt(0) - 1));
                state.rename(newTitle);
                renamedStates.add(state);
            }
        }
        for (State state : renamedStates) {
            states.put(state.getTitle(), state);

        }
        states.remove(titleGenerator.generateTitle(statesCount));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Start state: %s\n\n", startState.getTitle()));
        for (State state : states.values()) {
            sb.append(state.toString()).append("\n\n");
        }

        return sb.toString();
    }
}
