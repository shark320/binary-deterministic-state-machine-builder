package com.vpavlov.visualization.tools.custom_alert.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class CustomAlertController {

    @FXML
    TextArea message;

    @FXML
    Button okButton;

    @FXML
    Button cancelButton;

    private boolean isOkButton = false;

    public void setMessage(String message) {
        this.message.setWrapText(true);
        this.message.setText(message);
    }

    private void closeStage(ActionEvent e){
        Node source = (Node) e.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    public boolean isOkButton() {
        return isOkButton;
    }

    public void poor(){
        isOkButton = false;
    }

    public void initInfoAlert(){
        okButton.setOnAction(this::closeStage);
    }

    public void initConfirmAlert(){
        okButton.setOnAction(e->{
            isOkButton = true;
            closeStage(e);
        });

        cancelButton.setOnAction(this::closeStage);
    }

}
