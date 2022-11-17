package com.vpavlov.visualization.draw_model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.shape.Line;

import java.util.Collection;

public class Arrow extends ATitledLine {
    
    private static final double TITLE_PLACE = 0.7;

    private final Line line;

    public Arrow(Collection<String> titles) {

        this(new Line(), new Line(), new Line(), titles);
    }

    private static final double arrowLength = 20;
    private static final double arrowWidth = 7;

    private Arrow(Line line, Line arrow1, Line arrow2, Collection<String> titles) {
        super(titles);
        line.getStyleClass().add("titled-line");
        arrow1.getStyleClass().add("titled-line");
        arrow2.getStyleClass().add("titled-line");
        this.getChildren().addAll(line, arrow1, arrow2);
        this.line = line;
        ChangeListener<Number> updater = (o,d,d1) -> {
            double ex = getEndX();
            double ey = getEndY();
            double sx = getStartX();
            double sy = getStartY();

            arrow1.setEndX(ex);
            arrow1.setEndY(ey);
            arrow2.setEndX(ex);
            arrow2.setEndY(ey);

            if (ex == sx && ey == sy) {
                // arrow parts of length 0
                arrow1.setStartX(ex);
                arrow1.setStartY(ey);
                arrow2.setStartX(ex);
                arrow2.setStartY(ey);
            } else {
                double factor = arrowLength / Math.hypot(sx-ex, sy-ey);
                double factorO = arrowWidth / Math.hypot(sx-ex, sy-ey);

                // part in direction of main line
                double dx = (sx - ex) * factor;
                double dy = (sy - ey) * factor;

                // part ortogonal to main line
                double ox = (sx - ex) * factorO;
                double oy = (sy - ey) * factorO;

                arrow1.setStartX(ex + dx - oy);
                arrow1.setStartY(ey + dy + ox);
                arrow2.setStartX(ex + dx + oy);
                arrow2.setStartY(ey + dy - ox);
            }
        };

        // add updater to properties
        startXProperty().addListener(updater);
        startYProperty().addListener(updater);
        endXProperty().addListener(updater);
        endYProperty().addListener(updater);
    }

    // start/end properties

    public final void setStartX(double value) {
        line.setStartX(value);
    }

    public final double getStartX() {
        return line.getStartX();
    }

    public final DoubleProperty startXProperty() {
        return line.startXProperty();
    }

    public final void setStartY(double value) {
        line.setStartY(value);
    }

    public final double getStartY() {
        return line.getStartY();
    }

    public final DoubleProperty startYProperty() {
        return line.startYProperty();
    }

    public final void setEndX(double value) {
        line.setEndX(value);
    }

    public final double getEndX() {
        return line.getEndX();
    }

    public final DoubleProperty endXProperty() {
        return line.endXProperty();
    }

    public final void setEndY(double value) {
        line.setEndY(value);
    }

    public final double getEndY() {
        return line.getEndY();
    }

    public final DoubleProperty endYProperty() {
        return line.endYProperty();
    }

    protected double calculateAngle(double sx, double sy, double ex, double ey) {

        double tan = (ey - sy) / (ex - sx);

        return Math.atan(tan);
    }

    @Override
    public String toString() {
        return String.format("(Arrow [%f , %f] --> [%f , %f] )", line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY());
    }
    
    protected double getLength(){
        double dX = getStartX() - getEndX();
        double dY = getStartY() - getEndY();
        return Math.sqrt(dX*dX + dY*dY);
    }

    @Override
    protected void calculateTitle() {
        double angle = calculateAngle(getStartX(), getStartY(), getEndX(), getEndY());
        double length = getLength();
        double cx = getStartX() + length*TITLE_PLACE*Math.cos(Math.abs(angle))*Math.signum(getEndX()-getStartX());
        double cy = getStartY() + length*TITLE_PLACE*Math.sin(Math.abs(angle))*Math.signum(getEndY()-getStartY());
        double pAngle = -angle;
        double textPadding = Math.signum(pAngle)>0?title.getLayoutBounds().getWidth():0;
        double x = cx - TITLE_PADDING*Math.sin(pAngle)-textPadding;
        double y = cy - TITLE_PADDING*Math.cos(pAngle);
        title.setX(x);
        title.setY(y);
    }

}