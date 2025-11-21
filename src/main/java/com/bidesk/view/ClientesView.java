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
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.EventObject;

public class ClientesView extends JPanel {
    // Tabelas
    private JTable tabelaClientes;
    private DefaultTableModel clientesTableModel;
    
    private ClientesController controller;
    private List<Cliente> clientesList;
    private List<Cliente> clientesFiltrados;
    private Cliente clienteSelecionado;
    
    // Componentes para mesas agrupadas por número
    private JPanel mesasContentPanel;
    private CardLayout mesasCardLayout;
    private JScrollPane mesasScrollPane;
    private JPanel mesasContainerPanel;
    
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
        clientesFiltrados = new ArrayList<>();
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
        
        // Painel do título e pesquisa
        JPanel clientesHeaderPanel = new JPanel(new BorderLayout());
        clientesHeaderPanel.setBackground(Color.WHITE);
        clientesHeaderPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        JLabel clientesLabel = new JLabel("Clientes");
        clientesLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        clientesHeaderPanel.add(clientesLabel, BorderLayout.NORTH);
        
        // Campo de pesquisa
        PlaceholderTextField txtPesquisa = new PlaceholderTextField("Pesquisar cliente");
        txtPesquisa.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPesquisa.setPreferredSize(new Dimension(0, 35));
        txtPesquisa.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LIGHT_GREY.darker(), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        txtPesquisa.addActionListener(e -> filtrarClientes(txtPesquisa.getText()));
        txtPesquisa.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                filtrarClientes(txtPesquisa.getText());
            }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                filtrarClientes(txtPesquisa.getText());
            }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                filtrarClientes(txtPesquisa.getText());
            }
        });
        clientesHeaderPanel.add(txtPesquisa, BorderLayout.SOUTH);
        
        clientesPanel.add(clientesHeaderPanel, BorderLayout.NORTH);
        
        String[] clientesColumns = {"Nome", "Endereço", "Cidade", "Ações"};
        clientesTableModel = new DefaultTableModel(clientesColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3; // Apenas coluna de ações é editável
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
        tabelaClientes.setSelectionForeground(Color.BLACK);
        tabelaClientes.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, LIGHT_GREY.darker()));
        
        // Centralizar cabeçalhos
        DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) tabelaClientes.getTableHeader()
                .getDefaultRenderer();
        headerRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        // Renderers customizados para manter texto visível quando selecionado
        DefaultTableCellRenderer textRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                cell.setForeground(Color.BLACK);
                if (isSelected) {
                    cell.setBackground(table.getSelectionBackground());
                } else {
                    cell.setBackground(Color.WHITE);
                }
                ((JLabel) cell).setHorizontalAlignment(JLabel.CENTER);
                return cell;
            }
        };
        
        // Ajustar larguras das colunas
        tabelaClientes.getColumnModel().getColumn(0).setPreferredWidth(200);
        tabelaClientes.getColumnModel().getColumn(1).setPreferredWidth(250);
        tabelaClientes.getColumnModel().getColumn(2).setPreferredWidth(200);
        tabelaClientes.getColumnModel().getColumn(3).setPreferredWidth(150);
        
        // Aplicar renderer customizado nas colunas de texto
        tabelaClientes.getColumnModel().getColumn(0).setCellRenderer(textRenderer);
        tabelaClientes.getColumnModel().getColumn(1).setCellRenderer(textRenderer);
        tabelaClientes.getColumnModel().getColumn(2).setCellRenderer(textRenderer);
        
        // Renderer e Editor para coluna de Ações
        tabelaClientes.getColumnModel().getColumn(3).setCellRenderer(new AcoesClienteCellRenderer());
        tabelaClientes.getColumnModel().getColumn(3).setCellEditor(new AcoesClienteCellEditor());
        
        // Listener para seleção de cliente
        tabelaClientes.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = tabelaClientes.getSelectedRow();
                if (selectedRow >= 0 && selectedRow < clientesFiltrados.size()) {
                    clienteSelecionado = clientesFiltrados.get(selectedRow);
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
                    if (row < clientesFiltrados.size()) {
                        clienteSelecionado = clientesFiltrados.get(row);
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
        
        // Painel scrollável para múltiplas tabelas agrupadas por número
        mesasContainerPanel = new JPanel();
        mesasContainerPanel.setLayout(new BoxLayout(mesasContainerPanel, BoxLayout.Y_AXIS));
        mesasContainerPanel.setBackground(Color.WHITE);
        
        mesasScrollPane = new JScrollPane(mesasContainerPanel);
        mesasScrollPane.setBorder(BorderFactory.createEmptyBorder());
        mesasScrollPane.setBackground(Color.WHITE);
        mesasScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        mesasScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        // Mensagem inicial quando nenhum cliente está selecionado
        JLabel placeholderLabel = new JLabel("Selecione o cliente para ver suas mesas.");
        placeholderLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        placeholderLabel.setForeground(new Color(150, 150, 150));
        placeholderLabel.setHorizontalAlignment(JLabel.CENTER);
        placeholderLabel.setBorder(BorderFactory.createEmptyBorder(50, 0, 50, 0));
        
        // Usar CardLayout para alternar entre placeholder e tabelas
        CardLayout mesasCardLayout = new CardLayout();
        JPanel mesasContentPanel = new JPanel(mesasCardLayout);
        mesasContentPanel.setBackground(Color.WHITE);
        mesasContentPanel.add(placeholderLabel, "PLACEHOLDER");
        mesasContentPanel.add(mesasScrollPane, "TABLES");
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

            if (row < clientesFiltrados.size()) {
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
            
            if (row < clientesFiltrados.size()) {
                clienteAtual = clientesFiltrados.get(row);

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
        clientesList = controller.listarTodos();
        clientesFiltrados = new ArrayList<>(clientesList);
        atualizarTabelaClientes();
    }
    
    private void atualizarTabelaClientes() {
        clientesTableModel.setRowCount(0);
        
        for (Cliente cliente : clientesFiltrados) {
            Object[] row = {
                cliente.getNome() != null ? cliente.getNome() : "",
                cliente.getEndereco() != null ? cliente.getEndereco() : "",
                cliente.getCidade() != null ? cliente.getCidade() : "",
                ""
            };
            clientesTableModel.addRow(row);
        }
    }
    
    private void filtrarClientes(String textoPesquisa) {
        if (textoPesquisa == null || textoPesquisa.trim().isEmpty()) {
            clientesFiltrados = new ArrayList<>(clientesList);
        } else {
            String pesquisa = textoPesquisa.toLowerCase().trim();
            clientesFiltrados = new ArrayList<>();
            for (Cliente cliente : clientesList) {
                if ((cliente.getNome() != null && cliente.getNome().toLowerCase().contains(pesquisa)) ||
                    (cliente.getEndereco() != null && cliente.getEndereco().toLowerCase().contains(pesquisa)) ||
                    (cliente.getCidade() != null && cliente.getCidade().toLowerCase().contains(pesquisa))) {
                    clientesFiltrados.add(cliente);
                }
            }
        }
        atualizarTabelaClientes();
    }
    
    private void carregarMesasDoCliente(int clienteId) {
        // Limpar todas as tabelas anteriores
        mesasContainerPanel.removeAll();
        
        if (clienteSelecionado == null) {
            if (mesasCardLayout != null && mesasContentPanel != null) {
                mesasCardLayout.show(mesasContentPanel, "PLACEHOLDER");
            }
            mesasContainerPanel.revalidate();
            mesasContainerPanel.repaint();
            return;
        }
        
        List<Mesa> mesas = controller.listarMesasPorCliente(clienteId);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        
        if (mesas == null || mesas.isEmpty()) {
            // Mostrar placeholder quando não há mesas
            if (mesasCardLayout != null && mesasContentPanel != null) {
                mesasCardLayout.show(mesasContentPanel, "PLACEHOLDER");
            }
            mesasContainerPanel.revalidate();
            mesasContainerPanel.repaint();
            return;
        }
        
        // Mostrar tabelas quando há mesas
        if (mesasCardLayout != null && mesasContentPanel != null) {
            mesasCardLayout.show(mesasContentPanel, "TABLES");
        }
        
        // Agrupar mesas por número
        Map<String, List<Mesa>> mesasPorNumero = new HashMap<>();
        for (Mesa mesa : mesas) {
            String numero = mesa.getNumero() != null ? mesa.getNumero() : "Sem número";
            mesasPorNumero.computeIfAbsent(numero, k -> new ArrayList<>()).add(mesa);
        }
        
        // Criar uma tabela para cada número de mesa
        for (Map.Entry<String, List<Mesa>> entry : mesasPorNumero.entrySet()) {
            String numeroMesa = entry.getKey();
            List<Mesa> mesasDoNumero = entry.getValue();
            
            // Painel para cada grupo de mesa
            JPanel grupoPanel = new JPanel(new BorderLayout());
            grupoPanel.setBackground(Color.WHITE);
            grupoPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
            
            // Título do grupo (número da mesa) com fundo verde
            JPanel tituloPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            tituloPanel.setBackground(PRIMARY_GREEN);
            tituloPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
            
            JLabel grupoLabel = new JLabel("Mesa Nº " + numeroMesa);
            grupoLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
            grupoLabel.setForeground(Color.WHITE);
            tituloPanel.add(grupoLabel);
            
            grupoPanel.add(tituloPanel, BorderLayout.NORTH);
            
            // Criar tabela para este grupo
            String[] mesasColumns = {"Nº", "Data", "Registro", "Pago", "Deve", "Ações"};
            DefaultTableModel grupoTableModel = new DefaultTableModel(mesasColumns, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return column == 5; // Apenas coluna de ações é editável
                }
            };
            
            JTable grupoTable = new JTable(grupoTableModel);
            grupoTable.setRowHeight(45);
            grupoTable.getTableHeader().setBackground(LIGHT_GREY);
            grupoTable.getTableHeader().setForeground(new Color(41, 50, 65));
            grupoTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
            grupoTable.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            grupoTable.setShowGrid(false);
            grupoTable.setIntercellSpacing(new Dimension(0, 1));
            grupoTable.setBackground(Color.WHITE);
            grupoTable.setSelectionBackground(LIGHT_GREY.brighter());
            grupoTable.setSelectionForeground(Color.BLACK);
            grupoTable.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, LIGHT_GREY.darker()));
            
            // Centralizar cabeçalhos
            DefaultTableCellRenderer grupoHeaderRenderer = (DefaultTableCellRenderer) grupoTable.getTableHeader()
                    .getDefaultRenderer();
            grupoHeaderRenderer.setHorizontalAlignment(JLabel.CENTER);
            
            // Renderer customizado para manter texto visível quando selecionado
            DefaultTableCellRenderer grupoCenterRenderer = new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                        boolean hasFocus, int row, int column) {
                    Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    setHorizontalAlignment(JLabel.CENTER);
                    cell.setForeground(Color.BLACK);
                    if (isSelected) {
                        cell.setBackground(table.getSelectionBackground());
                    } else {
                        cell.setBackground(Color.WHITE);
                    }
                    return cell;
                }
            };
            
            // Ajustar larguras das colunas
            grupoTable.getColumnModel().getColumn(0).setPreferredWidth(80);
            grupoTable.getColumnModel().getColumn(1).setPreferredWidth(100);
            grupoTable.getColumnModel().getColumn(2).setPreferredWidth(120);
            grupoTable.getColumnModel().getColumn(3).setPreferredWidth(100);
            grupoTable.getColumnModel().getColumn(4).setPreferredWidth(100);
            grupoTable.getColumnModel().getColumn(5).setPreferredWidth(150);
            
            // Aplicar renderer customizado nas colunas de texto
            grupoTable.getColumnModel().getColumn(0).setCellRenderer(grupoCenterRenderer);
            grupoTable.getColumnModel().getColumn(1).setCellRenderer(grupoCenterRenderer);
            grupoTable.getColumnModel().getColumn(2).setCellRenderer(grupoCenterRenderer);
            grupoTable.getColumnModel().getColumn(3).setCellRenderer(grupoCenterRenderer);
            grupoTable.getColumnModel().getColumn(4).setCellRenderer(grupoCenterRenderer);
            
            // Renderer e Editor para coluna de Ações
            grupoTable.getColumnModel().getColumn(5).setCellRenderer(new AcoesMesaCellRenderer());
            AcoesMesaCellEditor grupoEditor = new AcoesMesaCellEditor();
            grupoEditor.setMesasList(mesasDoNumero);
            grupoTable.getColumnModel().getColumn(5).setCellEditor(grupoEditor);
            
            // Adicionar dados à tabela
            for (Mesa mesa : mesasDoNumero) {
                Object[] row = {
                    mesa.getNumero() != null ? mesa.getNumero() : "---",
                    mesa.getData() != null ? sdf.format(mesa.getData()) : "---",
                    mesa.getRegistro() != null ? mesa.getRegistro() : "---",
                    mesa.getPago() != null ? String.format("%.2f", mesa.getPago()) : "0,00",
                    mesa.getDeve() != null ? String.format("%.2f", mesa.getDeve()) : "0,00",
                    ""
                };
                grupoTableModel.addRow(row);
            }
            
            JScrollPane grupoScrollPane = new JScrollPane(grupoTable);
            grupoScrollPane.setBorder(BorderFactory.createEmptyBorder());
            grupoScrollPane.setBackground(Color.WHITE);
            grupoScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
            
            grupoPanel.add(grupoScrollPane, BorderLayout.CENTER);
            mesasContainerPanel.add(grupoPanel);
        }
        
        mesasContainerPanel.revalidate();
        mesasContainerPanel.repaint();
    }
    
    // --- Métodos de Ação ---
    
    private void mostrarDialogoAdicionarCliente() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                "Adicionar Cliente", true);
        dialog.setSize(450, 560);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(Color.WHITE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        PlaceholderTextField txtNome = new PlaceholderTextField("Nome");
        txtNome.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtNome.setPreferredSize(new Dimension(0, 40));
        txtNome.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(LIGHT_GREY.darker(), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        formPanel.add(txtNome, gbc);

        gbc.gridy++;
        PlaceholderTextField txtEndereco = new PlaceholderTextField("Endereço");
        txtEndereco.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtEndereco.setPreferredSize(new Dimension(0, 40));
        txtEndereco.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(LIGHT_GREY.darker(), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        formPanel.add(txtEndereco, gbc);

        gbc.gridy++;
        PlaceholderTextField txtCidade = new PlaceholderTextField("Cidade");
        txtCidade.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtCidade.setPreferredSize(new Dimension(0, 40));
        txtCidade.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(LIGHT_GREY.darker(), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        formPanel.add(txtCidade, gbc);

        gbc.gridy++;
        PlaceholderTextField txtNumero = new PlaceholderTextField("Número da Mesa");
        txtNumero.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtNumero.setPreferredSize(new Dimension(0, 40));
        txtNumero.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(LIGHT_GREY.darker(), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        formPanel.add(txtNumero, gbc);

        gbc.gridy++;
        PlaceholderTextField txtData = new PlaceholderTextField("Data (dd/MM/yyyy)");
        txtData.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtData.setPreferredSize(new Dimension(0, 40));
        txtData.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(LIGHT_GREY.darker(), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        formPanel.add(txtData, gbc);

        gbc.gridy++;
        PlaceholderTextField txtRegistro = new PlaceholderTextField("Registro");
        txtRegistro.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtRegistro.setPreferredSize(new Dimension(0, 40));
        txtRegistro.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(LIGHT_GREY.darker(), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
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
                JOptionPane.showMessageDialog(dialog,
                        "Por favor, preencha os campos obrigatórios (Nome, Endereço, Cidade)!",
                        "Validação",
                        JOptionPane.WARNING_MESSAGE);
                txtNome.requestFocus();
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
                JOptionPane.showMessageDialog(dialog, "Cliente adicionado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
                carregarDados();
            } else {
                JOptionPane.showMessageDialog(dialog,
                        "Erro ao adicionar cliente!",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        buttonPanel.add(btnCancelar);
        buttonPanel.add(btnSalvar);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        dialog.add(mainPanel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }
    
    private void mostrarDialogoAdicionarMesa() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                "Adicionar Mesa", true);
        dialog.setSize(450, 400);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(Color.WHITE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // ComboBox de clientes com estilo similar ao PlaceholderTextField
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
                } else if (value == null) {
                    setText("Selecione o cliente");
                    setForeground(new Color(150, 150, 150));
                }
                return this;
            }
        });
        
        for (Cliente cliente : clientesList) {
            comboClientes.addItem(cliente);
        }
        comboClientes.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(LIGHT_GREY.darker(), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        formPanel.add(comboClientes, gbc);

        gbc.gridy++;
        PlaceholderTextField txtNumero = new PlaceholderTextField("Número da Mesa");
        txtNumero.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtNumero.setPreferredSize(new Dimension(0, 40));
        txtNumero.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(LIGHT_GREY.darker(), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        formPanel.add(txtNumero, gbc);

        gbc.gridy++;
        PlaceholderTextField txtData = new PlaceholderTextField("Data (dd/MM/yyyy)");
        txtData.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtData.setPreferredSize(new Dimension(0, 40));
        txtData.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(LIGHT_GREY.darker(), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        formPanel.add(txtData, gbc);

        gbc.gridy++;
        PlaceholderTextField txtRegistro = new PlaceholderTextField("Registro");
        txtRegistro.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtRegistro.setPreferredSize(new Dimension(0, 40));
        txtRegistro.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(LIGHT_GREY.darker(), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
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
                JOptionPane.showMessageDialog(dialog,
                        "Por favor, selecione um cliente!",
                        "Validação",
                        JOptionPane.WARNING_MESSAGE);
                comboClientes.requestFocus();
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
                JOptionPane.showMessageDialog(dialog, "Mesa adicionada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
                carregarDados();
                // Recarregar mesas se este cliente estiver selecionado
                if (clienteSelecionado.equals(this.clienteSelecionado)) {
                    carregarMesasDoCliente(clienteSelecionado.getId());
                }
            } else {
                JOptionPane.showMessageDialog(dialog,
                        "Erro ao adicionar mesa!",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        buttonPanel.add(btnCancelar);
        buttonPanel.add(btnSalvar);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        dialog.add(mainPanel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }
    
    private void editarCliente(Cliente cliente) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), 
                                 "Editar Cliente", true);
        dialog.setSize(450, 280); 
        dialog.setResizable(false);
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

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.2;
        JLabel lblNome = new JLabel("Nome:");
        lblNome.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(lblNome, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.8;
        JTextField txtNome = new JTextField(cliente.getNome(), 20);
        txtNome.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtNome.setPreferredSize(new Dimension(0, 35));
        txtNome.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LIGHT_GREY.darker(), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        formPanel.add(txtNome, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.2;
        JLabel lblEndereco = new JLabel("Endereço:");
        lblEndereco.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(lblEndereco, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.8;
        JTextField txtEndereco = new JTextField(cliente.getEndereco(), 20);
        txtEndereco.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtEndereco.setPreferredSize(new Dimension(0, 35));
        txtEndereco.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LIGHT_GREY.darker(), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        formPanel.add(txtEndereco, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.2;
        JLabel lblCidade = new JLabel("Cidade:");
        lblCidade.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(lblCidade, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.8;
        JTextField txtCidade = new JTextField(cliente.getCidade(), 20);
        txtCidade.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtCidade.setPreferredSize(new Dimension(0, 35));
        txtCidade.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LIGHT_GREY.darker(), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        formPanel.add(txtCidade, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        RoundedButton btnSalvar = new RoundedButton("Salvar");
        btnSalvar.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnSalvar.setBackground(PRIMARY_BLUE);
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.setPreferredSize(new Dimension(110, 40));
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
        
        RoundedButton btnCancelar = new RoundedButton("Cancelar");
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnCancelar.setBackground(LIGHT_GREY);
        btnCancelar.setForeground(new Color(41, 50, 65));
        btnCancelar.setPreferredSize(new Dimension(110, 40));
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

        btnSalvar.addActionListener(e -> {
            String novoNome = txtNome.getText().trim();
            String novoEndereco = txtEndereco.getText().trim();
            String novaCidade = txtCidade.getText().trim();

            if (novoNome.isEmpty() || novoEndereco.isEmpty() || novaCidade.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Por favor, preencha todos os campos obrigatórios!", "Validação",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            cliente.setNome(novoNome);
            cliente.setEndereco(novoEndereco);
            cliente.setCidade(novaCidade);
            
            if (controller.atualizar(cliente)) {
                JOptionPane.showMessageDialog(dialog, "Cliente atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
                carregarDados();
                if (clienteSelecionado != null && clienteSelecionado.getId() == cliente.getId()) {
                    carregarMesasDoCliente(cliente.getId());
                }
            } else {
                JOptionPane.showMessageDialog(dialog, "Erro ao atualizar cliente!", "Erro",
                        JOptionPane.ERROR_MESSAGE);
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
                if (mesasContainerPanel != null) {
                    mesasContainerPanel.removeAll();
                    mesasContainerPanel.revalidate();
                    mesasContainerPanel.repaint();
                }
                if (mesasCardLayout != null && mesasContentPanel != null) {
                    mesasCardLayout.show(mesasContentPanel, "PLACEHOLDER");
                }
                carregarDados();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao excluir cliente!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void editarMesa(Mesa mesa) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), 
                                 "Editar Mesa", true);
        dialog.setSize(450, 380); 
        dialog.setResizable(false);
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

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.2;
        JLabel lblNumero = new JLabel("Número:");
        lblNumero.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(lblNumero, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.8;
        JTextField txtNumero = new JTextField(mesa.getNumero() != null ? mesa.getNumero() : "", 20);
        txtNumero.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtNumero.setPreferredSize(new Dimension(0, 35));
        txtNumero.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LIGHT_GREY.darker(), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        formPanel.add(txtNumero, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.2;
        JLabel lblData = new JLabel("Data (dd/MM/yyyy):");
        lblData.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(lblData, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.8;
        JTextField txtData = new JTextField(mesa.getData() != null ? sdf.format(mesa.getData()) : "", 20);
        txtData.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtData.setPreferredSize(new Dimension(0, 35));
        txtData.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LIGHT_GREY.darker(), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        formPanel.add(txtData, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.2;
        JLabel lblRegistro = new JLabel("Registro:");
        lblRegistro.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(lblRegistro, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.8;
        JTextField txtRegistro = new JTextField(mesa.getRegistro() != null ? mesa.getRegistro() : "", 20);
        txtRegistro.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtRegistro.setPreferredSize(new Dimension(0, 35));
        txtRegistro.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LIGHT_GREY.darker(), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        formPanel.add(txtRegistro, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.2;
        JLabel lblPago = new JLabel("Pago:");
        lblPago.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(lblPago, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.8;
        JTextField txtPago = new JTextField(mesa.getPago() != null ? String.format("%.2f", mesa.getPago()) : "0.00", 20);
        txtPago.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPago.setPreferredSize(new Dimension(0, 35));
        txtPago.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LIGHT_GREY.darker(), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        formPanel.add(txtPago, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.2;
        JLabel lblDeve = new JLabel("Deve:");
        lblDeve.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(lblDeve, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.8;
        JTextField txtDeve = new JTextField(mesa.getDeve() != null ? String.format("%.2f", mesa.getDeve()) : "0.00", 20);
        txtDeve.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtDeve.setPreferredSize(new Dimension(0, 35));
        txtDeve.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LIGHT_GREY.darker(), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        formPanel.add(txtDeve, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        RoundedButton btnSalvar = new RoundedButton("Salvar");
        btnSalvar.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnSalvar.setBackground(PRIMARY_BLUE);
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.setPreferredSize(new Dimension(110, 40));
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
        
        RoundedButton btnCancelar = new RoundedButton("Cancelar");
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnCancelar.setBackground(LIGHT_GREY);
        btnCancelar.setForeground(new Color(41, 50, 65));
        btnCancelar.setPreferredSize(new Dimension(110, 40));
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

        btnSalvar.addActionListener(e -> {
            String numero = txtNumero.getText().trim();
            String dataStr = txtData.getText().trim();
            String registro = txtRegistro.getText().trim();
            String pagoStr = txtPago.getText().trim();
            String deveStr = txtDeve.getText().trim();
            
            try {
                mesa.setNumero(numero.isEmpty() ? null : numero);
                mesa.setRegistro(registro.isEmpty() ? null : registro);
                
                if (!dataStr.isEmpty()) {
                    java.util.Date utilDate = sdf.parse(dataStr);
                    mesa.setData(new Date(utilDate.getTime()));
                }
                
                BigDecimal pago = new BigDecimal(pagoStr.replace(",", "."));
                BigDecimal deve = new BigDecimal(deveStr.replace(",", "."));
                mesa.setPago(pago);
                mesa.setDeve(deve);
                
                if (controller.atualizarMesa(mesa)) {
                    JOptionPane.showMessageDialog(dialog, "Mesa atualizada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                    carregarMesasDoCliente(mesa.getClienteId());
                    carregarDados();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Erro ao atualizar mesa!", "Erro",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(dialog, "Data inválida! Use o formato dd/MM/yyyy", "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Valores de Pago e Deve devem ser números decimais!", "Erro", JOptionPane.ERROR_MESSAGE);
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
