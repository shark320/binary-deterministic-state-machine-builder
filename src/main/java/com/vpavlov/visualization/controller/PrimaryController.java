package com.vpavlov.visualization.controller;

import com.vpavlov.proprety.AppProperties;
import com.vpavlov.services.machine.MachineService;
import com.vpavlov.services.machine.MachineServiceFileManager;
import com.vpavlov.services.machine.MachineTransition;
import com.vpavlov.visualization.handlers.InputFormatter;
import com.vpavlov.visualization.handlers.TextInputController;
import com.vpavlov.visualization.machineBuilder.MachineBuilderStage;
import com.vpavlov.visualization.tools.custom_alert.CustomAlert;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.ParsePosition;
import java.util.*;

public class PrimaryController implements Initializable {

    private static final AppProperties properties = AppProperties.getInstance();


    private Pane canvas;

    @FXML
    private BorderPane rootPane;

    @FXML
    private Hyperlink openLink;

    @FXML
    private Hyperlink createLink;

    @FXML
    private TextField input;

    @FXML
    private MenuItem saveFile;

    @FXML
    private MenuItem openFile;

    @FXML
    private MenuItem editMachine;

    @FXML
    private MenuItem createNew;

    @FXML
    private VBox startVbox;

    @FXML
    ListView<MachineTransition> listView;

    private MachineService machineService;

    private final FileChooser fileChooser = new FileChooser();

    private Stage stage;

    public boolean clear = false;


    //private TextInputController textInputController;

    private InputFormatter inputFormatter;

    public Pane getCanvas() {
        return canvas;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        double paneWidth = Double.parseDouble(properties.getProperty("canvas-width"));
        double paneHeight = Double.parseDouble(properties.getProperty("canvas-height"));
        startVbox.setPrefSize(paneWidth, paneHeight);
        this.inputFormatter = new InputFormatter(this);
        //this.textInputController = new TextInputController(this);
        //input.textProperty().addListener(textInputController);
        input.setTextFormatter(inputFormatter);


        saveFile.setDisable(true);
        editMachine.setDisable(true);

        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Machine files (*.mcn)", "*.mcn");
        fileChooser.getExtensionFilters().add(extensionFilter);
        try {
            fileChooser.setInitialDirectory((new File(".")).getCanonicalFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        saveFile.setOnAction(e -> {
            if (machineService != null) {
                File file = fileChooser.showSaveDialog(stage);
                if (file == null) {
                    return;
                }
                try {
                    MachineServiceFileManager.saveToFile(machineService, file);
                } catch (IOException ex) {
                    CustomAlert.showInfoAlert("An error occurred while writing to file.");
                }
            } else {
                CustomAlert.showInfoAlert("There is no machine to save.");
            }
        });

        openFile.setOnAction(e -> openFromFile());

        openLink.setOnAction(e -> {
            openFromFile();
            openLink.setVisited(false);
        });


        editMachine.setOnAction(e -> {
            if (machineService != null) {
                MachineService newMachineService = MachineBuilderStage.openAndWait(new MachineService(machineService));
                if (newMachineService != null) {
                    canvas.getChildren().remove(machineService.getMachineGraph());
                    setMachineService(newMachineService);
                }
            }
        });

        createLink.setOnAction(e -> {
            createNewMachine();
            createLink.setVisited(false);
        });

        createNew.setOnAction(e -> createNewMachine());

    }

    private void openFromFile() {
        File file = fileChooser.showOpenDialog(stage);
        if (file == null) {
            return;
        }
        try {
            MachineService machineService = MachineServiceFileManager.createFromFile(file);
            if (machineService != null) {
                setMachineService(machineService);
            } else {
                CustomAlert.showErrorAlert("An error occurred while reading machine from the file.");
            }
        } catch (IOException ex) {
            CustomAlert.showErrorAlert("An error occurred while opening the file.");
        }
    }

    private void createNewMachine() {
        MachineService newMachineService = MachineBuilderStage.openAndWait();
        if (newMachineService != null) {
            setMachineService(newMachineService);
        }
    }

    private void setCanvas() {
        if (canvas == null) {
            canvas = new Pane();
            canvas.getStyleClass().add("canvas");
            double paneWidth = Double.parseDouble(properties.getProperty("canvas-width"));
            double paneHeight = Double.parseDouble(properties.getProperty("canvas-height"));
            getCanvas().setPrefWidth(paneWidth);
            getCanvas().setPrefHeight(paneHeight);
            getCanvas().setMinWidth(paneWidth);
            getCanvas().setMinHeight(paneHeight);
        }
        rootPane.setCenter(canvas);
    }

    public void undoSymbol() {
        try {
            if (!machineService.undo()) {
                CustomAlert.showErrorAlert("An error occurred while undoing last transition.");
            }
        } catch (IllegalStateException e) {
            CustomAlert.showInfoAlert(e.getMessage());
        }
    }

    public void resetMachine() {
        try {
            if (machineService.reset()) {
                CustomAlert.showInfoAlert("Input string is accepted.");
            } else {
                CustomAlert.showInfoAlert("Input string is not accepted.");
            }
            clearInput();
        } catch (IllegalStateException e) {
            CustomAlert.showInfoAlert(e.getMessage());
        }
    }

    public void quit() {
        canvas.getChildren().removeAll(machineService.getMachineGraph());
        rootPane.setCenter(startVbox);
        listView.setItems(null);
        openLink.setDisable(false);
        saveFile.setDisable(true);
        editMachine.setDisable(true);
        createNew.setDisable(false);
        clearInput();
        input.setDisable(true);
        if (machineService.quit()) {
            CustomAlert.showInfoAlert("Input string is accepted.");
        } else {
            CustomAlert.showInfoAlert("Input string is not accepted.");
        }
        machineService = null;
    }

    public boolean isMachineSet() {
        return machineService != null;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public boolean useSymbol(String symbol) {
        return machineService.makeTransition(symbol);
    }

    public void setMachineService(MachineService machineService) {
        setCanvas();

        if (this.machineService != null) {
            canvas.getChildren().removeAll(this.machineService.getMachineGraph());
        }
        clearInput();
        input.setDisable(false);
        saveFile.setDisable(false);
        editMachine.setDisable(false);
        this.machineService = machineService;
        listView.setItems(machineService.getTransitionsLog());
        machineService.initStart();

        canvas.getChildren().add(machineService.getMachineGraph());

//        input.setTextFormatter(new TextFormatter<Object>(change -> {
//            System.out.println(change.getControlNewText());
//            if (change.isDeleted()) {
//                return null;
//            }
//            if (machineService.getAlphabet().contains(change.getControlNewText())) {
//                return change;
//            }
//            return null;
//        }));
    }

    private void clearInput() {
    //input = new TextField();
    //rootPane.setBottom(input);
    //input.textProperty().addListener(textInputController);
        input.setTextFormatter(null);
        input.clear();
        input.setTextFormatter(inputFormatter);

//        input.textProperty().addListener(textInputController);
    }

    public TextField getInput() {
        return input;
    }


}
