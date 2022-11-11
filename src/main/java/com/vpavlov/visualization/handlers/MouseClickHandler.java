package com.vpavlov.visualization.handlers;

import com.vpavlov.visualization.controller.PrimaryController;
import com.vpavlov.visualization.draw_model.Arrow;
import com.vpavlov.visualization.draw_model.MachineNode;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class MouseClickHandler implements EventHandler<MouseEvent> {

    private final PrimaryController controller;

    public MouseClickHandler(PrimaryController controller) {
        this.controller = controller;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        double x = mouseEvent.getX();
        double y = mouseEvent.getY();
        switch (mouseEvent.getButton()) {
            //Circle circle = new Circle(x, y,10, Color.AQUA);
            case PRIMARY -> {
                MachineNode md = new MachineNode(x, y, "A");
                controller.machineNodes.getChildren().add(md);
            }
        }
    }
}
