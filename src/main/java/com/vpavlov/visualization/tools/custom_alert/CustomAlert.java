package com.vpavlov.visualization.tools.custom_alert;

import com.vpavlov.App;
import com.vpavlov.visualization.tools.custom_alert.controller.CustomAlertController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public final class CustomAlert extends Stage {

    private final CustomAlertController controller;

    private CustomAlert(FXMLLoader loader) throws IOException {
        super(StageStyle.UNIFIED);
        Scene scene = new Scene(loader.load());
        this.setScene(scene);
        this.initModality(Modality.APPLICATION_MODAL);
        scene.getStylesheets().add(App.class.getResource("custom-alert.css").toExternalForm());
        controller = loader.getController();
        this.setResizable(false);
    }

    public static void showInfoAlert(String message){
        FXMLLoader loader = new FXMLLoader(App.class.getResource("custom-alert-info.fxml"));
        try {
            CustomAlert instance = new CustomAlert(loader);
            instance.controller.setMessage(message);
            instance.controller.initInfoAlert();
            instance.showAndWait();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean showConfirmAlert(String message){
        FXMLLoader loader = new FXMLLoader(App.class.getResource("custom-alert-confirmation.fxml"));
        try {
            CustomAlert instance = new CustomAlert(loader);
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
