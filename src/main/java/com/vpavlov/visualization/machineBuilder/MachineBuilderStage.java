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

/**
 * Machine builder window stage
 *
 * @author vpavlov
 */
public class MachineBuilderStage extends Stage {

    /**
     * Machine builder window controller
     */
    private final MachineBuilderController controller;

    /**
     * App properties
     */
    private static final AppProperties properties = AppProperties.getInstance();

    /**
     * Window title
     */
    private static final String TITLE = properties.getProperty("builder-title");

    /**
     * Is the window is active
     */
    private boolean isActive = false;

    /**
     * Constructor
     *
     * @throws IOException if any
     */
    private MachineBuilderStage() throws IOException {
        super(StageStyle.UNIFIED);
        FXMLLoader loader = new FXMLLoader(App.class.getResource("fxml/machineBuilder.fxml"));
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(App.class.getResource("css/custom.css").toExternalForm());
        controller = loader.getController();
        this.setResizable(false);
        this.setTitle(TITLE);
        this.setScene(scene);
        this.initModality(Modality.APPLICATION_MODAL);
        this.setOnCloseRequest(e -> controller.closeRequest());
    }

    /**
     * Opens the machine builder window and waits
     *
     * @return created machine service
     */
    public static MachineService openAndWait() {
        try {
            MachineBuilderStage instance = new MachineBuilderStage();
            instance.isActive = true;
            instance.controller.createMachineService();
            instance.showAndWait();

            return instance.controller.getMachineService();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Opens the machine builder window and waits
     *
     * @param machineService machine service to edit
     * @return edited machine service
     */
    public static MachineService openAndWait(MachineService machineService) {
        try {
            MachineBuilderStage instance = new MachineBuilderStage();
            instance.controller.setMachineService(machineService);
            instance.showAndWait();
            return instance.controller.getMachineService();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
