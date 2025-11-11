package com.bidesk.dao;

import com.bidesk.database.DatabaseConnection;
import com.bidesk.model.RegistroFinanceiro;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RegistroFinanceiroDAO {
    
    public List<RegistroFinanceiro> listarTodos() {
        List<RegistroFinanceiro> registros = new ArrayList<>();
        String sql = "SELECT * FROM registros_financeiros ORDER BY data DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                RegistroFinanceiro registro = new RegistroFinanceiro();
                registro.setId(rs.getInt("id"));
                registro.setClienteId(rs.getObject("cliente_id", Integer.class));
                registro.setNumero(rs.getString("numero"));
                registro.setData(rs.getDate("data"));
                registro.setRegistro(rs.getString("registro"));
                registro.setPago(rs.getBigDecimal("pago"));
                registro.setDeve(rs.getBigDecimal("deve"));
                registros.add(registro);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return registros;
    }
    
    public List<RegistroFinanceiro> listarPorCliente(int clienteId) {
        List<RegistroFinanceiro> registros = new ArrayList<>();
        String sql = "SELECT * FROM registros_financeiros WHERE cliente_id = ? ORDER BY data DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, clienteId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                RegistroFinanceiro registro = new RegistroFinanceiro();
                registro.setId(rs.getInt("id"));
                registro.setClienteId(rs.getObject("cliente_id", Integer.class));
                registro.setNumero(rs.getString("numero"));
                registro.setData(rs.getDate("data"));
                registro.setRegistro(rs.getString("registro"));
                registro.setPago(rs.getBigDecimal("pago"));
                registro.setDeve(rs.getBigDecimal("deve"));
                registros.add(registro);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return registros;
    }
    
    public boolean inserir(RegistroFinanceiro registro) {
        String sql = "INSERT INTO registros_financeiros (cliente_id, numero, data, registro, pago, deve) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            if (registro.getClienteId() != null) {
                stmt.setInt(1, registro.getClienteId());
            } else {
                stmt.setNull(1, Types.INTEGER);
            }
            stmt.setString(2, registro.getNumero());
            stmt.setDate(3, registro.getData());
            stmt.setString(4, registro.getRegistro());
            stmt.setBigDecimal(5, registro.getPago());
            stmt.setBigDecimal(6, registro.getDeve());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean atualizar(RegistroFinanceiro registro) {
        String sql = "UPDATE registros_financeiros SET cliente_id = ?, numero = ?, data = ?, registro = ?, pago = ?, deve = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            if (registro.getClienteId() != null) {
                stmt.setInt(1, registro.getClienteId());
            } else {
                stmt.setNull(1, Types.INTEGER);
            }
            stmt.setString(2, registro.getNumero());
            stmt.setDate(3, registro.getData());
            stmt.setString(4, registro.getRegistro());
            stmt.setBigDecimal(5, registro.getPago());
            stmt.setBigDecimal(6, registro.getDeve());
            stmt.setInt(7, registro.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deletar(int id) {
        String sql = "DELETE FROM registros_financeiros WHERE id = ?";
        
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


