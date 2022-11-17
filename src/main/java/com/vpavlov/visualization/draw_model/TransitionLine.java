package com.vpavlov.visualization.draw_model;

import javafx.scene.layout.Region;

import java.util.Collection;
import java.util.Set;

public class TransitionLine extends Region{

    private final MachineNode start;

    private final MachineNode end;

    private final ATitledLine line;

    public TransitionLine(MachineNode start, MachineNode end, Collection<String> titles) {
        this.start = start;
        this.end = end;
        if (end != start) {
            line = new NodeArrow(start, end, titles);
        }else{
            line = new LoopLine(start, titles);
        }
        this.getChildren().add(line);
    }


    public MachineNode getStart(){
        return this.start;
    }

    public MachineNode getEnd(){
        return this.end;
    }

    public void addTitle(String title){
        line.addTitle(title);
    }

    public void removeTitle(String title){
        line.removeTitle(title);
    }

    public boolean isEmptyTitles(){
        return line.getTitles().isEmpty();
    }

    public Set<String> getTitles(){
        return line.getTitles();
    }

    public void addTitles(Collection<String> titles) {
        line.addTitles(titles);
    }

    public void removeTitles(Collection<String> symbols) {
        line.removeTitles(symbols);
    }

    public boolean containsTitles(String title){
       return line.titles.contains(title);
    }

    @Override
    public String toString() {
        return String.format("TransitionLine <%s> --%s--> <%s>", start.getTitle(), line.getTitles(), end.getTitle());
    }
}
