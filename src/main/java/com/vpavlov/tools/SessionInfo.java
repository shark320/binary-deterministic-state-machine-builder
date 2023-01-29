package com.vpavlov.tools;

import com.vpavlov.App;
import com.vpavlov.visualization.tools.custom_alert.CustomAlert;

import java.io.*;
import java.util.Properties;

public class SessionInfo {

    /**
     * Properties instance
     */
    private final Properties properties = new Properties();

    /**
     * Session info file name
     */
    private static final String FILE_NAME = App.getProperties().getProperty("session-file");


    /**
     * AppProperties static instance
     */
    private static final SessionInfo INSTANCE = init();

    /**
     * Constructor
     */
    private SessionInfo(File file) {
        InputStream input = null;
        try {
            input = new BufferedInputStream(new FileInputStream(file));
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static SessionInfo init(){
        File file = getFile();
        if (file == null){
            return null;
        }
        return new SessionInfo(file);
    }

    public static String getParameter(String name){
        if (INSTANCE==null){
            CustomAlert.showErrorAlert("Error occurred while loading session information.");
            return null;
        }
        return INSTANCE.properties.getProperty(name);
    }

    public static void setParameter(String name, String value){
        File file = getFile();
        if (INSTANCE==null || file == null){
            CustomAlert.showErrorAlert("Error occurred while loading session information.");
            return;
        }
        INSTANCE.properties.setProperty(name, value);
        try (FileOutputStream fos = new FileOutputStream(file)){
            INSTANCE.properties.store(fos,"");
        } catch (IOException e) {
            CustomAlert.showErrorAlert("Error occurred while loading session information.");
            e.printStackTrace();
        }
    }

    private static File getFile(){
        File file = new File(FILE_NAME);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return file;
    }
}
