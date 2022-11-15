package com.vpavlov.visualization.machineBuilder.controller;

import com.vpavlov.proprety.AppProperties;
import com.vpavlov.services.machine.MachineService;
import com.vpavlov.visualization.draw_model.MachineNode;
import com.vpavlov.visualization.machineBuilder.handlers.KeyPressedHandler;
import com.vpavlov.visualization.machineBuilder.handlers.MouseClickHandler;
import com.vpavlov.visualization.machineBuilder.handlers.MouseDragHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class MachineBuilderController implements Initializable {

    private static final AppProperties properties = AppProperties.getInstance();
    @FXML
    private Pane canvas;

    private Map<String,MachineNode> machineNodes;

    private MachineService machineService;


    private MachineNode firstSelectedNode = null;

    private MachineNode secondSelectedNode = null;

    private int selectedNodesCount = 0;

    public boolean isDrag;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        double paneWidth = Double.parseDouble(properties.getProperty("canvas-width"));
        double paneHeight = Double.parseDouble(properties.getProperty("canvas-height"));
        canvas.setPrefWidth(paneWidth);
        canvas.setPrefHeight(paneHeight);
        canvas.setOnMouseClicked(new MouseClickHandler(this));
        canvas.setOnMouseDragged(new MouseDragHandler(this));
        canvas.setOnKeyPressed(new KeyPressedHandler(this));
    }

    public MachineNode getClickedMachineNode(Point2D point){
        for (MachineNode node : machineNodes.values()){
            if (node.contains(point)){
                return node;
            }
        }

        return null;
    }

    public void createMachineService(){
        machineService = new MachineService();
        machineNodes = machineService.getNodes();
        canvas.getChildren().add(machineService.getMachineGraph());
    }

    public MachineService getMachineService(){
        return machineService;
    }

    public Map<String,MachineNode> getMachineNodes(){
        return machineNodes;
    }

    public Pane getCanvas() {
        return canvas;
    }

    public void selectNode(MachineNode node){
        node.select();
        switch (selectedNodesCount){
            case 0 -> {
                firstSelectedNode = node;
                secondSelectedNode = node;
                ++selectedNodesCount;
            }

            case 1 -> {
                secondSelectedNode = node;
                ++selectedNodesCount;
            }

            case 2 -> {
                firstSelectedNode.unselect();
                secondSelectedNode.unselect();
                firstSelectedNode = node;
                secondSelectedNode = node;
                selectedNodesCount = 1;
            }
        }
    }

    public void createTransition(){
        machineService.addTransition("1", firstSelectedNode, secondSelectedNode);
    }

    public void createNode(Point2D point) {
        machineService.addMachineNode(point);
    }
}
