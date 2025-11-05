package com.bidesk.ui.panels;

import com.bidesk.database.DatabaseConnection;
import com.bidesk.models.Material;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EstoquePanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private List<Material> materiais;
    
    public EstoquePanel() {
        initializeComponents();
        setupLayout();
        loadData();
    }
    
    private void initializeComponents() {
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        
        // Título centralizado
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(Color.WHITE);
        JLabel titleLabel = new JLabel("Aba Estoque");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titlePanel.add(titleLabel);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(titlePanel, BorderLayout.NORTH);
        
        // Painel central que preenche toda a área
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        
        // Tabela
        String[] columns = {"Materiais", "Unidade"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel);
        table.setRowHeight(50);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(144, 238, 144));
        table.getTableHeader().setForeground(Color.BLACK);
        
        // Centralizar colunas
        table.getColumnModel().getColumn(0).setCellRenderer(new MaterialCellRenderer());
        table.getColumnModel().getColumn(1).setCellRenderer(new CenterCellRenderer());
        
        // Configurar larguras das colunas para preencher toda a largura
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.getColumnModel().getColumn(0).setPreferredWidth(600);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20));
        
        // Container da tabela que preenche toda a área central
        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBackground(Color.WHITE);
        tableContainer.add(scrollPane, BorderLayout.CENTER);
        
        // Adicionar legenda
        JPanel legendPanel = createLegendPanel();
        tableContainer.add(legendPanel, BorderLayout.SOUTH);
        
        centerPanel.add(tableContainer, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);
        
        // Botão adicionar que preenche a largura
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        
        JButton addButton = new JButton("+ Adicionar novo material");
        addButton.setBackground(new Color(144, 238, 144));
        addButton.setForeground(Color.BLACK);
        addButton.setFont(new Font("Arial", Font.BOLD, 16));
        addButton.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        addButton.setFocusPainted(false);
        addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddMaterialDialog();
            }
        });
        
        buttonPanel.add(addButton, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void setupLayout() {
        // Layout já configurado no initializeComponents
    }
    
    private JPanel createLegendPanel() {
        JPanel legendPanel = new JPanel(new BorderLayout());
        legendPanel.setBackground(Color.WHITE);
        legendPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        // Painel central com os itens da legenda
        JPanel centerLegend = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 0));
        centerLegend.setBackground(Color.WHITE);
        
        // Vermelho - Vazio
        JPanel redPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        redPanel.setBackground(Color.WHITE);
        JLabel redLabel = new JLabel();
        redLabel.setPreferredSize(new Dimension(20, 20));
        redLabel.setOpaque(true);
        redLabel.setBackground(Color.RED);
        redLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        JLabel redText = new JLabel("Vazio");
        redText.setFont(new Font("Arial", Font.BOLD, 13));
        redPanel.add(redLabel);
        redPanel.add(redText);
        
        // Amarelo - Baixo
        JPanel yellowPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        yellowPanel.setBackground(Color.WHITE);
        JLabel yellowLabel = new JLabel();
        yellowLabel.setPreferredSize(new Dimension(20, 20));
        yellowLabel.setOpaque(true);
        yellowLabel.setBackground(Color.YELLOW);
        yellowLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        JLabel yellowText = new JLabel("Baixo");
        yellowText.setFont(new Font("Arial", Font.BOLD, 13));
        yellowPanel.add(yellowLabel);
        yellowPanel.add(yellowText);
        
        // Verde - Alto
        JPanel greenPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        greenPanel.setBackground(Color.WHITE);
        JLabel greenLabel = new JLabel();
        greenLabel.setPreferredSize(new Dimension(20, 20));
        greenLabel.setOpaque(true);
        greenLabel.setBackground(Color.GREEN);
        greenLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        JLabel greenText = new JLabel("Alto");
        greenText.setFont(new Font("Arial", Font.BOLD, 13));
        greenPanel.add(greenLabel);
        greenPanel.add(greenText);
        
        centerLegend.add(redPanel);
        centerLegend.add(yellowPanel);
        centerLegend.add(greenPanel);
        
        legendPanel.add(centerLegend, BorderLayout.CENTER);
        
        return legendPanel;
    }
    
    private void loadData() {
        materiais = new ArrayList<>();
        tableModel.setRowCount(0);
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM materiais ORDER BY nome");
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Material material = new Material(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getInt("quantidade"),
                    rs.getString("status")
                );
                materiais.add(material);
                
                Object[] row = {material, material.getQuantidade()};
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados: " + e.getMessage());
        }
    }
    
    private void showAddMaterialDialog() {
        JTextField nomeField = new JTextField(20);
        JSpinner quantidadeSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 9999, 1));
        
        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
        panel.add(new JLabel("Nome:"));
        panel.add(nomeField);
        panel.add(new JLabel("Quantidade:"));
        panel.add(quantidadeSpinner);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Adicionar Material", 
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            String nome = nomeField.getText().trim();
            int quantidade = (Integer) quantidadeSpinner.getValue();
            
            if (!nome.isEmpty()) {
                addMaterial(nome, quantidade);
            } else {
                JOptionPane.showMessageDialog(this, "Nome do material é obrigatório!");
            }
        }
    }
    
    private void addMaterial(String nome, int quantidade) {
        // Verificar se já existe um material com o mesmo nome
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement("SELECT COUNT(*) FROM materiais WHERE nome = ?")) {
            
            checkStmt.setString(1, nome);
            ResultSet rs = checkStmt.executeQuery();
            
            if (rs.next() && rs.getInt(1) > 0) {
                JOptionPane.showMessageDialog(this, "Já existe um material com este nome!", "Material Duplicado", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao verificar material existente: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Adicionar o material
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "INSERT INTO materiais (nome, quantidade, status) VALUES (?, ?, ?)")) {
            
            String status = getStatusByQuantity(quantidade);
            stmt.setString(1, nome);
            stmt.setInt(2, quantidade);
            stmt.setString(3, status);
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                loadData(); // Recarrega os dados da tabela
                JOptionPane.showMessageDialog(this, "Material '" + nome + "' adicionado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao adicionar material!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao adicionar material: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private String getStatusByQuantity(int quantidade) {
        if (quantidade == 0) return "VAZIO";
        if (quantidade <= 5) return "BAIXO";
        return "ALTO";
    }
    
    // Renderizador personalizado para as células da tabela
    private class MaterialCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            
            if (value instanceof Material) {
                Material material = (Material) value;
                JPanel panel = new JPanel(new BorderLayout(20, 0));
                panel.setOpaque(true);
                panel.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
                
                // Status indicator à esquerda
                JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                statusPanel.setOpaque(false);
                JLabel statusLabel = new JLabel();
                statusLabel.setPreferredSize(new Dimension(25, 25));
                statusLabel.setOpaque(true);
                statusLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                
                switch (material.getStatus()) {
                    case "VAZIO":
                        statusLabel.setBackground(Color.RED);
                        break;
                    case "BAIXO":
                        statusLabel.setBackground(Color.YELLOW);
                        break;
                    case "ALTO":
                        statusLabel.setBackground(Color.GREEN);
                        break;
                }
                statusPanel.add(statusLabel);
                
                // Nome do material centralizado
                JPanel nomePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                nomePanel.setOpaque(false);
                JLabel nomeLabel = new JLabel(material.getNome());
                nomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
                nomePanel.add(nomeLabel);
                
                // Painel com controles à direita
                JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
                controlsPanel.setOpaque(false);
                
                JButton deleteBtn = new JButton("🗑");
                deleteBtn.setPreferredSize(new Dimension(35, 35));
                deleteBtn.setFont(new Font("Arial", Font.PLAIN, 14));
                deleteBtn.setFocusPainted(false);
                deleteBtn.setBackground(Color.WHITE);
                deleteBtn.setForeground(Color.BLACK);
                deleteBtn.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
                deleteBtn.addActionListener(e -> deleteMaterial(material.getId()));
                
                JButton minusBtn = new JButton("-");
                minusBtn.setPreferredSize(new Dimension(35, 35));
                minusBtn.setFont(new Font("Arial", Font.BOLD, 18));
                minusBtn.setFocusPainted(false);
                minusBtn.setBackground(Color.WHITE);
                minusBtn.setForeground(Color.BLACK);
                minusBtn.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
                minusBtn.addActionListener(e -> updateQuantity(material.getId(), material.getQuantidade() - 1));
                
                JButton plusBtn = new JButton("+");
                plusBtn.setPreferredSize(new Dimension(35, 35));
                plusBtn.setFont(new Font("Arial", Font.BOLD, 18));
                plusBtn.setFocusPainted(false);
                plusBtn.setBackground(Color.WHITE);
                plusBtn.setForeground(Color.BLACK);
                plusBtn.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
                plusBtn.addActionListener(e -> updateQuantity(material.getId(), material.getQuantidade() + 1));
                
                controlsPanel.add(deleteBtn);
                controlsPanel.add(minusBtn);
                controlsPanel.add(plusBtn);
                
                panel.add(statusPanel, BorderLayout.WEST);
                panel.add(nomePanel, BorderLayout.CENTER);
                panel.add(controlsPanel, BorderLayout.EAST);
                
                if (isSelected) {
                    panel.setBackground(table.getSelectionBackground());
                } else {
                    panel.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 248, 248));
                }
                
                return panel;
            }
            
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
    }
    
    // Renderizador para centralizar texto
    private class CenterCellRenderer extends DefaultTableCellRenderer {
        public CenterCellRenderer() {
            setHorizontalAlignment(SwingConstants.CENTER);
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            if (value instanceof Integer) {
                JLabel label = new JLabel(String.valueOf(value));
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setFont(new Font("Arial", Font.BOLD, 16));
                label.setOpaque(true);
                
                if (isSelected) {
                    label.setBackground(table.getSelectionBackground());
                } else {
                    label.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 248, 248));
                }
                
                return label;
            }
            
            return c;
        }
    }
    
    private void deleteMaterial(int id) {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Tem certeza que deseja excluir este material?\n\nEsta ação não pode ser desfeita.", 
            "Confirmar Exclusão", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement("DELETE FROM materiais WHERE id = ?")) {
                
                stmt.setInt(1, id);
                int rowsAffected = stmt.executeUpdate();
                
                if (rowsAffected > 0) {
                    loadData(); // Recarrega os dados da tabela
                    JOptionPane.showMessageDialog(this, "Material excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Material não encontrado!", "Erro", JOptionPane.ERROR_MESSAGE);
                }
                
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao excluir material: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void updateQuantity(int id, int newQuantity) {
        if (newQuantity < 0) {
            JOptionPane.showMessageDialog(this, "A quantidade não pode ser negativa!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "UPDATE materiais SET quantidade = ?, status = ? WHERE id = ?")) {
            
            String status = getStatusByQuantity(newQuantity);
            stmt.setInt(1, newQuantity);
            stmt.setString(2, status);
            stmt.setInt(3, id);
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                loadData(); // Recarrega os dados da tabela
                // Opcional: mostrar mensagem de sucesso
                // JOptionPane.showMessageDialog(this, "Quantidade atualizada com sucesso!");
            } else {
                JOptionPane.showMessageDialog(this, "Material não encontrado!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao atualizar quantidade: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
