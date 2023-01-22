package com.vpavlov.visualization.text_window.controller;

import com.vpavlov.App;
import com.vpavlov.visualization.tools.custom_alert.CustomAlert;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
public class TextWindowController implements Initializable {

    @FXML
    private Label title;

    /**
     * Close button
     */
    @FXML
    private Button closeButton;

    /**
     * Help text
     */
    @FXML
    private TextArea text;

    /**
     * Initialize help info window
     */
    public void initHelp() {
        title.setText("HELP INFORMATION");
        setHelpText();
    }

    /**
     * Initialize about info window
     */
    public void initAbout() {
        title.setText("ABOUT INFORMATION");
        setAboutText();
    }


    /**
     * Set help text from file
     */
    public void setHelpText() {
        URL fileURL = App.class.getResource(App.getProperties().getProperty("help-text"));
        setText(fileURL);
    }

    /**
     * Set about text from file
     */
    public void setAboutText() {
        URL fileURL = App.class.getResource(App.getProperties().getProperty("about-text"));
        setText(fileURL);
    }

    /**
     * Set text from file
     *
     * @param fileURL file URL to get info from
     */
    public void setText(URL fileURL) {
        if (fileURL == null) {
            CustomAlert.showErrorAlert("Help file was not found!");
            return;
        }
        String text = null;
        try {
            text = getText(new File(Objects.requireNonNull(fileURL).toURI()));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        if (text != null) {
            this.text.setText(text);
        }
    }

    /**
     * Close the stage
     *
     * @param e pressed button event
     */
    private void closeStage(ActionEvent e) {
        Node source = (Node) e.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    /**
     * Get text from the file
     *
     * @param file file to get info from
     * @return string from file or null if file does not exist
     */
    private String getText(File file) {
        System.out.println("Reading help text");
        StringBuilder helpText = new StringBuilder();
        try {
            if (file.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                reader.lines().forEach(line -> helpText.append(line).append("\n"));
                return helpText.toString();
            } else {
                CustomAlert.showErrorAlert("Help text was not found!");
            }
        } catch (FileNotFoundException e) {
            CustomAlert.showErrorAlert("An error occurred while reading help file!");
        }
        return null;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        closeButton.setOnAction(this::closeStage);
    }
}
