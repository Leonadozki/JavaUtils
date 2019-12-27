package com.mail;


import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class PropertiesUtils {

    private static Configuration config;

    static {
        if (null == config){
            try {
                config = new PropertiesConfiguration("config.properties");
            } catch (ConfigurationException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getString(String key){
        return config.getString(key, "");
    }

    public static String[] getStringArray(String key){
        return config.getStringArray(key);
    }

    public static void main(String[] args) {
        String[] stringArray = getStringArray("email.to");
        for (String s: stringArray){
            System.out.println(s);
        }
    }
}
