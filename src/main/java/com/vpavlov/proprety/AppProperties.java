package com.vpavlov.proprety;

import java.io.*;
import java.util.Properties;

public class AppProperties {

    private static final String FILE_NAME = "app.properties";

    private static final Properties properties = new Properties();

    private static final AppProperties INSTANCE = new AppProperties();



    private AppProperties(){
        InputStream input = null;
        try{
            input = new BufferedInputStream(new FileInputStream(FILE_NAME));

            properties.load(input);

        }catch (IOException e){
            e.printStackTrace();
        }finally{
            if (input != null){
                try{
                    input.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    public String getProperty(String key){
        return properties.getProperty(key);
    }

    public static AppProperties getInstance(){
        return INSTANCE;
    }

}
