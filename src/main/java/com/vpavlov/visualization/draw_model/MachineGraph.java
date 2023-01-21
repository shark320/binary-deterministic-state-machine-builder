package com.vpavlov.visualization.draw_model;

import com.vpavlov.App;
import com.vpavlov.services.machine.api.TitleGenerator;
import javafx.geometry.Point2D;
import javafx.scene.Group;

import java.util.*;

/**
 * Graphical representation of machine graph (nodes and transitions)
 *
 * @author vpavlov
 */
public class MachineGraph extends Group {

    /**
     * Map of machine nodes
     */
    private final Map<String, MachineNode> nodes;

    /**
     * Set of machine transitions
     */
    private final Set<TransitionLine> transitionLines;

    /**
     * machine nodes count
     */
    private int machineNodesCount = 0;

    /**
     * Title generator for machine nodes
     */
    private final TitleGenerator titleGenerator;

    /**
     * Current machine node
     */
    private MachineNode currentNode = null;

    /**
     * Constructor
     *
     * @param titleGenerator title generator for machine nodes
     */
    public MachineGraph(TitleGenerator titleGenerator) {
        this.titleGenerator = titleGenerator;
        this.nodes = new HashMap<>();
        this.transitionLines = new HashSet<>();
        this.getStylesheets().add(App.class.getResource("css/machine-graph.css").toExternalForm());
    }

    /**
     * Deep copy constructor
     *
     * @param machineGraph machine graph to copy
     */
    public MachineGraph(MachineGraph machineGraph) {
        this.currentNode = null;
        this.machineNodesCount = machineGraph.machineNodesCount;
        this.titleGenerator = machineGraph.titleGenerator;
        this.nodes = copyNodes(machineGraph.nodes);
        this.transitionLines = new HashSet<>();
        copyTransitionLines(machineGraph.transitionLines);
        this.getStylesheets().add(App.class.getResource("css/machine-graph.css").toExternalForm());
    }

    /**
     * Deep copy of transition lines
     *
     * @param transitionLines transition lines to copy
     */
    private void copyTransitionLines(Set<TransitionLine> transitionLines) {
        for (TransitionLine transitionLine : transitionLines) {
            String from = transitionLine.getStart().getTitle();
            String to = transitionLine.getEnd().getTitle();
            Set<String> symbols = transitionLine.getTitles();
            addTransitionLine(symbols, from, to);
        }
    }

    /**
     * Deep copy of machine nodes
     *
     * @param nodes machine nodes to copy
     * @return deep copy map of machine nodes
     */
    private Map<String, MachineNode> copyNodes(Map<String, MachineNode> nodes) {
        Map<String, MachineNode> newNodes = new HashMap<>();
        for (MachineNode node : nodes.values()) {
            MachineNode newNode = new MachineNode(node);
            newNodes.put(node.getTitle(), newNode);
            this.getChildren().add(newNode);
        }
        return newNodes;
    }

    /**
     * Machine node getter
     *
     * @param title title of machine node to get
     * @return machine node with specified title or null if not found
     */
    public MachineNode getNode(String title) {
        return nodes.get(title);
    }

    /**
     * Add new machine node. Title is generated automatically using title generation
     *
     * @param point new machine node coordinates
     * @see TitleGenerator
     */
    public void addNode(Point2D point) {
        String title = titleGenerator.generateTitle(machineNodesCount);
        MachineNode node = new MachineNode(point.getX(), point.getY(), title);
        nodes.put(title, node);
        this.getChildren().add(node);
        ++machineNodesCount;
    }

    /**
     * Add new machine node
     *
     * @param title machine node title
     * @param x     X coordinate of machine node
     * @param y     Y coordinate of machine node
     */
    public void addNode(String title, double x, double y) {
        MachineNode node = new MachineNode(x, y, title);
        nodes.put(title, node);
        this.getChildren().add(node);
        ++machineNodesCount;
    }

    /**
     * Remove node with specified title
     *
     * @param title node title to remove
     */
    public void removeNode(String title) {
        --machineNodesCount;
        MachineNode node = nodes.get(title);
        nodes.remove(title);
        this.getChildren().removeAll(node.removeAllTransitions());
        this.getChildren().remove(node);
        renameNodes(node);
    }

