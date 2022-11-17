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
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class SelectionWindow extends Stage {

    private static final AppProperties properties = AppProperties.getInstance();

    private static final String TITLE = properties.getProperty("selection-title");

    private boolean closeRequest = false;

    private final SelectionWindowController controller;

    private SelectionWindow() throws IOException {
        super(StageStyle.UNIFIED);
        FXMLLoader loader = new FXMLLoader(App.class.getResource("selectionWindow.fxml"));
        Scene scene = new Scene(loader.load());
        controller = loader.getController();
        scene.getStylesheets().add(App.class.getResource("selection-window.css").toExternalForm());
        this.initModality(Modality.APPLICATION_MODAL);
        this.setTitle(TITLE);
        this.setResizable(false);
        this.setScene(scene);
        this.initModality(Modality.APPLICATION_MODAL);
        this.sizeToScene();
        this.setOnCloseRequest(e-> closeRequest = true);
    }

    public static String showRadioSelectionAndWait(Collection<String> variants) {
        SelectionWindow instance = null;
        try {
            instance = new SelectionWindow();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String selected;
        instance.controller.setRadioVariants(variants);
        instance.showAndWait();
        selected = instance.controller.getSelectedRadioButton();
        instance.controller.poor();
        return selected;
    }

    public static Set<String> showCheckSelectionAndWait(Collection<String> variants) {
        SelectionWindow instance = null;
        try {
            instance = new SelectionWindow();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Set<String> checkedVariants;
        instance.controller.setCheckVariants(variants);
        instance.showAndWait();
        if (!instance.closeRequest) {
            checkedVariants = instance.controller.getCheckedVariants();
        }else{
            checkedVariants = null;
        }
        instance.controller.poor();
        return checkedVariants;
    }

}
