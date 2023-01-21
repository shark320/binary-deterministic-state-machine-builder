package com.vpavlov.visualization.draw_model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.shape.Line;

import java.util.Collection;

/**
 * Class represents arrow graphic model with title.
 *
 * @author vpavlov
 */
public class Arrow extends ATitledLine {

    /**
     * Title place according to the arrow length
     */
    private static final double TITLE_PLACE = 0.7;

    /**
     * Arrow end length
     */
    private static final double ARROW_LENGTH = 20;

    /**
     * Arrow end width
     */
    private static final double ARROW_WIDTH = 7;

    /**
     * Arrow line
     */
    private final Line line;

    /**
     * Constructor
     *
     * @param titles arrow line titles
     */
    public Arrow(Collection<String> titles) {

        this(new Line(), new Line(), new Line(), titles);
    }

    /**
     * Private constructor
     *
     * @param line   arrow lines
     * @param arrow1 first arrow end line
     * @param arrow2 second arrow end line
     * @param titles arrow titles
     */
    private Arrow(Line line, Line arrow1, Line arrow2, Collection<String> titles) {
        super(titles);
        line.getStyleClass().add("titled-line");
        arrow1.getStyleClass().add("titled-line");
        arrow2.getStyleClass().add("titled-line");
        this.getChildren().addAll(line, arrow1, arrow2);
        this.line = line;
        ChangeListener<Number> updater = (o, d, d1) -> {
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
                double factor = ARROW_LENGTH / Math.hypot(sx - ex, sy - ey);
                double factorO = ARROW_WIDTH / Math.hypot(sx - ex, sy - ey);

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


    /**
     * Start X setter
     *
     * @param value start x
     */
    public final void setStartX(double value) {
        line.setStartX(value);
    }

    /**
     * Start X getter
     *
     * @return start X
     */
    public final double getStartX() {
        return line.getStartX();
    }

    /**
     * Start X property
     *
     * @return start X property
     */
    public final DoubleProperty startXProperty() {
        return line.startXProperty();
    }

    /**
     * Start Y setter
     *
     * @param value start Y
     */
    public final void setStartY(double value) {
        line.setStartY(value);
    }

    /**
     * Start Y getter
     *
     * @return start Y
     */
    public final double getStartY() {
        return line.getStartY();
    }

    /**
     * Start Y property getter
     *
     * @return start Y
     */
    public final DoubleProperty startYProperty() {
        return line.startYProperty();
    }

    /**
     * End X setter
     *
     * @param value end X
     */
    public final void setEndX(double value) {
        line.setEndX(value);
    }

    /**
     * End X getter
     *
     * @return end X
     */
    public final double getEndX() {
        return line.getEndX();
    }

    /**
     * End X property getter
     *
     * @return end X property
     */
    public final DoubleProperty endXProperty() {
        return line.endXProperty();
    }

    /**
     * End Y setter
     *
     * @param value end Y
     */
    public final void setEndY(double value) {
        line.setEndY(value);
    }

    /**
     * End Y getter
     *
     * @return end Y
     */
    public final double getEndY() {
        return line.getEndY();
    }

    /**
     * End Y property getter
     *
     * @return end Y property
     */
    public final DoubleProperty endYProperty() {
        return line.endYProperty();
    }

    /**
     * Calculates angle of line
     *
     * @param sx start x
     * @param sy start y
     * @param ex end x
     * @param ey end y
     * @return angle of line in radians
     */
    protected double calculateAngle(double sx, double sy, double ex, double ey) {
        double tan = (ey - sy) / (ex - sx);
        return Math.atan(tan);
    }

    @Override
    public String toString() {
        return String.format("(Arrow [%f , %f] --> [%f , %f] )", line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY());
    }

    protected double getLength() {
        double dX = getStartX() - getEndX();
        double dY = getStartY() - getEndY();
        return Math.sqrt(dX * dX + dY * dY);
    }

    @Override
    protected void calculateTitle() {
        double angle = calculateAngle(getStartX(), getStartY(), getEndX(), getEndY());
        double length = getLength();
        double cx = getStartX() + length * TITLE_PLACE * Math.cos(Math.abs(angle)) * Math.signum(getEndX() - getStartX());
        double cy = getStartY() + length * TITLE_PLACE * Math.sin(Math.abs(angle)) * Math.signum(getEndY() - getStartY());
        double pAngle = -angle;
        double textPadding = Math.signum(pAngle) > 0 ? title.getLayoutBounds().getWidth() : 0;
        double x = cx - TITLE_PADDING * Math.sin(pAngle) - textPadding;
        double y = cy - TITLE_PADDING * Math.cos(pAngle);
        title.setX(x);
        title.setY(y);
    }

}