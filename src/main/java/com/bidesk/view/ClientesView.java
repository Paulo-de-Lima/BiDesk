package com.bidesk.view;

import com.bidesk.controller.ClientesController;
import com.bidesk.model.Cliente;
import com.bidesk.model.Mesa;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.AbstractCellEditor;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.EventObject;

public class ClientesView extends JPanel {
    // Tabelas
    private JTable tabelaClientes;
    private JTable tabelaMesas;
    private DefaultTableModel clientesTableModel;
    private DefaultTableModel mesasTableModel;
    
    private ClientesController controller;
    private List<Cliente> clientesList;
    private Cliente clienteSelecionado;
    
    // Componentes para alternar entre placeholder e tabela de mesas
    private JPanel mesasContentPanel;
    private CardLayout mesasCardLayout;
    
    // Constantes de Cores
    private static final Color PRIMARY_GREEN = new Color(39, 174, 96);
    private static final Color PRIMARY_BLUE = new Color(41, 128, 185);
    private static final Color HOVER_BLUE = new Color(52, 152, 219);
    private static final Color DANGER_RED = new Color(231, 76, 60);
    private static final Color INFO_BLUE = new Color(52, 152, 219);
    private static final Color LIGHT_GREY = new Color(236, 240, 241);
    
    public ClientesView() {
        controller = new ClientesController();
        clientesList = new ArrayList<>();
        clienteSelecionado = null;
        initializeComponents();
        carregarDados();
    }
    
    // --- Componentes Auxiliares de Estilo ---
    
    private class RoundedButton extends JButton {
        private Color shadowColor = new Color(0, 0, 0, 30);
        private int shadowGap = 4;
        private int cornerRadius = 12;

