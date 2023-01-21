package com.vpavlov.services.machine;

import com.vpavlov.App;
import com.vpavlov.machine.Alphabet;
import com.vpavlov.machine.Machine;
import com.vpavlov.services.machine.exceptions.StartStateSetException;
import com.vpavlov.services.machine.exceptions.TransitionsExistException;
import com.vpavlov.visualization.draw_model.MachineGraph;
import com.vpavlov.visualization.draw_model.MachineNode;
import com.vpavlov.visualization.draw_model.TransitionLine;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;

import java.util.*;

/**
 * Machine service class. Encapsulates logical ang graphical representations of the machine.
 *
 * @author vpavlov
 * @see com.vpavlov.machine.Machine
 * @see com.vpavlov.visualization.draw_model.MachineGraph
 */
public class MachineService {

    /**
     * Machine alphabet
     */
    private final Alphabet alphabet;

    /**
     * Machine logical representation
     */
    private final Machine machine;

    /**
     * Log for machine transitions
     */
    private final ObservableList<MachineTransition> transitionsLog = FXCollections.observableArrayList();

    /**
     * Machine graphical representation
     */
    MachineGraph machineGraph;

    /**
     * Constructor. Generates a new machine.
     */
    public MachineService() {
        alphabet = new Alphabet(App.getProperties().getProperty("alphabet"));
        machine = new Machine(alphabet, this::generateTitle);
        machineGraph = new MachineGraph(this::generateTitle);
    }

    /**
     * Deep copy constructor.
     *
     * @param machineService machine service to copy.
     */
    public MachineService(MachineService machineService) {
        alphabet = new Alphabet(machineService.alphabet);
        machine = new Machine(machineService.machine);
        machineGraph = new MachineGraph(machineService.machineGraph);
    }

    /**
     * Set the node with the given title as start a start node.
     *
     * @param title the title of the start node
     * @throws StartStateSetException if the start state is already set.
     */
    public void setAsStartNode(String title) throws StartStateSetException {
        machine.setStartState(title);
        machineGraph.setAsStartNode(title);
    }

    /**
     * Unset the node with the given title as start a start node.
     *
     * @param title the title of the node
     */
    public void unsetAsStartNode(String title) {
        machine.removeStartState();
        machineGraph.unsetAsStartNode(title);
    }

    public void overrideStartNode(String title) {
        String previous = machine.overrideStartNode(title);
        machineGraph.unsetAsStartNode(previous);
        machineGraph.setAsStartNode(title);
    }

    /**
     * Set the node with the given title as a final node.
     *
     * @param title the title of the node.
     */
    public void setAsFinalNode(String title) {
        machine.addFinalState(title);
        machineGraph.setAsFinalNode(title);
    }

    /**
     * Unset the node with the given title as a final node.
     *
     * @param title the title of the node.
     */
    public void unsetAsFinalNode(String title) {
        machine.removeFinalState(title);
        machineGraph.unsetAsFinalNode(title);
    }

    /**
     * Make transition from current state to the next one according to the given symbol.
     *
     * @param symbol the symbol to make transition
     * @return true if the symbol is in the machine alphabet; else false
     */
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

    /**
     * Indicate the start node in the machine graph
     */
    public void initStart() {
        String start = machine.getStartState();
        machineGraph.setCurrentNode(start);
    }

    /**
     * Undo last machine transition. Indicates it in the machine graph.
     *
     * @return true if successful; else false
     * @throws IllegalStateException if the machine is in initial state
     */
    public boolean undo() throws IllegalStateException {
        if (transitionsLog.isEmpty()) {
            throw new IllegalStateException("The machine is in initial state.");
        }
        int lastIndex = transitionsLog.size() - 1;
        MachineTransition lastTransition = transitionsLog.get(lastIndex);
        String previous = lastTransition.from();
        if (!machine.setCurrentState(previous)) {
            return false;
        }
        machineGraph.setCurrentNode(previous);
        transitionsLog.remove(lastIndex);
        return true;
    }

    /**
     * Reset the machine to the initial state.
     *
     * @return if successful; else false
     * @throws IllegalStateException if the machine is already reset.
     */
    public boolean reset() throws IllegalStateException {
        if (transitionsLog.isEmpty()) {
            throw new IllegalStateException("The machine is already reset.");
        }
        boolean isFinalState = machine.isFinalState();
        machine.reset();
        initStart();
        transitionsLog.clear();
        return isFinalState;
    }

    /**
     * Quit the machine.
     *
     * @return true if current state is a final state; else false
     */
    public boolean quit() {
        return machine.isFinalState();
    }

    /**
     * Add a new node in the machine graph on the given position. Add new state to the machine.
     *
     * @param point the position to create node on.
     */
    public void addMachineNode(Point2D point) {
        machine.addState();
        machineGraph.addNode(point);
    }

