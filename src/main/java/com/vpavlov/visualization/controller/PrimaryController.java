package com.vpavlov.visualization.controller;

import com.vpavlov.App;
import com.vpavlov.proprety.AppProperties;
import com.vpavlov.services.machine.MachineService;
import com.vpavlov.services.machine.MachineServiceFileManager;
import com.vpavlov.services.machine.MachineTransition;
import com.vpavlov.tools.SessionInfo;
import com.vpavlov.visualization.handlers.InputFormatter;
import com.vpavlov.visualization.text_window.TextWindowStage;
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
import java.util.*;

/**
 * Primary scene controller
 *
 * @author vpavlov
 */
public class PrimaryController implements Initializable {

    /**
     * Application properties
     */
    private static final AppProperties properties = AppProperties.getInstance();

    /**
     * Last opened file parameter name
     */
    private static final String LAST_OPENED_FILE_PARAMETER = "last-opened-file";

    /**
     * Help menu item
     */
    @FXML
    public MenuItem help;

    /**
     * About menu item
     */
    @FXML
    public MenuItem about;

    /**
     * Graph canvas
     */
    private Pane canvas;

    /**
     * Root window pane
     */
    @FXML
    private BorderPane rootPane;

    /**
     * Open file hyperlink
     */
    @FXML
    private Hyperlink openLink;

    /**
     * Create new machine hyperlink
     */
    @FXML
    private Hyperlink createLink;

    /**
     * Machine input
     */
    @FXML
    private TextField input;

    /**
     * Save file menu button
     */
    @FXML
    private MenuItem saveFile;

    /**
     * Open file menu button
     */
    @FXML
    private MenuItem openFile;

    /**
     * Edit machine menu button
     */
    @FXML
    private MenuItem editMachine;

    /**
     * Create new machine menu button
     */
    @FXML
    private MenuItem createNew;

    /**
     * VBox with start hyperlinks
     */
    @FXML
    private VBox startVbox;

    /**
     * Machine transitions log
     */
    @FXML
    ListView<MachineTransition> listView;

    /**
     * Machine service instance
     */
    private MachineService machineService;

    /**
     * File chooser to save/open machines
     */
    private final FileChooser fileChooser = new FileChooser();

    /**
     * Window stage
     */
    private Stage stage;

    /**
     * Machine input formatter
     */
    private InputFormatter inputFormatter;

    private boolean isSaved = true;

    private boolean isActive = false;

    private String fileName = null;

