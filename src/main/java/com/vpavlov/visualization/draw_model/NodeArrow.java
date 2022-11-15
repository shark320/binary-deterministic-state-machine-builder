package com.vpavlov.visualization.draw_model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class NodeArrow extends Arrow {

    private class CoordinatesChangedListener implements ChangeListener<Number> {

        @Override
        public void changed(ObservableValue<? extends Number> observableValue, Number aDouble, Number t1) {
            calculateArrow();
            calculateTitle();
        }
    }

    private DoubleProperty startNodeX;

    private DoubleProperty startNodeY;

    private DoubleProperty endNodeX;

    private DoubleProperty endNodeY;

    private DoubleProperty nodeRadius;

    public NodeArrow(MachineNode startNode, MachineNode endNode, String title) {
        super(title);
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
        calculateTitle();
        configureTitles();
    }

    private void calculateArrow() {
        double radius = nodeRadius.getValue();
        double endNodeXValue = endNodeX.doubleValue();
        double endNodeYValue = endNodeY.doubleValue();
        double startNodeXValue = startNodeX.doubleValue();
        double startNodeYValue = startNodeY.doubleValue();
        double angle = Math.abs(calculateAngle(startNodeXValue, startNodeYValue,endNodeXValue, endNodeYValue));

        double difX = radius * Math.cos(angle) * Math.signum(endNodeXValue-startNodeXValue);
        double difY = radius * Math.sin(angle) * Math.signum(endNodeYValue-startNodeYValue);

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
