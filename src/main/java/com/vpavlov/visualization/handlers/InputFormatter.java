package com.vpavlov.visualization.handlers;

import com.vpavlov.visualization.controller.PrimaryController;
import javafx.scene.control.TextFormatter;

import java.util.function.UnaryOperator;

public class InputFormatter extends TextFormatter<Object> {

    private static class CustomOperator implements UnaryOperator<Change> {

        @Override
        public Change apply(Change change) {
            if (change.isDeleted()){
                return null;
            }
            String newSymbol = change.getControlNewText().replace(change.getControlText(), "");
            if (controller.isMachineSet()) {
                switch (newSymbol) {
                    case "u" -> {
                        controller.undoSymbol();
                        return change;
                    }
                    case "r" ->{
                        controller.resetMachine();
                        return change;
                    }
                    case "q" ->{
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
            }else{
                return null;
            }
        }
    }

    static PrimaryController controller;

    public InputFormatter(PrimaryController controller) {
        super(new CustomOperator());
        InputFormatter.controller = controller;
    }
}
