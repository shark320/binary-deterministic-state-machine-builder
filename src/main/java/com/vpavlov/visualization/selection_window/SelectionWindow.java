package com.vpavlov.visualization.selection_window;

import com.vpavlov.App;
import com.vpavlov.proprety.AppProperties;
import com.vpavlov.visualization.selection_window.controller.SelectionWindowController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class SelectionWindow extends Stage {

    private static final AppProperties properties = AppProperties.getInstance();

    private static final String TITLE = properties.getProperty("selection-title");

    private final SelectionWindowController controller;

    private SelectionWindow() throws IOException {
        super(StageStyle.UNIFIED);
        FXMLLoader loader = new FXMLLoader(App.class.getResource("selectionWindow.fxml"));
        Scene scene = new Scene(loader.load());
        controller = loader.getController();
        scene.getStylesheets().add(App.class.getResource(properties.getProperty("custom-stylesheet")).toExternalForm());
        this.initModality(Modality.APPLICATION_MODAL);
        this.setTitle(TITLE);
        this.setResizable(false);
        this.setScene(scene);
        this.initModality(Modality.APPLICATION_MODAL);
        this.sizeToScene();
    }

    public static String showSelectionAndWait(){
        try {
            SelectionWindow instance = new SelectionWindow();
            instance.showAndWait();
            return instance.controller.getSelectedButton();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
