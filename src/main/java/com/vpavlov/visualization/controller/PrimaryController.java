package com.vpavlov.visualization.controller;

import com.vpavlov.proprety.AppProperties;
import com.vpavlov.visualization.draw_model.MachineNode;
import com.vpavlov.visualization.machineBuilder.handlers.KeyPressedHandler;
import com.vpavlov.visualization.handlers.TextInputController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.*;

public class PrimaryController implements Initializable {

    public static final double MINIMAL_NODE_DISTANCE = 20;

    private static final AppProperties properties = AppProperties.getInstance();

    @FXML
    private Pane canvas;

    @FXML
    public TextField input;

    private Map<String,MachineNode> machineNodes = new HashMap();


    public void init() {
        canvas.getChildren().addAll(machineNodes.values());
        input.textProperty().addListener(new TextInputController(this));

    }

    public Pane getCanvas(){
        return canvas;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        double paneWidth = Double.parseDouble(properties.getProperty("canvas-width"));
        double paneHeight = Double.parseDouble(properties.getProperty("canvas-height"));
        getCanvas().setPrefWidth(paneWidth);
        getCanvas().setPrefHeight(paneHeight);
        getCanvas().setMinWidth(paneWidth);
        getCanvas().setMinHeight(paneHeight);
    }
}
