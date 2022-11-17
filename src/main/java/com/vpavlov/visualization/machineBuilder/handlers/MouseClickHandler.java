package com.vpavlov.visualization.machineBuilder.handlers;

import com.vpavlov.visualization.controller.PrimaryController;
import com.vpavlov.visualization.draw_model.Arrow;
import com.vpavlov.visualization.draw_model.LoopLine;
import com.vpavlov.visualization.draw_model.MachineNode;
import com.vpavlov.visualization.draw_model.TransitionLine;
import com.vpavlov.visualization.machineBuilder.MachineBuilderStage;
import com.vpavlov.visualization.machineBuilder.controller.MachineBuilderController;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;

public class MouseClickHandler implements EventHandler<MouseEvent> {
    private final MachineBuilderController controller;

    public MouseClickHandler(MachineBuilderController controller) {
        this.controller = controller;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        controller.getCanvas().requestFocus();
        if (!controller.isDrag) {
            double x = mouseEvent.getX();
            double y = mouseEvent.getY();
            Point2D point = new Point2D(x, y);
            switch (mouseEvent.getButton()) {
                case PRIMARY -> {
                    MachineNode node = controller.getClickedMachineNode(point);
                    if (node != null) {
                        controller.selectNode(node);
                    }else{
                        controller.createNode(point);
                    }
                }
            }
        }else{
            controller.isDrag = false;
        }
        mouseEvent.consume();
    }
}
