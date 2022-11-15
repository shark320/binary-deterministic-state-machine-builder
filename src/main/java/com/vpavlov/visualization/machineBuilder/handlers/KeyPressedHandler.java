package com.vpavlov.visualization.machineBuilder.handlers;

import com.vpavlov.visualization.machineBuilder.controller.MachineBuilderController;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

public class KeyPressedHandler implements EventHandler<KeyEvent> {

    private final MachineBuilderController controller;

    public KeyPressedHandler(MachineBuilderController controller) {
        this.controller = controller;
    }

    @Override
    public void handle(KeyEvent keyEvent) {
        if (keyEvent.isControlDown()){
            switch (keyEvent.getCode()){
                case C ->{
                    controller.createTransition();
                }
            }
        }
    }
}
