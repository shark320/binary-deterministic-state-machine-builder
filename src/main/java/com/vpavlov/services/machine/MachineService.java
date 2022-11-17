package com.vpavlov.services.machine;

import com.vpavlov.App;
import com.vpavlov.machine.Alphabet;
import com.vpavlov.machine.Machine;
import com.vpavlov.machine.State;
import com.vpavlov.machine.exceptions.StartStateSetException;
import com.vpavlov.machine.exceptions.TransitionsExistException;
import com.vpavlov.services.api.Service;
import com.vpavlov.visualization.draw_model.MachineNode;
import com.vpavlov.visualization.draw_model.TransitionLine;
import com.vpavlov.visualization.machineBuilder.exceptions.StartNodeSetException;
import com.vpavlov.visualization.machineBuilder.exceptions.TransitionLinesExistsException;
import javafx.geometry.Point2D;
import javafx.scene.Group;

import java.util.*;

public class MachineService implements Service {

    private static final int LETTERS_COUNT = 26;

    private final Alphabet alphabet;

    private final Machine machine;

    private final Map<String, MachineNode> nodes = new HashMap<>();

    private final Map<MachineNode, Map<MachineNode, TransitionLine>> transitionsMap = new HashMap<>();

    private final Group machineGraph = new Group();

    private int machineNodesCount = 0;

    public MachineService() {
        alphabet = new Alphabet(App.getProperties().getProperty("alphabet"));
        machine = new Machine(alphabet);
        machineGraph.getStylesheets().add(App.class.getResource("machine-graph.css").toExternalForm());
    }


    public void setAsStartNode(MachineNode node) throws StartNodeSetException {
        String title = node.getTitle();
        try {
            machine.setStartState(title);
        } catch (StartStateSetException e) {
            MachineNode startNode = nodes.get(e.getStartState().getTitle());
            throw new StartNodeSetException(startNode);
        }
        node.setAsStartNode();
    }

    public void unsetAsStartNode(MachineNode node){
        machine.removeStartState();
        node.unsetAsStartNode();
    }

    public void overrideStartNode (MachineNode node){
        State previous = machine.overrideStartNode(node.getTitle());
        nodes.get(previous.getTitle()).unsetAsStartNode();
        node.setAsStartNode();
    }

    public void setAsFinalNode(MachineNode node){
        machine.addFinalState(node.getTitle());
        node.setAsFinalNode();
    }

    public void unsetAsFinalNode(MachineNode node){
        machine.removeFinalState(node.getTitle());
        node.unsetAsFinalNode();
    }

    public State makeTransition(String symbol) {
        return machine.transition(symbol);
    }

    public void addMachineNode(Point2D point) {
        int serialNumber = machineNodesCount;
        String title = generateTitle(serialNumber);
        MachineNode node = new MachineNode(point.getX(), point.getY(), title, serialNumber);
        machine.addState(title, serialNumber);
        nodes.put(title, node);
        transitionsMap.put(node, new HashMap<>());
        machineGraph.getChildren().add(node);
        ++machineNodesCount;
    }

    private String generateTitle(int serialNumber) {
        return String.valueOf((char) ('A' + serialNumber));
    }

    public boolean addTransition(Collection<String> symbols, MachineNode fromNode, MachineNode toNode) throws TransitionLinesExistsException {
        String from = fromNode.getTitle();
        String to = toNode.getTitle();
        try {
            if (!machine.addTransitions(symbols, from, to)) {
                return false;
            }
        } catch (TransitionsExistException e) {
            Map<String, TransitionLine> exitingTransitionLines = getExitingTransitionLines(fromNode, e.getTransitions());
            throw new TransitionLinesExistsException(fromNode, exitingTransitionLines);
        }
        addTransitionLine(symbols, fromNode, toNode);
        return true;
    }

