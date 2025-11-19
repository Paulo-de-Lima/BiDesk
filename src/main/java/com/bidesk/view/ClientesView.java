package com.bidesk.view;

import com.bidesk.controller.ClientesController;
import com.bidesk.model.Cliente;
import com.bidesk.model.Mesa;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientesView extends JPanel {
    private JTable tabelaMesclada;
    private DefaultTableModel tableModel;
    private ClientesController controller;
    private Map<Integer, Cliente> clientesMap;
    private List<LinhaTabela> linhasTabela;
    
    private class LinhaTabela {
        Cliente cliente;
        Mesa mesa;
        boolean isPrimeiraLinha;
        
        LinhaTabela(Cliente cliente, Mesa mesa, boolean isPrimeiraLinha) {
            this.cliente = cliente;
            this.mesa = mesa;
            this.isPrimeiraLinha = isPrimeiraLinha;
        }
    }
    
    public ClientesView() {
        controller = new ClientesController();
        clientesMap = new HashMap<>();
        linhasTabela = new ArrayList<>();
        initializeComponents();
        setupLayout();
    }
    
    private void initializeComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        
        JLabel titleLabel = new JLabel("Clientes e Mesas");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titlePanel.add(titleLabel, BorderLayout.WEST);
        
        String[] columns = {"Nome", "Endereço", "Cidade", "Nº Mesa", "Data", "Registro", "Pago", "Deve", "Ações", "ClienteID", "MesaID"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6 || column == 7;
            }
        };
        
        tabelaMesclada = new JTable(tableModel);
        tabelaMesclada.setRowHeight(50);
        tabelaMesclada.getTableHeader().setBackground(new Color(200, 230, 200));
        tabelaMesclada.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tabelaMesclada.setFont(new Font("Arial", Font.PLAIN, 13));
        
        // Ocultar colunas de ID
        tabelaMesclada.getColumnModel().getColumn(9).setMinWidth(0);
        tabelaMesclada.getColumnModel().getColumn(9).setMaxWidth(0);
        tabelaMesclada.getColumnModel().getColumn(10).setMinWidth(0);
        tabelaMesclada.getColumnModel().getColumn(10).setMaxWidth(0);
        
        // Ajustar larguras
        tabelaMesclada.getColumnModel().getColumn(0).setPreferredWidth(150);
        tabelaMesclada.getColumnModel().getColumn(1).setPreferredWidth(150);
        tabelaMesclada.getColumnModel().getColumn(2).setPreferredWidth(100);
        tabelaMesclada.getColumnModel().getColumn(3).setPreferredWidth(80);
        tabelaMesclada.getColumnModel().getColumn(4).setPreferredWidth(100);
        tabelaMesclada.getColumnModel().getColumn(5).setPreferredWidth(100);
        tabelaMesclada.getColumnModel().getColumn(6).setPreferredWidth(80);
        tabelaMesclada.getColumnModel().getColumn(7).setPreferredWidth(80);
        tabelaMesclada.getColumnModel().getColumn(8).setPreferredWidth(180);
        
        // Centralizar cabeçalhos
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 3; i < 8; i++) {
            tabelaMesclada.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        // Renderer para coluna de ações
        tabelaMesclada.getColumnModel().getColumn(8).setCellRenderer(new AcoesRenderer());
        tabelaMesclada.getColumnModel().getColumn(8).setCellEditor(new AcoesEditor());
        
        // Listener para salvar edições
        tableModel.addTableModelListener(e -> {
            if (e.getType() == javax.swing.event.TableModelEvent.UPDATE) {
                int row = e.getFirstRow();
                int column = e.getColumn();
                if (row >= 0 && (column == 6 || column == 7)) {
                    salvarEdicaoMesa(row, column);
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tabelaMesclada);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JPanel actionPanel = new JPanel(new BorderLayout());
        actionPanel.setBackground(Color.WHITE);
        actionPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        
        JButton btnAdicionarCliente = new JButton("+ Adicionar Cliente");
        btnAdicionarCliente.setBackground(new Color(51, 171, 118));
        btnAdicionarCliente.setForeground(Color.WHITE);
        btnAdicionarCliente.setFont(new Font("Arial", Font.PLAIN, 14));
        btnAdicionarCliente.setPreferredSize(new Dimension(200, 50));
        btnAdicionarCliente.setOpaque(true);
        btnAdicionarCliente.setBorderPainted(false);
        btnAdicionarCliente.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAdicionarCliente.addActionListener(e -> mostrarDialogoAdicionarCliente());
        
        btnAdicionarCliente.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnAdicionarCliente.setBackground(new Color(39, 140, 98));
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnAdicionarCliente.setBackground(new Color(51, 171, 118));
            }
        });
        
        actionPanel.add(btnAdicionarCliente, BorderLayout.CENTER);
        
        add(titlePanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(actionPanel, BorderLayout.SOUTH);
    }
    
    private class AcoesRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
            panel.setBackground(isSelected ? table.getSelectionBackground() : Color.WHITE);
            
            if (row < linhasTabela.size()) {
                LinhaTabela linhaTabela = linhasTabela.get(row);
                
                if (linhaTabela.isPrimeiraLinha) {
                    JLabel lblEditar = new JLabel("Editar");
                    lblEditar.setFont(new Font("Arial", Font.PLAIN, 12));
                    lblEditar.setForeground(new Color(0, 100, 200));
                    
                    JLabel lblExcluir = new JLabel("Excluir");
                    lblExcluir.setFont(new Font("Arial", Font.PLAIN, 12));
                    lblExcluir.setForeground(Color.RED);
                    
                    panel.add(lblEditar);
                    panel.add(new JLabel("|"));
                    panel.add(lblExcluir);
                }
            }
            
            return panel;
        }
    }
    
    private class AcoesEditor extends AbstractCellEditor implements javax.swing.table.TableCellEditor {
        private JPanel panel;
        private int currentRow;
        
        public AcoesEditor() {
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        }
        
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            panel.removeAll();
            panel.setBackground(Color.WHITE);
            currentRow = row;
            
            if (row < linhasTabela.size()) {
                LinhaTabela linhaTabela = linhasTabela.get(row);
                
                if (linhaTabela.isPrimeiraLinha) {
                    JButton btnEditar = new JButton("Editar");
                    btnEditar.setFont(new Font("Arial", Font.PLAIN, 12));
                    btnEditar.setForeground(new Color(0, 100, 200));
                    btnEditar.setBorderPainted(false);
                    btnEditar.setContentAreaFilled(false);
                    btnEditar.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    btnEditar.addActionListener(e -> {
                        fireEditingStopped();
                        editarCliente(linhaTabela.cliente);
                    });
                    
                    JButton btnExcluir = new JButton("Excluir");
                    btnExcluir.setFont(new Font("Arial", Font.PLAIN, 12));
                    btnExcluir.setForeground(Color.RED);
                    btnExcluir.setBorderPainted(false);
                    btnExcluir.setContentAreaFilled(false);
                    btnExcluir.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    btnExcluir.addActionListener(e -> {
                        fireEditingStopped();
                        excluirCliente(linhaTabela.cliente);
                    });
                    
                    panel.add(btnEditar);
                    panel.add(new JLabel("|"));
                    panel.add(btnExcluir);
                }
            }
            
            return panel;
        }
        
        @Override
        public Object getCellEditorValue() {
            return "";
        }
    }
    
    private void setupLayout() {
    }
    
    public void carregarDados() {
        tableModel.setRowCount(0);
        linhasTabela.clear();
        clientesMap.clear();
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        
        try {
            List<Cliente> clientes = controller.listarTodos();
            System.out.println("Clientes carregados: " + (clientes != null ? clientes.size() : 0));
            
            if (clientes == null || clientes.isEmpty()) {
                System.out.println("Nenhum cliente encontrado no banco de dados.");
                return;
            }
            
            for (Cliente cliente : clientes) {
                clientesMap.put(cliente.getId(), cliente);
                List<Mesa> mesas = controller.listarMesasPorCliente(cliente.getId());
                System.out.println("Cliente: " + cliente.getNome() + " - Mesas: " + (mesas != null ? mesas.size() : 0));
                
                if (mesas == null || mesas.isEmpty()) {
                    LinhaTabela linha = new LinhaTabela(cliente, null, true);
                    linhasTabela.add(linha);
                    
                    Object[] row = {
                        cliente.getNome(),
                        cliente.getEndereco(),
                        cliente.getCidade(),
                        "---",
                        "---",
                        "---",
                        "0.00",
                        "0.00",
                        "",
                        cliente.getId(),
                        null
                    };
                    tableModel.addRow(row);
                } else {
                    boolean primeiraLinha = true;
                    for (Mesa mesa : mesas) {
                        LinhaTabela linha = new LinhaTabela(cliente, mesa, primeiraLinha);
                        linhasTabela.add(linha);
                        
                        Object[] row = {
                            primeiraLinha ? cliente.getNome() : "",
                            primeiraLinha ? cliente.getEndereco() : "",
                            primeiraLinha ? cliente.getCidade() : "",
                            mesa.getNumero() != null ? mesa.getNumero() : "---",
                            mesa.getData() != null ? sdf.format(mesa.getData()) : "---",
                            mesa.getRegistro() != null ? mesa.getRegistro() : "---",
                            mesa.getPago() != null ? String.format("%.2f", mesa.getPago()) : "0.00",
                            mesa.getDeve() != null ? String.format("%.2f", mesa.getDeve()) : "0.00",
                            "",
                            cliente.getId(),
                            mesa.getId()
                        };
                        tableModel.addRow(row);
                        primeiraLinha = false;
                    }
                }
            }
            System.out.println("Dados carregados com sucesso. Total de linhas: " + linhasTabela.size());
        } catch (Exception e) {
            System.err.println("Erro ao carregar dados: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void salvarEdicaoMesa(int row, int column) {
        if (row < linhasTabela.size()) {
            LinhaTabela linhaTabela = linhasTabela.get(row);
            if (linhaTabela.mesa != null) {
                try {
                    Object valorObj = tableModel.getValueAt(row, column);
                    String valorStr = valorObj != null ? valorObj.toString() : "0";
                    valorStr = valorStr.trim().replace(",", ".").replace(" ", "");
                    
                    if (valorStr.isEmpty() || valorStr.equals("---")) {
                        valorStr = "0";
                    }
                    
                    BigDecimal valor = new BigDecimal(valorStr);
                    
                    if (column == 6) {
                        linhaTabela.mesa.setPago(valor);
                    } else if (column == 7) {
                        linhaTabela.mesa.setDeve(valor);
                    }
                    
                    if (controller.atualizarMesa(linhaTabela.mesa)) {
                        tableModel.setValueAt(String.format("%.2f", valor), row, column);
                    } else {
                        JOptionPane.showMessageDialog(this, "Erro ao salvar alteração!", "Erro", JOptionPane.ERROR_MESSAGE);
                        carregarDados();
                    }
                } catch (NumberFormatException | ClassCastException e) {
                    JOptionPane.showMessageDialog(this, "Valor inválido! Use números decimais.", "Erro", JOptionPane.ERROR_MESSAGE);
                    carregarDados();
                }
            }
        }
    }
    
    private void mostrarDialogoAdicionarCliente() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Adicionar Cliente", true);
        dialog.setSize(550, 550);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(Color.WHITE);
        
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
        PlaceholderTextField txtNumero = new PlaceholderTextField("Número da Mesa");
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
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
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
                data = new Date(System.currentTimeMillis());
            }
            
            if (controller.inserirComMesa(nome, endereco, cidade, numero, data, registro)) {
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
    
    private void editarCliente(Cliente cliente) {
        System.out.println("Editando cliente ID: " + cliente.getId() + " - " + cliente.getNome());
        
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Editar Cliente", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(Color.WHITE);
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1;
        JTextField txtNome = new JTextField(cliente.getNome(), 20);
        formPanel.add(txtNome, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Endereço:"), gbc);
        gbc.gridx = 1;
        JTextField txtEndereco = new JTextField(cliente.getEndereco(), 20);
        formPanel.add(txtEndereco, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Cidade:"), gbc);
        gbc.gridx = 1;
        JTextField txtCidade = new JTextField(cliente.getCidade(), 20);
        formPanel.add(txtCidade, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.setBackground(new Color(51, 171, 118));
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.setPreferredSize(new Dimension(100, 35));
        btnSalvar.setBorderPainted(false);
        btnSalvar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(new Color(232, 236, 240));
        btnCancelar.setPreferredSize(new Dimension(100, 35));
        btnCancelar.setBorderPainted(false);
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnSalvar.addActionListener(e -> {
            String novoNome = txtNome.getText().trim();
            String novoEndereco = txtEndereco.getText().trim();
            String novaCidade = txtCidade.getText().trim();
            
            if (novoNome.isEmpty() || novoEndereco.isEmpty() || novaCidade.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Todos os campos são obrigatórios!", "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            cliente.setNome(novoNome);
            cliente.setEndereco(novoEndereco);
            cliente.setCidade(novaCidade);
            
            System.out.println("Tentando atualizar cliente: " + cliente.getId());
            boolean sucesso = controller.atualizar(cliente);
            System.out.println("Resultado da atualização: " + sucesso);
            
            if (sucesso) {
                JOptionPane.showMessageDialog(dialog, "Cliente atualizado com sucesso!");
                dialog.dispose();
                carregarDados();
            } else {
                JOptionPane.showMessageDialog(dialog, "Erro ao atualizar cliente!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        btnCancelar.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(btnCancelar);
        buttonPanel.add(btnSalvar);
        
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    
    private void excluirCliente(Cliente cliente) {
        System.out.println("Excluindo cliente ID: " + cliente.getId() + " - " + cliente.getNome());
        
        int confirm = JOptionPane.showConfirmDialog(this,
                "Deseja realmente excluir o cliente \"" + cliente.getNome() + "\" e todas as suas mesas?",
                "Confirmar exclusão", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            System.out.println("Usuário confirmou a exclusão");
            boolean sucesso = controller.deletar(cliente.getId());
            System.out.println("Resultado da exclusão: " + sucesso);
            
            if (sucesso) {
                JOptionPane.showMessageDialog(this, "Cliente excluído com sucesso!");
                carregarDados();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao excluir cliente!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            System.out.println("Usuário cancelou a exclusão");
        }
    }
}