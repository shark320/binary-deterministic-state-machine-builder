package com.vpavlov.visualization.help_window;

import com.vpavlov.App;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;

/**
 * Help information window stage
 *
 * @author vpavlov
 */
public final class HelpWindowStage extends Stage {

    /**
     * Stage instance
     */
    private static HelpWindowStage instance = null;

    /**
     * Constructor
     * @throws IOException if any
     */
    private HelpWindowStage() throws IOException {
        System.out.println("Creating HelpWindow");
        FXMLLoader loader = new FXMLLoader(App.class.getResource("fxml/help.fxml"));
        Scene scene = new Scene(loader.load());
        this.setScene(scene);
        scene.getStylesheets().add(App.class.getResource("css/custom.css").toExternalForm());
        this.setTitle(App.getProperties().getProperty("help-title"));
    }

    /**
     * Shows help window
     */
    public static void showHelp(){
        System.out.println("Show help");
        if (instance ==null) {
            try {
                instance = new HelpWindowStage();
                instance.show();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else{
            instance.show();
            instance.requestFocus();
        }
    }

    /**
     * Closes help window
     */
    public static void closeWindow(){
        if(instance!=null){
            instance.close();
        }
    }
}
