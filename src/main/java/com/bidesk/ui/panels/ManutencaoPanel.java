package com.bidesk.ui.panels;

import com.bidesk.database.DatabaseConnection;
import com.bidesk.models.Cliente;
import com.bidesk.models.Manutencao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ManutencaoPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private List<Manutencao> manutencoes;
    private List<Cliente> clientes;
    
    public ManutencaoPanel() {
        initializeComponents();
        setupLayout();
        loadData();
    }
    
    private void initializeComponents() {
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        
        // Título
        JLabel titleLabel = new JLabel("Aba Manutenção");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        add(titleLabel, BorderLayout.NORTH);
        
        // Tabela
        String[] columns = {"Cliente", "Título", "Descrição"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel);
        table.setRowHeight(40);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(144, 238, 144));
        table.getTableHeader().setForeground(Color.BLACK);
        
        // Configurar largura das colunas
        table.getColumnModel().getColumn(0).setPreferredWidth(150);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);
        table.getColumnModel().getColumn(2).setPreferredWidth(300);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        add(scrollPane, BorderLayout.CENTER);
        
        // Botão adicionar
        JButton addButton = new JButton("+ Adicionar nova manutenção");
        addButton.setBackground(new Color(144, 238, 144));
        addButton.setForeground(Color.BLACK);
        addButton.setFont(new Font("Arial", Font.BOLD, 14));
        addButton.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        addButton.setFocusPainted(false);
        addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddManutencaoDialog();
            }
        });
        
        add(addButton, BorderLayout.SOUTH);
    }
    
    private void setupLayout() {
        // Layout já configurado no initializeComponents
    }
    
    private void loadData() {
        loadClientes();
        loadManutencoes();
    }
    
    private void loadClientes() {
        clientes = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM clientes ORDER BY nome");
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Cliente cliente = new Cliente(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("endereco"),
                    rs.getString("cidade")
                );
                clientes.add(cliente);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar clientes: " + e.getMessage());
        }
    }
    
    private void loadManutencoes() {
        manutencoes = new ArrayList<>();
        tableModel.setRowCount(0);
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "SELECT m.*, c.nome as cliente_nome FROM manutencoes m " +
                 "JOIN clientes c ON m.cliente_id = c.id " +
                 "ORDER BY m.id DESC");
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Manutencao manutencao = new Manutencao(
                    rs.getInt("id"),
                    rs.getInt("cliente_id"),
                    rs.getString("titulo"),
                    rs.getString("descricao"),
                    rs.getString("cliente_nome")
                );
                manutencoes.add(manutencao);
                
                Object[] row = {
                    manutencao.getClienteNome(),
                    manutencao.getTitulo(),
                    manutencao.getDescricao()
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar manutenções: " + e.getMessage());
        }
    }
    
    private void showAddManutencaoDialog() {
        if (clientes.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Não há clientes cadastrados. Adicione um cliente primeiro.");
            return;
        }
        
        // ComboBox para seleção de cliente
        JComboBox<Cliente> clienteComboBox = new JComboBox<>();
        for (Cliente cliente : clientes) {
            clienteComboBox.addItem(cliente);
        }
        
        JTextField tituloField = new JTextField(20);
        JTextArea descricaoArea = new JTextArea(4, 20);
        descricaoArea.setLineWrap(true);
        descricaoArea.setWrapStyleWord(true);
        JScrollPane descricaoScrollPane = new JScrollPane(descricaoArea);
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Cliente:"), gbc);
        gbc.gridx = 1;
        panel.add(clienteComboBox, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Título:"), gbc);
        gbc.gridx = 1;
        panel.add(tituloField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Descrição:"), gbc);
        gbc.gridx = 1;
        panel.add(descricaoScrollPane, gbc);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Adicionar Manutenção", 
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            Cliente selectedCliente = (Cliente) clienteComboBox.getSelectedItem();
            String titulo = tituloField.getText().trim();
            String descricao = descricaoArea.getText().trim();
            
            if (selectedCliente != null && !titulo.isEmpty() && !descricao.isEmpty()) {
                addManutencao(selectedCliente.getId(), titulo, descricao);
            } else {
                JOptionPane.showMessageDialog(this, "Todos os campos são obrigatórios!");
            }
        }
    }
    
    private void addManutencao(int clienteId, String titulo, String descricao) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "INSERT INTO manutencoes (cliente_id, titulo, descricao) VALUES (?, ?, ?)")) {
            
            stmt.setInt(1, clienteId);
            stmt.setString(2, titulo);
            stmt.setString(3, descricao);
            
            stmt.executeUpdate();
            loadManutencoes();
            JOptionPane.showMessageDialog(this, "Manutenção adicionada com sucesso!");
            
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao adicionar manutenção: " + e.getMessage());
        }
    }
}


