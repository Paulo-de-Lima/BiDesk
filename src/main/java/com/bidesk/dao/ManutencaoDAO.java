package com.bidesk.dao;

import com.bidesk.database.DatabaseConnection;
import com.bidesk.model.Manutencao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ManutencaoDAO {
    
    public List<Manutencao> listarTodos() {
        List<Manutencao> manutencoes = new ArrayList<>();
        String sql = "SELECT * FROM manutencoes ORDER BY created_at DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Manutencao manutencao = new Manutencao();
                manutencao.setId(rs.getInt("id"));
                manutencao.setClienteId(rs.getObject("cliente_id", Integer.class));
                manutencao.setTitulo(rs.getString("titulo"));
                manutencao.setDescricao(rs.getString("descricao"));
                manutencoes.add(manutencao);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar manutenções: " + e.getMessage());
            e.printStackTrace();
        }
        
        return manutencoes;
    }
    
    public List<Manutencao> listarPendentes() {
        // Como não há mais distinção, retorna todas as manutenções
        return listarTodos();
    }
    
    public boolean inserir(Manutencao manutencao) {
        String sql = "INSERT INTO manutencoes (cliente_id, titulo, descricao) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            if (manutencao.getClienteId() != null) {
                stmt.setInt(1, manutencao.getClienteId());
            } else {
                stmt.setNull(1, Types.INTEGER);
            }
            stmt.setString(2, manutencao.getTitulo());
            if (manutencao.getDescricao() != null) {
                stmt.setString(3, manutencao.getDescricao());
            } else {
                stmt.setNull(3, Types.VARCHAR);
            }
            
            int rowsAffected = stmt.executeUpdate();
            System.err.println("Manutenção inserida - Linhas afetadas: " + rowsAffected);
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao inserir manutenção: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean atualizar(Manutencao manutencao) {
        String sql = "UPDATE manutencoes SET cliente_id = ?, titulo = ?, descricao = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            if (manutencao.getClienteId() != null) {
                stmt.setInt(1, manutencao.getClienteId());
            } else {
                stmt.setNull(1, Types.INTEGER);
            }
            stmt.setString(2, manutencao.getTitulo());
            if (manutencao.getDescricao() != null) {
                stmt.setString(3, manutencao.getDescricao());
            } else {
                stmt.setNull(3, Types.VARCHAR);
            }
            stmt.setInt(4, manutencao.getId());
            
            int rowsAffected = stmt.executeUpdate();
            System.err.println("Manutenção atualizada - Linhas afetadas: " + rowsAffected);
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar manutenção: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deletar(int id) {
        String sql = "DELETE FROM manutencoes WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            System.err.println("Manutenção deletada - Linhas afetadas: " + rowsAffected);
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao deletar manutenção: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
