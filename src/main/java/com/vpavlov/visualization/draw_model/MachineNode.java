package com.vpavlov.visualization.draw_model;

import javafx.beans.property.DoubleProperty;
import javafx.geometry.Point2D;
import javafx.scene.layout.Region;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.util.*;

/**
 * Class representing machine node element
 *
 * @author vpavlov
 */
public class MachineNode extends Region {

    /**
     * Default node radius
     */
    private static final double DEFAULT_NODE_RADIUS = 30;

    /**
     * Default node style
     */
    private static final String DEFAULT_NODE_STYLE = "machine-node";

    /**
     * Selected node style
     */
    private static final String SELECTED_NODE_STYLE = "machine-node-selected";

    /**
     * Current node style
     */
    private static final String CURRENT_NODE_STYLE = "machine-node-current";

    /**
     * Start node style
     */
    private static final String START_NODE_STYLE = "machine-node-start";

    /**
     * Final node style
     */
    private static final String FINAL_NODE_STYLE = "machine-node-final";

    /**
     * Node title style
     */
    private static final String TEXT_NODE_STYLE = "machine-node-title";

    /**
     * Transition lines to the node
     */
    private final Map<MachineNode, TransitionLine> transitionsIn = new HashMap<>();

    /**
     * Transition lines from the node
     */
    private final Map<MachineNode, TransitionLine> transitionsOut = new HashMap<>();

    /**
     * Node circle element
     */
    private final Circle circle;

    /**
     * Node title element
     */
    private final Text title;

    /**
     * Is start node flag
     */
    private boolean isStartNode = false;

    /**
     * Is final node flag
     */
    private boolean isFinalNode = false;

    /**
     * Is current node flag
     */
    private boolean isCurrent = false;

    /**
     * Constructor
     *
     * @param x     center X coordinate
     * @param y     center Y coordinate
     * @param title node title
     */
    public MachineNode(double x, double y, String title) {
        circle = new Circle(x, y, DEFAULT_NODE_RADIUS);
        this.title = createTitle(x, y, title);
        this.title.getStyleClass().add(TEXT_NODE_STYLE);
        this.getChildren().addAll(this.circle, this.title);
        this.circle.getStyleClass().add(DEFAULT_NODE_STYLE);
    }

    /**
     * Deep copy constructor
     *
     * @param node node to copy
     */
    public MachineNode(MachineNode node) {
        double x = node.circle.getCenterX();
        double y = node.circle.getCenterY();
        this.circle = new Circle(x, y, DEFAULT_NODE_RADIUS);
        this.title = createTitle(x, y, node.getTitle());
        this.title.getStyleClass().add(TEXT_NODE_STYLE);
        this.getChildren().addAll(this.circle, this.title);
        this.circle.getStyleClass().add(DEFAULT_NODE_STYLE);
        if (node.isStartNode) {
            this.isStartNode = true;
            this.setAsStartNode();
        }
        if (node.isFinalNode) {
            this.isFinalNode = true;
            this.setAsFinalNode();
        }
        this.isCurrent = false;
    }

    /**
     * Create title text element
     *
     * @param x     text center X coordinate
     * @param y     text center Y coordinate
     * @param title title
     * @return text element with specified parameters
     */
    private Text createTitle(double x, double y, String title) {
        Text t = new Text(title);
        double width = t.getLayoutBounds().getWidth();
        double height = t.getLayoutBounds().getHeight();
        t.setX(x - width / 2);
        t.setY(y + height / 4);
        ;
        return t;
    }

    /**
     * Set the node as start node
     */
    public void setAsStartNode() {
        isStartNode = true;
        circle.getStyleClass().add(START_NODE_STYLE);
    }

    /**
     * Unset the node as start node
     */
    public void unsetAsStartNode() {
        isStartNode = false;
        circle.getStyleClass().remove(START_NODE_STYLE);
    }

    /**
     * Set the node as final node
     */
    public void setAsFinalNode() {
        isFinalNode = true;
        circle.getStyleClass().add(FINAL_NODE_STYLE);
    }

    /**
     * Unset the node as final node
     */
    public void unsetAsFinalNode() {
        isFinalNode = false;
        circle.getStyleClass().remove(FINAL_NODE_STYLE);
    }

    /**
     * Check if the node is final node
     *
     * @return true if the node is final node, else false
     */
    public boolean isFinalNode() {
        return isFinalNode;
    }

    @Override
    public boolean contains(Point2D point) {
        double x = circle.getCenterX();
        double y = circle.getCenterY();
        Point2D p = new Point2D(x, y);
        return p.distance(point) < circle.getRadius();
    }

    /**
     * Center X property getter
     *
     * @return center X property
     */
    public DoubleProperty getCenterXProperty() {
        return circle.centerXProperty();
    }

