package com.vpavlov.visualization.machineBuilder;

import com.vpavlov.App;
import com.vpavlov.proprety.AppProperties;
import com.vpavlov.services.machine.MachineService;
import com.vpavlov.visualization.machineBuilder.controller.MachineBuilderController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class MachineBuilderStage extends Stage {

    private final MachineBuilderController controller;

    private static final AppProperties properties = AppProperties.getInstance();

    private static final String TITLE = properties.getProperty("builder-title");

    private boolean isActive = false;

    public MachineBuilderStage () throws IOException {
        super(StageStyle.UNIFIED);
        FXMLLoader loader = new FXMLLoader(App.class.getResource("machineBuilder.fxml"));
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(App.class.getResource(properties.getProperty("custom-stylesheet")).toExternalForm());
        controller = loader.getController();
        this.setResizable(false);
        this.setTitle(TITLE);
        this.setScene(scene);
        this.initModality(Modality.APPLICATION_MODAL);
    }

    public MachineService openAndWait(){
        isActive = true;
        controller.createMachineService();
        showAndWait();

        return controller.getMachineService();
    }

    public MachineService create(){
        isActive = false;
        hide();
        return controller.getMachineService();
    }

    public boolean isActive(){
        return isActive;
    }

}
