package com.vpavlov.visualization.machineBuilder.handlers;

import com.vpavlov.visualization.machineBuilder.controller.MachineBuilderController;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * Key pressed controller for machine builder
 *
 * @author vpavlov
 */
public class KeyPressedHandler implements EventHandler<KeyEvent> {

    /**
     * Machine builder window controller
     */
    private final MachineBuilderController controller;

    /**
     * Constructor
     *
     * @param controller machine builder window controller
     */
    public KeyPressedHandler(MachineBuilderController controller) {
        this.controller = controller;
    }

    @Override
    public void handle(KeyEvent keyEvent) {
        if (keyEvent.isControlDown()) {
            switch (keyEvent.getCode()) {
                case C -> controller.createTransition();

                case R -> controller.removeTransition();

                case S -> controller.startNodeSetting();

                case D -> controller.removeNode();

                case F -> controller.finalNodeSetting();
            }
        } else {
            if (keyEvent.getCode() == KeyCode.ESCAPE) {
                controller.unselectAll();
            }
        }
    }
}
