package com.bidesk.controller;

import com.bidesk.dao.MaterialDAO;
import com.bidesk.model.Material;
import java.util.List;

public class EstoqueController {
    private MaterialDAO materialDAO;
    
    public EstoqueController() {
        materialDAO = new MaterialDAO();
    }
    
    public List<Material> listarTodos() {
        return materialDAO.listarTodos();
    }
    
    public String inserir(String nome, int quantidade) {
        try {
            Material material = new Material(nome, quantidade);
            boolean sucesso = materialDAO.inserir(material);
            return sucesso ? null : "Falha ao inserir no banco de dados. Verifique se o banco está configurado corretamente.";
        } catch (java.sql.SQLException e) {
            String mensagem = "Erro de banco de dados: " + e.getMessage();
            if (e.getMessage().contains("Access denied")) {
                mensagem += "\n\nVerifique as credenciais do banco de dados em DatabaseConnection.java";
            } else if (e.getMessage().contains("Unknown database")) {
                mensagem += "\n\nO banco de dados 'bidesk_db' não existe. Execute o script database/schema.sql";
            } else if (e.getMessage().contains("Table") && e.getMessage().contains("doesn't exist")) {
                mensagem += "\n\nA tabela não existe. Execute o script database/schema.sql";
            } else if (e.getMessage().contains("Communications link failure") || e.getMessage().contains("Connection refused")) {
                mensagem += "\n\nNão foi possível conectar ao MySQL. Verifique se o MySQL está rodando.";
            }
            return mensagem;
        } catch (Exception e) {
            return "Erro inesperado: " + e.getMessage() + "\nTipo: " + e.getClass().getSimpleName();
        }
    }
    
    public boolean atualizar(Material material) {
        return materialDAO.atualizar(material);
    }
    
    public boolean deletar(int id) {
        return materialDAO.deletar(id);
    }
}


