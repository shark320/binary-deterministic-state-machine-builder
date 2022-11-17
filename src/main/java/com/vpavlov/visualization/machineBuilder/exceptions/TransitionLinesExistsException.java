package com.vpavlov.visualization.machineBuilder.exceptions;

import com.vpavlov.visualization.draw_model.MachineNode;
import com.vpavlov.visualization.draw_model.TransitionLine;

import java.util.Map;

public class TransitionLinesExistsException extends Exception {

    private final MachineNode node;

    private final Map<String, TransitionLine> transitionLines;


    public TransitionLinesExistsException(MachineNode node, Map<String, TransitionLine> transitionLines) {
        this.node = node;
        this.transitionLines = transitionLines;
    }

    public MachineNode getNode() {
        return node;
    }

    public Map<String, TransitionLine> getTransitionLines() {
        return transitionLines;
    }
}
