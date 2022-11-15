package com.vpavlov.visualization.selection_window.controller;

import com.vpavlov.proprety.AppProperties;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class SelectionWindowController implements Initializable {

    private static final AppProperties properties = AppProperties.getInstance();

    @FXML
    private VBox vBox;

    @FXML
    private Button selectButton;

    private ToggleGroup toggleGroup;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        selectButton.setOnAction(e->{
            RadioButton selected = (RadioButton)toggleGroup.getSelectedToggle();
            if (selected != null){
                Node source = (Node) e.getSource();
                Stage stage = (Stage) source.getScene().getWindow();
                stage.close();
            }
        });
        String[] symbols = properties.getProperty("alphabet").split(",");
        toggleGroup = new ToggleGroup();
        for(String symbol : symbols){
            RadioButton rb = new RadioButton(symbol);
            rb.setToggleGroup(toggleGroup);
            vBox.getChildren().add(rb);
        }
    }

    public String getSelectedButton(){
        RadioButton selected = (RadioButton)toggleGroup.getSelectedToggle();
        return selected == null ? null : selected.getText();
    }
}
