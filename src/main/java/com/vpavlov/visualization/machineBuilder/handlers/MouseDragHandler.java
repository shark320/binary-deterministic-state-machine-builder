package com.vpavlov.visualization.machineBuilder.handlers;

import com.vpavlov.visualization.draw_model.MachineNode;
import com.vpavlov.visualization.machineBuilder.controller.MachineBuilderController;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;

/**
 * Mouse drag event handler for machine builder
 *
 * @author vpavlov
 */
public class MouseDragHandler implements EventHandler<MouseEvent> {

    /**
     * Machine builder window controller
     */
    private final MachineBuilderController controller;

    /**
     * Dragged node
     */
    private MachineNode draggedNode;

    /**
     * Constructor
     *
     * @param controller machine builder window controller
     */
    public MouseDragHandler(MachineBuilderController controller) {
        this.controller = controller;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        double x = mouseEvent.getX();
        double y = mouseEvent.getY();
        Point2D point = new Point2D(x, y);
        if (!controller.isDrag) {

            draggedNode = controller.getClickedMachineNode(point);
            if (draggedNode != null) {
                controller.isDrag = true;
                if (checkCoordinates(point, draggedNode)) {
                    draggedNode.setCoordinates(x, y);
                }
            }
        } else {
            if (checkCoordinates(point, draggedNode)) {
                draggedNode.setCoordinates(x, y);
            }
        }

        mouseEvent.consume();
    }

    /**
     * Check if the node can be dragged to given coordinates
     *
     * @param point coordinates to drag
     * @param node  node to drag
     * @return true if the node can be dragged, else false
     */
    private boolean checkCoordinates(Point2D point, MachineNode node) {
        double width = controller.getCanvasPane().getWidth();
        double height = controller.getCanvasPane().getHeight();
        double radius = node.getRadius();
        return !(point.getX() < (0 + radius)) && !(point.getX() > (width - radius)) && !(point.getY() < (0 + radius)) && !(point.getY() > (height - radius));
    }
}
