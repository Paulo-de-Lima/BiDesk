package com.bidesk.view;

import com.bidesk.controller.ClientesController;
import com.bidesk.model.Cliente;
import com.bidesk.model.RegistroFinanceiro;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;

public class ClientesView extends JPanel {
    private JTable clientesTable;
    private JTable registrosTable;
    private DefaultTableModel clientesTableModel;
    private DefaultTableModel registrosTableModel;
    private ClientesController controller;
    private Cliente clienteSelecionado;
    
    public ClientesView() {
        controller = new ClientesController();
        initializeComponents();
        setupLayout();
    }
    
    private void initializeComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        // Título
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        
        JLabel titleLabel = new JLabel("Aba Clientes");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        
        JLabel subtitleLabel = new JLabel("Matcheka");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        subtitleLabel.setForeground(new Color(150, 150, 150));
        
        titlePanel.add(titleLabel, BorderLayout.WEST);
        titlePanel.add(subtitleLabel, BorderLayout.EAST);
        
        // Painel principal com scroll
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        // Tabela de Clientes
        String[] clientesColumns = {"Nome", "Endereço", "Cidade", "ID"};
        clientesTableModel = new DefaultTableModel(clientesColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        clientesTable = new JTable(clientesTableModel);
        clientesTable.setRowHeight(35);
        clientesTable.getTableHeader().setBackground(new Color(200, 230, 200));
        clientesTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        clientesTable.setFont(new Font("Arial", Font.PLAIN, 14));
        clientesTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = clientesTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int clienteId = (Integer) clientesTableModel.getValueAt(selectedRow, 3);
                    clienteSelecionado = controller.buscarClientePorId(clienteId);
                    carregarRegistrosFinanceiros();
                }
            }
        });
        // Ocultar coluna ID
        clientesTable.getColumnModel().getColumn(3).setMinWidth(0);
        clientesTable.getColumnModel().getColumn(3).setMaxWidth(0);
        clientesTable.getColumnModel().getColumn(3).setWidth(0);
        
        JScrollPane clientesScroll = new JScrollPane(clientesTable);
        clientesScroll.setBorder(BorderFactory.createTitledBorder("Clientes"));
        clientesScroll.setPreferredSize(new Dimension(0, 150));
        
        // Tabela de Registros Financeiros
        String[] registrosColumns = {"N°", "Data", "Registro", "Pago", "Deve"};
        registrosTableModel = new DefaultTableModel(registrosColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        registrosTable = new JTable(registrosTableModel);
        registrosTable.setRowHeight(35);
        registrosTable.getTableHeader().setBackground(new Color(200, 230, 200));
        registrosTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        registrosTable.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JScrollPane registrosScroll = new JScrollPane(registrosTable);
        registrosScroll.setBorder(BorderFactory.createTitledBorder("Registros Financeiros"));
        registrosScroll.setPreferredSize(new Dimension(0, 200));
        
        mainPanel.add(clientesScroll);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(registrosScroll);
        
        // Painel de ações
        JPanel actionPanel = new JPanel(new BorderLayout());
        actionPanel.setBackground(Color.WHITE);
        actionPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        
        JButton btnAdicionar = new JButton("+ Adicionar novo cliente");
        btnAdicionar.setBackground(new Color(200, 230, 200));
        btnAdicionar.setForeground(Color.BLACK);
        btnAdicionar.setFont(new Font("Arial", Font.PLAIN, 14));
        btnAdicionar.setPreferredSize(new Dimension(0, 50));
        btnAdicionar.addActionListener(e -> mostrarDialogoAdicionarCliente());
        
        JButton btnFiltrar = new JButton("Filtrar por...");
        btnFiltrar.setFont(new Font("Arial", Font.PLAIN, 12));
        btnFiltrar.setPreferredSize(new Dimension(150, 40));
        
        JTextField txtPesquisar = new JTextField("Pesquisar...");
        txtPesquisar.setPreferredSize(new Dimension(200, 40));
        txtPesquisar.setFont(new Font("Arial", Font.PLAIN, 12));
        
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.add(btnFiltrar);
        searchPanel.add(txtPesquisar);
        
        actionPanel.add(btnAdicionar, BorderLayout.CENTER);
        actionPanel.add(searchPanel, BorderLayout.EAST);
        
        add(titlePanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(actionPanel, BorderLayout.SOUTH);
    }
    
    private void setupLayout() {
        // Layout já configurado
    }
    
    public void carregarDados() {
        carregarClientes();
        clienteSelecionado = null;
        registrosTableModel.setRowCount(0);
    }
    
    private void carregarClientes() {
        clientesTableModel.setRowCount(0);
        java.util.List<Cliente> clientes = controller.listarTodos();
        
        for (Cliente cliente : clientes) {
            Object[] row = {
                cliente.getNome(),
                cliente.getEndereco(),
                cliente.getCidade(),
                cliente.getId() // Armazenar ID na última coluna
            };
            clientesTableModel.addRow(row);
        }
    }
    
    private void carregarRegistrosFinanceiros() {
        registrosTableModel.setRowCount(0);
        if (clienteSelecionado != null) {
            java.util.List<RegistroFinanceiro> registros = controller.listarRegistrosPorCliente(clienteSelecionado.getId());
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            
            for (RegistroFinanceiro registro : registros) {
                Object[] row = {
                    registro.getNumero() != null ? registro.getNumero() : "---",
                    registro.getData() != null ? sdf.format(registro.getData()) : "---",
                    registro.getRegistro() != null ? registro.getRegistro() : "---",
                    registro.getPago() != null ? String.format("%.2f", registro.getPago()) : "---",
                    registro.getDeve() != null && registro.getDeve().compareTo(BigDecimal.ZERO) > 0 
                        ? String.format("%.2f", registro.getDeve()) : "---"
                };
                registrosTableModel.addRow(row);
            }
        }
    }
    
    private void mostrarDialogoAdicionarCliente() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Adicionar Cliente", true);
        dialog.setSize(500, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1;
        JTextField txtNome = new JTextField(25);
        formPanel.add(txtNome, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Endereço:"), gbc);
        gbc.gridx = 1;
        JTextField txtEndereco = new JTextField(25);
        formPanel.add(txtEndereco, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Cidade:"), gbc);
        gbc.gridx = 1;
        JTextField txtCidade = new JTextField(25);
        formPanel.add(txtCidade, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton btnSalvar = new JButton("Salvar");
        JButton btnCancelar = new JButton("Cancelar");
        
        btnSalvar.addActionListener(e -> {
            if (controller.inserir(txtNome.getText(), txtEndereco.getText(), txtCidade.getText())) {
                JOptionPane.showMessageDialog(dialog, "Cliente adicionado com sucesso!");
                dialog.dispose();
                carregarDados();
            } else {
                JOptionPane.showMessageDialog(dialog, "Erro ao adicionar cliente!", "Erro", JOptionPane.ERROR_MESSAGE);
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

