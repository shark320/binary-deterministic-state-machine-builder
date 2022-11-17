package com.vpavlov.visualization.machineBuilder.controller;

import com.vpavlov.proprety.AppProperties;
import com.vpavlov.services.machine.MachineService;
import com.vpavlov.visualization.draw_model.MachineNode;
import com.vpavlov.visualization.draw_model.TransitionLine;
import com.vpavlov.visualization.machineBuilder.exceptions.StartNodeSetException;
import com.vpavlov.visualization.machineBuilder.exceptions.TransitionLinesExistsException;
import com.vpavlov.visualization.machineBuilder.handlers.KeyPressedHandler;
import com.vpavlov.visualization.machineBuilder.handlers.MouseClickHandler;
import com.vpavlov.visualization.machineBuilder.handlers.MouseDragHandler;
import com.vpavlov.visualization.selection_window.SelectionWindow;
import com.vpavlov.visualization.tools.custom_alert.CustomAlert;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

public class MachineBuilderController implements Initializable {

    public static final double MINIMAL_NODE_DISTANCE = 20;

    private static final AppProperties properties = AppProperties.getInstance();

    @FXML
    private Pane canvas;

    @FXML
    private Button okButton;

    @FXML
    private Button cancelButton;

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

        okButton.setOnAction(e -> {
            StringBuilder message = new StringBuilder();
            if (!machineService.isCompleteMachineStates()) {
                message.append("Machine states are not complete.\n");
            }
            if (!machineService.isStartStateSet()) {
                message.append("Start state is not set.\n");
            }
            if (!machineService.isFinalStatesSet()) {
                message.append("Final states are not set.\n");
            }
            if (message.isEmpty()) {
                closeStage(e);
            } else {
                CustomAlert.showInfoAlert(message.toString());
            }

        });

