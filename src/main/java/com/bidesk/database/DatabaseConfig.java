package com.bidesk.database;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Classe responsável por carregar as configurações de banco de dados
 * a partir de um arquivo db.properties.
 */
public class DatabaseConfig {

    private static final Properties PROPERTIES = new Properties();
    private static final String CONFIG_FILE = "db.properties";

    static {
        // Bloco estático para carregar o arquivo uma única vez na memória
        try (InputStream input = DatabaseConfig.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                System.err.println("Desculpe, o arquivo " + CONFIG_FILE + " não foi encontrado no classpath.");
            } else {
                PROPERTIES.load(input);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static String getUrl() {
        return PROPERTIES.getProperty("db.url");
    }

    public static String getUser() {
        return PROPERTIES.getProperty("db.user");
    }

    public static String getPassword() {
        return PROPERTIES.getProperty("db.password");
    }
}