    /**
     * Machine graph canvas getter
     *
     * @return machine graph canvas
     */
    public Pane getCanvas() {
        return canvas;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        double paneWidth = Double.parseDouble(properties.getProperty("canvas-width"));
        double paneHeight = Double.parseDouble(properties.getProperty("canvas-height"));
        startVbox.setPrefSize(paneWidth, paneHeight);
        this.inputFormatter = new InputFormatter(this);
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
            saveToFile();
        });

        openFile.setOnAction(e -> {
            File file = fileChooser.showOpenDialog(stage);
            openFromFile(file);
        });

        openLink.setOnAction(e -> {
            File file = fileChooser.showOpenDialog(stage);
            if (openFromFile(file)) {
                openLink.setVisited(false);
            }
        });


        editMachine.setOnAction(e -> {
            if (isActive){
                CustomAlert.showInfoAlert("Please end current visualization session to edit the machine.");
                return;
            }
            if (machineService != null) {
                MachineService newMachineService = MachineBuilderStage.openAndWait(new MachineService(machineService));
                if (newMachineService != null) {
                    canvas.getChildren().remove(machineService.getMachineGraph());
                    setMachineService(newMachineService);
                    isSaved = false;
                    setTitleWithFile();
                }
            }
        });

        createLink.setOnAction(e -> {
            createNewMachine();
            createLink.setVisited(false);
        });

        createNew.setOnAction(e -> createNewMachine());

        help.setOnAction(e -> TextWindowStage.showHelp());

        about.setOnAction(e->TextWindowStage.showAbout());
    }

    public boolean onCLoseRequest(){
        boolean saveConfirm = true;
        boolean activeConfirm = true;
        if (!isSaved){
            saveConfirm = CustomAlert.showConfirmAlert("Are you sure you want to close the program without machine saving?");
        }

        if (isActive){
            activeConfirm = CustomAlert.showConfirmAlert("Are you sure you want to close the program while machine visualization is active?");
        }

        return saveConfirm && activeConfirm;
    }

    private void saveToFile(){
        if (machineService != null) {
            File file = fileChooser.showSaveDialog(stage);
            if (file == null) {
                return;
            }
            try {
                MachineServiceFileManager.saveToFile(machineService, file);
                SessionInfo.setParameter(LAST_OPENED_FILE_PARAMETER, file.getAbsolutePath());
                isSaved = true;
            } catch (IOException ex) {
                CustomAlert.showInfoAlert("An error occurred while writing to file.");
            }
        } else {
            CustomAlert.showInfoAlert("There is no machine to save.");
        }
    }

    public void checkLastOpenedFile(){
        String fileFullName = SessionInfo.getParameter(LAST_OPENED_FILE_PARAMETER);
        if (fileFullName!=null){
            File file = new File(fileFullName);
            if (file.exists()){
                if (CustomAlert.showConfirmAlert(String.format("Continue with last opened file [%s]?", file.getName()))){
                    openFromFile(file);
                }
            }
        }
    }

    /**
     * Opens machine from file
     */
    private boolean openFromFile(File file) {
        if (file == null) {
            return false;
        }
        try {
            MachineService machineService = MachineServiceFileManager.createFromFile(file);
            if (machineService != null) {
                setMachineService(machineService);
                SessionInfo.setParameter(LAST_OPENED_FILE_PARAMETER, file.getAbsolutePath());
                input.requestFocus();
                fileName = file.getName();
                setTitleWithFile();
                return true;
            } else {
                CustomAlert.showErrorAlert("An error occurred while reading machine from the file.");
            }
        } catch (IOException ex) {
            CustomAlert.showErrorAlert("An error occurred while opening the file.");
        }
        return false;
    }

    private void setTitleWithFile(){
        String mainTitle = App.getProperties().getProperty("main-title");
        String titleWithFile;
        if (mainTitle != null){
            if (fileName != null){
                titleWithFile = mainTitle + ": " + fileName + (isSaved ? "" : "*");
            }else{
                titleWithFile = mainTitle + ": untitled*";
            }
            stage.setTitle(titleWithFile);
        }
    }

    private void setDefaultTitle(){
        String mainTitle = App.getProperties().getProperty("main-title");
        if (mainTitle != null){
            stage.setTitle(mainTitle);
        }
    }

    /**
     * Opens create new machine window
     */
    private void createNewMachine() {
        MachineService newMachineService = MachineBuilderStage.openAndWait();
        if (newMachineService != null) {
            setMachineService(newMachineService);
            fileName = null;
            isSaved = false;
            setTitleWithFile();
        }
    }

    /**
     * Set canvas to the main root or create new with parameters from app properties
     */
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

    /**
     * Undo last transitions
     */
    public void undoSymbol() {
        System.out.println("Input: " + input.getText());
        try {
            if (!machineService.undo()) {
                CustomAlert.showErrorAlert("An error occurred while undoing last transition.");
            }
            input.setTextFormatter(null);
            if (input.getLength() <2){
                input.clear();
            }else {
                input.deleteText(input.getLength() - 1, input.getLength());
            }
            input.setTextFormatter(inputFormatter);
        } catch (IllegalStateException e) {
            CustomAlert.showInfoAlert(e.getMessage());
            clearInput();
            isActive =false;
        }
    }

    /**
     * Reset machine to its initial state
     */
    public void resetMachine() {
        try {
            if (machineService.reset()) {
                CustomAlert.showInfoAlert("Input string is accepted.");
            } else {
                CustomAlert.showInfoAlert("Input string is not accepted.");
            }
            clearInput();
            isActive = false;
        } catch (IllegalStateException e) {
            CustomAlert.showInfoAlert(e.getMessage());
        }
    }

    /**
     * Stop and close the machine
     */
    public void quit() {
        if (!isSaved && !CustomAlert.showConfirmAlert("Are you sure you want to quit the machine without saving?")){
            return;
        }
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
        isActive = false;
        isSaved = true;
        setDefaultTitle();
    }

    /**
     * Check if machine is set
     *
     * @return true if set, else false
     */
    public boolean isMachineSet() {
        return machineService != null;
    }

    /**
     * Stage setter
     *
     * @param stage stage to set
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Propagate input symbol to the machine service
     *
     * @param symbol symbol to propagate
     * @return true if the machine is set and symbol in the machine alphabet, else false
     */
    public boolean useSymbol(String symbol) {
        if (machineService == null) {
            return false;
        }
        if (machineService.makeTransition(symbol)){
            isActive = true;
            return true;
        }
        return false;
    }

    /**
     * Machine service setter. Show machine nodes and transitions on the canvas
     *
     * @param machineService machine service to set
     */
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
    }

    /**
     * Clears input
     */
    private void clearInput() {
        input.setTextFormatter(null);
        input.clear();
        input.setTextFormatter(inputFormatter);
    }

    /**
     * Input getter
     *
     * @return input text field element
     */
    public TextField getInput() {
        return input;
    }


}
