package com.vpavlov.visualization.controller;

import com.vpavlov.proprety.AppProperties;
import com.vpavlov.services.machine.MachineService;
import com.vpavlov.services.machine.MachineServiceFileManager;
import com.vpavlov.visualization.handlers.TextInputController;
import com.vpavlov.visualization.machineBuilder.MachineBuilderStage;
import com.vpavlov.visualization.tools.custom_alert.CustomAlert;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class PrimaryController implements Initializable {

    private static final AppProperties properties = AppProperties.getInstance();

    @FXML
    private Pane canvas;

    @FXML
    private TextField input;

    @FXML
    private MenuItem saveFile;

    @FXML
    private MenuItem openFile;

    @FXML
    private MenuItem editMachine;

    @FXML
    ListView<String> listView;

    private MachineService machineService;

    private FileChooser fileChooser  = new FileChooser();

    private  Stage stage;

    public Pane getCanvas(){
        return canvas;
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        double paneWidth = Double.parseDouble(properties.getProperty("canvas-width"));
        double paneHeight = Double.parseDouble(properties.getProperty("canvas-height"));
        input.textProperty().addListener(new TextInputController(this));
        getCanvas().setPrefWidth(paneWidth);
        getCanvas().setPrefHeight(paneHeight);
        getCanvas().setMinWidth(paneWidth);
        getCanvas().setMinHeight(paneHeight);

        saveFile.setDisable(true);
        editMachine.setDisable(true);

        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Machine files (*.mcn)","*.mcn");
        fileChooser.getExtensionFilters().add(extensionFilter);
        try {
            fileChooser.setInitialDirectory((new File(".")).getCanonicalFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        saveFile.setOnAction(e->{
            if (machineService!= null){
                File file = fileChooser.showSaveDialog(stage);
                try {
                    MachineServiceFileManager.saveToFile(machineService, file);
                } catch (IOException ex) {
                    CustomAlert.showInfoAlert("An error occurred while writing to file.");
                }
            }else{
                CustomAlert.showInfoAlert("There is no machine to save.");
            }
        });

        openFile.setOnAction(e->{
           File file = fileChooser.showOpenDialog(stage);
           if (machineService!= null){
               canvas.getChildren().remove(machineService.getMachineGraph());
           }
            try {
                MachineService machineService = MachineServiceFileManager.createFromFile(file);
                setMachineService(machineService);
            } catch (IOException ex) {
                CustomAlert.showInfoAlert("An error occurred while opening the file.");
            }
        });

        editMachine.setOnAction(e->{
            if (machineService!=null){
                MachineService newMachineService = MachineBuilderStage.openAndWait(new MachineService(machineService));
                if (newMachineService != null) {
                    canvas.getChildren().remove(machineService.getMachineGraph());
                    setMachineService(newMachineService);
                }
            }
        });

    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setMachineService(MachineService machineService){
        saveFile.setDisable(false);
        editMachine.setDisable(false);
        this.machineService = machineService;
        canvas.getChildren().add(machineService.getMachineGraph());
    }

    public TextField getInput() {
        return input;
    }
}
