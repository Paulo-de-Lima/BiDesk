package com.bidesk.view;

import com.bidesk.controller.EstoqueController;
import com.bidesk.model.Material;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.AbstractCellEditor;
import java.awt.*;

public class EstoqueView extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private EstoqueController controller;
    private java.util.List<Material> materiaisList;
    
    public EstoqueView() {
        controller = new EstoqueController();
        materiaisList = new java.util.ArrayList<>();
        initializeComponents();
        setupLayout();
    }
    
    private void initializeComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        
        // Título
        JLabel titleLabel = new JLabel("Estoque");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Tabela
        String[] columns = {"Materiais", "Status", "Ações"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(40);
        table.getTableHeader().setBackground(new Color(200, 230, 200));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.setFont(new Font("Arial", Font.PLAIN, 14));
      
        DefaultTableCellRenderer centerRenderer = (DefaultTableCellRenderer)table.getTableHeader().getDefaultRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        // Ajustar largura das colunas
        table.getColumnModel().getColumn(0).setPreferredWidth(400);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        JButton btnAdicionar = new JButton("+ Adicionar novo material");
        btnAdicionar.setBackground(new Color(51, 171, 118)); 
        btnAdicionar.setForeground(Color.WHITE); 
        btnAdicionar.setFont(new Font("Arial", Font.PLAIN, 14));
        btnAdicionar.setPreferredSize(new Dimension(0, 50));
        btnAdicionar.setOpaque(true);
        btnAdicionar.setBorderPainted(false);
        btnAdicionar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAdicionar.addActionListener(e -> mostrarDialogoAdicionar());
        
        // Efeito hover
        btnAdicionar.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnAdicionar.setBackground(new Color(39, 140, 98)); // Verde mais escuro no hover
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnAdicionar.setBackground(new Color(51, 171, 118)); // Volta para cor original
            }
        });
        
        // Legenda
        JPanel legendPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        legendPanel.setBackground(Color.WHITE);
        legendPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JLabel legendLabel = new JLabel("Legenda: ");
        JLabel redLabel = new JLabel("■ Vazio");
        redLabel.setForeground(Color.RED);
        JLabel yellowLabel = new JLabel("■ Baixo");
        yellowLabel.setForeground(new Color(255, 200, 0));
        JLabel greenLabel = new JLabel("■ Alto");
        greenLabel.setForeground(Color.GREEN);
        
        legendPanel.add(legendLabel);
        legendPanel.add(redLabel);
        legendPanel.add(Box.createHorizontalStrut(10));
        legendPanel.add(yellowLabel);
        legendPanel.add(Box.createHorizontalStrut(10));
        legendPanel.add(greenLabel);
        
        add(titleLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(legendPanel, BorderLayout.SOUTH);
        
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(legendPanel, BorderLayout.EAST);
        bottomPanel.add(btnAdicionar, BorderLayout.CENTER);
        bottomPanel.setBackground(Color.WHITE);
        
        add(bottomPanel, BorderLayout.SOUTH);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }
    
    private void setupLayout() {
        // Layout já configurado em initializeComponents
    }
    
    public void carregarDados() {
        tableModel.setRowCount(0);
        materiaisList = controller.listarTodos();
        
        for (Material material : materiaisList) {
            Object[] row = {
                material.getNome(),
                getStatusIcon(material.getStatus()),
                "Ações" // Placeholder - será renderizado pelo renderer
            };
            tableModel.addRow(row);
        }
        
        // Configurar renderer customizado para a coluna de ações
        table.getColumn("Ações").setCellRenderer(new AcoesCellRenderer());
        table.getColumn("Ações").setCellEditor(new AcoesCellEditor());
    }
    
    private String getStatusIcon(Material.StatusMaterial status) {
        switch (status) {
            case VAZIO:
                return "🔴";
            case BAIXO:
                return "🟡";
            case ALTO:
                return "🟢";
            default:
                return "";
        }
    }
    
    // Renderer customizado para a coluna de ações
    private class AcoesCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, 
                boolean hasFocus, int row, int column) {
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            panel.setBackground(isSelected ? table.getSelectionBackground() : Color.WHITE);
            
            if (row < materiaisList.size()) {
                Material material = materiaisList.get(row);
                
                JLabel lblDeletar = new JLabel("🗑");
                lblDeletar.setFont(new Font("Arial", Font.PLAIN, 16));
                
                JLabel lblEditar = new JLabel("✏️");
                lblEditar.setFont(new Font("Arial", Font.PLAIN, 16));
                
                JLabel lblAdicionar = new JLabel("➕");
                lblAdicionar.setFont(new Font("Arial", Font.PLAIN, 16));
                
                panel.add(lblDeletar);
                panel.add(lblEditar);
                panel.add(lblAdicionar);
            }
            
            return panel;
        }
    }
    
    // Editor customizado para a coluna de ações
    private class AcoesCellEditor extends AbstractCellEditor implements TableCellEditor {
        private JPanel panel;
        private Material materialAtual;
        
        public AcoesCellEditor() {
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            panel.setBackground(Color.WHITE);
        }
        
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if (row < materiaisList.size()) {
                materialAtual = materiaisList.get(row);
                panel.removeAll();
                
                JButton btnDeletar = new JButton("🗑");
                btnDeletar.setFont(new Font("Arial", Font.PLAIN, 16));
                btnDeletar.setBorderPainted(false);
                btnDeletar.setContentAreaFilled(false);
                btnDeletar.addActionListener(e -> {
                    fireEditingStopped();
                    deletarMaterial(materialAtual);
                });
                
                JButton btnEditar = new JButton("✏️");
                btnEditar.setFont(new Font("Arial", Font.PLAIN, 16));
                btnEditar.setBorderPainted(false);
                btnEditar.setContentAreaFilled(false);
                btnEditar.addActionListener(e -> {
                    fireEditingStopped();
                    mostrarDialogoEditar(materialAtual);
                });
                
                JButton btnAdicionar = new JButton("➕");
                btnAdicionar.setFont(new Font("Arial", Font.PLAIN, 16));
                btnAdicionar.setBorderPainted(false);
                btnAdicionar.setContentAreaFilled(false);
                btnAdicionar.addActionListener(e -> {
                    fireEditingStopped();
                    adicionarQuantidade(materialAtual);
                });
                
                panel.add(btnDeletar);
                panel.add(btnEditar);
                panel.add(btnAdicionar);
            }
            return panel;
        }
        
        @Override
        public Object getCellEditorValue() {
            return "Ações";
        }
    }
    
    private void mostrarDialogoAdicionar() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                "Adicionar Material", true);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(Color.WHITE);
    
        // Painel principal com padding
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
    
        // Painel do formulário (campos em coluna, igual ao Adicionar Cliente)
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
    
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
    
        // CAMPO NOME (placeholder, igual ao de cliente)
        PlaceholderTextField txtNome = new PlaceholderTextField("Nome");
        txtNome.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtNome.setPreferredSize(new Dimension(0, 40));
        txtNome.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        formPanel.add(txtNome, gbc);
    
        // CAMPO QUANTIDADE (logo abaixo, mesma largura)
        // CAMPO QUANTIDADE (logo abaixo, mesma largura)
        gbc.gridy++;

        JSpinner spinnerQuantidade = new JSpinner(
                new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1) // Começa com 0
        );
        spinnerQuantidade.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        spinnerQuantidade.setPreferredSize(new Dimension(0, 40));

        JComponent editor = spinnerQuantidade.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            JTextField spinnerField = ((JSpinner.DefaultEditor) editor).getTextField();
            spinnerField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                    BorderFactory.createEmptyBorder(8, 12, 8, 12)
            ));
        }

        formPanel.add(spinnerQuantidade, gbc);

    
        formPanel.add(spinnerQuantidade, gbc);
    
        mainPanel.add(formPanel, BorderLayout.CENTER);
    
        // ----- BOTÕES (igual estilo do cliente) -----
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
    
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnCancelar.setBackground(new Color(232, 236, 240));
        btnCancelar.setForeground(new Color(41, 50, 65));
        btnCancelar.setPreferredSize(new Dimension(130, 45));
        btnCancelar.setBorderPainted(false);
        btnCancelar.setOpaque(true);
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancelar.setFocusPainted(false);
        btnCancelar.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCancelar.setBackground(new Color(214, 222, 228));
            }
    
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCancelar.setBackground(new Color(232, 236, 240));
            }
        });
        btnCancelar.addActionListener(e -> dialog.dispose());
    
        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnSalvar.setBackground(new Color(51, 171, 118));
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.setPreferredSize(new Dimension(130, 45));
        btnSalvar.setBorderPainted(false);
        btnSalvar.setOpaque(true);
        btnSalvar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSalvar.setFocusPainted(false);
        btnSalvar.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSalvar.setBackground(new Color(39, 140, 98));
            }
    
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSalvar.setBackground(new Color(51, 171, 118));
            }
        });
    
        btnSalvar.addActionListener(e -> {
            String nome = txtNome.getText().trim();
    
            if (nome.isEmpty()) {
                JOptionPane.showMessageDialog(dialog,
                        "Por favor, preencha o nome do material!",
                        "Validação",
                        JOptionPane.WARNING_MESSAGE);
                txtNome.requestFocus();
                return;
            }
    
            int quantidade = (Integer) spinnerQuantidade.getValue();
            String resultado = controller.inserir(nome, quantidade);
    
            if (resultado == null || resultado.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Material adicionado com sucesso!");
                dialog.dispose();
                carregarDados();
            } else {
                JOptionPane.showMessageDialog(dialog,
                        "Erro ao adicionar material:\n" + resultado,
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    
        buttonPanel.add(btnCancelar);
        buttonPanel.add(btnSalvar);
    
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        dialog.add(mainPanel, BorderLayout.CENTER);
    
        dialog.setVisible(true);
    }
    
    
    private void mostrarDialogoEditar(Material material) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Editar Material", true);
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1;
        JTextField txtNome = new JTextField(material.getNome(), 20);
        formPanel.add(txtNome, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Quantidade:"), gbc);
        gbc.gridx = 1;
        JSpinner spinnerQuantidade = new JSpinner(new SpinnerNumberModel(material.getQuantidade(), 0, Integer.MAX_VALUE, 1));
        formPanel.add(spinnerQuantidade, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton btnSalvar = new JButton("Salvar");
        JButton btnCancelar = new JButton("Cancelar");
        
        btnSalvar.addActionListener(e -> {
            material.setNome(txtNome.getText());
            material.setQuantidade((Integer) spinnerQuantidade.getValue());
            if (controller.atualizar(material)) {
                JOptionPane.showMessageDialog(dialog, "Material atualizado com sucesso!");
                dialog.dispose();
                carregarDados();
            } else {
                JOptionPane.showMessageDialog(dialog, "Erro ao atualizar material!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        btnCancelar.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(btnSalvar);
        buttonPanel.add(btnCancelar);
        
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    
    private void deletarMaterial(Material material) {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Deseja realmente deletar o material " + material.getNome() + "?",
            "Confirmar exclusão", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (controller.deletar(material.getId())) {
                JOptionPane.showMessageDialog(this, "Material deletado com sucesso!");
                carregarDados();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao deletar material!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void adicionarQuantidade(Material material) {
        String input = JOptionPane.showInputDialog(this, 
            "Quantidade a adicionar:", 
            "Adicionar Quantidade", 
            JOptionPane.QUESTION_MESSAGE);
        
        if (input != null && !input.isEmpty()) {
            try {
                int quantidade = Integer.parseInt(input);
                material.setQuantidade(material.getQuantidade() + quantidade);
                if (controller.atualizar(material)) {
                    JOptionPane.showMessageDialog(this, "Quantidade atualizada com sucesso!");
                    carregarDados();
                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao atualizar quantidade!", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Por favor, insira um número válido!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