    /**
     * Rename all nodes after node removing
     *
     * @param removedNode removed machine node
     */
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
        nodes.remove(titleGenerator.generateTitle(machineNodesCount));
    }

    /**
     * Add transition line between nodes
     *
     * @param symbols transition symbols
     * @param from    from node title
     * @param to      to node title
     */
    public void addTransitionLine(Collection<String> symbols, String from, String to) {
        MachineNode fromNode = nodes.get(from);
        MachineNode toNode = nodes.get(to);
        TransitionLine transitionLine = fromNode.getTransitionOut(toNode);
        if (transitionLine == null) {
            transitionLine = new TransitionLine(fromNode, toNode, symbols);
            fromNode.addTransitionOut(transitionLine, toNode);
            toNode.addTransitionIn(transitionLine, fromNode);
            this.getChildren().add(transitionLine);
        } else {
            transitionLine.addTitles(symbols);
        }
        transitionLines.add(transitionLine);
    }

    /**
     * All transitions which exist from specified node getter
     *
     * @param from        node to get transitions from
     * @param transitions transitions to check
     * @return map of exiting transition lines
     */
    public Map<String, TransitionLine> getExitingTransitionLines(String from, Map<String, String> transitions) {
        MachineNode fromNode = nodes.get(from);
        Map<String, TransitionLine> exitingTransitionLines = new HashMap<>();
        Map<String, MachineNode> toNodes = new HashMap<>();
        for (String symbol : transitions.keySet()) {
            toNodes.put(symbol, getNode(transitions.get(symbol)));
        }
        for (String title : toNodes.keySet()) {
            MachineNode toNode = toNodes.get(title);
            exitingTransitionLines.put(title, fromNode.getTransitionOut(toNode));
        }
        return exitingTransitionLines;
    }

    /**
     * Removes titles from specified transitions
     *
     * @param exitingTransitionLines map [title to remove , transitions to remove from]
     */
    private void removeTransitionLinesTitles(Map<String, TransitionLine> exitingTransitionLines) {
        for (String title : exitingTransitionLines.keySet()) {
            TransitionLine transitionLine = exitingTransitionLines.get(title);
            transitionLine.removeTitle(title);
            if (transitionLine.isEmptyTitles()) {
                MachineNode fromNode = transitionLine.getStart();
                MachineNode toNode = transitionLine.getEnd();
                this.getChildren().remove(transitionLine);
                fromNode.removeTransitionOut(toNode);
                toNode.removeTransitionIn(fromNode);
                transitionLines.remove(transitionLine);
            }
        }
    }

    /**
     * Start node setter
     *
     * @param title title of the node to set as start node
     */
    public void setAsStartNode(String title) {
        MachineNode node = nodes.get(title);
        node.setAsStartNode();
    }

    /**
     * Unset start node
     *
     * @param title title of the node to unset as start node
     */
    public void unsetAsStartNode(String title) {
        MachineNode node = nodes.get(title);
        node.unsetAsStartNode();
    }

    /**
     * Final node setter
     *
     * @param title title of the node to set as final node
     */
    public void setAsFinalNode(String title) {
        MachineNode node = nodes.get(title);
        node.setAsFinalNode();
    }

    /**
     * Unset final node
     *
     * @param title title of the node to unset as final node
     */
    public void unsetAsFinalNode(String title) {
        MachineNode node = nodes.get(title);
        node.unsetAsFinalNode();
    }

    /**
     * Add new transitions and replace exiting transitions for these nodes
     *
     * @param symbols                transition symbols
     * @param from                   from node title
     * @param to                     to node title
     * @param exitingTransitionLines exiting transition for specified nodes
     */
    public void addAndReplaceTransitionLines(Collection<String> symbols, String from, String to, Map<String, TransitionLine> exitingTransitionLines) {
        removeTransitionLinesTitles(exitingTransitionLines);
        addTransitionLine(symbols, from, to);
    }

    /**
     * Machine graph nodes getter
     *
     * @return map of machine nodes
     */
    public Map<String, MachineNode> getNodes() {
        return nodes;
    }

    /**
     * Machine graph transitions getter
     *
     * @return machine graph transitions
     */
    public Set<TransitionLine> getTransitionLines() {
        return transitionLines;
    }

    /**
     * Current machine node setter
     *
     * @param nextState node to set as current
     */
    public void setCurrentNode(String nextState) {
        MachineNode node = nodes.get(nextState);
        if (currentNode != null) {
            currentNode.unsetAsCurrent();
        }
        currentNode = node;
        currentNode.setAsCurrent();
    }
}