        public RoundedButton(String text) {
            super(text);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Desenha sombra
            g2.setColor(shadowColor);
            g2.fill(new RoundRectangle2D.Float(shadowGap, shadowGap, 
                getWidth() - shadowGap, getHeight() - shadowGap, 
                cornerRadius, cornerRadius));

            // Desenha botão
            g2.setColor(getBackground());
            g2.fill(new RoundRectangle2D.Float(0, 0, 
                getWidth() - shadowGap, getHeight() - shadowGap, 
                cornerRadius, cornerRadius));

            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static class TableActionButton extends JLabel {
        private Color baseColor;
        private Color hoverColor;

        public TableActionButton(String text, Color color) {
            super(text);
            this.baseColor = color;
            this.hoverColor = color.brighter();
            
            setFont(new Font("Segoe UI", Font.BOLD, 14));
            setForeground(baseColor);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    setForeground(hoverColor);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    setForeground(baseColor);
                }
            });
        }
    }
    
    // --- Inicialização e Layout ---
    
    private void initializeComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Painel do cabeçalho com fundo verde
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(PRIMARY_GREEN);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 15, 20));

        // Título com fundo verde
        JLabel titleLabel = new JLabel("Clientes");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        headerPanel.add(titleLabel);

        // Painel principal com duas tabelas lado a lado
        JPanel tablesPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        tablesPanel.setBackground(Color.WHITE);
        tablesPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // === TABELA DE CLIENTES (ESQUERDA) ===
        JPanel clientesPanel = new JPanel(new BorderLayout());
        clientesPanel.setBackground(Color.WHITE);
        
        JLabel clientesLabel = new JLabel("Clientes");
        clientesLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        clientesLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        clientesPanel.add(clientesLabel, BorderLayout.NORTH);
        
        String[] clientesColumns = {"Nome", "Endereço", "Cidade", "Pago", "Deve", "Ações"};
        clientesTableModel = new DefaultTableModel(clientesColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Apenas coluna de ações é editável
            }
        };
        
        tabelaClientes = new JTable(clientesTableModel);
        tabelaClientes.setRowHeight(45);
        tabelaClientes.getTableHeader().setBackground(LIGHT_GREY);
        tabelaClientes.getTableHeader().setForeground(new Color(41, 50, 65));
        tabelaClientes.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        tabelaClientes.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        tabelaClientes.setShowGrid(false);
        tabelaClientes.setIntercellSpacing(new Dimension(0, 1));
        tabelaClientes.setBackground(Color.WHITE);
        tabelaClientes.setSelectionBackground(LIGHT_GREY.brighter());
        tabelaClientes.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, LIGHT_GREY.darker()));
        
        // Centralizar cabeçalhos
        DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) tabelaClientes.getTableHeader()
                .getDefaultRenderer();
        headerRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        // Ajustar larguras das colunas
        tabelaClientes.getColumnModel().getColumn(0).setPreferredWidth(150);
        tabelaClientes.getColumnModel().getColumn(1).setPreferredWidth(200);
        tabelaClientes.getColumnModel().getColumn(2).setPreferredWidth(150);
        tabelaClientes.getColumnModel().getColumn(3).setPreferredWidth(100);
        tabelaClientes.getColumnModel().getColumn(4).setPreferredWidth(100);
        tabelaClientes.getColumnModel().getColumn(5).setPreferredWidth(150);
        
        // Centralizar colunas numéricas
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tabelaClientes.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        tabelaClientes.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        
        // Renderer e Editor para coluna de Ações
        tabelaClientes.getColumnModel().getColumn(5).setCellRenderer(new AcoesClienteCellRenderer());
        tabelaClientes.getColumnModel().getColumn(5).setCellEditor(new AcoesClienteCellEditor());
        
        // Listener para seleção de cliente
        tabelaClientes.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = tabelaClientes.getSelectedRow();
                if (selectedRow >= 0 && selectedRow < clientesList.size()) {
                    clienteSelecionado = clientesList.get(selectedRow);
                    carregarMesasDoCliente(clienteSelecionado.getId());
                }
            }
        });
        
        // Listener para clique no nome do cliente
        tabelaClientes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tabelaClientes.rowAtPoint(e.getPoint());
                int col = tabelaClientes.columnAtPoint(e.getPoint());
                if (row >= 0 && col == 0) { // Coluna Nome
                    if (row < clientesList.size()) {
                        clienteSelecionado = clientesList.get(row);
                        carregarMesasDoCliente(clienteSelecionado.getId());
                        tabelaClientes.setRowSelectionInterval(row, row);
                    }
                }
            }
        });
        
        JScrollPane clientesScrollPane = new JScrollPane(tabelaClientes);
        clientesScrollPane.setBorder(BorderFactory.createEmptyBorder());
        clientesScrollPane.setBackground(Color.WHITE);
        clientesPanel.add(clientesScrollPane, BorderLayout.CENTER);

        // === TABELA DE MESAS (DIREITA) ===
        JPanel mesasPanel = new JPanel(new BorderLayout());
        mesasPanel.setBackground(Color.WHITE);
        
        JLabel mesasLabel = new JLabel("Mesas");
        mesasLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        mesasLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        mesasPanel.add(mesasLabel, BorderLayout.NORTH);
        
        String[] mesasColumns = {"Nº", "Data", "Registro", "Ações"};
        mesasTableModel = new DefaultTableModel(mesasColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3; // Apenas coluna de ações é editável
            }
        };
        
        tabelaMesas = new JTable(mesasTableModel);
        tabelaMesas.setRowHeight(45);
        tabelaMesas.getTableHeader().setBackground(LIGHT_GREY);
        tabelaMesas.getTableHeader().setForeground(new Color(41, 50, 65));
        tabelaMesas.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        tabelaMesas.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        tabelaMesas.setShowGrid(false);
        tabelaMesas.setIntercellSpacing(new Dimension(0, 1));
        tabelaMesas.setBackground(Color.WHITE);
        tabelaMesas.setSelectionBackground(LIGHT_GREY.brighter());
        tabelaMesas.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, LIGHT_GREY.darker()));
        
        // Centralizar cabeçalhos
        DefaultTableCellRenderer mesasHeaderRenderer = (DefaultTableCellRenderer) tabelaMesas.getTableHeader()
                .getDefaultRenderer();
        mesasHeaderRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        // Ajustar larguras das colunas
        tabelaMesas.getColumnModel().getColumn(0).setPreferredWidth(100);
        tabelaMesas.getColumnModel().getColumn(1).setPreferredWidth(120);
        tabelaMesas.getColumnModel().getColumn(2).setPreferredWidth(150);
        tabelaMesas.getColumnModel().getColumn(3).setPreferredWidth(150);
        
        // Centralizar colunas
        DefaultTableCellRenderer mesasCenterRenderer = new DefaultTableCellRenderer();
        mesasCenterRenderer.setHorizontalAlignment(JLabel.CENTER);
        tabelaMesas.getColumnModel().getColumn(0).setCellRenderer(mesasCenterRenderer);
        tabelaMesas.getColumnModel().getColumn(1).setCellRenderer(mesasCenterRenderer);
        tabelaMesas.getColumnModel().getColumn(2).setCellRenderer(mesasCenterRenderer);
        
        // Renderer e Editor para coluna de Ações
        tabelaMesas.getColumnModel().getColumn(3).setCellRenderer(new AcoesMesaCellRenderer());
        tabelaMesas.getColumnModel().getColumn(3).setCellEditor(new AcoesMesaCellEditor());
        
        JScrollPane mesasScrollPane = new JScrollPane(tabelaMesas);
        mesasScrollPane.setBorder(BorderFactory.createEmptyBorder());
        mesasScrollPane.setBackground(Color.WHITE);
        
        // Mensagem inicial quando nenhum cliente está selecionado
        JLabel placeholderLabel = new JLabel("Selecione o cliente para ver suas mesas.");
        placeholderLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        placeholderLabel.setForeground(new Color(150, 150, 150));
        placeholderLabel.setHorizontalAlignment(JLabel.CENTER);
        placeholderLabel.setBorder(BorderFactory.createEmptyBorder(50, 0, 50, 0));
        
        // Usar CardLayout para alternar entre placeholder e tabela
        CardLayout mesasCardLayout = new CardLayout();
        JPanel mesasContentPanel = new JPanel(mesasCardLayout);
        mesasContentPanel.setBackground(Color.WHITE);
        mesasContentPanel.add(placeholderLabel, "PLACEHOLDER");
        mesasContentPanel.add(mesasScrollPane, "TABLE");
        mesasCardLayout.show(mesasContentPanel, "PLACEHOLDER");
        
        mesasPanel.add(mesasContentPanel, BorderLayout.CENTER);
        
        // Guardar referência para poder alternar depois
        this.mesasContentPanel = mesasContentPanel;
        this.mesasCardLayout = mesasCardLayout;

        tablesPanel.add(clientesPanel);
        tablesPanel.add(mesasPanel);

        // Painel inferior com botões (seguindo padrão do EstoqueView)
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        // Painel interno para os dois botões lado a lado
        JPanel buttonsContainer = new JPanel(new GridLayout(1, 2, 15, 0));
        buttonsContainer.setBackground(Color.WHITE);
        buttonsContainer.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        RoundedButton btnAdicionarCliente = new RoundedButton("+ Adicionar novo cliente");
        btnAdicionarCliente.setBackground(PRIMARY_BLUE);
        btnAdicionarCliente.setForeground(Color.WHITE);
        btnAdicionarCliente.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnAdicionarCliente.setPreferredSize(new Dimension(0, 55));
        btnAdicionarCliente.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAdicionarCliente.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                btnAdicionarCliente.setBackground(HOVER_BLUE);
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                btnAdicionarCliente.setBackground(PRIMARY_BLUE);
            }
        });
        btnAdicionarCliente.addActionListener(e -> mostrarDialogoAdicionarCliente());

        RoundedButton btnAdicionarMesa = new RoundedButton("+ Adicionar mesa a cliente");
        btnAdicionarMesa.setBackground(PRIMARY_GREEN);
        btnAdicionarMesa.setForeground(Color.WHITE);
        btnAdicionarMesa.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnAdicionarMesa.setPreferredSize(new Dimension(0, 55));
        btnAdicionarMesa.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAdicionarMesa.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                btnAdicionarMesa.setBackground(PRIMARY_GREEN.brighter());
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                btnAdicionarMesa.setBackground(PRIMARY_GREEN);
            }
        });
        btnAdicionarMesa.addActionListener(e -> mostrarDialogoAdicionarMesa());

        buttonsContainer.add(btnAdicionarCliente);
        buttonsContainer.add(btnAdicionarMesa);
        
        bottomPanel.add(buttonsContainer, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);
        add(tablesPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        
        setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
    }
    
    // --- Renderers e Editors ---
    
    // Renderer para coluna de Ações dos Clientes
    private class AcoesClienteCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
            panel.setBackground(isSelected ? table.getSelectionBackground() : Color.WHITE);
            panel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

            if (row < clientesList.size()) {
                TableActionButton lblEditar = new TableActionButton("Editar", INFO_BLUE);
                TableActionButton lblExcluir = new TableActionButton("Excluir", DANGER_RED);
                
                panel.add(lblEditar);
                panel.add(lblExcluir);
            }

            return panel;
        }
    }

    // Editor para coluna de Ações dos Clientes
    private class AcoesClienteCellEditor extends AbstractCellEditor implements javax.swing.table.TableCellEditor {
        private JPanel panel;
        private Cliente clienteAtual;

        public AcoesClienteCellEditor() {
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
            panel.setBackground(LIGHT_GREY.brighter());
            panel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
                int column) {
            panel.removeAll();
            
            if (row < clientesList.size()) {
                clienteAtual = clientesList.get(row);

                // Botão Editar
                TableActionButton btnEditar = new TableActionButton("Editar", INFO_BLUE);
                btnEditar.setToolTipText("Editar cliente");
                btnEditar.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        e.consume();
                        SwingUtilities.invokeLater(() -> {
                            fireEditingStopped();
                            editarCliente(clienteAtual);
                        });
                    }
                });

                // Botão Excluir
                TableActionButton btnExcluir = new TableActionButton("Excluir", DANGER_RED);
                btnExcluir.setToolTipText("Excluir cliente");
                btnExcluir.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        e.consume();
                        SwingUtilities.invokeLater(() -> {
                            fireEditingStopped();
                            excluirCliente(clienteAtual);
                        });
                    }
                });

                panel.add(btnEditar);
                panel.add(btnExcluir);
            }

            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return "";
        }
        
        @Override
        public boolean shouldSelectCell(EventObject anEvent) {
            return false;
        }
    }
    
    // Renderer para coluna de Ações das Mesas
    private class AcoesMesaCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
            panel.setBackground(isSelected ? table.getSelectionBackground() : Color.WHITE);
            panel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

            TableActionButton lblEditar = new TableActionButton("Editar", INFO_BLUE);
            TableActionButton lblExcluir = new TableActionButton("Excluir", DANGER_RED);
            
            panel.add(lblEditar);
            panel.add(lblExcluir);

            return panel;
        }
    }

    // Editor para coluna de Ações das Mesas
    private class AcoesMesaCellEditor extends AbstractCellEditor implements javax.swing.table.TableCellEditor {
        private JPanel panel;
        private Mesa mesaAtual;
        private List<Mesa> mesasList = new ArrayList<>();

        public AcoesMesaCellEditor() {
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
            panel.setBackground(LIGHT_GREY.brighter());
            panel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
                int column) {
            panel.removeAll();
            
            if (clienteSelecionado != null && row < mesasList.size()) {
                mesaAtual = mesasList.get(row);

                // Botão Editar
                TableActionButton btnEditar = new TableActionButton("Editar", INFO_BLUE);
                btnEditar.setToolTipText("Editar mesa");
                btnEditar.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        e.consume();
                        SwingUtilities.invokeLater(() -> {
                            fireEditingStopped();
                            editarMesa(mesaAtual);
                        });
                    }
                });

                // Botão Excluir
                TableActionButton btnExcluir = new TableActionButton("Excluir", DANGER_RED);
                btnExcluir.setToolTipText("Excluir mesa");
                btnExcluir.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        e.consume();
                        SwingUtilities.invokeLater(() -> {
                            fireEditingStopped();
                            excluirMesa(mesaAtual);
                        });
                    }
                });

                panel.add(btnEditar);
                panel.add(btnExcluir);
            }

            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return "";
        }
        
        @Override
        public boolean shouldSelectCell(EventObject anEvent) {
            return false;
        }
        
        public void setMesasList(List<Mesa> mesas) {
            this.mesasList = mesas;
        }
    }
    
    // --- Métodos de Dados ---
    
    public void carregarDados() {
        clientesTableModel.setRowCount(0);
        clientesList = controller.listarTodos();
        
        for (Cliente cliente : clientesList) {
            // Calcular total pago e deve das mesas do cliente
            List<Mesa> mesas = controller.listarMesasPorCliente(cliente.getId());
            BigDecimal totalPago = BigDecimal.ZERO;
            BigDecimal totalDeve = BigDecimal.ZERO;
            
            if (mesas != null) {
                for (Mesa mesa : mesas) {
                    if (mesa.getPago() != null) {
                        totalPago = totalPago.add(mesa.getPago());
                    }
                    if (mesa.getDeve() != null) {
                        totalDeve = totalDeve.add(mesa.getDeve());
                    }
                }
            }
            
            Object[] row = {
                cliente.getNome() != null ? cliente.getNome() : "",
                cliente.getEndereco() != null ? cliente.getEndereco() : "",
                cliente.getCidade() != null ? cliente.getCidade() : "",
                String.format("%.2f", totalPago),
                String.format("%.2f", totalDeve),
                ""
            };
            clientesTableModel.addRow(row);
        }
    }
    
    private void carregarMesasDoCliente(int clienteId) {
        mesasTableModel.setRowCount(0);
        
        if (clienteSelecionado == null) {
            if (mesasCardLayout != null && mesasContentPanel != null) {
                mesasCardLayout.show(mesasContentPanel, "PLACEHOLDER");
            }
            return;
        }
        
        List<Mesa> mesas = controller.listarMesasPorCliente(clienteId);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        
        if (mesas == null || mesas.isEmpty()) {
            // Mostrar placeholder quando não há mesas
            if (mesasCardLayout != null && mesasContentPanel != null) {
                mesasCardLayout.show(mesasContentPanel, "PLACEHOLDER");
            }
            return;
        }
        
        // Mostrar tabela quando há mesas
        if (mesasCardLayout != null && mesasContentPanel != null) {
            mesasCardLayout.show(mesasContentPanel, "TABLE");
        }
        
        // Atualizar lista de mesas no editor
        AcoesMesaCellEditor editor = (AcoesMesaCellEditor) tabelaMesas.getColumnModel().getColumn(3).getCellEditor();
        if (editor != null) {
            editor.setMesasList(mesas);
        }
        
        for (Mesa mesa : mesas) {
            Object[] row = {
                mesa.getNumero() != null ? mesa.getNumero() : "---",
                mesa.getData() != null ? sdf.format(mesa.getData()) : "---",
                mesa.getRegistro() != null ? mesa.getRegistro() : "---",
                ""
            };
            mesasTableModel.addRow(row);
        }
    }
    
    // --- Métodos de Ação ---
    
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
            BorderFactory.createLineBorder(LIGHT_GREY.darker(), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        formPanel.add(txtNome, gbc);
        
        gbc.gridy = 1;
        PlaceholderTextField txtEndereco = new PlaceholderTextField("Endereço");
        txtEndereco.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtEndereco.setPreferredSize(new Dimension(0, 40));
        txtEndereco.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LIGHT_GREY.darker(), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        formPanel.add(txtEndereco, gbc);
        
        gbc.gridy = 2;
        PlaceholderTextField txtCidade = new PlaceholderTextField("Cidade");
        txtCidade.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtCidade.setPreferredSize(new Dimension(0, 40));
        txtCidade.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LIGHT_GREY.darker(), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        formPanel.add(txtCidade, gbc);
        
        gbc.gridy = 3;
        PlaceholderTextField txtNumero = new PlaceholderTextField("Número da Mesa");
        txtNumero.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtNumero.setPreferredSize(new Dimension(0, 40));
        txtNumero.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LIGHT_GREY.darker(), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        formPanel.add(txtNumero, gbc);
        
        gbc.gridy = 4;
        PlaceholderTextField txtData = new PlaceholderTextField("Data (dd/MM/yyyy)");
        txtData.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtData.setPreferredSize(new Dimension(0, 40));
        txtData.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LIGHT_GREY.darker(), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        formPanel.add(txtData, gbc);
        
        gbc.gridy = 5;
        PlaceholderTextField txtRegistro = new PlaceholderTextField("Registro");
        txtRegistro.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtRegistro.setPreferredSize(new Dimension(0, 40));
        txtRegistro.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LIGHT_GREY.darker(), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        formPanel.add(txtRegistro, gbc);
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
        
        RoundedButton btnCancelar = new RoundedButton("Cancelar");
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnCancelar.setBackground(LIGHT_GREY);
        btnCancelar.setForeground(new Color(41, 50, 65));
        btnCancelar.setPreferredSize(new Dimension(130, 45));
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancelar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                btnCancelar.setBackground(LIGHT_GREY.darker());
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                btnCancelar.setBackground(LIGHT_GREY);
            }
        });
        btnCancelar.addActionListener(e -> dialog.dispose());

        RoundedButton btnSalvar = new RoundedButton("Salvar");
        btnSalvar.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnSalvar.setBackground(PRIMARY_BLUE);
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.setPreferredSize(new Dimension(130, 45));
        btnSalvar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSalvar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                btnSalvar.setBackground(HOVER_BLUE);
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                btnSalvar.setBackground(PRIMARY_BLUE);
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
                JOptionPane.showMessageDialog(dialog, "Por favor, preencha os campos obrigatórios (Nome, Endereço, Cidade)!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Date data_d = null;
            if (!dataStr.isEmpty()) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    java.util.Date utilDate = sdf.parse(dataStr);
                    data_d = new Date(utilDate.getTime());
                } catch (ParseException ex) {
                    JOptionPane.showMessageDialog(dialog, "Data inválida! Use o formato dd/MM/yyyy", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else {
                data_d = new Date(System.currentTimeMillis());
            }
            
            if (controller.inserirComMesa(nome, endereco, cidade, numero, data_d, registro)) {
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
        dialog.add(mainPanel);
        dialog.setVisible(true);
    }
    
    private void mostrarDialogoAdicionarMesa() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Adicionar Mesa", true);
        dialog.setSize(500, 400);
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
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        // Dropdown de clientes
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblCliente = new JLabel("Selecione o cliente:");
        lblCliente.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(lblCliente, gbc);
        
        gbc.gridy = 1;
        JComboBox<Cliente> comboClientes = new JComboBox<>();
        comboClientes.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        comboClientes.setPreferredSize(new Dimension(0, 40));
        comboClientes.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Cliente) {
                    setText(((Cliente) value).getNome());
                }
                return this;
            }
        });
        
        for (Cliente cliente : clientesList) {
            comboClientes.addItem(cliente);
        }
        formPanel.add(comboClientes, gbc);
        
        gbc.gridy = 2;
        PlaceholderTextField txtNumero = new PlaceholderTextField("Número da Mesa");
        txtNumero.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtNumero.setPreferredSize(new Dimension(0, 40));
        txtNumero.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LIGHT_GREY.darker(), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        formPanel.add(txtNumero, gbc);
        
        gbc.gridy = 3;
        PlaceholderTextField txtData = new PlaceholderTextField("Data (dd/MM/yyyy)");
        txtData.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtData.setPreferredSize(new Dimension(0, 40));
        txtData.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LIGHT_GREY.darker(), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        formPanel.add(txtData, gbc);
        
        gbc.gridy = 4;
        PlaceholderTextField txtRegistro = new PlaceholderTextField("Registro");
        txtRegistro.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtRegistro.setPreferredSize(new Dimension(0, 40));
        txtRegistro.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LIGHT_GREY.darker(), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        formPanel.add(txtRegistro, gbc);
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
        
        RoundedButton btnCancelar = new RoundedButton("Cancelar");
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnCancelar.setBackground(LIGHT_GREY);
        btnCancelar.setForeground(new Color(41, 50, 65));
        btnCancelar.setPreferredSize(new Dimension(130, 45));
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancelar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                btnCancelar.setBackground(LIGHT_GREY.darker());
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                btnCancelar.setBackground(LIGHT_GREY);
            }
        });
        btnCancelar.addActionListener(e -> dialog.dispose());

        RoundedButton btnSalvar = new RoundedButton("Salvar");
        btnSalvar.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnSalvar.setBackground(PRIMARY_GREEN);
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.setPreferredSize(new Dimension(130, 45));
        btnSalvar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSalvar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                btnSalvar.setBackground(PRIMARY_GREEN.brighter());
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                btnSalvar.setBackground(PRIMARY_GREEN);
            }
        });
        
        btnSalvar.addActionListener(e -> {
            Cliente clienteSelecionado = (Cliente) comboClientes.getSelectedItem();
            String numero = txtNumero.getText().trim();
            String dataStr = txtData.getText().trim();
            String registro = txtRegistro.getText().trim();
            
            if (clienteSelecionado == null) {
                JOptionPane.showMessageDialog(dialog, "Por favor, selecione um cliente!", "Erro", JOptionPane.ERROR_MESSAGE);
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
            
            if (controller.adicionarMesa(clienteSelecionado.getId(), numero, data, registro)) {
                JOptionPane.showMessageDialog(dialog, "Mesa adicionada com sucesso!");
                dialog.dispose();
                carregarDados();
                // Recarregar mesas se este cliente estiver selecionado
                if (clienteSelecionado.equals(this.clienteSelecionado)) {
                    carregarMesasDoCliente(clienteSelecionado.getId());
                }
            } else {
                JOptionPane.showMessageDialog(dialog, "Erro ao adicionar mesa!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        buttonPanel.add(btnCancelar);
        buttonPanel.add(btnSalvar);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        dialog.add(mainPanel);
        dialog.setVisible(true);
    }
    
    private void editarCliente(Cliente cliente) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Editar Cliente", true);
        dialog.setSize(500, 400);
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
        
        // Calcular totais atuais de Pago e Deve
        List<Mesa> mesas = controller.listarMesasPorCliente(cliente.getId());
        BigDecimal totalPago = BigDecimal.ZERO;
        BigDecimal totalDeve = BigDecimal.ZERO;
        
        if (mesas != null) {
            for (Mesa mesa : mesas) {
                if (mesa.getPago() != null) {
                    totalPago = totalPago.add(mesa.getPago());
                }
                if (mesa.getDeve() != null) {
                    totalDeve = totalDeve.add(mesa.getDeve());
                }
            }
        }
        
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
        
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Pago:"), gbc);
        gbc.gridx = 1;
        JTextField txtPago = new JTextField(String.format("%.2f", totalPago), 20);
        formPanel.add(txtPago, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Deve:"), gbc);
        gbc.gridx = 1;
        JTextField txtDeve = new JTextField(String.format("%.2f", totalDeve), 20);
        formPanel.add(txtDeve, gbc);
        
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
            String pagoStr = txtPago.getText().trim();
            String deveStr = txtDeve.getText().trim();
            
            if (novoNome.isEmpty() || novoEndereco.isEmpty() || novaCidade.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Por favor, preencha todos os campos obrigatórios!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            cliente.setNome(novoNome);
            cliente.setEndereco(novoEndereco);
            cliente.setCidade(novaCidade);
            
            // Atualizar valores de Pago e Deve nas mesas
            try {
                BigDecimal novoPago = new BigDecimal(pagoStr.replace(",", "."));
                BigDecimal novoDeve = new BigDecimal(deveStr.replace(",", "."));
                
                // Calcular diferença e distribuir proporcionalmente entre as mesas
                if (mesas != null && !mesas.isEmpty()) {
                    BigDecimal pagoAtual = BigDecimal.ZERO;
                    BigDecimal deveAtual = BigDecimal.ZERO;
                    
                    for (Mesa mesa : mesas) {
                        if (mesa.getPago() != null) {
                            pagoAtual = pagoAtual.add(mesa.getPago());
                        }
                        if (mesa.getDeve() != null) {
                            deveAtual = deveAtual.add(mesa.getDeve());
                        }
                    }
                    
                    BigDecimal diffPago = novoPago.subtract(pagoAtual);
                    BigDecimal diffDeve = novoDeve.subtract(deveAtual);
                    
                    // Distribuir a diferença proporcionalmente
                    if (!pagoAtual.equals(BigDecimal.ZERO)) {
                        for (Mesa mesa : mesas) {
                            BigDecimal proporcao = mesa.getPago() != null && !mesa.getPago().equals(BigDecimal.ZERO) 
                                ? mesa.getPago().divide(pagoAtual, 4, RoundingMode.HALF_UP)
                                : BigDecimal.ONE.divide(new BigDecimal(mesas.size()), 4, RoundingMode.HALF_UP);
                            BigDecimal novoValorPago = (mesa.getPago() != null ? mesa.getPago() : BigDecimal.ZERO)
                                .add(diffPago.multiply(proporcao));
                            mesa.setPago(novoValorPago);
                        }
                    } else {
                        // Se não há valores atuais, distribuir igualmente
                        BigDecimal valorPorMesa = novoPago.divide(new BigDecimal(mesas.size()), 2, RoundingMode.HALF_UP);
                        for (Mesa mesa : mesas) {
                            mesa.setPago(valorPorMesa);
                        }
                    }
                    
                    if (!deveAtual.equals(BigDecimal.ZERO)) {
                        for (Mesa mesa : mesas) {
                            BigDecimal proporcao = mesa.getDeve() != null && !mesa.getDeve().equals(BigDecimal.ZERO)
                                ? mesa.getDeve().divide(deveAtual, 4, RoundingMode.HALF_UP)
                                : BigDecimal.ONE.divide(new BigDecimal(mesas.size()), 4, RoundingMode.HALF_UP);
                            BigDecimal novoValorDeve = (mesa.getDeve() != null ? mesa.getDeve() : BigDecimal.ZERO)
                                .add(diffDeve.multiply(proporcao));
                            mesa.setDeve(novoValorDeve);
                        }
                    } else {
                        // Se não há valores atuais, distribuir igualmente
                        BigDecimal valorPorMesa = novoDeve.divide(new BigDecimal(mesas.size()), 2, RoundingMode.HALF_UP);
                        for (Mesa mesa : mesas) {
                            mesa.setDeve(valorPorMesa);
                        }
                    }
                    
                    // Atualizar todas as mesas
                    for (Mesa mesa : mesas) {
                        controller.atualizarMesa(mesa);
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Valores de Pago e Deve devem ser números decimais!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (controller.atualizar(cliente)) {
                JOptionPane.showMessageDialog(dialog, "Cliente atualizado com sucesso!");
                dialog.dispose();
                carregarDados();
                if (clienteSelecionado != null && clienteSelecionado.getId() == cliente.getId()) {
                    carregarMesasDoCliente(cliente.getId());
                }
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
        int confirm = JOptionPane.showConfirmDialog(this,
                "Deseja realmente excluir o cliente \"" + cliente.getNome() + "\" e todas as suas mesas?",
                "Confirmar exclusão", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (controller.deletar(cliente.getId())) {
                JOptionPane.showMessageDialog(this, "Cliente excluído com sucesso!");
                clienteSelecionado = null;
                mesasTableModel.setRowCount(0);
                carregarDados();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao excluir cliente!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void editarMesa(Mesa mesa) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Editar Mesa", true);
        dialog.setSize(500, 300);
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
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Número:"), gbc);
        gbc.gridx = 1;
        JTextField txtNumero = new JTextField(mesa.getNumero() != null ? mesa.getNumero() : "", 20);
        formPanel.add(txtNumero, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Data (dd/MM/yyyy):"), gbc);
        gbc.gridx = 1;
        JTextField txtData = new JTextField(mesa.getData() != null ? sdf.format(mesa.getData()) : "", 20);
        formPanel.add(txtData, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Registro:"), gbc);
        gbc.gridx = 1;
        JTextField txtRegistro = new JTextField(mesa.getRegistro() != null ? mesa.getRegistro() : "", 20);
        formPanel.add(txtRegistro, gbc);
        
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
            String numero = txtNumero.getText().trim();
            String dataStr = txtData.getText().trim();
            String registro = txtRegistro.getText().trim();
            
            try {
                mesa.setNumero(numero.isEmpty() ? null : numero);
                mesa.setRegistro(registro.isEmpty() ? null : registro);
                
                if (!dataStr.isEmpty()) {
                    java.util.Date utilDate = sdf.parse(dataStr);
                    mesa.setData(new Date(utilDate.getTime()));
                }
                
                if (controller.atualizarMesa(mesa)) {
                    JOptionPane.showMessageDialog(dialog, "Mesa atualizada com sucesso!");
                    dialog.dispose();
                    carregarMesasDoCliente(mesa.getClienteId());
                    carregarDados(); // Atualizar totais na tabela de clientes
                } else {
                    JOptionPane.showMessageDialog(dialog, "Erro ao atualizar mesa!", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(dialog, "Data inválida! Use o formato dd/MM/yyyy", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        btnCancelar.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(btnCancelar);
        buttonPanel.add(btnSalvar);
        
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    
    private void excluirMesa(Mesa mesa) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Deseja realmente excluir esta mesa?",
                "Confirmar exclusão", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (controller.deletarMesa(mesa.getId())) {
                JOptionPane.showMessageDialog(this, "Mesa excluída com sucesso!");
                carregarMesasDoCliente(mesa.getClienteId());
                carregarDados(); // Atualizar totais na tabela de clientes
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao excluir mesa!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
