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
        dialog.setSize(450, 350);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Data:"), gbc);
        gbc.gridx = 1;
        JTextField txtData = new JTextField(20);
        txtData.setToolTipText("Formato: dd/MM/yyyy");
        formPanel.add(txtData, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Cidade:"), gbc);
        gbc.gridx = 1;
        JTextField txtCidade = new JTextField(20);
        formPanel.add(txtCidade, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Despesa:"), gbc);
        gbc.gridx = 1;
        JTextField txtDespesa = new JTextField(20);
        formPanel.add(txtDespesa, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Total:"), gbc);
        gbc.gridx = 1;
        JTextField txtTotal = new JTextField(20);
        formPanel.add(txtTotal, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton btnSalvar = new JButton("Salvar");
        JButton btnCancelar = new JButton("Cancelar");
        
        btnSalvar.addActionListener(e -> {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                java.util.Date data = sdf.parse(txtData.getText());
                java.sql.Date sqlDate = new java.sql.Date(data.getTime());
                BigDecimal despesa = new BigDecimal(txtDespesa.getText().replace(",", "."));
                BigDecimal total = new BigDecimal(txtTotal.getText().replace(",", "."));
                
                if (controller.inserir(sqlDate, txtCidade.getText(), despesa, total)) {
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
        
        btnCancelar.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(btnSalvar);
        buttonPanel.add(btnCancelar);
        
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

}


