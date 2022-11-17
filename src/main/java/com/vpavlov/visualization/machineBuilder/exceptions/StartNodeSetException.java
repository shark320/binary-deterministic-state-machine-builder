package com.vpavlov.visualization.machineBuilder.exceptions;

import com.vpavlov.visualization.draw_model.MachineNode;

public class StartNodeSetException  extends Exception{

    private final MachineNode startNode;

    public StartNodeSetException(MachineNode startNode) {
        this.startNode = startNode;
    }

    public MachineNode getStartNode() {
        return startNode;
    }
}
