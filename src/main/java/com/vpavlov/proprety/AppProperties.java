package com.vpavlov.proprety;

import java.io.*;
import java.util.Properties;

/**
 * Class-reader app properties from .properties file.
 *
 * @author vpavlov
 */
public class AppProperties {

    /**
     * .properties file name
     */
    private static final String FILE_NAME = "app.properties";

    /**
     * Properties static instance
     */
    private static final Properties properties = new Properties();

    /**
     * AppProperties static instance
     */
    private static final AppProperties INSTANCE = new AppProperties();


    /**
     * Constructor
     */
    private AppProperties() {
        InputStream input = null;
        try {
            input = new BufferedInputStream(new FileInputStream(FILE_NAME));

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

    /**
     * Property getter
     *
     * @param key the property name
     * @return the property value
     */
    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     * Instance getter
     *
     * @return static instance
     */
    public static AppProperties getInstance() {
        return INSTANCE;
    }

}
