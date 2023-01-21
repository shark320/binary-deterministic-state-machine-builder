package com.vpavlov.visualization.help_window.controller;

import com.vpavlov.App;
import com.vpavlov.visualization.tools.custom_alert.CustomAlert;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Help window controller
 *
 * @author vpalvov
 */
public class HelpWindowController implements Initializable {

    /**
     * Close button
     */
    @FXML
    private Button closeButton;

    /**
     * Help text
     */
    @FXML
    private TextArea helpText;

    /**
     * Set help text from file
     */
    public void setHelpText(){
        System.out.println("Setting help text");
        String text = getHelpText();
        if (text != null){
            helpText.setText(text);
        }

    }

    /**
     * Close the stage
     * @param e pressed button event
     */
    private void closeStage(ActionEvent e) {
        Node source = (Node) e.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    /**
     * Get help text from files
     * @return help text
     */
    private String getHelpText(){
        System.out.println("Reading help text");
        StringBuilder helpText = new StringBuilder();
        try {
            URL fileURL = App.class.getResource(App.getProperties().getProperty("help-text"));
            if (fileURL==null){
                CustomAlert.showErrorAlert("Help file was not found!");
                return null;
            }
            File file = new File(Objects.requireNonNull(fileURL).toURI());
            if (file.exists()){
                BufferedReader reader = new BufferedReader(new FileReader(file));
                reader.lines().forEach(line -> helpText.append(line).append("\n"));
                return helpText.toString();
            }else{
                CustomAlert.showErrorAlert("Help text was not found!");
            }
        } catch (URISyntaxException | FileNotFoundException e) {
            CustomAlert.showErrorAlert("An error occurred while reading help file!");
        }
        return null;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setHelpText();
        closeButton.setOnAction(this::closeStage);
    }
}
