package com.vpavlov.visualization.draw_model;

import com.vpavlov.machine.Machine;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.Point2D;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.util.*;

public class MachineNode extends Region {

    private static final double DEFAULT_NODE_RADIUS = 30;

    private static final String DEFAULT_NODE_STYLE = "machine-node";

    private static final String SELECTED_NODE_STYLE = "machine-node-selected";

    private static final String CURRENT_NODE_STYLE = "machine-node-current";

    private static final String START_NODE_STYLE = "machine-node-start";

    private static final String FINAL_NODE_STYLE = "machine-node-final";

    private static final String TEXT_NODE_STYLE = "machine-node-title";


    private final Map<MachineNode,TransitionLine> transitionsIn = new HashMap<>();

    private final Map<MachineNode,TransitionLine> transitionsOut = new HashMap<>();

    private final Circle circle;

    private final Text title;
    private boolean isStartNode = false;

    private boolean isFinalNode = false;

    private boolean isCurrent = false;

    public MachineNode(double x, double y, String title){
        circle = new Circle(x, y, DEFAULT_NODE_RADIUS);
        this.title = createTitle(x,y,title);
        this.title.getStyleClass().add(TEXT_NODE_STYLE);
        this.getChildren().addAll(this.circle,this.title);
        this.circle.getStyleClass().add(DEFAULT_NODE_STYLE);
    }

    public MachineNode(MachineNode node){
        double x = node.circle.getCenterX();
        double y = node.circle.getCenterY();
        this.circle = new Circle(x, y, DEFAULT_NODE_RADIUS);
        this.title = createTitle(x,y,node.getTitle());
        this.title.getStyleClass().add(TEXT_NODE_STYLE);
        this.getChildren().addAll(this.circle,this.title);
        this.circle.getStyleClass().add(DEFAULT_NODE_STYLE);
        if (node.isStartNode){
            this.isStartNode = true;
            this.setAsStartNode();
        }
        if (node.isFinalNode){
            this.isFinalNode = true;
            this.setAsFinalNode();
        }
        this.isCurrent = false;
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
        isStartNode = true;
        circle.getStyleClass().add(START_NODE_STYLE);
        //circle.getStyleClass().add("machine-node-start");
    }

    public void unsetAsStartNode(){
        isStartNode = false;
        circle.getStyleClass().remove(START_NODE_STYLE);
    }

    public void setAsFinalNode(){
        isFinalNode = true;
        circle.getStyleClass().add(FINAL_NODE_STYLE);
    }

    public void unsetAsFinalNode(){
        isFinalNode = false;
        circle.getStyleClass().remove(FINAL_NODE_STYLE);
    }

    public boolean isFinalNode() {
        return isFinalNode;
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
        circle.getStyleClass().add(SELECTED_NODE_STYLE);
    }

    public void unselect(){
        circle.getStyleClass().remove(SELECTED_NODE_STYLE);
    }

    public void setAsCurrent(){
        isCurrent = true;
        circle.getStyleClass().add(CURRENT_NODE_STYLE);
    }

    public void unsetAsCurrent(){
        isCurrent = false;
        circle.getStyleClass().remove(CURRENT_NODE_STYLE);
    }

    public String getTitle(){
        return this.title.getText();
    }

    public void setTitle(String newTitle){
        this.title.setText(newTitle);
    }

    public boolean isStartNode(){
        return isStartNode;
    }

    public void addTransitionIn(TransitionLine transition, MachineNode fromNode){
        transitionsIn.put(fromNode,transition);
    }

    public void addTransitionOut(TransitionLine transition, MachineNode toNode){
        transitionsOut.put(toNode,transition);
    }

    public TransitionLine getTransitionIn (MachineNode fromNode){
        return transitionsIn.get(fromNode);
    }

    public TransitionLine getTransitionOut (MachineNode toNode){
        return transitionsOut.get(toNode);
    }

    public void removeTransitionIn(MachineNode fromNode){
        transitionsIn.remove(fromNode);
    }

    public void removeTransitionOut(MachineNode toNode){
        transitionsOut.remove(toNode);
    }


    public Collection<TransitionLine> removeAllTransitions(){
        Set<TransitionLine> toRemove = new HashSet<>();
        toRemove.addAll(removeAllTransitionsIn());
        toRemove.addAll(removeAllTransitionsOut());

        return toRemove;
    }

    private Collection<TransitionLine> removeAllTransitionsIn(){
        Set<TransitionLine> toRemove = new HashSet<>();
        for (MachineNode node : transitionsIn.keySet()){
            TransitionLine transitionLine = transitionsIn.get(node);
            node.removeTransitionOut(this);
            toRemove.add(transitionLine);
        }
        return toRemove;
    }

    private Collection<TransitionLine> removeAllTransitionsOut(){
        Set<TransitionLine> toRemove = new HashSet<>();
        for (MachineNode node : transitionsOut.keySet()){
            TransitionLine transitionLine = transitionsOut.get(node);
            node.removeTransitionIn(this);
            toRemove.add(transitionLine);
        }
        return toRemove;
    }

    @Override
    public String toString(){
        return String.format("(MachineNode '%s' [%f , %f] : %f)",title.getText(), circle.getCenterX(), circle.getCenterY(), circle.getRadius());
    }

    public Point2D getPosition(){
        return new Point2D(circle.getCenterX(), circle.getCenterY());
    }
}
