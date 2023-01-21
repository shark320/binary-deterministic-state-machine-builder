package com.vpavlov.visualization.tools.custom_alert;

import com.vpavlov.App;
import com.vpavlov.visualization.tools.custom_alert.controller.CustomAlertController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

/**
 * Custom alert window stage
 */
public final class CustomAlert extends Stage {

    /**
     * Window controller
     */
    private final CustomAlertController controller;

    /**
     * Constructor
     *
     * @param loader FXML loader
     * @throws IOException if any
     */
    private CustomAlert(FXMLLoader loader) throws IOException {
        super(StageStyle.UNIFIED);
        Scene scene = new Scene(loader.load());
        this.setScene(scene);
        this.initModality(Modality.APPLICATION_MODAL);
        scene.getStylesheets().add(App.class.getResource("css/custom-alert.css").toExternalForm());
        controller = loader.getController();
        this.setResizable(false);
    }

    /**
     * Show info alert
     *
     * @param message message to show
     */
    public static void showInfoAlert(String message) {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("fxml/custom-alert-info.fxml"));
        try {
            CustomAlert instance = new CustomAlert(loader);
            instance.setTitle("Information");
            instance.controller.setMessage(message);
            instance.controller.initInfoErrorAlert();
            instance.showAndWait();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Show error alert
     *
     * @param message error message
     */
    public static void showErrorAlert(String message) {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("fxml/custom-alert-error.fxml"));
        try {
            CustomAlert instance = new CustomAlert(loader);
            instance.setTitle("Error");
            instance.controller.setMessage(message);
            instance.controller.initInfoErrorAlert();
            instance.showAndWait();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Show confirmation alert
     *
     * @param message message to show
     * @return true if confirmed, else false
     */
    public static boolean showConfirmAlert(String message) {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("fxml/custom-alert-confirmation.fxml"));
        try {
            CustomAlert instance = new CustomAlert(loader);
            instance.setTitle("Confirmation");
            instance.controller.setMessage(message);
            instance.controller.initConfirmAlert();
            instance.showAndWait();
            boolean result = instance.controller.isOkButton();
            instance.controller.poor();
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
