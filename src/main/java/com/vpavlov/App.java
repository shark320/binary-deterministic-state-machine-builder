package com.vpavlov;

import com.vpavlov.machine.Alphabet;
import com.vpavlov.machine.Machine;
import com.vpavlov.visualization.controller.PrimaryController;
import com.vpavlov.proprety.AppProperties;
import com.vpavlov.visualization.machineBuilder.MachineBuilderStage;
import com.vpavlov.visualization.selection_window.SelectionWindow;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Machine machine;

    private static Alphabet alphabet;

    private static final AppProperties properties = AppProperties.getInstance();

    private static final double WIDTH = Double.parseDouble(properties.getProperty("width"));

    private static final double HEIGHT = Double.parseDouble(properties.getProperty("height"));

    private static final String TITLE = properties.getProperty("main-title");
    private static Scene scene;
    private static PrimaryController primaryController;


    @Override
    public void start(Stage stage) throws IOException {
        initMachine();
        testMachine();
        scene = new Scene(loadFXML("primary"));
        stage.setScene(scene);
        setCustomStylesheet();
        primaryController.init();
        stage.show();
        stage.setTitle(TITLE);
        stage.setResizable(false);
        System.out.println(SelectionWindow.showSelectionAndWait());
        getMachineBuilder().openAndWait();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));

    }

    private static void setCustomStylesheet(){
        scene.getStylesheets().add(App.class.getResource(properties.getProperty("custom-stylesheet")).toExternalForm());
    }

    private static void initMachine(){
        String symbols = properties.getProperty("alphabet");
        alphabet = new Alphabet(symbols);
        machine = new Machine(alphabet);
    }

    private static MachineBuilderStage getMachineBuilder() throws IOException{
        MachineBuilderStage mb = new MachineBuilderStage();
        return mb;
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        Parent parent = fxmlLoader.load();
        primaryController = fxmlLoader.getController();
        return parent;
    }

    public static void showInfoAlert(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static Alphabet getAlphabet(){
        return alphabet;
    }

    public static Machine getMachine(){
        return machine;
    }

    public static void testMachine(){
        machine.addState("A");
        machine.addState("B");
        machine.addTransition("1", "A", "A");
        machine.addTransition("0", "A", "B");
        machine.addTransition("1", "B", "A");
        machine.addTransition("0", "B", "B");
        machine.setStartState("A");
    }

    public static AppProperties getProperties() {
        return properties;
    }


    public static void main(String[] args) {
        launch();
    }

}