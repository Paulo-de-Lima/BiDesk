package com.bidesk.ui.panels;

import com.bidesk.database.DatabaseConnection;
import com.bidesk.models.Despesa;

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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class FinanceiroPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private List<Despesa> despesas;
    
    public FinanceiroPanel() {
        initializeComponents();
        setupLayout();
        loadData();
    }
    
    private void initializeComponents() {
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        
        // Título
        JLabel titleLabel = new JLabel("Aba Financeiro");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        add(titleLabel, BorderLayout.NORTH);
        
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
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(144, 238, 144));
        table.getTableHeader().setForeground(Color.BLACK);
        
        // Configurar renderizador para valores monetários
        table.getColumnModel().getColumn(2).setCellRenderer(new CurrencyRenderer());
        table.getColumnModel().getColumn(3).setCellRenderer(new CurrencyRenderer());
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        add(scrollPane, BorderLayout.CENTER);
        
        // Botão adicionar
        JButton addButton = new JButton("+ Adicionar nova cobrança");
        addButton.setBackground(new Color(144, 238, 144));
        addButton.setForeground(Color.BLACK);
        addButton.setFont(new Font("Arial", Font.BOLD, 14));
        addButton.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        addButton.setFocusPainted(false);
        addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddDespesaDialog();
            }
        });
        
        add(addButton, BorderLayout.SOUTH);
    }
    
    private void setupLayout() {
        // Layout já configurado no initializeComponents
    }
    
    private void loadData() {
        despesas = new ArrayList<>();
        tableModel.setRowCount(0);
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM despesas ORDER BY data DESC");
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Despesa despesa = new Despesa(
                    rs.getInt("id"),
                    rs.getString("data"),
                    rs.getString("cidade"),
                    rs.getDouble("despesa"),
                    rs.getDouble("total")
                );
                despesas.add(despesa);
                
                Object[] row = {
                    despesa.getData(),
                    despesa.getCidade(),
                    despesa.getDespesa(),
                    despesa.getTotal()
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados: " + e.getMessage());
        }
    }
    
    private void showAddDespesaDialog() {
        JTextField dataField = new JTextField(20);
        JTextField cidadeField = new JTextField(20);
        JSpinner despesaSpinner = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 999999.99, 0.01));
        JSpinner totalSpinner = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 999999.99, 0.01));
        
        // Configurar formato para os spinners monetários
        JSpinner.NumberEditor despesaEditor = new JSpinner.NumberEditor(despesaSpinner, "0.00");
        despesaSpinner.setEditor(despesaEditor);
        
        JSpinner.NumberEditor totalEditor = new JSpinner.NumberEditor(totalSpinner, "0.00");
        totalSpinner.setEditor(totalEditor);
        
        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
        panel.add(new JLabel("Data (DD/MM/AAAA):"));
        panel.add(dataField);
        panel.add(new JLabel("Cidade:"));
        panel.add(cidadeField);
        panel.add(new JLabel("Despesa:"));
        panel.add(despesaSpinner);
        panel.add(new JLabel("Total:"));
        panel.add(totalSpinner);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Adicionar Despesa", 
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            String data = dataField.getText().trim();
            String cidade = cidadeField.getText().trim();
            double despesa = (Double) despesaSpinner.getValue();
            double total = (Double) totalSpinner.getValue();
            
            if (!data.isEmpty() && !cidade.isEmpty()) {
                addDespesa(data, cidade, despesa, total);
            } else {
                JOptionPane.showMessageDialog(this, "Data e cidade são obrigatórios!");
            }
        }
    }
    
    private void addDespesa(String data, String cidade, double despesa, double total) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "INSERT INTO despesas (data, cidade, despesa, total) VALUES (?, ?, ?, ?)")) {
            
            stmt.setString(1, data);
            stmt.setString(2, cidade);
            stmt.setDouble(3, despesa);
            stmt.setDouble(4, total);
            
            stmt.executeUpdate();
            loadData();
            JOptionPane.showMessageDialog(this, "Despesa adicionada com sucesso!");
            
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao adicionar despesa: " + e.getMessage());
        }
    }
    
    // Renderizador personalizado para valores monetários
    private class CurrencyRenderer extends DefaultTableCellRenderer {
        private DecimalFormat currencyFormat = new DecimalFormat("0.00");
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            if (value instanceof Number) {
                setText(currencyFormat.format(value));
                setHorizontalAlignment(SwingConstants.RIGHT);
            }
            
            return c;
        }
    }
}
