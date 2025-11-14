package com.bidesk.view;

import com.bidesk.controller.FinanceiroController;
import com.bidesk.model.Despesa;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;

public class FinanceiroView extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private FinanceiroController controller;
    
    public FinanceiroView() {
        controller = new FinanceiroController();
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
        JLabel titleLabel = new JLabel("Financeiro");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        // Tabela
        String[] columns = {"Data", "Cidade", "Despesa", "Total"};
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
        table.getColumnModel().getColumn(0).setPreferredWidth(200);
        table.getColumnModel().getColumn(1).setPreferredWidth(300);
        table.getColumnModel().getColumn(2).setPreferredWidth(200);
        table.getColumnModel().getColumn(3).setPreferredWidth(200);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        // Botão adicionar
        JButton btnAdicionar = new JButton("+ Adicionar nova cobrança");
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
        java.util.List<Despesa> despesas = controller.listarTodos();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        
        for (Despesa despesa : despesas) {
            Object[] row = {
                despesa.getData() != null ? sdf.format(despesa.getData()) : "---",
                despesa.getCidade() != null ? despesa.getCidade() : "---",
                despesa.getDespesa() != null ? String.format("%.2f", despesa.getDespesa()) : "---",
                despesa.getTotal() != null ? String.format("%.2f", despesa.getTotal()) : "---"
            };
            tableModel.addRow(row);
        }
    }
    
    private void mostrarDialogoAdicionar() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Adicionar Cobrança", true);
        dialog.setSize(600, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(Color.WHITE);
        dialog.setResizable(false);
        
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
        gbc.gridx = 0;
        
        gbc.gridy = 0;
        PlaceholderTextField txtData = new PlaceholderTextField("Data (dd/MM/yyyy)");
        txtData.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtData.setMinimumSize(new Dimension(400, 40));
        txtData.setPreferredSize(new Dimension(400, 40));
        txtData.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtData.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        formPanel.add(txtData, gbc);
        
        gbc.gridy = 1;
        PlaceholderTextField txtCidade = new PlaceholderTextField("Cidade");
        txtCidade.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtCidade.setMinimumSize(new Dimension(400, 40));
        txtCidade.setPreferredSize(new Dimension(400, 40));
        txtCidade.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtCidade.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        formPanel.add(txtCidade, gbc);
        
        gbc.gridy = 2;
        PlaceholderTextField txtDespesa = new PlaceholderTextField("Despesa");
        txtDespesa.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtDespesa.setMinimumSize(new Dimension(400, 40));
        txtDespesa.setPreferredSize(new Dimension(400, 40));
        txtDespesa.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtDespesa.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        formPanel.add(txtDespesa, gbc);
        
        gbc.gridy = 3;
        PlaceholderTextField txtTotal = new PlaceholderTextField("Total");
        txtTotal.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtTotal.setMinimumSize(new Dimension(400, 40));
        txtTotal.setPreferredSize(new Dimension(400, 40));
        txtTotal.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtTotal.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        formPanel.add(txtTotal, gbc);
        
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
            try {
                String dataStr = txtData.getText().trim();
                String cidadeStr = txtCidade.getText().trim();
                String despesaStr = txtDespesa.getText().trim();
                String totalStr = txtTotal.getText().trim();
                
                if (dataStr.isEmpty() || cidadeStr.isEmpty() || despesaStr.isEmpty() || totalStr.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Por favor, preencha todos os campos!", "Atenção", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                java.util.Date data = sdf.parse(dataStr);
                java.sql.Date sqlDate = new java.sql.Date(data.getTime());
                BigDecimal despesa = new BigDecimal(despesaStr.replace(",", "."));
                BigDecimal total = new BigDecimal(totalStr.replace(",", "."));
                
                if (controller.inserir(sqlDate, cidadeStr, despesa, total)) {
                    JOptionPane.showMessageDialog(dialog, "Cobrança adicionada com sucesso!");
                    dialog.dispose();
                    carregarDados();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Erro ao adicionar cobrança!", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Erro ao processar dados: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        buttonPanel.add(btnCancelar);
        buttonPanel.add(btnSalvar);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        dialog.add(mainPanel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

}


