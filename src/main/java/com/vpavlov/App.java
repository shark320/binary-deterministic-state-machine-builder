package com.vpavlov;

import com.vpavlov.visualization.controller.PrimaryController;
import com.vpavlov.proprety.AppProperties;
import com.vpavlov.visualization.text_window.TextWindowStage;
import com.vpavlov.visualization.tools.custom_alert.CustomAlert;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.Locale;

/**
 * Main Application class
 *
 * @author vpalvov
 */
public class App extends Application {

    /**
     * App properties
     */
    private static final AppProperties properties = AppProperties.getInstance();

    /**
     * Main window title
     */
    private static final String TITLE = properties.getProperty("main-title");

    /**
     * Main window scene
     */
    private static Scene scene;

    /**
     * Main window controller
     */
    private static PrimaryController primaryController;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML());
        primaryController.setStage(stage);
        stage.setScene(scene);
        setCustomStylesheet();
        stage.show();
        stage.setTitle(TITLE);
        stage.setResizable(false);
        stage.setOnCloseRequest(this::onCloseRequest);
        primaryController.checkLastOpenedFile();
    }

    private void onCloseRequest(WindowEvent event) {
        if (primaryController.onCLoseRequest()) {
            TextWindowStage.closeWindow();
        }else{
            event.consume();
        }
    }

    /**
     * Sets custom stylesheet
     */
    private static void setCustomStylesheet() {
        scene.getStylesheets().add(App.class.getResource("css/custom.css").toExternalForm());
    }

    /**
     * Load main window FXML
     *
     * @return Loaded window
     * @throws IOException if any
     */
    private Parent loadFXML() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("fxml/primary.fxml"));
        Parent parent = fxmlLoader.load();
        primaryController = fxmlLoader.getController();
        return parent;
    }

    /**
     * Gets app properties
     *
     * @return app properties
     */
    public static AppProperties getProperties() {
        return properties;
    }

    /**
     * Main application method
     *
     * @param args unused arguments
     */
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        launch();
    }

}