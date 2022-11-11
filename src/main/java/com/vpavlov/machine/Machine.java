package com.vpavlov.machine;

import com.vpavlov.App;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Machine {


    private final Map<String, State> states = new HashMap<>();

    private final Set<String> finalStates = new HashSet<>();

    private String currentState = null;

    private String startState = null;

    private final Alphabet alphabet;

    private boolean isComplete = false;

    public Machine(Alphabet alphabet){
        this.alphabet = alphabet;
    }

    public void addState(String title){
        states.put(title, new State(title, alphabet));
    }

    public void addFinalState(String title){
        finalStates.add(title);
    }

    private boolean hasAllTranslations(){
        boolean flag = true;
        for (State state : states.values()){
            if (!state.isComplete()){
                flag = false;
                break;
            }
        }
        return flag;
    }

    public boolean addTransition(String symbol, String from, String to){
        State fromState = states.get(from);
        State toState = states.get(to);
        if (fromState == null || toState == null){
            //no states with specified titles
            return false;
        }

        fromState.addTransitionOut(symbol, toState);
        toState.addTransitionIn(symbol, fromState);

        isComplete = hasAllTranslations();

        return true;
    }

    public boolean isComplete(){
        return isComplete && (startState!=null);
    }

    public void setStartState(String title){
        this.startState = title;
        this.currentState = this.startState;
    }

    public String getCurrentState(){
        return currentState;
    }

    public String getStartState(){
        return startState;
    }

    public String transition(String symbol){
        State current = states.get(currentState);
        State newState = current.getTransition(symbol);
        currentState = newState.getTitle();
        return currentState;
    }

}
