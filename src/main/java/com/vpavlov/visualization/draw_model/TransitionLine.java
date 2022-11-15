package com.vpavlov.visualization.draw_model;

import javafx.scene.layout.Region;

import java.util.Set;

public class TransitionLine extends Region{

    private final MachineNode start;

    private final MachineNode end;

    private final ATitledLine line;

    public TransitionLine(MachineNode start, MachineNode end, String title) {
        this.start = start;
        this.end = end;
        if (end != start) {
            line = new NodeArrow(start, end, title);
        }else{
            line = new LoopLine(start, title);
        }
        this.getChildren().add(line);
    }

    public TransitionLine(MachineNode node, String title){
        this.start = this.end = node;
        line = new LoopLine(node, title);
        this.getChildren().add(line);
    }

    public MachineNode getStart(){
        return this.start;
    }

    public MachineNode getEnd(){
        return this.end;
    }

    public void addTitle(String title){

    }

    public Set<String> getTitles(){
        return line.getTitles();
    }
}
