package com.bidesk.dao;

import com.bidesk.database.DatabaseConnection;
import com.bidesk.model.Despesa;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DespesaDAO {
    
    public List<Despesa> listarTodos() {
        List<Despesa> despesas = new ArrayList<>();
        String sql = "SELECT * FROM despesas ORDER BY data DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Despesa despesa = new Despesa();
                despesa.setId(rs.getInt("id"));
                despesa.setData(rs.getDate("data"));
                despesa.setCidade(rs.getString("cidade"));
                despesa.setDespesa(rs.getBigDecimal("despesa"));
                despesa.setTotal(rs.getBigDecimal("total"));
                despesas.add(despesa);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return despesas;
    }
    
    public boolean inserir(Despesa despesa) {
        String sql = "INSERT INTO despesas (data, cidade, despesa, total) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDate(1, despesa.getData());
            stmt.setString(2, despesa.getCidade());
            stmt.setBigDecimal(3, despesa.getDespesa());
            stmt.setBigDecimal(4, despesa.getTotal());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean atualizar(Despesa despesa) {
        String sql = "UPDATE despesas SET data = ?, cidade = ?, despesa = ?, total = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDate(1, despesa.getData());
            stmt.setString(2, despesa.getCidade());
            stmt.setBigDecimal(3, despesa.getDespesa());
            stmt.setBigDecimal(4, despesa.getTotal());
            stmt.setInt(5, despesa.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deletar(int id) {
        String sql = "DELETE FROM despesas WHERE id = ?";
        
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


