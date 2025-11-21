package com.bidesk.dao;

import com.bidesk.database.DatabaseConnection;
import com.bidesk.model.Mesa;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MesaDAO {
    
    public List<Mesa> listarTodas() {
        List<Mesa> mesas = new ArrayList<>();
        String sql = "SELECT * FROM registros_financeiros ORDER BY cliente_id, data";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Mesa mesa = new Mesa();
                mesa.setId(rs.getInt("id"));
                mesa.setClienteId(rs.getInt("cliente_id"));
                mesa.setNumero(rs.getString("numero"));
                mesa.setData(rs.getDate("data"));
                mesa.setRegistro(rs.getString("registro"));
                mesa.setPago(rs.getBigDecimal("pago"));
                mesa.setDeve(rs.getBigDecimal("deve"));
                mesas.add(mesa);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return mesas;
    }
    
    public List<Mesa> listarPorCliente(int clienteId) {
        List<Mesa> mesas = new ArrayList<>();
        String sql = "SELECT * FROM registros_financeiros WHERE cliente_id = ? ORDER BY data";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, clienteId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Mesa mesa = new Mesa();
                mesa.setId(rs.getInt("id"));
                mesa.setClienteId(rs.getInt("cliente_id"));
                mesa.setNumero(rs.getString("numero"));
                mesa.setData(rs.getDate("data"));
                mesa.setRegistro(rs.getString("registro"));
                mesa.setPago(rs.getBigDecimal("pago"));
                mesa.setDeve(rs.getBigDecimal("deve"));
                mesas.add(mesa);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return mesas;
    }
    
    public boolean inserir(Mesa mesa) {
        String sql = "INSERT INTO registros_financeiros (cliente_id, numero, data, registro, pago, deve) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, mesa.getClienteId());
            stmt.setString(2, mesa.getNumero());
            stmt.setDate(3, mesa.getData());
            stmt.setString(4, mesa.getRegistro());
            stmt.setBigDecimal(5, mesa.getPago() != null ? mesa.getPago() : java.math.BigDecimal.ZERO);
            stmt.setBigDecimal(6, mesa.getDeve() != null ? mesa.getDeve() : java.math.BigDecimal.ZERO);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean atualizar(Mesa mesa) {
        String sql = "UPDATE registros_financeiros SET numero = ?, data = ?, registro = ?, pago = ?, deve = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, mesa.getNumero());
            stmt.setDate(2, mesa.getData());
            stmt.setString(3, mesa.getRegistro());
            stmt.setBigDecimal(4, mesa.getPago() != null ? mesa.getPago() : java.math.BigDecimal.ZERO);
            stmt.setBigDecimal(5, mesa.getDeve() != null ? mesa.getDeve() : java.math.BigDecimal.ZERO);
            stmt.setInt(6, mesa.getId());
            
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
    
    public boolean deletarPorCliente(int clienteId) {
        String sql = "DELETE FROM registros_financeiros WHERE cliente_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, clienteId);
            stmt.executeUpdate(); // Executa mesmo se não houver registros
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public Mesa buscarPorId(int id) {
        String sql = "SELECT * FROM registros_financeiros WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Mesa mesa = new Mesa();
                mesa.setId(rs.getInt("id"));
                mesa.setClienteId(rs.getInt("cliente_id"));
                mesa.setNumero(rs.getString("numero"));
                mesa.setData(rs.getDate("data"));
                mesa.setRegistro(rs.getString("registro"));
                mesa.setPago(rs.getBigDecimal("pago"));
                mesa.setDeve(rs.getBigDecimal("deve"));
                return mesa;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
}
