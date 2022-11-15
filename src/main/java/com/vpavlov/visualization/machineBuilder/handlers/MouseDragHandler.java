package com.vpavlov.visualization.machineBuilder.handlers;

import com.vpavlov.visualization.draw_model.MachineNode;
import com.vpavlov.visualization.machineBuilder.controller.MachineBuilderController;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;

public class MouseDragHandler implements EventHandler<MouseEvent> {

    private final MachineBuilderController controller;

    private MachineNode draggedNode;

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
        }else{
            if (checkCoordinates(point, draggedNode)) {
                draggedNode.setCoordinates(x, y);
            }
        }

        mouseEvent.consume();
    }

    private boolean checkCoordinates(Point2D point, MachineNode node){
        double width = controller.getCanvas().getWidth();
        double height = controller.getCanvas().getHeight();
        double radius = node.getRadius();
        if (point.getX() < (0+radius) || point.getX() > (width-radius) || point.getY()<(0+radius) || point.getY()>(height-radius)){
            return false;
        }
        return true;
    }
}