    /**
     * Set new node center
     *
     * @param x center X property
     * @param y center Y property
     */
    public void setCoordinates(double x, double y) {
        circle.setCenterX(x);
        circle.setCenterY(y);
        setTitleCoordinates(x, y);
    }

    /**
     * Set node title coordinates
     *
     * @param x title center X coordinates
     * @param y title center Y coordinates
     */
    private void setTitleCoordinates(double x, double y) {
        double width = title.getLayoutBounds().getWidth();
        double height = title.getLayoutBounds().getHeight();
        title.setX(x - width / 2);
        title.setY(y + height / 4);
    }

    /**
     * Center Y property getter
     *
     * @return center Y property
     */
    public DoubleProperty getCenterYProperty() {
        return circle.centerYProperty();
    }

    /**
     * Node radius property getter
     *
     * @return node radius property
     */
    public DoubleProperty getRadiusProperty() {
        return circle.radiusProperty();
    }

    /**
     * Node radius getter
     *
     * @return node radius
     */
    public double getRadius() {
        return circle.getRadius();
    }

    /**
     * Set the node as selected
     */
    public void select() {
        circle.getStyleClass().add(SELECTED_NODE_STYLE);
    }

    /**
     * Unset the node as selected
     */
    public void unselect() {
        circle.getStyleClass().remove(SELECTED_NODE_STYLE);
    }

    /**
     * Set the node as current
     */
    public void setAsCurrent() {
        isCurrent = true;
        circle.getStyleClass().add(CURRENT_NODE_STYLE);
    }

    /**
     * Unset the node as current
     */
    public void unsetAsCurrent() {
        isCurrent = false;
        circle.getStyleClass().remove(CURRENT_NODE_STYLE);
    }

    /**
     * Node title getter
     *
     * @return node title
     */
    public String getTitle() {
        return this.title.getText();
    }

    /**
     * Set new node title
     *
     * @param newTitle new title to set
     */
    public void setTitle(String newTitle) {
        this.title.setText(newTitle);
        //TODO: check if title element size was changed
    }

    /**
     * Is the node is start node
     *
     * @return true if the node is start node, else false
     */
    public boolean isStartNode() {
        return isStartNode;
    }

    /**
     * Add new transition line to the node
     *
     * @param transition transition line to add
     * @param fromNode   node from which transition is
     */
    public void addTransitionIn(TransitionLine transition, MachineNode fromNode) {
        transitionsIn.put(fromNode, transition);
    }


    /**
     * Add new transition line from the node
     *
     * @param transition transition line to add
     * @param toNode     node to which transition is
     */
    public void addTransitionOut(TransitionLine transition, MachineNode toNode) {
        transitionsOut.put(toNode, transition);
    }

    /**
     * Transition line from the node getter
     *
     * @param toNode node to which transition is
     * @return found transition line
     */
    public TransitionLine getTransitionOut(MachineNode toNode) {
        return transitionsOut.get(toNode);
    }

    /**
     * Transition line to the node remover
     *
     * @param fromNode node from which transition is have to be removed
     */
    public void removeTransitionIn(MachineNode fromNode) {
        transitionsIn.remove(fromNode);
    }

    /**
     * Transition line from the node remover
     *
     * @param toNode node to which transition is have to be removed
     */
    public void removeTransitionOut(MachineNode toNode) {
        transitionsOut.remove(toNode);
    }

    /**
     * Removes all transitions for the node
     *
     * @return removed transitions
     */
    public Collection<TransitionLine> removeAllTransitions() {
        Set<TransitionLine> toRemove = new HashSet<>();
        toRemove.addAll(removeAllTransitionsIn());
        toRemove.addAll(removeAllTransitionsOut());

        return toRemove;
    }

    /**
     * Removes all transitions to the node
     *
     * @return removed transitions to the node
     */
    private Collection<TransitionLine> removeAllTransitionsIn() {
        Set<TransitionLine> toRemove = new HashSet<>();
        for (MachineNode node : transitionsIn.keySet()) {
            TransitionLine transitionLine = transitionsIn.get(node);
            node.removeTransitionOut(this);
            toRemove.add(transitionLine);
        }
        return toRemove;
    }

    /**
     * Removes all transitions from the node
     *
     * @return removed transitions from the node
     */
    private Collection<TransitionLine> removeAllTransitionsOut() {
        Set<TransitionLine> toRemove = new HashSet<>();
        for (MachineNode node : transitionsOut.keySet()) {
            TransitionLine transitionLine = transitionsOut.get(node);
            node.removeTransitionIn(this);
            toRemove.add(transitionLine);
        }
        return toRemove;
    }

    @Override
    public String toString() {
        return String.format("(MachineNode '%s' [%f , %f] : %f)", title.getText(), circle.getCenterX(), circle.getCenterY(), circle.getRadius());
    }

    /**
     * Node position getter
     *
     * @return node position
     */
    public Point2D getPosition() {
        return new Point2D(circle.getCenterX(), circle.getCenterY());
    }
}
