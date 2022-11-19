package com.vpavlov.visualization.draw_model;

import com.vpavlov.App;
import com.vpavlov.services.api.TitleGenerator;
import javafx.geometry.Point2D;
import javafx.scene.Group;

import java.util.*;

public class MachineGraph extends Group {

    private final Map<String, MachineNode> nodes;

    private final Set<TransitionLine> transitionLines;

    private int machineNodesCount = 0;

    private final TitleGenerator titleGenerator;

    private MachineNode currentNode = null;

    public MachineGraph(TitleGenerator titleGenerator) {
        this.titleGenerator = titleGenerator;
        this.nodes = new HashMap<>();
        this.transitionLines = new HashSet<>();
        this.getStylesheets().add(App.class.getResource("machine-graph.css").toExternalForm());
    }

    public MachineGraph(MachineGraph machineGraph) {
        this.currentNode = null;
        this.machineNodesCount = machineGraph.machineNodesCount;
        this.titleGenerator = machineGraph.titleGenerator;
        this.nodes = copyNodes(machineGraph.nodes);
        this.transitionLines = new HashSet<>();
        copyTransitionLines(machineGraph.transitionLines);
        this.getStylesheets().add(App.class.getResource("machine-graph.css").toExternalForm());
    }

    private void copyTransitionLines(Set<TransitionLine> transitionLines) {
        Set<TransitionLine> newTransitionLines = new HashSet<>();
        for (TransitionLine transitionLine : transitionLines) {
            String from = transitionLine.getStart().getTitle();
            String to = transitionLine.getEnd().getTitle();
            Set<String> symbols = transitionLine.getTitles();
            addTransitionLine(symbols, from, to);
        }
    }

    private Map<String, MachineNode> copyNodes(Map<String, MachineNode> nodes) {
        Map<String, MachineNode> newNodes = new HashMap<>();
        for (MachineNode node : nodes.values()) {
            MachineNode newNode = new MachineNode(node);
            newNodes.put(node.getTitle(), newNode);
            this.getChildren().add(newNode);
        }
        return newNodes;
    }

    public MachineNode getNode(String title) {
        return nodes.get(title);
    }

    public void addNode(Point2D point) {
        String title = titleGenerator.generateTitle(machineNodesCount);
        MachineNode node = new MachineNode(point.getX(), point.getY(), title);
        nodes.put(title, node);
        this.getChildren().add(node);
        ++machineNodesCount;
    }

    public void addNode(String title, double x, double y) {
        MachineNode node = new MachineNode(x, y, title);
        nodes.put(title, node);
        this.getChildren().add(node);
        ++machineNodesCount;
    }

    public void removeNode(String title) {
        --machineNodesCount;
        MachineNode node = nodes.get(title);
        nodes.remove(title);
        this.getChildren().removeAll(node.removeAllTransitions());
        this.getChildren().remove(node);
        renameNodes(node);
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
        nodes.remove(titleGenerator.generateTitle(machineNodesCount));
    }


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

    public void setAsStartNode(String title) {
        MachineNode node = nodes.get(title);
        node.setAsStartNode();
    }

    public void unsetAsStartNode(String title) {
        MachineNode node = nodes.get(title);
        node.unsetAsStartNode();
    }

    public void setAsFinalNode(String title) {
        MachineNode node = nodes.get(title);
        node.setAsFinalNode();
    }

    public void unsetAsFinalNode(String title) {
        MachineNode node = nodes.get(title);
        node.unsetAsFinalNode();
    }

    public void addAndReplaceTransitionLines(Collection<String> symbols, String from, String to, Map<String, TransitionLine> exitingTransitionLines) {
        removeTransitionLinesTitles(exitingTransitionLines);
        addTransitionLine(symbols, from, to);
    }

    public Map<String, MachineNode> getNodes() {
        return nodes;
    }

    public Set<TransitionLine> getTransitionLines() {
        return transitionLines;
    }

    public void setCurrentNode(String nextState) {
        MachineNode node = nodes.get(nextState);
        if (currentNode != null){
            currentNode.unsetAsCurrent();
        }
        currentNode = node;
        currentNode.setAsCurrent();
    }
}
