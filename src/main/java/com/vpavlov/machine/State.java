package com.vpavlov.machine;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class State {


    //TODO check for remove
    private String title;

    private  int serialNumber;

    private final Map<String, State> transitionsOut = new HashMap<>();

    private final Map<String, Set<State>> transitionsIn = new HashMap<>();

    private boolean isComplete = false;

    public State(String title) {
        this.title = title;
    }

    public void addTransitionOut(String symbol, State next) {
        transitionsOut.put(symbol, next);
    }

    public void addTransitionIn(String symbol, State previous){
        Set<State> fromStates = transitionsIn.computeIfAbsent(symbol, k -> new HashSet<>());
        fromStates.add(previous);
    }

    public void removeTransitionOut(String symbol){
        System.out.printf("<%s> Removing transitionOut %s\n", this.title, symbol);
        transitionsOut.remove(symbol);
        isComplete = false;
    }

    public void removeTransitionIn(String symbol, State fromState){
        System.out.printf("<%s> Removing transitionIn %s\n", this.title, symbol);
        Set<State> fromStates = transitionsIn.get(symbol);
        fromStates.remove(fromState);
        if (fromStates.isEmpty()){
            transitionsIn.remove(symbol);
        }
    }

    public State getTransition(String symbol) {
        return transitionsOut.get(symbol);
    }

    public boolean isComplete() {
        return isComplete;
    }

    public String getTitle() {
        return title;
    }

    public void removeTransitionsOut(){
        for(String symbol : transitionsOut.keySet()){
            System.out.printf("<%s> Removing propagation for transitionOut %s\n", this.title, symbol);
            transitionsOut.get(symbol).removeTransitionIn(symbol, this);
        }
        transitionsOut.clear();
    }

    public void removeTransitionsIn(){
        for (String symbol : transitionsIn.keySet()){
            System.out.printf("<%s> Removing propagation for transitionIn %s\n", this.title, symbol);
            Set<State> fromStates = transitionsIn.get(symbol);
            for (State state : fromStates){
                state.removeTransitionOut(symbol);
            }
        }
        transitionsIn.clear();
    }

    public State getTransitionOut(String symbol){
        return transitionsOut.get(symbol);
    }

    public void removeAllTransitions(){
        removeTransitionsOut();
        removeTransitionsIn();
    }

    public String rename(String newTitle){
        String oldTitle = this.title;
        this.title = newTitle;
        return oldTitle;
    }


}
