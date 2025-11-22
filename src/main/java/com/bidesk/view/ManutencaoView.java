package com.bidesk.view;

import com.bidesk.controller.ManutencaoController;
import com.bidesk.model.Manutencao;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.AbstractCellEditor;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.EventObject;

public class ManutencaoView extends JPanel {
    // Tabelas
    private JTable tabelaPendentes;
    private DefaultTableModel pendentesTableModel;
    
    private ManutencaoController controller;
    private List<Manutencao> pendentesList;
    
    // Constantes de Cores
    private static final Color PRIMARY_GREEN = new Color(39, 174, 96);
    private static final Color PRIMARY_BLUE = new Color(41, 128, 185);
    private static final Color HOVER_BLUE = new Color(52, 152, 219);
    private static final Color DANGER_RED = new Color(231, 76, 60);
    private static final Color INFO_BLUE = new Color(52, 152, 219);
    private static final Color LIGHT_GREY = new Color(236, 240, 241);
    private static final Color ROW_ALTERNATE_GREEN = new Color(229, 243, 234); // Verde claro para linhas alternadas
    private static final Color ROW_SELECTED_GREEN = new Color(200, 230, 210); // Verde mais escuro para linha verde selecionada
    private static final Color ROW_SELECTED_WHITE = new Color(240, 240, 240); // Cinza claro para linha branca selecionada
    
    public ManutencaoView() {
        controller = new ManutencaoController();
        pendentesList = new ArrayList<>();
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
            setForeground(baseColor);
            setFont(new Font("Segoe UI", Font.BOLD, 13));
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
        JLabel titleLabel = new JLabel("Manutenção");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        headerPanel.add(titleLabel);

        // Painel principal com uma tabela
        JPanel tablesPanel = new JPanel(new BorderLayout());
        tablesPanel.setBackground(Color.WHITE);
        tablesPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // === TABELA DE PENDENTES (ESQUERDA) ===
        JPanel pendentesPanel = new JPanel(new BorderLayout());
        pendentesPanel.setBackground(Color.WHITE);
        
        JLabel pendentesLabel = new JLabel("Manutenções pendentes");
        pendentesLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        pendentesLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        pendentesPanel.add(pendentesLabel, BorderLayout.NORTH);
        
        String[] pendentesColumns = {"Cliente", "Título", "Descrição", "Ações"};
        pendentesTableModel = new DefaultTableModel(pendentesColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3; // Apenas coluna de ações é editável
            }
        };
        
