package com.vpavlov.visualization.handlers;

import com.vpavlov.visualization.controller.PrimaryController;
import javafx.scene.control.TextFormatter;

import java.util.function.UnaryOperator;

/**
 * Text input formatter for machine alphabet and special characters
 *
 * @author vpavlov
 */
public class InputFormatter extends TextFormatter<Object> {

    private static class CustomOperator implements UnaryOperator<Change> {

        @Override
        public Change apply(Change change) {
            if (change.isDeleted()) {
                return null;
            }
            String newSymbol = change.getControlNewText();
            newSymbol = newSymbol.substring(newSymbol.length() - 1);
            System.out.println("new symbol: " + newSymbol);
            if (controller.isMachineSet()) {
                switch (newSymbol) {
                    case "u" -> {
                        controller.undoSymbol();
                        change.setText("");
                        return change;
                    }
                    case "r" -> {
                        controller.resetMachine();
                        return change;
                    }
                    case "q" -> {
                        controller.quit();
                        return change;
                    }
                    default -> {
                        if (controller.useSymbol(newSymbol)) {
                            return change;
                        } else {
                            return null;
                        }
                    }
                }
            } else {
                return null;
            }
        }
    }

    /**
     * Primary window controller
     */
    static PrimaryController controller;

    /**
     * Constructor
     *
     * @param controller controller of window where machine input element is
     */
    public InputFormatter(PrimaryController controller) {
        super(new CustomOperator());
        InputFormatter.controller = controller;
    }
}
