package com.vpavlov.services.machine;

import com.vpavlov.App;
import com.vpavlov.machine.Alphabet;
import com.vpavlov.machine.Machine;
import com.vpavlov.services.machine.exceptions.StartStateSetException;
import com.vpavlov.services.machine.exceptions.TransitionsExistException;
import com.vpavlov.services.api.Service;
import com.vpavlov.visualization.draw_model.MachineGraph;
import com.vpavlov.visualization.draw_model.MachineNode;
import com.vpavlov.visualization.draw_model.TransitionLine;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;

import java.util.*;

public class MachineService implements Service {

    private final Alphabet alphabet;

    private final Machine machine;

    private final ObservableList<MachineTransition> transitionsLog = FXCollections.observableArrayList() ;

    MachineGraph machineGraph;

    public MachineService() {
        alphabet = new Alphabet(App.getProperties().getProperty("alphabet"));
        machine = new Machine(alphabet, this::generateTitle);
        machineGraph = new MachineGraph(this::generateTitle);
    }

    public MachineService(MachineService machineService){
        alphabet = new Alphabet(machineService.alphabet);
        machine = new Machine(machineService.machine);
        machineGraph = new MachineGraph(machineService.machineGraph);
    }


    public void setAsStartNode(String title) throws StartStateSetException {
        machine.setStartState(title);
        machineGraph.setAsStartNode(title);
    }

    public void unsetAsStartNode(String title) {
        machine.removeStartState();
        machineGraph.unsetAsStartNode(title);
    }

    public void overrideStartNode(String title) {
        String previous = machine.overrideStartNode(title);
        machineGraph.unsetAsStartNode(previous);
        machineGraph.setAsStartNode(title);
    }

    public void setAsFinalNode(String title) {
        machine.addFinalState(title);
        machineGraph.setAsFinalNode(title);
    }

    public void unsetAsFinalNode(String title) {
        machine.removeFinalState(title);
        machineGraph.unsetAsFinalNode(title);
    }

    public boolean makeTransition(String symbol) {
        if (!alphabet.contains(symbol)) {
            return false;
        }
        String currentState = machine.getCurrentState();
        String nextState = machine.transition(symbol);
        machineGraph.setCurrentNode(nextState);
        transitionsLog.add(new MachineTransition(symbol, currentState, nextState));
        return true;
    }

    public void initStart(){
        String start = machine.getStartState();
        machineGraph.setCurrentNode(start);
    }

    public void undo() throws IllegalStateException{
        if (transitionsLog.isEmpty()) {
            throw new IllegalStateException("The machine in initial state.");
        }
        int lastIndex = transitionsLog.size()-1;
        MachineTransition lastTransition = transitionsLog.get(lastIndex);
        String previous = lastTransition.getFrom();
        machine.setCurrentState(previous);
        machineGraph.setCurrentNode(previous);
        transitionsLog.remove(lastIndex);
    }

    public boolean reset() throws IllegalStateException{
        if (transitionsLog.isEmpty()){
            throw new IllegalStateException("The machine is already reset.");
        }
        boolean isFinalState = machine.isFinalState();
        machine.reset();
        initStart();
        transitionsLog.clear();
        return isFinalState;
    }

    public boolean quit(){
        return machine.isFinalState();
    }

    public void addMachineNode(Point2D point) {
        machine.addState();
        machineGraph.addNode(point);
    }

    public void addMachineNode(String title, double x, double y, boolean isStartNode, boolean isFinalNode) {
        machine.addState(title);
        machineGraph.addNode(title, x, y);
        if (isStartNode) {
            try {
                setAsStartNode(title);
            } catch (StartStateSetException e) {
                throw new RuntimeException(e);
            }
        }

        if (isFinalNode) {
            setAsFinalNode(title);
        }
    }

    private String generateTitle(int serialNumber) {
        return String.valueOf((char) ('A' + serialNumber));
    }

    public void addTransitions(Collection<String> symbols, String from, String to) throws TransitionsExistException {
        machine.addTransitions(symbols, from, to);
        machineGraph.addTransitionLine(symbols, from, to);
    }

    public void addAndReplaceTransitions(Collection<String> symbols, String from, String to) {
        Map<String, TransitionLine> exitingTransitionLines = machineGraph.getExitingTransitionLines(from, machine.getExistingTransitions(symbols, from));
        machine.addAndReplaceTransitions(symbols, from, to);
        machineGraph.addAndReplaceTransitionLines(symbols, from, to, exitingTransitionLines);
    }

    public void removeTransitions(MachineNode fromNode, MachineNode toNode, Collection<String> symbols) {
        TransitionLine transitionLine = fromNode.getTransitionOut(toNode);
        if (transitionLine != null) {
            machine.removeTransitions(fromNode.getTitle(), toNode.getTitle(), symbols);
            transitionLine.removeTitles(symbols);
            if (transitionLine.isEmptyTitles()) {
                machineGraph.getChildren().remove(transitionLine);
                fromNode.removeTransitionOut(toNode);
                toNode.removeTransitionIn(fromNode);
            }
        }
    }

    public void removeNode(String title) {
        //deleting from machine
        machine.removeState(title);
        //deleting from machineGraph
        machineGraph.removeNode(title);
    }


    public TransitionLine getTransitionLine(MachineNode fromNode, MachineNode toNode) {
        return fromNode.getTransitionOut(toNode);
    }

    public Map<String, MachineNode> getNodes() {
        return machineGraph.getNodes();
    }

    public MachineGraph getMachineGraph() {
        return machineGraph;
    }

    public Machine getMachine() {
        return machine;
    }

    public Alphabet getAlphabet() {
        return alphabet;
    }

    public boolean isCompleteMachineStates() {
        return machine.isCompleteStates();
    }

    public boolean isStartStateSet() {
        return machine.isStartStateSet();
    }

    public boolean isFinalStatesSet() {
        return machine.isFinalStatesSet();
    }

    public Set<TransitionLine> getTransitionLines() {
        return machineGraph.getTransitionLines();
    }

    public ObservableList<MachineTransition> getTransitionsLog(){
        return transitionsLog;
    }

}
