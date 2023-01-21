package com.vpavlov.visualization.draw_model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.util.Collection;

/**
 * Class representing arrow from one machine node to another
 *
 * @author vpavlov
 * @see MachineNode
 */
public class NodeArrow extends Arrow {

    /**
     * Inner class-listener for start and end coordinates changes
     */
    private class CoordinatesChangedListener implements ChangeListener<Number> {

        @Override
        public void changed(ObservableValue<? extends Number> observableValue, Number aDouble, Number t1) {
            calculateArrow();
            calculateTitle();
        }
    }

    /**
     * Start X property
     */
    private final DoubleProperty startNodeX;

    /**
     * Start Y property
     */
    private final DoubleProperty startNodeY;

    /**
     * End X property
     */
    private final DoubleProperty endNodeX;

    /**
     * End Y property
     */
    private final DoubleProperty endNodeY;

    /**
     * Node radius property
     */
    private final DoubleProperty nodeRadius;

    /**
     * Constructor
     *
     * @param startNode node from which arrow is
     * @param endNode   node to which arrow is
     * @param titles    node titles
     */
    public NodeArrow(MachineNode startNode, MachineNode endNode, Collection<String> titles) {
        super(titles);
        this.startNodeX = startNode.getCenterXProperty();
        this.startNodeY = startNode.getCenterYProperty();
        this.endNodeX = endNode.getCenterXProperty();
        this.endNodeY = endNode.getCenterYProperty();
        this.nodeRadius = startNode.getRadiusProperty();

        CoordinatesChangedListener updater = new CoordinatesChangedListener();

        startNodeX.addListener(updater);
        startNodeY.addListener(updater);
        endNodeX.addListener(updater);
        endNodeY.addListener(updater);

        calculateArrow();
        configureTitles();
        calculateTitle();
    }

    /**
     * Calculates start and end node coordinates
     */
    private void calculateArrow() {
        double radius = nodeRadius.getValue();
        double endNodeXValue = endNodeX.doubleValue();
        double endNodeYValue = endNodeY.doubleValue();
        double startNodeXValue = startNodeX.doubleValue();
        double startNodeYValue = startNodeY.doubleValue();
        double angle = Math.abs(calculateAngle(startNodeXValue, startNodeYValue, endNodeXValue, endNodeYValue));

        double difX = radius * Math.cos(angle) * Math.signum(endNodeXValue - startNodeXValue);
        double difY = radius * Math.sin(angle) * Math.signum(endNodeYValue - startNodeYValue);

        double sx = startNodeXValue + difX;
        double sy = startNodeYValue + difY;

        double ex = endNodeXValue - difX;
        double ey = endNodeYValue - difY;

        setStartX(sx);
        setStartY(sy);

        setEndX(ex);
        setEndY(ey);
    }
}
