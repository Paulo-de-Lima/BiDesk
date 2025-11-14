package com.bidesk.view;

import com.bidesk.controller.ManutencaoController;
import com.bidesk.model.Manutencao;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ManutencaoView extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private ManutencaoController controller;
    
    public ManutencaoView() {
        controller = new ManutencaoController();
        initializeComponents();
        setupLayout();
    }
    
    private void initializeComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
     
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Título
        JLabel titleLabel = new JLabel("Aba Manutenção");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
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
        table.getTableHeader().setBackground(new Color(200, 230, 200));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Ajustar largura das colunas
        table.getColumnModel().getColumn(0).setPreferredWidth(200);
        table.getColumnModel().getColumn(1).setPreferredWidth(300);
        table.getColumnModel().getColumn(2).setPreferredWidth(400);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        // Botão adicionar
        JButton btnAdicionar = new JButton("+ Adicionar nova manutenção");
        btnAdicionar.setBackground(new Color(51, 171, 118)); // Mesma cor verde da logo
        btnAdicionar.setForeground(Color.WHITE);
        btnAdicionar.setFont(new Font("Arial", Font.PLAIN, 14));
        btnAdicionar.setPreferredSize(new Dimension(0, 50));
        btnAdicionar.setOpaque(true);
        btnAdicionar.setBorderPainted(false);
        btnAdicionar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAdicionar.addActionListener(e -> mostrarDialogoAdicionar());
        
        add(titleLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(btnAdicionar, BorderLayout.SOUTH);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
   
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
    }

    
    private void setupLayout() {
        // Layout já configurado
    }
    
    public void carregarDados() {
        tableModel.setRowCount(0);
        java.util.List<Manutencao> manutencoes = controller.listarTodos();
        
        for (Manutencao manutencao : manutencoes) {
            String nomeCliente = controller.getNomeCliente(manutencao.getClienteId());
            Object[] row = {
                nomeCliente != null ? nomeCliente : "---",
                manutencao.getTitulo() != null ? manutencao.getTitulo() : "---",
                manutencao.getDescricao() != null ? manutencao.getDescricao() : "---"
            };
            tableModel.addRow(row);
        }
    }
    
    private void mostrarDialogoAdicionar() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Adicionar Manutenção", true);
        dialog.setSize(550, 450);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(Color.WHITE);
        
        // Painel principal com padding
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 0, 20, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        gbc.gridx = 0; gbc.gridy = 0;
        JComboBox<String> comboCliente = new JComboBox<>();
        comboCliente.addItem("Selecione um cliente");
        java.util.List<String> clientes = controller.listarNomesClientes();
        for (String nome : clientes) {
            comboCliente.addItem(nome);
        }
        comboCliente.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        comboCliente.setPreferredSize(new Dimension(0, 40));
        comboCliente.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        formPanel.add(comboCliente, gbc);
        
        gbc.gridy = 1;
        PlaceholderTextField txtTitulo = new PlaceholderTextField("Título");
        txtTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtTitulo.setPreferredSize(new Dimension(0, 40));
        txtTitulo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        formPanel.add(txtTitulo, gbc);
        
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        PlaceholderTextArea txtDescricao = new PlaceholderTextArea("Descrição");
        txtDescricao.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtDescricao.setLineWrap(true);
        txtDescricao.setWrapStyleWord(true);
        txtDescricao.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        JScrollPane scrollDescricao = new JScrollPane(txtDescricao);
        scrollDescricao.setPreferredSize(new Dimension(0, 120));
        formPanel.add(scrollDescricao, gbc);
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        // Painel de botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        // Botão Cancelar
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
        
        // Botão Salvar
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
            if (comboCliente.getSelectedIndex() <= 0) {
                JOptionPane.showMessageDialog(dialog, "Por favor, selecione um cliente!", "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            Integer clienteId = controller.getIdClientePorNome((String) comboCliente.getSelectedItem());
            String titulo = txtTitulo.getText().trim();
            String descricao = txtDescricao.getText().trim();
            
            if (titulo.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Por favor, preencha o título!", "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (controller.inserir(clienteId, titulo, descricao)) {
                JOptionPane.showMessageDialog(dialog, "Manutenção adicionada com sucesso!");
                dialog.dispose();
                carregarDados();
            } else {
                JOptionPane.showMessageDialog(dialog, "Erro ao adicionar manutenção!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        buttonPanel.add(btnCancelar);
        buttonPanel.add(btnSalvar);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        dialog.add(mainPanel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }
}


