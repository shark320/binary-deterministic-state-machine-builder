package com.vpavlov.visualization.handlers;

import com.vpavlov.App;
import com.vpavlov.machine.Alphabet;
import com.vpavlov.machine.Machine;
import com.vpavlov.visualization.controller.PrimaryController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

public class TextInputController implements ChangeListener<String> {

    private final Alphabet alphabet;

    private final TextField input;

    private final Machine machine;

    private PrimaryController controller;

    public TextInputController(PrimaryController controller) {
        //bind machine-related objects
        this.alphabet = App.getAlphabet();
        this.machine = App.getMachine();
        this.input = controller.input;
    }

    private boolean isLoop = false;


    @Override
    public void changed(ObservableValue<? extends String> observableValue, String s, String s1) {
        String newSymbol;
        if (s == null || s1 == null || isLoop)
            return;
        //App.showInfoAlert(String.format("s: %s, s1: %s", s, s1));
        //App.showInfoAlert(alphabet.getSymbols().toString());
        isLoop = true;
        //TODO propagate event
        //prevent deleting
        if (s1.length() < s.length()) {
            input.setText(s);
        }else {
            newSymbol = s1.substring(s.length());
            //App.showInfoAlert(String.format("s: %s, s1: %s, new: %s", s, s1, newSymbol));
            if (checkSymbol(newSymbol)) {
                input.setText(s1);
                //makeTransition(newSymbol);
            } else {
                input.setText(s);
            }
        }
        isLoop = false;
    }

    private boolean checkSymbol(String s){
        for (String symbol : alphabet.getSymbols()){
            if (s.equals(symbol)){
                return true;
            }
        }

        return false;
    }


}
