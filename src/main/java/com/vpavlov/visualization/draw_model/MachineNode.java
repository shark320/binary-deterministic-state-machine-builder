package com.vpavlov.visualization.draw_model;

import javafx.beans.property.DoubleProperty;
import javafx.geometry.Point2D;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.util.HashMap;
import java.util.Map;

public class MachineNode extends Region {

    private static final Color DEFAULT_NODE_COLOR = Color.AQUA;

    private static final Color CURRENT_NODE_COLOR = Color.RED;

    private static final Color SELECTION_NODE_COLOR = Color.BLUE;

    private static final Color START_NODE_BORDER_COLOR = Color.YELLOW;

    private static final Color END_NODE_BORDER_COLOR = Color.ORANGE;

    private static final double DEFAULT_NODE_RADIUS = 20;

    private static final double SPECIAL_NODE_BORDER_WIDTH = DEFAULT_NODE_RADIUS*0.2;

    private final Map<String,TransitionLine> transitions = new HashMap<>();

    private final TransitionLine transitionLoop = null;

    private Circle circle;

    private Text title;

    private boolean isCurrent = false;

    public MachineNode(double x, double y, String title){
        circle = new Circle(x, y, DEFAULT_NODE_RADIUS, DEFAULT_NODE_COLOR);
        this.title = createTitle(x,y,title);
        this.getChildren().addAll(circle,this.title);
    }

    public MachineNode (MachineNode node){
        circle = node.circle;
        title = node.title;
    }

    private Text createTitle(double x, double y, String title){
        Text t = new Text(title);
        double width = t.getLayoutBounds().getWidth();
        double height = t.getLayoutBounds().getHeight();
        t.setX(x-width/2);
        t.setY(y+height/4);
        //t.setY(y);
        return t;
    }

    public void setAsStartNode(){
        circle.setStrokeWidth(SPECIAL_NODE_BORDER_WIDTH);
        circle.setStroke(START_NODE_BORDER_COLOR);
    }

    @Override
    public boolean contains(Point2D point){
        double x = circle.getCenterX();
        double y = circle.getCenterY();
        Point2D p = new Point2D(x,y);
        return p.distance(point) < circle.getRadius();
    }

    public DoubleProperty getCenterXProperty(){
        return circle.centerXProperty();
    }

    public void setCoordinates(double x, double y){
        circle.setCenterX(x);
        circle.setCenterY(y);
        setTitleCoordinates(x,y);
    }

    private void setTitleCoordinates(double x, double y){
        double width = title.getLayoutBounds().getWidth();
        double height = title.getLayoutBounds().getHeight();
        title.setX(x-width/2);
        title.setY(y+height/4);
    }

    public DoubleProperty getCenterYProperty(){
        return circle.centerYProperty();
    }

    public DoubleProperty getRadiusProperty(){
        return circle.radiusProperty();
    }

    public double getRadius() {
        return circle.getRadius();
    }

    public void select(){
        circle.setFill(SELECTION_NODE_COLOR);
    }

    public void unselect(){
        if (!isCurrent) {
            circle.setFill(DEFAULT_NODE_COLOR);
        }else{
            setAsCurrent();
        }
    }

    public void setAsCurrent(){
        isCurrent = true;
        circle.setFill(CURRENT_NODE_COLOR);
    }

    public void unsetAsCurrent(){
        isCurrent = false;
        circle.setFill(DEFAULT_NODE_COLOR);
    }

    public String getTitle(){
        return this.title.getText();
    }



    @Override
    public String toString(){
        return String.format("(MachineNode '%s' [%f , %f] : %f)",title.getText(), circle.getCenterX(), circle.getCenterY(), circle.getRadius());
    }

    public Point2D getPosition(){
        return new Point2D(circle.getCenterX(), circle.getCenterY());
    }
}
