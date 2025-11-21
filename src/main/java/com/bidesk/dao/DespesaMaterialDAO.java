package com.bidesk.dao;

import com.bidesk.database.DatabaseConnection;
import com.bidesk.model.DespesaMaterial;
import java.sql.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class DespesaMaterialDAO {
    
    public List<DespesaMaterial> listarTodos() {
        List<DespesaMaterial> despesasMateriais = new ArrayList<>();
        String sql = "SELECT * FROM despesas_materiais ORDER BY created_at DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                DespesaMaterial despesaMaterial = new DespesaMaterial();
                despesaMaterial.setId(rs.getInt("id"));
                despesaMaterial.setNome(rs.getString("nome"));
                despesaMaterial.setDespesaId(rs.getObject("despesa_id", Integer.class));
                despesaMaterial.setGasto(rs.getBigDecimal("gasto"));
                despesasMateriais.add(despesaMaterial);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar despesas materiais: " + e.getMessage());
            e.printStackTrace();
        }
        
        return despesasMateriais;
    }
    
    public List<DespesaMaterial> listarPorDespesa(Integer despesaId) {
        List<DespesaMaterial> despesasMateriais = new ArrayList<>();
        
        // ✅ CORREÇÃO: Verifica se despesaId é null
        if (despesaId == null) {
            System.out.println("DespesaId é null, retornando lista vazia ou todas as despesas");
            return listarTodos(); // ou retorne uma lista vazia: return despesasMateriais;
        }
        
        String sql = "SELECT * FROM despesas_materiais WHERE despesa_id = ? ORDER BY created_at DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, despesaId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    DespesaMaterial despesaMaterial = new DespesaMaterial();
                    despesaMaterial.setId(rs.getInt("id"));
                    despesaMaterial.setNome(rs.getString("nome"));
                    despesaMaterial.setDespesaId(rs.getObject("despesa_id", Integer.class));
                    despesaMaterial.setGasto(rs.getBigDecimal("gasto"));
                    despesasMateriais.add(despesaMaterial);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar despesas materiais por despesa: " + e.getMessage());
            e.printStackTrace();
        }
        
        return despesasMateriais;
    }
    
    public boolean inserir(DespesaMaterial despesaMaterial) {
        String sql = "INSERT INTO despesas_materiais (nome, despesa_id, gasto) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, despesaMaterial.getNome());
            if (despesaMaterial.getDespesaId() != null) {
                stmt.setInt(2, despesaMaterial.getDespesaId());
            } else {
                stmt.setNull(2, Types.INTEGER);
            }
            stmt.setBigDecimal(3, despesaMaterial.getGasto());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao inserir despesa de material: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean atualizar(DespesaMaterial despesaMaterial) {
        String sql = "UPDATE despesas_materiais SET nome = ?, despesa_id = ?, gasto = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            System.out.println("=== DEBUG DAO ATUALIZAR ===");
            System.out.println("ID: " + despesaMaterial.getId());
            System.out.println("Nome: " + despesaMaterial.getNome());
            System.out.println("Despesa ID: " + despesaMaterial.getDespesaId());
            System.out.println("Gasto: " + despesaMaterial.getGasto());
            
            stmt.setString(1, despesaMaterial.getNome());
            if (despesaMaterial.getDespesaId() != null) {
                stmt.setInt(2, despesaMaterial.getDespesaId());
            } else {
                stmt.setNull(2, Types.INTEGER);
            }
            stmt.setBigDecimal(3, despesaMaterial.getGasto());
            stmt.setInt(4, despesaMaterial.getId());
            
            int rowsAffected = stmt.executeUpdate();
            System.out.println("Linhas afetadas: " + rowsAffected);
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar despesa de material: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deletar(int id) {
        String sql = "DELETE FROM despesas_materiais WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            System.out.println("Deletar - Linhas afetadas: " + rowsAffected);
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao deletar despesa de material: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public BigDecimal calcularTotalGasto() {
        String sql = "SELECT SUM(gasto) as total FROM despesas_materiais";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                BigDecimal total = rs.getBigDecimal("total");
                return total != null ? total : BigDecimal.ZERO;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao calcular total gasto: " + e.getMessage());
            e.printStackTrace();
        }
        
        return BigDecimal.ZERO;
    }
}