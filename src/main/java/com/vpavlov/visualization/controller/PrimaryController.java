package com.vpavlov.visualization.controller;

import com.vpavlov.App;
import com.vpavlov.visualization.handlers.MachineNodeMouseClickHandler;
import com.vpavlov.visualization.handlers.MouseClickHandler;
import com.vpavlov.visualization.handlers.TextInputController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class PrimaryController   {

    @FXML
    public Pane canvas;

    @FXML
    public TextField input;

    public Group machineNodes = new Group();

    public static MachineNodeMouseClickHandler machineNodeMouseClickHandler = new MachineNodeMouseClickHandler();

    public void init(){
        canvas.getChildren().add(machineNodes);
        canvas.setOnMouseClicked(new MouseClickHandler(this));
        input.textProperty().addListener(new TextInputController(this));

    }


}
