package com.bidesk.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    
    public static void initializeDatabase() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Tabela de materiais (estoque)
            stmt.execute("CREATE TABLE IF NOT EXISTS materiais (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "nome TEXT NOT NULL," +
                        "quantidade INTEGER NOT NULL DEFAULT 0," +
                        "status TEXT NOT NULL DEFAULT 'VAZIO'" +
                        ")");
            
            // Tabela de clientes
            stmt.execute("CREATE TABLE IF NOT EXISTS clientes (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "nome TEXT NOT NULL," +
                        "endereco TEXT NOT NULL," +
                        "cidade TEXT NOT NULL" +
                        ")");
            
            // Tabela de pedidos
            stmt.execute("CREATE TABLE IF NOT EXISTS pedidos (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "cliente_id INTEGER NOT NULL," +
                        "numero TEXT NOT NULL," +
                        "data TEXT NOT NULL," +
                        "registro TEXT NOT NULL," +
                        "pago REAL NOT NULL DEFAULT 0," +
                        "deve REAL NOT NULL DEFAULT 0," +
                        "FOREIGN KEY (cliente_id) REFERENCES clientes(id)" +
                        ")");
            
            // Tabela de despesas (financeiro)
            stmt.execute("CREATE TABLE IF NOT EXISTS despesas (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "data TEXT NOT NULL," +
                        "cidade TEXT NOT NULL," +
                        "despesa REAL NOT NULL," +
                        "total REAL NOT NULL" +
                        ")");
            
            // Tabela de manutenções
            stmt.execute("CREATE TABLE IF NOT EXISTS manutencoes (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "cliente_id INTEGER NOT NULL," +
                        "titulo TEXT NOT NULL," +
                        "descricao TEXT NOT NULL," +
                        "FOREIGN KEY (cliente_id) REFERENCES clientes(id)" +
                        ")");
            
            // Inserir dados iniciais
            insertInitialData(stmt);
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private static void insertInitialData(Statement stmt) throws SQLException {
        // Inserir materiais iniciais
        stmt.execute("INSERT OR IGNORE INTO materiais (nome, quantidade, status) VALUES " +
                    "('Taco', 0, 'VAZIO')," +
                    "('Jogo de bolas', 4, 'BAIXO')," +
                    "('Cabeça de taco', 0, 'VAZIO')," +
                    "('Giz', 12, 'ALTO')," +
                    "('Giz azul', 15, 'ALTO')," +
                    "('Bola branca', 0, 'VAZIO')");
        
        // Inserir clientes iniciais
        stmt.execute("INSERT OR IGNORE INTO clientes (nome, endereco, cidade) VALUES " +
                    "('Natalino Varela', 'Rua 1 - Bairro 2 - 123', 'Cidade aleatória - AL')," +
                    "('Alexandre de Moraes', 'Rua 2 - Bairro 2 - 222', 'Cidade aleatória - PE')," +
                    "('Paulo de Lima', 'Rua 3 - Bairro 4 - 412', 'Paulo Afonso - BA')," +
                    "('Pedro Oliveira', 'Rua 5 - Bairro 2 - 22', 'Paulo Afonso - BA')");
        
        // Inserir pedidos iniciais
        stmt.execute("INSERT OR IGNORE INTO pedidos (cliente_id, numero, data, registro, pago, deve) VALUES " +
                    "(1, '123', '12/02/25', '12345', 100, 0)," +
                    "(2, '321', '24/02/25', '41234', 300, 700)");
        
        // Inserir despesas iniciais
        stmt.execute("INSERT OR IGNORE INTO despesas (data, cidade, despesa, total) VALUES " +
                    "('10/07/1945', 'Paulo Afonso - BA', 0.01, 0.01)," +
                    "('17/07/1945', 'Tilambuco - CA', 0.01, 0.01)");
        
        // Inserir manutenções iniciais
        stmt.execute("INSERT OR IGNORE INTO manutencoes (cliente_id, titulo, descricao) VALUES " +
                    "(1, 'Jogo de bolas com defeito', 'Bola 6 e 9 quebradas')," +
                    "(2, 'Pano rasgado', 'Pano rasgado e velho')");
    }
}