        cancelButton.setOnAction(e -> {
            machineService = null;
            closeStage(e);
        });

    }

    private void closeStage(ActionEvent e) {
        Node source = (Node) e.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    public MachineNode getClickedMachineNode(Point2D point) {
        for (MachineNode node : machineService.getNodes().values()) {
            if (node.contains(point)) {
                return node;
            }
        }

        return null;
    }

    public void createMachineService() {
        machineService = new MachineService();
        canvas.getChildren().add(machineService.getMachineGraph());
    }

    public MachineService getMachineService() {
        return machineService;
    }

    public Pane getCanvas() {
        return canvas;
    }

    public void selectNode(MachineNode node) {
        node.select();
        switch (selectedNodesCount) {
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

    public void removeTransition() {
        TransitionLine transitionLine = machineService.getTransitionLine(firstSelectedNode, secondSelectedNode);
        String firstNodeTitle = firstSelectedNode.getTitle();
        String secondNodeTitle = secondSelectedNode.getTitle();
        if (transitionLine != null) {
            Set<String> titles = transitionLine.getTitles();
            Set<String> toRemove = SelectionWindow.showCheckSelectionAndWait(titles);
            if (toRemove != null) {
                if (showTransitionsRemoveConfirmMessage(firstNodeTitle, secondNodeTitle, toRemove)) {
                    machineService.removeTransitions(firstSelectedNode, secondSelectedNode, toRemove);
                    unselectAll();
                }
            }
        } else {
            CustomAlert.showInfoAlert("There is no transitions to delete.");
        }
    }

    private boolean showTransitionsRemoveConfirmMessage(String firstSelectedNode, String secondSelectedNode, Set<String> toRemove) {
        String message = String.format("Are you sure you want to remove transition: \n<%s> --%s--> <%s> ?", firstSelectedNode, toRemove, secondSelectedNode);
        return CustomAlert.showConfirmAlert(message);
    }


    public void createTransition() {
        Set<String> symbols = SelectionWindow.showCheckSelectionAndWait(machineService.getAlphabet().getSymbols());
        if (symbols != null) {
            try {
                machineService.addTransition(symbols, firstSelectedNode, secondSelectedNode);
            } catch (TransitionLinesExistsException e) {
                if (showTransitionsReplaceConfirmMessage(e.getTransitionLines())) {
                    machineService.addAndReplaceTransitions(symbols, firstSelectedNode, secondSelectedNode);
                }
            }
            unselectAll();
        }
    }

    private boolean showTransitionsReplaceConfirmMessage(Map<String, TransitionLine> transitionLines) {
        StringBuilder message = new StringBuilder();
        message.append("Are you sure you want to replace transitions:\n");
        for (String title : transitionLines.keySet()) {
            TransitionLine transitionLine = transitionLines.get(title);
            message.append(String.format("<%s> --[%s]--> <%s>\n", transitionLine.getStart().getTitle(), title, transitionLine.getEnd().getTitle()));
        }
        message.append("?");
        return CustomAlert.showConfirmAlert(message.toString());
    }

    public void removeNode() {
        switch (selectedNodesCount) {
            case 0 -> {
                CustomAlert.showInfoAlert("Please select a node to remove.");
            }
            case 1 -> {
                if (CustomAlert.showConfirmAlert(String.format("Are you sure you want to remove node <%s>?", firstSelectedNode.getTitle()))) {
                    machineService.removeNode(firstSelectedNode);
                    unselectAll();
                }
            }
            case 2 -> {
                CustomAlert.showInfoAlert("Please select only one node to remove.");
            }
        }
    }

    public void createNode(Point2D point) {
        unselectAll();
        if (checkDistance(point)) {
            machineService.addMachineNode(point);
        }
    }

    public void unselectAll() {
        if (selectedNodesCount != 0) {
            firstSelectedNode.unselect();
            secondSelectedNode.unselect();
            firstSelectedNode = null;
            secondSelectedNode = null;
            selectedNodesCount = 0;
        }
    }

    private boolean checkDistance(Point2D point) {
        double distance;
        for (MachineNode node : machineService.getNodes().values()) {
            distance = point.distance(node.getPosition());
            if (distance < (node.getRadiusProperty().get() * 2 + MINIMAL_NODE_DISTANCE)) {
                return false;
            }
        }

        return true;
    }

    public void startNodeSetting() {
        switch (selectedNodesCount) {
            case 0 -> CustomAlert.showInfoAlert("Select one node to set as start node.");
            case 1 -> {
                if (firstSelectedNode.isFinalNode()) {
                    CustomAlert.showInfoAlert("This node is set as final node!");
                    unselectAll();
                } else {
                    if (!firstSelectedNode.isStartNode()) {
                        try {
                            machineService.setAsStartNode(firstSelectedNode);
                            unselectAll();
                        } catch (StartNodeSetException e) {
                            if (showStartNodeOverrideConfirm(e.getStartNode())) {
                                machineService.overrideStartNode(firstSelectedNode);
                                unselectAll();
                            }
                        }
                    } else {
                        if (showRemoveStartNodeConfirm(firstSelectedNode)) {
                            machineService.unsetAsStartNode(firstSelectedNode);
                            unselectAll();
                        }
                    }
                }
            }
            case 2 -> CustomAlert.showInfoAlert("Select only one node to set as start node.");
        }
    }

    public void finalNodeSetting() {
        switch (selectedNodesCount) {
            case 0 -> CustomAlert.showInfoAlert("Select one node to set as final node.");
            case 1 -> {
                if (firstSelectedNode.isStartNode()) {
                    CustomAlert.showInfoAlert("This node is already set as start node!");
                    unselectAll();
                } else {
                    if (!firstSelectedNode.isFinalNode()) {
                        machineService.setAsFinalNode(firstSelectedNode);
                        unselectAll();
                    } else {
                        if (showRemoveFinalNodeConfirm(firstSelectedNode)) {
                            machineService.unsetAsFinalNode(firstSelectedNode);
                            unselectAll();
                        }
                    }
                }
            }
            case 2 -> CustomAlert.showInfoAlert("Select only one node to set as final node.");
        }
    }

    private boolean showRemoveFinalNodeConfirm(MachineNode node) {
        String message = String.format("Are you sure you want to unset node <%s> as final node?", node.getTitle());
        return CustomAlert.showConfirmAlert(message);
    }

    private boolean showRemoveStartNodeConfirm(MachineNode startNode) {
        String message = String.format("Are you sure you want to unset node <%s> as start node?", startNode.getTitle());
        return CustomAlert.showConfirmAlert(message);
    }

    private boolean showStartNodeOverrideConfirm(MachineNode startNode) {
        String message = String.format("Are you sure you want to override start node <%s> ?", startNode.getTitle());
        return CustomAlert.showConfirmAlert(message);
    }

    public void closeRequest() {
        machineService = null;
    }
}