    public boolean addAndReplaceTransitions(Collection<String> symbols, MachineNode fromNode, MachineNode toNode) {
        String from = fromNode.getTitle();
        String to = toNode.getTitle();
        Map<String, TransitionLine> exitingTransitionLines = getExitingTransitionLines(fromNode, machine.getExistingTransitions(symbols, from));
        if (!machine.addAndReplaceTransitions(symbols, from, to)) {
            return false;
        }
        removeExitingTransitionLines(exitingTransitionLines);
        addTransitionLine(symbols, fromNode, toNode);
        return true;
    }

    private void addTransitionLine(Collection<String> symbols, MachineNode fromNode, MachineNode toNode) {
        TransitionLine transitionLine = fromNode.getTransitionOut(toNode);
        if (transitionLine == null) {
            transitionLine = new TransitionLine(fromNode, toNode, symbols);
            fromNode.addTransitionOut(transitionLine, toNode);
            toNode.addTransitionIn(transitionLine, fromNode);
            machineGraph.getChildren().add(transitionLine);
        } else {
            transitionLine.addTitles(symbols);
        }
    }

    private Map<String, TransitionLine> getExitingTransitionLines(MachineNode fromNode, Map<String, State> transitions) {
        Map<String, TransitionLine> exitingTransitionLines = new HashMap<>();
        Map<String, MachineNode> toNodes = new HashMap<>();
        for (String symbol : transitions.keySet()) {
            toNodes.put(symbol, nodes.get(transitions.get(symbol).getTitle()));
        }
        for (String title : toNodes.keySet()) {
            MachineNode toNode = toNodes.get(title);
            exitingTransitionLines.put(title, fromNode.getTransitionOut(toNode));
        }

        return exitingTransitionLines;
    }

    private void removeExitingTransitionLines(Map<String, TransitionLine> exitingTransitionLines) {
        for (String title : exitingTransitionLines.keySet()) {
            TransitionLine transitionLine = exitingTransitionLines.get(title);
            transitionLine.removeTitle(title);
            if (transitionLine.isEmptyTitles()) {
                MachineNode fromNode = transitionLine.getStart();
                MachineNode toNode = transitionLine.getEnd();
                machineGraph.getChildren().remove(transitionLine);
                fromNode.removeTransitionOut(toNode);
                toNode.removeTransitionIn(fromNode);
            }
        }
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

    public void removeNode(MachineNode node) {
        --machineNodesCount;
        //deleting from machine
        machine.removeState(node.getTitle());

        nodes.remove(node.getTitle());
        //deleting from machineGraph
        machineGraph.getChildren().removeAll(node.removeAllTransitions());
        machineGraph.getChildren().remove(node);
        renameNodes(node);
        renameStates(node.getTitle());
    }

    private Set<MachineNode> getAllTransitionsInMachineNodes(MachineNode node) {
        Set<MachineNode> nodesIn = new HashSet<>();
        for (MachineNode key : transitionsMap.keySet()) {
            if (transitionsMap.get(key).containsKey(node)) {
                nodesIn.add(key);
            }
        }
        return nodesIn;
    }

    private void renameNodes(MachineNode removedNode) {
        Set<MachineNode> renamed = new HashSet<>();
        for (MachineNode node : nodes.values()) {
            if (node.getTitle().compareTo(removedNode.getTitle()) > 0) {
                String newTitle = String.valueOf((char) (node.getTitle().charAt(0) - 1));
                node.setTitle(newTitle);
                renamed.add(node);
            }
        }
        for (MachineNode node : renamed) {
            nodes.put(node.getTitle(), node);
        }
        nodes.remove(generateTitle(machineNodesCount));
    }

    private void renameStates(String removedTitle) {
        Map<String, State> states = machine.getStates();
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
        states.remove(generateTitle(machineNodesCount));
    }


    public TransitionLine getTransitionLine(MachineNode fromNode, MachineNode toNode) {
        return fromNode.getTransitionOut(toNode);
    }

    public Map<String, MachineNode> getNodes() {
        return nodes;
    }

    public Group getMachineGraph() {
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

    public boolean isStartStateSet(){
        return machine.isStartStateSet();
    }

    public boolean isFinalStatesSet(){
        return machine.isFinalStatesSet();
    }

}
