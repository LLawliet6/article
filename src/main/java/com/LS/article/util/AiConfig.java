package com.LS.article.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AiConfig {
    private static Properties props = new Properties();
    static {
        try(InputStream in = AiConfig.class.getClassLoader().getResourceAsStream("jdbc.properties")) {
            props.load(in);
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }
    public static String get(String key) {
        return props.getProperty(key);
    }
}
