package com.vpavlov.visualization.text_window;

import com.vpavlov.App;
import com.vpavlov.visualization.text_window.controller.TextWindowController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;

/**
 * Help information window stage
 *
 * @author vpavlov
 */
public final class TextWindowStage extends Stage {

    /**
     * Help window stage instance
     */
    private static TextWindowStage helpInstance = null;

    /**
     * About window stage instance
     */
    private static TextWindowStage aboutInstance = null;

    /**
     * Constructor
     * @throws IOException if any
     */
    private TextWindowStage(FXMLLoader loader) throws IOException {
        System.out.println("Creating HelpWindow");

        Scene scene = new Scene(loader.load());
        this.setScene(scene);
        scene.getStylesheets().add(App.class.getResource("css/custom.css").toExternalForm());
    }

    /**
     * Shows help window
     */
    public static void showHelp(){
        if (helpInstance == null) {
            try {
                FXMLLoader loader = new FXMLLoader(App.class.getResource("fxml/text-window.fxml"));
                helpInstance = new TextWindowStage(loader);
                TextWindowController controller = loader.getController();
                controller.initHelp();
                helpInstance.setTitle(App.getProperties().getProperty("help-title"));
                helpInstance.show();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else{
            helpInstance.show();
            helpInstance.requestFocus();
        }
    }

    /**
     * Shows about info window
     */
    public static void showAbout(){
        if (aboutInstance == null) {
            try {
                FXMLLoader loader = new FXMLLoader(App.class.getResource("fxml/text-window.fxml"));
                aboutInstance = new TextWindowStage(loader);
                aboutInstance.setTitle(App.getProperties().getProperty("about-title"));
                TextWindowController controller = loader.getController();
                controller.initAbout();
                aboutInstance.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else{
            aboutInstance.show();
            aboutInstance.requestFocus();
        }
    }

    /**
     * Closes all text windows
     */
    public static void closeWindow(){
        if(helpInstance !=null){
            helpInstance.close();
        }
        if (aboutInstance != null){
            aboutInstance.close();
        }
    }
}
