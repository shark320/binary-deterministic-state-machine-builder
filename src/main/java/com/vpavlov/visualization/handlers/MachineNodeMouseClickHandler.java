package com.vpavlov.visualization.handlers;

import com.vpavlov.App;
import com.vpavlov.visualization.draw_model.MachineNode;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class MachineNodeMouseClickHandler implements EventHandler<MouseEvent> {

    @Override
    public void handle(MouseEvent mouseEvent) {
        MachineNode node = (MachineNode) mouseEvent.getSource();
        switch (mouseEvent.getButton()){
            case PRIMARY -> node.setAsStartNode();
        }
        App.showInfoAlert(mouseEvent.getButton().name());
        mouseEvent.consume();
    }
}