    /**
     * Add new node in the machine graph on the given position with given parameters. Add new state with given parameters to the machine.
     *
     * @param title       node title
     * @param x           node X-coordinate
     * @param y           node Y-coordinate
     * @param isStartNode is a start node
     * @param isFinalNode is a final node
     */
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

    /**
     * Title generation method.
     *
     * @param index the index of the node/state to generate title for
     * @return generated title
     * @see com.vpavlov.services.machine.api.TitleGenerator
     */
    private String generateTitle(int index) {
        return String.valueOf((char) ('A' + index));
    }

    /**
     * Add new transitions between nodes and state.
     *
     * @param symbols transition symbols
     * @param from    title of state/node from which transitions will be.
     * @param to      title of the state/node to which transitions will be.
     * @return if successful; else false.
     * @throws TransitionsExistException if transitions from the given state already exist.
     */
    public boolean addTransitions(Collection<String> symbols, String from, String to) throws TransitionsExistException {
        if (!machine.addTransitions(symbols, from, to)) {
            return false;
        }
        machineGraph.addTransitionLine(symbols, from, to);
        return true;
    }

    /**
     * Add new transitions between nodes and state. Replace transitions if existed.
     *
     * @param symbols transitions symbols
     * @param from    title of state/node from which transitions will be.
     * @param to      title of state/node from which transitions will be.
     * @return if successful; else false.
     */
    public boolean addAndReplaceTransitions(Collection<String> symbols, String from, String to) {
        Map<String, TransitionLine> exitingTransitionLines = machineGraph.getExitingTransitionLines(from, machine.getExistingTransitions(symbols, from));
        if (!machine.addAndReplaceTransitions(symbols, from, to)) {
            return false;
        }
        machineGraph.addAndReplaceTransitionLines(symbols, from, to, exitingTransitionLines);
        return true;
    }

    /**
     * Remove transitions between machine states and graph nodes.
     *
     * @param from    title of state/node from which transitions remove.
     * @param to      title of state/node to which transitions remove.
     * @param symbols transitions symbols to remove
     */
    public void removeTransitions(String from, String to, Collection<String> symbols) {
        MachineNode fromNode = machineGraph.getNode(from);
        MachineNode toNode = machineGraph.getNode(to);
        TransitionLine transitionLine = fromNode.getTransitionOut(toNode);
        if (transitionLine != null) {
            machine.removeTransitions(from, to, symbols);
            transitionLine.removeTitles(symbols);
            if (transitionLine.isEmptyTitles()) {
                machineGraph.getChildren().remove(transitionLine);
                fromNode.removeTransitionOut(toNode);
                toNode.removeTransitionIn(fromNode);
            }
        }
    }

    /**
     * Remove machine state and graph node.
     *
     * @param title title of state/node to remove.
     * @return true if successful; else false
     */
    public boolean removeNode(String title) {
        //deleting from machine
        if (!machine.removeState(title)) {
            return false;
        }
        //deleting from machineGraph
        machineGraph.removeNode(title);
        return true;
    }

    /**
     * Transition line getter
     *
     * @param fromNode start node
     * @param toNode   end node
     * @return transition line if exists, else null
     */
    public TransitionLine getTransitionLine(MachineNode fromNode, MachineNode toNode) {
        return fromNode.getTransitionOut(toNode);
    }

    /**
     * Nodes getter
     *
     * @return Map [Node name, Node instance] of machine nodes
     */
    public Map<String, MachineNode> getNodes() {
        return machineGraph.getNodes();
    }

    /**
     * Machine graph getter
     *
     * @return current machine graph
     */
    public MachineGraph getMachineGraph() {
        return machineGraph;
    }

    /**
     * Machine getter
     *
     * @return current machine
     */
    public Machine getMachine() {
        return machine;
    }

    /**
     * Current machine alphabet getter
     *
     * @return current machine alphabet
     */
    public Alphabet getAlphabet() {
        return alphabet;
    }

    /**
     * Machine states completeness checker
     *
     * @return true if all machine nodes are complete, else false
     */
    public boolean isCompleteMachineStates() {
        return machine.isCompleteStates();
    }

    /**
     * Machine start state set checker
     *
     * @return true if machine start state is set, else false
     */
    public boolean isStartStateSet() {
        return machine.isStartStateSet();
    }

    /**
     * Machine final state set checker
     *
     * @return true if machine final state is set, else false
     */
    public boolean isFinalStatesSet() {
        return machine.isFinalStatesSet();
    }

    /**
     * Machine graph transition lines getter
     *
     * @return set of machine graph transition lines
     */
    public Set<TransitionLine> getTransitionLines() {
        return machineGraph.getTransitionLines();
    }

    /**
     * Machine transitions log getter
     *
     * @return list of machine transitions log
     */
    public ObservableList<MachineTransition> getTransitionsLog() {
        return transitionsLog;
    }

}
