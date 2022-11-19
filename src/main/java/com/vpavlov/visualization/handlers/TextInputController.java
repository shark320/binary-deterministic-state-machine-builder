package com.vpavlov.visualization.handlers;

import com.vpavlov.App;
import com.vpavlov.machine.Alphabet;
import com.vpavlov.machine.Machine;
import com.vpavlov.visualization.controller.PrimaryController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

public class TextInputController implements ChangeListener<String> {


    private final TextField input;


    private PrimaryController controller;

    public TextInputController(PrimaryController controller) {
        //bind machine-related objects
        this.controller = controller;
        this.input = controller.getInput();
    }

    private boolean isLoop = false;


    @Override
    public void changed(ObservableValue<? extends String> observableValue, String s, String s1) {
        String newSymbol;
        if (s == null || s1 == null || isLoop)
            return;
        isLoop = true;
        //prevent deleting
        if (s1.length() < s.length()) {
            input.setText(s);
        }else {
            newSymbol = s1.substring(s.length());
            if (controller.isMachineSet()) {
                switch (newSymbol) {
                    case "u" -> {
                        controller.undoSymbol();
                        input.setText(s1);
                    }
                    case "r" ->{
                        controller.resetMachine();
                        input.setText(s1);
                    }
                    case "q" ->{
                        controller.quit();
                        input.setText(s1);
                    }
                    default -> {
                        if (controller.useSymbol(newSymbol)) {
                            input.setText(s1);
                        } else {
                            input.setText(s);
                        }
                    }
                }
            }else{
                input.setText(s);
            }
        }
        isLoop = false;
    }


}
