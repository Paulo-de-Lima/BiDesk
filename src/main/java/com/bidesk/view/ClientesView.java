package com.bidesk.view;

import com.bidesk.controller.ClientesController;
import com.bidesk.model.Cliente;
import com.bidesk.model.RegistroFinanceiro;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class ClientesView extends JPanel {
    private JTable clientesTable;
    private JTable registrosTable;
    private DefaultTableModel clientesTableModel;
    private DefaultTableModel registrosTableModel;
    private ClientesController controller;
    private Cliente clienteSelecionado;
    private List<RegistroFinanceiro> registrosFinanceiros;
    
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
        String[] registrosColumns = {"N°", "Data", "Registro", "Pago", "Deve", "ID"};
        registrosTableModel = new DefaultTableModel(registrosColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Permite editar apenas as colunas Pago (3) e Deve (4)
                return column == 3 || column == 4;
            }
        };
        registrosTable = new JTable(registrosTableModel);
        registrosTable.setRowHeight(35);
        registrosTable.getTableHeader().setBackground(new Color(200, 230, 200));
        registrosTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        registrosTable.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Ocultar coluna ID
        registrosTable.getColumnModel().getColumn(5).setMinWidth(0);
        registrosTable.getColumnModel().getColumn(5).setMaxWidth(0);
        registrosTable.getColumnModel().getColumn(5).setWidth(0);
        
        // Listener para salvar edições de Pago e Deve
        registrosTable.getModel().addTableModelListener(e -> {
            if (e.getType() == javax.swing.event.TableModelEvent.UPDATE) {
                int row = e.getFirstRow();
                int column = e.getColumn();
                if (row >= 0 && (column == 3 || column == 4)) {
                    salvarEdicaoRegistro(row, column);
                }
            }
        });
        
        registrosFinanceiros = new ArrayList<>();
        
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
        btnAdicionar.setBackground(new Color(51, 171, 118)); // Mesma cor verde da logo
        btnAdicionar.setForeground(Color.WHITE);
        btnAdicionar.setFont(new Font("Arial", Font.PLAIN, 14));
        btnAdicionar.setPreferredSize(new Dimension(0, 50));
        btnAdicionar.setOpaque(true);
        btnAdicionar.setBorderPainted(false);
        btnAdicionar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAdicionar.addActionListener(e -> mostrarDialogoAdicionarCliente());
        
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
        registrosFinanceiros.clear();
        if (clienteSelecionado != null) {
            java.util.List<RegistroFinanceiro> registros = controller.listarRegistrosPorCliente(clienteSelecionado.getId());
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            
            for (RegistroFinanceiro registro : registros) {
                registrosFinanceiros.add(registro);
                Object[] row = {
                    registro.getNumero() != null ? registro.getNumero() : "---",
                    registro.getData() != null ? sdf.format(registro.getData()) : "---",
                    registro.getRegistro() != null ? registro.getRegistro() : "---",
                    registro.getPago() != null ? String.format("%.2f", registro.getPago()) : "0.00",
                    registro.getDeve() != null ? String.format("%.2f", registro.getDeve()) : "0.00",
                    registro.getId()
                };
                registrosTableModel.addRow(row);
            }
        }
    }
    
    private void salvarEdicaoRegistro(int row, int column) {
        if (row < registrosFinanceiros.size()) {
            RegistroFinanceiro registro = registrosFinanceiros.get(row);
            try {
                Object valorObj = registrosTableModel.getValueAt(row, column);
                String valorStr = valorObj != null ? valorObj.toString() : "0";
                // Remove espaços e substitui vírgula por ponto
                valorStr = valorStr.trim().replace(",", ".").replace(" ", "");
                
                if (valorStr.isEmpty() || valorStr.equals("---")) {
                    valorStr = "0";
                }
                
                BigDecimal valor = new BigDecimal(valorStr);
                
                if (column == 3) { // Pago
                    registro.setPago(valor);
                } else if (column == 4) { // Deve
                    registro.setDeve(valor);
                }
                
                if (controller.atualizarRegistroFinanceiro(registro)) {
                    // Atualiza o valor formatado na tabela
                    registrosTableModel.setValueAt(String.format("%.2f", valor), row, column);
                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao salvar alteração!", "Erro", JOptionPane.ERROR_MESSAGE);
                    carregarRegistrosFinanceiros(); // Recarrega para reverter
                }
            } catch (NumberFormatException | ClassCastException e) {
                JOptionPane.showMessageDialog(this, "Valor inválido! Use números decimais.", "Erro", JOptionPane.ERROR_MESSAGE);
                carregarRegistrosFinanceiros(); // Recarrega para reverter
            }
        }
    }
    
    private void mostrarDialogoAdicionarCliente() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Adicionar Cliente", true);
        dialog.setSize(550, 550);
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
        gbc.insets = new Insets(15, 0, 15, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        gbc.gridx = 0; gbc.gridy = 0;
        PlaceholderTextField txtNome = new PlaceholderTextField("Nome");
        txtNome.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtNome.setPreferredSize(new Dimension(0, 40));
        txtNome.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        formPanel.add(txtNome, gbc);
        
        gbc.gridy = 1;
        PlaceholderTextField txtEndereco = new PlaceholderTextField("Endereço");
        txtEndereco.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtEndereco.setPreferredSize(new Dimension(0, 40));
        txtEndereco.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        formPanel.add(txtEndereco, gbc);
        
        gbc.gridy = 2;
        PlaceholderTextField txtCidade = new PlaceholderTextField("Cidade");
        txtCidade.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtCidade.setPreferredSize(new Dimension(0, 40));
        txtCidade.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        formPanel.add(txtCidade, gbc);
        
        gbc.gridy = 3;
        PlaceholderTextField txtNumero = new PlaceholderTextField("Número");
        txtNumero.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtNumero.setPreferredSize(new Dimension(0, 40));
        txtNumero.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        formPanel.add(txtNumero, gbc);
        
        gbc.gridy = 4;
        PlaceholderTextField txtData = new PlaceholderTextField("Data (dd/MM/yyyy)");
        txtData.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtData.setPreferredSize(new Dimension(0, 40));
        txtData.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        formPanel.add(txtData, gbc);
        
        gbc.gridy = 5;
        PlaceholderTextField txtRegistro = new PlaceholderTextField("Registro");
        txtRegistro.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtRegistro.setPreferredSize(new Dimension(0, 40));
        txtRegistro.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        formPanel.add(txtRegistro, gbc);
        
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
            String nome = txtNome.getText().trim();
            String endereco = txtEndereco.getText().trim();
            String cidade = txtCidade.getText().trim();
            String numero = txtNumero.getText().trim();
            String dataStr = txtData.getText().trim();
            String registro = txtRegistro.getText().trim();
            
            if (nome.isEmpty() || endereco.isEmpty() || cidade.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Por favor, preencha Nome, Endereço e Cidade!", "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            Date data = null;
            if (!dataStr.isEmpty()) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    java.util.Date utilDate = sdf.parse(dataStr);
                    data = new Date(utilDate.getTime());
                } catch (ParseException ex) {
                    JOptionPane.showMessageDialog(dialog, "Data inválida! Use o formato dd/MM/yyyy", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else {
                data = new Date(System.currentTimeMillis()); // Data atual se não informada
            }
            
            if (controller.inserirComRegistro(nome, endereco, cidade, numero, data, registro)) {
                JOptionPane.showMessageDialog(dialog, "Cliente adicionado com sucesso!");
                dialog.dispose();
                carregarDados();
            } else {
                JOptionPane.showMessageDialog(dialog, "Erro ao adicionar cliente!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        buttonPanel.add(btnCancelar);
        buttonPanel.add(btnSalvar);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        dialog.add(mainPanel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }
}

