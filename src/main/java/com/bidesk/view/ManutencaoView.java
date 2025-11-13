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
        btnAdicionar.setBackground(new Color(200, 230, 200));
        btnAdicionar.setForeground(Color.BLACK);
        btnAdicionar.setFont(new Font("Arial", Font.PLAIN, 14));
        btnAdicionar.setPreferredSize(new Dimension(0, 50));
        btnAdicionar.addActionListener(e -> mostrarDialogoAdicionar());
        
        add(titleLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(btnAdicionar, BorderLayout.SOUTH);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
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
        dialog.setSize(500, 350);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Cliente:"), gbc);
        gbc.gridx = 1;
        JComboBox<String> comboCliente = new JComboBox<>();
        comboCliente.addItem("Selecione um cliente");
        java.util.List<String> clientes = controller.listarNomesClientes();
        for (String nome : clientes) {
            comboCliente.addItem(nome);
        }
        comboCliente.setPreferredSize(new Dimension(250, 25));
        formPanel.add(comboCliente, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Título:"), gbc);
        gbc.gridx = 1;
        JTextField txtTitulo = new JTextField(25);
        formPanel.add(txtTitulo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Descrição:"), gbc);
        gbc.gridx = 1;
        JTextArea txtDescricao = new JTextArea(5, 25);
        txtDescricao.setLineWrap(true);
        txtDescricao.setWrapStyleWord(true);
        JScrollPane scrollDescricao = new JScrollPane(txtDescricao);
        formPanel.add(scrollDescricao, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton btnSalvar = new JButton("Salvar");
        JButton btnCancelar = new JButton("Cancelar");
        
        btnSalvar.addActionListener(e -> {
            int clienteId = comboCliente.getSelectedIndex() > 0 
                ? controller.getIdClientePorNome((String) comboCliente.getSelectedItem()) 
                : null;
            
            if (controller.inserir(clienteId, txtTitulo.getText(), txtDescricao.getText())) {
                JOptionPane.showMessageDialog(dialog, "Manutenção adicionada com sucesso!");
                dialog.dispose();
                carregarDados();
            } else {
                JOptionPane.showMessageDialog(dialog, "Erro ao adicionar manutenção!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        btnCancelar.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(btnSalvar);
        buttonPanel.add(btnCancelar);
        
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
}


