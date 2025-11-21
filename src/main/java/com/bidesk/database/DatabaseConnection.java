package com.bidesk.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // Estas constantes AGORA SÃO OBTIDAS DA CLASSE DE CONFIGURAÇÃO!
    private static final String URL = DatabaseConfig.getUrl();
    private static final String USER = DatabaseConfig.getUser();
    private static final String PASSWORD = DatabaseConfig.getPassword();

    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        // Verifica se a configuração foi carregada corretamente
        if (URL == null || USER == null || PASSWORD == null) {
            throw new SQLException("Configurações do banco de dados (URL, User, Password) não encontradas ou não carregadas.");
        }
        
        if (connection == null || connection.isClosed()) {
            try {
                // Carrega o driver MySQL
                Class.forName("com.mysql.cj.jdbc.Driver");
                
                // Estabelece a conexão usando as propriedades
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (ClassNotFoundException e) {
                throw new SQLException("Driver MySQL não encontrado. Verifique se o conector JDBC está no classpath.", e);
            }
        }
        return connection;
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}