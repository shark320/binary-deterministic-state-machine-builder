package com.vpavlov.machine;

import java.util.HashMap;
import java.util.Map;

public class State {

    //TODO check for remove
    private final String title;

    private final Map<String, State> transitionsOut = new HashMap<>();

    private final Map<String, State> transitionsIn = new HashMap<>();

    private final Alphabet alphabet;

    private boolean isComplete = false;

    public State(String title, Alphabet alphabet) {
        this.title = title;
        this.alphabet = alphabet;
    }

    public void addTransitionOut(String symbol, State next) {
        transitionsOut.put(symbol, next);
        isComplete = hasAllSymbols();
    }

    public void addTransitionIn(String symbol, State previous){
        transitionsIn.put(symbol, previous);
    }

    public void deleteTransitionOut(String symbol){
        transitionsOut.remove(symbol);
        isComplete = false;
    }

    public void deleteTransitionIn(String symbol){
        transitionsIn.remove(symbol);
    }

    public State getTransition(String symbol) {
        return transitionsOut.get(symbol);
    }

    private boolean hasAllSymbols(){
        boolean flag = true;
        for(String symbol : alphabet.getSymbols()){
            if(!transitionsOut.containsKey(symbol)){
                flag = false;
                break;
            }
        }
        return flag;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public String getTitle() {
        return title;
    }


}
