package com.vpavlov.visualization.selection_window.controller;

import com.vpavlov.proprety.AppProperties;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

public class SelectionWindowController implements Initializable {

    private static final AppProperties properties = AppProperties.getInstance();

    @FXML
    private VBox vBox;

    @FXML
    private Button selectButton;

    private ToggleGroup toggleGroup;

    private Set<CheckBox> checkBoxes;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        selectButton.setOnAction(new EventHandler<>() {

            private void closeStage(ActionEvent e){
                Node source = (Node) e.getSource();
                Stage stage = (Stage) source.getScene().getWindow();
                stage.close();
            }
            @Override
            public void handle(ActionEvent actionEvent) {
                if (toggleGroup != null) {
                    if (getSelectedRadioButton() != null) {
                        closeStage(actionEvent);
                    }
                }else{
                    if (checkBoxes != null){
                        if (getCheckedVariants() != null){
                            closeStage(actionEvent);
                        }
                    }
                }
            }
        });
    }

    public void setRadioVariants(Collection<String> variants){
        toggleGroup = new ToggleGroup();
        for(String variant : variants){
            RadioButton rb = new RadioButton(variant);
            rb.setToggleGroup(toggleGroup);
            vBox.getChildren().add(rb);
        }
    }

    public String getSelectedRadioButton(){
        RadioButton selected = (RadioButton)toggleGroup.getSelectedToggle();
        return selected == null ? null : selected.getText();
    }

    public void setCheckVariants(Collection<String> variants){
        checkBoxes = new HashSet<>();
        for(String variant : variants){
            CheckBox checkBox = new CheckBox(variant);
            checkBox.setTextFill(Color.AQUA);
            vBox.getChildren().add(checkBox);
            checkBoxes.add(checkBox);
        }
    }

    public Set<String> getCheckedVariants(){
        Set<String> checkedVariants = new HashSet<>();
        for (CheckBox checkBox: checkBoxes){
            if (checkBox.isSelected()){
                checkedVariants.add(checkBox.getText());

            }
        }
        return checkedVariants.isEmpty()?null : checkedVariants;
    }


    public void poor(){
        vBox.getChildren().clear();
        checkBoxes = null;
        toggleGroup = null;
    }
}
