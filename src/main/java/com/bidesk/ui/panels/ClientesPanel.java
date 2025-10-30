package com.bidesk.ui.panels;

import com.bidesk.database.DatabaseConnection;
import com.bidesk.models.Cliente;
import com.bidesk.models.Pedido;

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

public class ClientesPanel extends JPanel {
    private JTable clientesTable;
    private JTable pedidosTable;
    private DefaultTableModel clientesTableModel;
    private DefaultTableModel pedidosTableModel;
    private List<Cliente> clientes;
    private List<Pedido> pedidos;
    private JTextField searchField;
    private JComboBox<String> filterComboBox;
    
    public ClientesPanel() {
        initializeComponents();
        setupLayout();
        loadData();
    }
    
    private void initializeComponents() {
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        
        // Título
        JLabel titleLabel = new JLabel("Aba Clientes");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        add(titleLabel, BorderLayout.NORTH);
        
        // Painel principal com scroll
        JScrollPane mainScrollPane = new JScrollPane();
        mainScrollPane.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        mainScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        mainScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        
        // Tabela de clientes
        String[] clientesColumns = {"Nome", "Endereço", "Cidade"};
        clientesTableModel = new DefaultTableModel(clientesColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        clientesTable = new JTable(clientesTableModel);
        clientesTable.setRowHeight(35);
        clientesTable.setFont(new Font("Arial", Font.PLAIN, 12));
        clientesTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        clientesTable.getTableHeader().setBackground(new Color(144, 238, 144));
        clientesTable.getTableHeader().setForeground(Color.BLACK);
        
        // Adicionar listener para seleção de cliente
        clientesTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadPedidosForSelectedCliente();
            }
        });
        
        JScrollPane clientesScrollPane = new JScrollPane(clientesTable);
        clientesScrollPane.setPreferredSize(new Dimension(0, 150));
        clientesScrollPane.setBorder(BorderFactory.createTitledBorder("Clientes"));
        
        // Separador
        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 10));
        
        // Tabela de pedidos
        String[] pedidosColumns = {"N°", "Data", "Registro", "Pago", "Deve"};
        pedidosTableModel = new DefaultTableModel(pedidosColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        pedidosTable = new JTable(pedidosTableModel);
        pedidosTable.setRowHeight(35);
        pedidosTable.setFont(new Font("Arial", Font.PLAIN, 12));
        pedidosTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        pedidosTable.getTableHeader().setBackground(new Color(144, 238, 144));
        pedidosTable.getTableHeader().setForeground(Color.BLACK);
        
        JScrollPane pedidosScrollPane = new JScrollPane(pedidosTable);
        pedidosScrollPane.setPreferredSize(new Dimension(0, 150));
        pedidosScrollPane.setBorder(BorderFactory.createTitledBorder("Pedidos"));
        
        contentPanel.add(clientesScrollPane);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(separator);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(pedidosScrollPane);
        
        mainScrollPane.setViewportView(contentPanel);
        add(mainScrollPane, BorderLayout.CENTER);
        
        // Painel de controles na parte inferior
        JPanel controlsPanel = new JPanel(new BorderLayout());
        controlsPanel.setBackground(Color.WHITE);
        controlsPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        
        // Painel esquerdo com botões
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setBackground(Color.WHITE);
        
        JButton addClienteBtn = new JButton("+ Adicionar novo cliente");
        addClienteBtn.setBackground(new Color(144, 238, 144));
        addClienteBtn.setForeground(Color.BLACK);
        addClienteBtn.setFont(new Font("Arial", Font.BOLD, 12));
        addClienteBtn.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        addClienteBtn.setFocusPainted(false);
        addClienteBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        addClienteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddClienteDialog();
            }
        });
        
        JButton filterBtn = new JButton("🔍 Filtrar por...");
        filterBtn.setBackground(Color.LIGHT_GRAY);
        filterBtn.setForeground(Color.BLACK);
        filterBtn.setFont(new Font("Arial", Font.PLAIN, 12));
        filterBtn.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        filterBtn.setFocusPainted(false);
        filterBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        filterBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showFilterDialog();
            }
        });
        
        leftPanel.add(addClienteBtn);
        leftPanel.add(filterBtn);
        
        // Painel direito com campo de pesquisa
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setBackground(Color.WHITE);
        
        searchField = new JTextField(20);
        searchField.setFont(new Font("Arial", Font.PLAIN, 12));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        JButton searchBtn = new JButton("🔍");
        searchBtn.setBackground(Color.LIGHT_GRAY);
        searchBtn.setForeground(Color.BLACK);
        searchBtn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        searchBtn.setFocusPainted(false);
        searchBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        searchBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchClientes();
            }
        });
        
        rightPanel.add(new JLabel("Pesquisar: "));
        rightPanel.add(searchField);
        rightPanel.add(searchBtn);
        
        controlsPanel.add(leftPanel, BorderLayout.WEST);
        controlsPanel.add(rightPanel, BorderLayout.EAST);
        
        add(controlsPanel, BorderLayout.SOUTH);
    }
    
    private void setupLayout() {
        // Layout já configurado no initializeComponents
    }
    
    private void loadData() {
        loadClientes();
        loadPedidosForSelectedCliente();
    }
    
    private void loadClientes() {
        clientes = new ArrayList<>();
        clientesTableModel.setRowCount(0);
        
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
                
                Object[] row = {cliente.getNome(), cliente.getEndereco(), cliente.getCidade()};
                clientesTableModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar clientes: " + e.getMessage());
        }
    }
    
    private void loadPedidosForSelectedCliente() {
        pedidos = new ArrayList<>();
        pedidosTableModel.setRowCount(0);
        
        int selectedRow = clientesTable.getSelectedRow();
        if (selectedRow == -1) {
            // Se nenhum cliente estiver selecionado, mostrar todos os pedidos
            loadAllPedidos();
            return;
        }
        
        Cliente selectedCliente = clientes.get(selectedRow);
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "SELECT * FROM pedidos WHERE cliente_id = ? ORDER BY data DESC");
             ) {
            
            stmt.setInt(1, selectedCliente.getId());
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Pedido pedido = new Pedido(
                    rs.getInt("id"),
                    rs.getInt("cliente_id"),
                    rs.getString("numero"),
                    rs.getString("data"),
                    rs.getString("registro"),
                    rs.getDouble("pago"),
                    rs.getDouble("deve")
                );
                pedidos.add(pedido);
                
                String pagoStr = pedido.getPago() > 0 ? String.valueOf(pedido.getPago()) : "---";
                String deveStr = pedido.getDeve() > 0 ? String.valueOf(pedido.getDeve()) : "---";
                
                Object[] row = {pedido.getNumero(), pedido.getData(), pedido.getRegistro(), pagoStr, deveStr};
                pedidosTableModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar pedidos: " + e.getMessage());
        }
    }
    
    private void loadAllPedidos() {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM pedidos ORDER BY data DESC");
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Pedido pedido = new Pedido(
                    rs.getInt("id"),
                    rs.getInt("cliente_id"),
                    rs.getString("numero"),
                    rs.getString("data"),
                    rs.getString("registro"),
                    rs.getDouble("pago"),
                    rs.getDouble("deve")
                );
                pedidos.add(pedido);
                
                String pagoStr = pedido.getPago() > 0 ? String.valueOf(pedido.getPago()) : "---";
                String deveStr = pedido.getDeve() > 0 ? String.valueOf(pedido.getDeve()) : "---";
                
                Object[] row = {pedido.getNumero(), pedido.getData(), pedido.getRegistro(), pagoStr, deveStr};
                pedidosTableModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar pedidos: " + e.getMessage());
        }
    }
    
    private void showAddClienteDialog() {
        JTextField nomeField = new JTextField(20);
        JTextField enderecoField = new JTextField(20);
        JTextField cidadeField = new JTextField(20);
        
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        panel.add(new JLabel("Nome:"));
        panel.add(nomeField);
        panel.add(new JLabel("Endereço:"));
        panel.add(enderecoField);
        panel.add(new JLabel("Cidade:"));
        panel.add(cidadeField);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Adicionar Cliente", 
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            String nome = nomeField.getText().trim();
            String endereco = enderecoField.getText().trim();
            String cidade = cidadeField.getText().trim();
            
            if (!nome.isEmpty() && !endereco.isEmpty() && !cidade.isEmpty()) {
                addCliente(nome, endereco, cidade);
            } else {
                JOptionPane.showMessageDialog(this, "Todos os campos são obrigatórios!");
            }
        }
    }
    
    private void addCliente(String nome, String endereco, String cidade) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "INSERT INTO clientes (nome, endereco, cidade) VALUES (?, ?, ?)")) {
            
            stmt.setString(1, nome);
            stmt.setString(2, endereco);
            stmt.setString(3, cidade);
            
            stmt.executeUpdate();
            loadClientes();
            JOptionPane.showMessageDialog(this, "Cliente adicionado com sucesso!");
            
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao adicionar cliente: " + e.getMessage());
        }
    }
    
    private void showFilterDialog() {
        String[] options = {"Nome", "Cidade", "Endereço"};
        filterComboBox = new JComboBox<>(options);
        
        JPanel panel = new JPanel();
        panel.add(new JLabel("Filtrar por:"));
        panel.add(filterComboBox);
        
        JOptionPane.showMessageDialog(this, panel, "Filtro", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void searchClientes() {
        String searchTerm = searchField.getText().trim().toLowerCase();
        
        if (searchTerm.isEmpty()) {
            loadClientes();
            return;
        }
        
        clientesTableModel.setRowCount(0);
        
        for (Cliente cliente : clientes) {
            if (cliente.getNome().toLowerCase().contains(searchTerm) ||
                cliente.getEndereco().toLowerCase().contains(searchTerm) ||
                cliente.getCidade().toLowerCase().contains(searchTerm)) {
                
                Object[] row = {cliente.getNome(), cliente.getEndereco(), cliente.getCidade()};
                clientesTableModel.addRow(row);
            }
        }
    }
}

