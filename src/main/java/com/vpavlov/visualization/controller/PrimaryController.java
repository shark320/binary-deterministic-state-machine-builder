package com.vpavlov.visualization.controller;

import com.vpavlov.proprety.AppProperties;
import com.vpavlov.services.machine.MachineService;
import com.vpavlov.visualization.handlers.TextInputController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.*;

public class PrimaryController implements Initializable {

    private static final AppProperties properties = AppProperties.getInstance();

    @FXML
    private Pane canvas;

    @FXML
    public TextField input;

    private MachineService machineService;


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
    }

    public void setMachineService(MachineService machineService){
        this.machineService = machineService;
        canvas.getChildren().add(machineService.getMachineGraph());
    }
}
