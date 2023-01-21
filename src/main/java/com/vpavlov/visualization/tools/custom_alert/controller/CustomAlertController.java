package com.vpavlov.visualization.tools.custom_alert.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

/**
 * Custom alert window controller
 *
 * @author vpavlov
 */
public class CustomAlertController {

    /**
     * Alert message
     */
    @FXML
    TextArea message;

    /**
     * OK button
     */
    @FXML
    Button okButton;

    /**
     * Cancel button
     */
    @FXML
    Button cancelButton;

    /**
     * If OK button is clicked flag
     */
    private boolean isOkButton = false;

    /**
     * Alert message setter
     *
     * @param message message to set
     */
    public void setMessage(String message) {
        this.message.setWrapText(true);
        this.message.setText(message);
    }

    /**
     * Close alert window
     *
     * @param e button clicked event
     */
    private void closeStage(ActionEvent e) {
        Node source = (Node) e.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    /**
     * Check if OK button is clicked
     *
     * @return true if OK button is clicked, else false
     */
    public boolean isOkButton() {
        return isOkButton;
    }

    /**
     * Clear data
     */
    public void poor() {
        isOkButton = false;
    }

    /**
     * Init alert as error message
     */
    public void initInfoErrorAlert() {
        okButton.setOnAction(this::closeStage);
    }

    /**
     * Init alert as confirmation message
     */
    public void initConfirmAlert() {
        okButton.setOnAction(e -> {
            isOkButton = true;
            closeStage(e);
        });

        cancelButton.setOnAction(this::closeStage);
    }

}
