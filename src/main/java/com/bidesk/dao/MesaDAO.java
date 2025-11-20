package com.bidesk.dao;

import com.bidesk.database.DatabaseConnection;
import com.bidesk.model.Mesa;
import com.bidesk.model.Mesa.TipoContrato;
import com.bidesk.model.RegistroFinanceiro;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MesaDAO {
    public boolean inserir(Mesa mesa, int clienteId) {
        String sql = "INSERT INTO mesas (numero, tipo_contrato, cliente_id) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, mesa.getNumero());
            stmt.setString(2, mesa.getTipoContrato().name());
            stmt.setInt(3, clienteId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    mesa.setId(generatedKeys.getInt(1));
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Mesa> listarPorCliente(int clienteId) {
        List<Mesa> mesas = new ArrayList<>();
        String sql = "SELECT * FROM mesas WHERE cliente_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, clienteId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Mesa mesa = new Mesa();
                mesa.setId(rs.getInt("id"));
                mesa.setNumero(rs.getString("numero"));
                mesa.setTipoContrato(TipoContrato.valueOf(rs.getString("tipo_contrato")));
                // Load registros financeiros for this mesa if needed
                mesas.add(mesa);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mesas;
    }

    public boolean atualizar(Mesa mesa) {
        String sql = "UPDATE mesas SET numero = ?, tipo_contrato = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, mesa.getNumero());
            stmt.setString(2, mesa.getTipoContrato().name());
            stmt.setInt(3, mesa.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deletar(int id) {
        String sql = "DELETE FROM mesas WHERE id = ?";
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