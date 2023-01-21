package com.vpavlov.visualization.draw_model;

import javafx.scene.layout.Region;

import java.util.Collection;
import java.util.Set;

/**
 * Class representing transition line in machine graph
 *
 * @author vpavlov
 */
public class TransitionLine extends Region {

    /**
     * Start machine node
     */
    private final MachineNode start;

    /**
     * End machine node
     */
    private final MachineNode end;

    /**
     * Title line
     */
    private final ATitledLine line;

    /**
     * Constructor
     *
     * @param start  start transition node
     * @param end    end transition node
     * @param titles transition symbols
     */
    public TransitionLine(MachineNode start, MachineNode end, Collection<String> titles) {
        this.start = start;
        this.end = end;
        if (end != start) {
            line = new NodeArrow(start, end, titles);
        } else {
            line = new LoopLine(start, titles);
        }
        this.getChildren().add(line);
    }

    /**
     * Start node getter
     *
     * @return start node
     */
    public MachineNode getStart() {
        return this.start;
    }

    /**
     * End node getter
     *
     * @return end node
     */
    public MachineNode getEnd() {
        return this.end;
    }

    /**
     * Remove given titles
     *
     * @param title title to remove
     */
    public void removeTitle(String title) {
        line.removeTitle(title);
    }

    /**
     * Check is the line has empty titles
     *
     * @return true if the line has empty titles, else false
     */
    public boolean isEmptyTitles() {
        return line.getTitles().isEmpty();
    }

    /**
     * Line titles getter
     *
     * @return line titles
     */
    public Set<String> getTitles() {
        return line.getTitles();
    }

    /**
     * Add titles to the line
     *
     * @param titles titles to add
     */
    public void addTitles(Collection<String> titles) {
        line.addTitles(titles);
    }

    /**
     * Remove titles from the line
     *
     * @param symbols titles to remove
     */
    public void removeTitles(Collection<String> symbols) {
        line.removeTitles(symbols);
    }

    @Override
    public String toString() {
        return String.format("TransitionLine <%s> --%s--> <%s>", start.getTitle(), line.getTitles(), end.getTitle());
    }
}