        tabelaPendentes = new JTable(pendentesTableModel);
        tabelaPendentes.setRowHeight(45);
        tabelaPendentes.getTableHeader().setBackground(LIGHT_GREY);
        tabelaPendentes.getTableHeader().setForeground(new Color(41, 50, 65));
        tabelaPendentes.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        tabelaPendentes.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        tabelaPendentes.setShowGrid(false);
        tabelaPendentes.setIntercellSpacing(new Dimension(0, 1));
        tabelaPendentes.setBackground(Color.WHITE);
        tabelaPendentes.setSelectionBackground(ROW_SELECTED_GREEN);
        tabelaPendentes.setSelectionForeground(Color.BLACK);
        tabelaPendentes.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, LIGHT_GREY.darker()));
        
        // Centralizar cabeçalhos
        DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) tabelaPendentes.getTableHeader()
                .getDefaultRenderer();
        headerRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        // Renderers customizados para manter texto visível quando selecionado e centralizado
        DefaultTableCellRenderer textRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(JLabel.CENTER);
                cell.setForeground(Color.BLACK);
                // Linhas alternadas: verde claro para linhas pares, branco para linhas ímpares
                Color rowColor = (row % 2 == 0) ? ROW_ALTERNATE_GREEN : Color.WHITE;
                // Quando selecionada, mantém a cor verde mas mais escura, ou cinza claro se for linha branca
                Color selectedColor = (row % 2 == 0) ? ROW_SELECTED_GREEN : ROW_SELECTED_WHITE;
                if (isSelected) {
                    cell.setBackground(selectedColor);
                } else {
                    cell.setBackground(rowColor);
                }
                return cell;
            }
        };
        
        // Ajustar larguras das colunas
        tabelaPendentes.getColumnModel().getColumn(0).setPreferredWidth(150);
        tabelaPendentes.getColumnModel().getColumn(1).setPreferredWidth(200);
        tabelaPendentes.getColumnModel().getColumn(2).setPreferredWidth(300);
        tabelaPendentes.getColumnModel().getColumn(3).setPreferredWidth(200);
        
        // Aplicar renderer customizado nas colunas de texto
        tabelaPendentes.getColumnModel().getColumn(0).setCellRenderer(textRenderer);
        tabelaPendentes.getColumnModel().getColumn(1).setCellRenderer(textRenderer);
        tabelaPendentes.getColumnModel().getColumn(2).setCellRenderer(textRenderer);
        
        // Renderer e Editor para coluna de Ações
        tabelaPendentes.getColumnModel().getColumn(3).setCellRenderer(new AcoesPendentesCellRenderer());
        tabelaPendentes.getColumnModel().getColumn(3).setCellEditor(new AcoesPendentesCellEditor());
        
        JScrollPane pendentesScrollPane = new JScrollPane(tabelaPendentes);
        pendentesScrollPane.setBorder(BorderFactory.createEmptyBorder());
        pendentesScrollPane.setBackground(Color.WHITE);
        pendentesPanel.add(pendentesScrollPane, BorderLayout.CENTER);

        tablesPanel.add(pendentesPanel, BorderLayout.CENTER);

        // Botões no rodapé
        JPanel buttonsContainer = new JPanel(new GridLayout(1, 1, 0, 0));
        buttonsContainer.setBackground(Color.WHITE);
        buttonsContainer.setBorder(BorderFactory.createEmptyBorder(15, 20, 0, 20));

        RoundedButton btnAdicionar = new RoundedButton("+ Adicionar nova manutenção");
        btnAdicionar.setBackground(PRIMARY_BLUE);
        btnAdicionar.setForeground(Color.WHITE);
        btnAdicionar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnAdicionar.setPreferredSize(new Dimension(0, 55));
        btnAdicionar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAdicionar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                btnAdicionar.setBackground(HOVER_BLUE);
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                btnAdicionar.setBackground(PRIMARY_BLUE);
            }
        });
        btnAdicionar.addActionListener(e -> mostrarDialogoAdicionar());

        buttonsContainer.add(btnAdicionar);

        add(headerPanel, BorderLayout.NORTH);
        add(tablesPanel, BorderLayout.CENTER);
        add(buttonsContainer, BorderLayout.SOUTH);
    }
    
    // --- Métodos de Dados ---
    
    public void carregarDados() {
        try {
            pendentesList = controller.listarPendentes();
            
            if (pendentesList == null) {
                pendentesList = new ArrayList<>();
            }
            
            // Carregar pendentes
            pendentesTableModel.setRowCount(0);
            for (Manutencao manutencao : pendentesList) {
                if (manutencao == null) continue;
                String nomeCliente = controller.getNomeCliente(manutencao.getClienteId());
                String descricao = manutencao.getDescricao();
                if (descricao != null && descricao.length() > 50) {
                    descricao = descricao.substring(0, 47) + "...";
                }
                Object[] row = {
                    nomeCliente != null ? nomeCliente : "---",
                    manutencao.getTitulo() != null ? manutencao.getTitulo() : "---",
                    descricao != null ? descricao : "---",
                    ""
                };
                pendentesTableModel.addRow(row);
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar dados de manutenção: " + e.getMessage());
            e.printStackTrace();
            pendentesList = new ArrayList<>();
            pendentesTableModel.setRowCount(0);
        }
    }
    
    // --- Classes de Renderer e Editor para Ações ---
    
    private class AcoesPendentesCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
            // Linhas alternadas: verde claro para linhas pares, branco para linhas ímpares
            Color rowColor = (row % 2 == 0) ? ROW_ALTERNATE_GREEN : Color.WHITE;
            // Quando selecionada, mantém a cor verde mas mais escura, ou cinza claro se for linha branca
            Color selectedColor = (row % 2 == 0) ? ROW_SELECTED_GREEN : ROW_SELECTED_WHITE;
            panel.setBackground(isSelected ? selectedColor : rowColor);
            panel.setOpaque(true);
            
            if (row < pendentesList.size()) {
                TableActionButton btnPronto = new TableActionButton("Pronto", PRIMARY_GREEN);
                TableActionButton btnEditar = new TableActionButton("Editar", INFO_BLUE);
                
                panel.add(btnPronto);
                panel.add(btnEditar);
            }
            
            return panel;
        }
    }
    
    private class AcoesPendentesCellEditor extends AbstractCellEditor implements javax.swing.table.TableCellEditor {
        private JPanel panel;
        private Manutencao manutencaoAtual;
        
        public AcoesPendentesCellEditor() {
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            // Linhas alternadas: verde claro para linhas pares, branco para linhas ímpares
            Color rowColor = (row % 2 == 0) ? ROW_ALTERNATE_GREEN : Color.WHITE;
            // Quando selecionada, mantém a cor verde mas mais escura, ou cinza claro se for linha branca
            Color selectedColor = (row % 2 == 0) ? ROW_SELECTED_GREEN : ROW_SELECTED_WHITE;
            panel.setBackground(isSelected ? selectedColor : rowColor);
            
            panel.removeAll();
            
            if (row < pendentesList.size()) {
                manutencaoAtual = pendentesList.get(row);

                TableActionButton btnPronto = new TableActionButton("Pronto", PRIMARY_GREEN);
                btnPronto.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        e.consume();
                        SwingUtilities.invokeLater(() -> marcarComoPronto(manutencaoAtual));
                        fireEditingStopped();
                    }
                });

                TableActionButton btnEditar = new TableActionButton("Editar", INFO_BLUE);
                btnEditar.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        e.consume();
                        SwingUtilities.invokeLater(() -> editarManutencao(manutencaoAtual));
                        fireEditingStopped();
                    }
                });

                panel.add(btnPronto);
                panel.add(btnEditar);
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
    
    // --- Métodos de Ação ---
    
    private void marcarComoPronto(Manutencao manutencao) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Deseja marcar esta manutenção como resolvida? Ela será removida da lista.",
                "Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (controller.deletar(manutencao.getId())) {
                JOptionPane.showMessageDialog(this, "Manutenção resolvida e removida!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                carregarDados();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao remover manutenção!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void editarManutencao(Manutencao manutencao) {
        mostrarDialogoEditar(manutencao);
    }
    
    private void mostrarDialogoAdicionar() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                "Adicionar Manutenção", true);
        dialog.setSize(450, 400);
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

        // ComboBox de clientes
        JComboBox<String> comboClientes = new JComboBox<>();
        comboClientes.addItem("Selecione o cliente");
        java.util.List<String> clientes = controller.listarNomesClientes();
        for (String nome : clientes) {
            comboClientes.addItem(nome);
        }
        comboClientes.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        comboClientes.setPreferredSize(new Dimension(0, 40));
        comboClientes.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(LIGHT_GREY.darker(), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        formPanel.add(comboClientes, gbc);

        gbc.gridy++;
        PlaceholderTextField txtTitulo = new PlaceholderTextField("Título");
        txtTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtTitulo.setPreferredSize(new Dimension(0, 40));
        txtTitulo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(LIGHT_GREY.darker(), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        formPanel.add(txtTitulo, gbc);

        gbc.gridy++;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        PlaceholderTextArea txtDescricao = new PlaceholderTextArea("Descrição");
        txtDescricao.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtDescricao.setLineWrap(true);
        txtDescricao.setWrapStyleWord(true);
        txtDescricao.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(LIGHT_GREY.darker(), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        JScrollPane scrollDescricao = new JScrollPane(txtDescricao);
        scrollDescricao.setPreferredSize(new Dimension(0, 120));
        formPanel.add(scrollDescricao, gbc);
        
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
            if (comboClientes.getSelectedIndex() <= 0) {
                JOptionPane.showMessageDialog(dialog,
                        "Por favor, selecione um cliente!",
                        "Validação",
                        JOptionPane.WARNING_MESSAGE);
                comboClientes.requestFocus();
                return;
            }

            String titulo = txtTitulo.getText().trim();
            if (titulo.isEmpty()) {
                JOptionPane.showMessageDialog(dialog,
                        "Por favor, preencha o título!",
                        "Validação",
                        JOptionPane.WARNING_MESSAGE);
                txtTitulo.requestFocus();
                return;
            }

            String nomeClienteSelecionado = (String) comboClientes.getSelectedItem();
            if (nomeClienteSelecionado == null || nomeClienteSelecionado.equals("Selecione o cliente")) {
                JOptionPane.showMessageDialog(dialog,
                        "Por favor, selecione um cliente válido!",
                        "Validação",
                        JOptionPane.WARNING_MESSAGE);
                comboClientes.requestFocus();
                return;
            }
            
            Integer clienteId = controller.getIdClientePorNome(nomeClienteSelecionado);
            if (clienteId == null) {
                JOptionPane.showMessageDialog(dialog,
                        "Cliente não encontrado! Por favor, selecione um cliente válido.",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
                comboClientes.requestFocus();
                return;
            }
            
            String descricao = txtDescricao.getText().trim();
            
            System.out.println("=== DEBUG ADICIONAR MANUTENÇÃO ===");
            System.out.println("Cliente selecionado: " + nomeClienteSelecionado);
            System.out.println("Cliente ID: " + clienteId);
            System.out.println("Título: " + titulo);
            System.out.println("Descrição: " + (descricao != null && !descricao.isEmpty() ? descricao : "null"));

            if (controller.inserir(clienteId, titulo, descricao.isEmpty() ? null : descricao)) {
                JOptionPane.showMessageDialog(dialog, "Manutenção adicionada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
                carregarDados();
            } else {
                JOptionPane.showMessageDialog(dialog,
                        "Erro ao adicionar manutenção! Verifique o console para mais detalhes.",
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
    
    private void mostrarDialogoEditar(Manutencao manutencao) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), 
                                 "Editar Manutenção", true);
        dialog.setSize(450, 400); 
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
        JLabel lblCliente = new JLabel("Cliente:");
        lblCliente.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(lblCliente, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.8;
        JComboBox<String> comboClientes = new JComboBox<>();
        comboClientes.addItem("Selecione o cliente");
        String clienteAtual = controller.getNomeCliente(manutencao.getClienteId());
        java.util.List<String> clientes = controller.listarNomesClientes();
        for (String nome : clientes) {
            comboClientes.addItem(nome);
        }
        if (clienteAtual != null) {
            comboClientes.setSelectedItem(clienteAtual);
        } else {
            comboClientes.setSelectedIndex(0);
        }
        comboClientes.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboClientes.setPreferredSize(new Dimension(0, 35));
        comboClientes.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LIGHT_GREY.darker(), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        formPanel.add(comboClientes, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.2;
        JLabel lblTitulo = new JLabel("Título:");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(lblTitulo, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.8;
        JTextField txtTitulo = new JTextField(manutencao.getTitulo() != null ? manutencao.getTitulo() : "", 20);
        txtTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtTitulo.setPreferredSize(new Dimension(0, 35));
        txtTitulo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LIGHT_GREY.darker(), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        formPanel.add(txtTitulo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        JLabel lblDescricao = new JLabel("Descrição:");
        lblDescricao.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(lblDescricao, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.8;
        JTextArea txtDescricao = new JTextArea(manutencao.getDescricao() != null ? manutencao.getDescricao() : "", 5, 20);
        txtDescricao.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtDescricao.setLineWrap(true);
        txtDescricao.setWrapStyleWord(true);
        txtDescricao.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LIGHT_GREY.darker(), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        JScrollPane scrollDescricao = new JScrollPane(txtDescricao);
        scrollDescricao.setPreferredSize(new Dimension(0, 100));
        formPanel.add(scrollDescricao, gbc);

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
            if (comboClientes.getSelectedIndex() <= 0) {
                JOptionPane.showMessageDialog(dialog, "Por favor, selecione um cliente!", "Validação",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            String novoTitulo = txtTitulo.getText().trim();
            if (novoTitulo.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "O título não pode estar vazio!", "Validação",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            String nomeClienteSelecionado = (String) comboClientes.getSelectedItem();
            if (nomeClienteSelecionado == null || nomeClienteSelecionado.equals("Selecione o cliente")) {
                JOptionPane.showMessageDialog(dialog, "Por favor, selecione um cliente válido!", "Validação",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            Integer novoClienteId = controller.getIdClientePorNome(nomeClienteSelecionado);
            if (novoClienteId == null) {
                JOptionPane.showMessageDialog(dialog, "Cliente não encontrado! Por favor, selecione um cliente válido.", "Erro",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String novaDescricao = txtDescricao.getText().trim();
            
            System.out.println("=== DEBUG ATUALIZAR MANUTENÇÃO ===");
            System.out.println("ID Manutenção: " + manutencao.getId());
            System.out.println("Cliente selecionado: " + nomeClienteSelecionado);
            System.out.println("Cliente ID: " + novoClienteId);
            System.out.println("Título: " + novoTitulo);
            System.out.println("Descrição: " + (novaDescricao != null && !novaDescricao.isEmpty() ? novaDescricao : "null"));
            
            manutencao.setClienteId(novoClienteId);
            manutencao.setTitulo(novoTitulo);
            manutencao.setDescricao(novaDescricao.isEmpty() ? null : novaDescricao);
            
            if (controller.atualizar(manutencao)) {
                JOptionPane.showMessageDialog(dialog, "Manutenção atualizada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
                carregarDados();
            } else {
                JOptionPane.showMessageDialog(dialog, "Erro ao atualizar manutenção! Verifique o console para mais detalhes.", "Erro",
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
}
