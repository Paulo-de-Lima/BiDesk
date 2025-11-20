package com.bidesk.dao;

import com.bidesk.database.DatabaseConnection;
import com.bidesk.model.Material;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MaterialDAO {
    
    private static final String UNIDADE_PADRAO = "UN";
    
    public List<Material> listarTodos() {
        List<Material> materiais = new ArrayList<>();
        String sql = "SELECT id, nome, quantidade, status FROM materiais ORDER BY nome";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Material material = new Material();
                material.setId(rs.getInt("id"));
                material.setNome(rs.getString("nome"));
                material.setQuantidade(rs.getInt("quantidade"));
                // Note: O status deve ser lido do BD e atualizado no Model, se for o caso
                try {
                    material.setStatus(Material.StatusMaterial.valueOf(rs.getString("status")));
                } catch (IllegalArgumentException e) {
                    // Tratar caso o valor do status no BD esteja inválido, ou re-calcular
                    material.atualizarStatus(); 
                }
                materiais.add(material);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return materiais;
    }
    
    public boolean inserir(Material material) throws SQLException {
        String sql = "INSERT INTO materiais (nome, unidade, quantidade, status) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            material.atualizarStatus();
            stmt.setString(1, material.getNome());
            stmt.setString(2, UNIDADE_PADRAO);
            stmt.setInt(3, material.getQuantidade());
            stmt.setString(4, material.getStatus().name());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
    
    public boolean atualizar(Material material) {
        // Atualiza nome, quantidade e status (unidade permanece fixa)
        String sql = "UPDATE materiais SET nome = ?, quantidade = ?, status = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            material.atualizarStatus();
            
            // Ordem Correta dos índices:
            stmt.setString(1, material.getNome());
            stmt.setInt(2, material.getQuantidade());
            stmt.setString(3, material.getStatus().name());
            stmt.setInt(4, material.getId()); // Onde id = ?
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deletar(int id) {
        String sql = "DELETE FROM materiais WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}