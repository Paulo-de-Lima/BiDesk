package com.bidesk.view;

import com.bidesk.controller.FinanceiroController;
import com.bidesk.model.Despesa;
import com.bidesk.model.DespesaMaterial;
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
import java.util.List;
import java.util.EventObject;

public class FinanceiroView extends JPanel {
    // Tabelas
    private JTable tabelaCobrancas;
    private DefaultTableModel cobrancasTableModel;
    private JTable tabelaDespesasMateriais;
    private DefaultTableModel despesasMateriaisTableModel;
    
    private FinanceiroController controller;
    private List<Despesa> despesasList;
    private List<Despesa> despesasFiltradas;
    private List<DespesaMaterial> despesasMateriaisList;
    
    // Constantes de Cores
    private static final Color PRIMARY_GREEN = new Color(39, 174, 96);
    private static final Color PRIMARY_BLUE = new Color(41, 128, 185);
    private static final Color HOVER_BLUE = new Color(52, 152, 219);
    private static final Color DANGER_RED = new Color(231, 76, 60);
    private static final Color INFO_BLUE = new Color(52, 152, 219);
    private static final Color LIGHT_GREY = new Color(236, 240, 241);
    
    public FinanceiroView() {
        controller = new FinanceiroController();
        despesasList = new ArrayList<>();
        despesasFiltradas = new ArrayList<>();
        despesasMateriaisList = new ArrayList<>();
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
        JLabel titleLabel = new JLabel("Financeiro");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        headerPanel.add(titleLabel);

        // Painel principal com duas tabelas lado a lado
        JPanel tablesPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        tablesPanel.setBackground(Color.WHITE);
        tablesPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // === TABELA DE COBRANÇAS (ESQUERDA) ===
        JPanel cobrancasPanel = new JPanel(new BorderLayout());
        cobrancasPanel.setBackground(Color.WHITE);
        
        JPanel cobrancasHeaderPanel = new JPanel(new BorderLayout());
        cobrancasHeaderPanel.setBackground(Color.WHITE);
        cobrancasHeaderPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        JLabel cobrancasLabel = new JLabel("Histórico de cobranças");
        cobrancasLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        cobrancasHeaderPanel.add(cobrancasLabel, BorderLayout.NORTH);
        
        // Painel de filtros e pesquisa
        JPanel filtrosPanel = new JPanel(new GridLayout(1, 1, 0, 10));
        filtrosPanel.setBackground(Color.WHITE);
        
        // Filtro por cidade
        PlaceholderTextField txtFiltroCidade = new PlaceholderTextField("Pesquisar por cidade");
        txtFiltroCidade.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtFiltroCidade.setPreferredSize(new Dimension(0, 35));
        txtFiltroCidade.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LIGHT_GREY.darker(), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        filtrosPanel.add(txtFiltroCidade);
        
        // Listeners para filtros
        txtFiltroCidade.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                filtrarCobrancas(txtFiltroCidade.getText());
            }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                filtrarCobrancas(txtFiltroCidade.getText());
            }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                filtrarCobrancas(txtFiltroCidade.getText());
            }
        });
        
        cobrancasHeaderPanel.add(filtrosPanel, BorderLayout.SOUTH);
        cobrancasPanel.add(cobrancasHeaderPanel, BorderLayout.NORTH);
        
        String[] cobrancasColumns = {"Data", "Cidade", "Total Despesa", "Total adquirido", "Ações"};
        cobrancasTableModel = new DefaultTableModel(cobrancasColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // Apenas coluna de ações é editável
            }
        };
        
        tabelaCobrancas = new JTable(cobrancasTableModel);
        tabelaCobrancas.setRowHeight(45);
        tabelaCobrancas.getTableHeader().setBackground(LIGHT_GREY);
        tabelaCobrancas.getTableHeader().setForeground(new Color(41, 50, 65));
        tabelaCobrancas.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        tabelaCobrancas.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        tabelaCobrancas.setShowGrid(false);
        tabelaCobrancas.setIntercellSpacing(new Dimension(0, 1));
        tabelaCobrancas.setBackground(Color.WHITE);
        tabelaCobrancas.setSelectionBackground(LIGHT_GREY.brighter());
        tabelaCobrancas.setSelectionForeground(Color.BLACK);
        tabelaCobrancas.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, LIGHT_GREY.darker()));
        
        // Centralizar cabeçalhos
        DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) tabelaCobrancas.getTableHeader()
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
                return cell;
            }
        };
        
        // Renderer centralizado para valores monetários
        DefaultTableCellRenderer moneyRenderer = new DefaultTableCellRenderer() {
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
        tabelaCobrancas.getColumnModel().getColumn(0).setPreferredWidth(120);
        tabelaCobrancas.getColumnModel().getColumn(1).setPreferredWidth(200);
        tabelaCobrancas.getColumnModel().getColumn(2).setPreferredWidth(150);
        tabelaCobrancas.getColumnModel().getColumn(3).setPreferredWidth(180);
        tabelaCobrancas.getColumnModel().getColumn(4).setPreferredWidth(150);
        
        // Aplicar renderer customizado nas colunas de texto
        tabelaCobrancas.getColumnModel().getColumn(0).setCellRenderer(textRenderer);
        tabelaCobrancas.getColumnModel().getColumn(1).setCellRenderer(textRenderer);
        // Colunas monetárias centralizadas
        tabelaCobrancas.getColumnModel().getColumn(2).setCellRenderer(moneyRenderer);
        tabelaCobrancas.getColumnModel().getColumn(3).setCellRenderer(moneyRenderer);
        
        // Renderer e Editor para coluna de Ações
        tabelaCobrancas.getColumnModel().getColumn(4).setCellRenderer(new AcoesCobrancaCellRenderer());
        tabelaCobrancas.getColumnModel().getColumn(4).setCellEditor(new AcoesCobrancaCellEditor());
        
        JScrollPane cobrancasScrollPane = new JScrollPane(tabelaCobrancas);
        cobrancasScrollPane.setBorder(BorderFactory.createEmptyBorder());
        cobrancasScrollPane.setBackground(Color.WHITE);
        cobrancasPanel.add(cobrancasScrollPane, BorderLayout.CENTER);

        // === TABELA DE DESPESAS MATERIAIS (DIREITA) ===
        JPanel despesasMateriaisPanel = new JPanel(new BorderLayout());
        despesasMateriaisPanel.setBackground(Color.WHITE);
        
        JLabel despesasMateriaisLabel = new JLabel("Despesas Materiais");
        despesasMateriaisLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        despesasMateriaisLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        despesasMateriaisPanel.add(despesasMateriaisLabel, BorderLayout.NORTH);
        
        String[] despesasMateriaisColumns = {"Material", "Gasto", "Total gasto"};
        despesasMateriaisTableModel = new DefaultTableModel(despesasMateriaisColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0; // Apenas coluna de Material é editável (para ações)
            }
        };
        
        tabelaDespesasMateriais = new JTable(despesasMateriaisTableModel);
        tabelaDespesasMateriais.setRowHeight(45);
        tabelaDespesasMateriais.getTableHeader().setBackground(LIGHT_GREY);
        tabelaDespesasMateriais.getTableHeader().setForeground(new Color(41, 50, 65));
        tabelaDespesasMateriais.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        tabelaDespesasMateriais.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        tabelaDespesasMateriais.setShowGrid(false);
        tabelaDespesasMateriais.setIntercellSpacing(new Dimension(0, 1));
        tabelaDespesasMateriais.setBackground(Color.WHITE);
        tabelaDespesasMateriais.setSelectionBackground(LIGHT_GREY.brighter());
        tabelaDespesasMateriais.setSelectionForeground(Color.BLACK);
        tabelaDespesasMateriais.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, LIGHT_GREY.darker()));
        
        // Centralizar cabeçalhos
        DefaultTableCellRenderer despesasHeaderRenderer = (DefaultTableCellRenderer) tabelaDespesasMateriais.getTableHeader()
                .getDefaultRenderer();
        despesasHeaderRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        // Renderer centralizado para valores monetários
        DefaultTableCellRenderer despesasMoneyRenderer = new DefaultTableCellRenderer() {
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
        tabelaDespesasMateriais.getColumnModel().getColumn(0).setPreferredWidth(300);
        tabelaDespesasMateriais.getColumnModel().getColumn(1).setPreferredWidth(150);
        tabelaDespesasMateriais.getColumnModel().getColumn(2).setPreferredWidth(150);
        
        // Renderer e Editor para coluna de Material (com ações integradas)
        tabelaDespesasMateriais.getColumnModel().getColumn(0).setCellRenderer(new MaterialDespesaCellRenderer());
        tabelaDespesasMateriais.getColumnModel().getColumn(0).setCellEditor(new MaterialDespesaCellEditor());
        
        // Aplicar renderer monetário nas outras colunas
        tabelaDespesasMateriais.getColumnModel().getColumn(1).setCellRenderer(despesasMoneyRenderer);
        tabelaDespesasMateriais.getColumnModel().getColumn(2).setCellRenderer(despesasMoneyRenderer);
        
        JScrollPane despesasMateriaisScrollPane = new JScrollPane(tabelaDespesasMateriais);
        despesasMateriaisScrollPane.setBorder(BorderFactory.createEmptyBorder());
        despesasMateriaisScrollPane.setBackground(Color.WHITE);
        despesasMateriaisPanel.add(despesasMateriaisScrollPane, BorderLayout.CENTER);

        tablesPanel.add(cobrancasPanel);
        tablesPanel.add(despesasMateriaisPanel);

        // Botões no rodapé
        JPanel buttonsContainer = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonsContainer.setBackground(Color.WHITE);
        buttonsContainer.setBorder(BorderFactory.createEmptyBorder(15, 20, 0, 20));

        RoundedButton btnAdicionarCobranca = new RoundedButton("+ Adicionar novo registro de cobrança");
        btnAdicionarCobranca.setBackground(PRIMARY_BLUE);
        btnAdicionarCobranca.setForeground(Color.WHITE);
        btnAdicionarCobranca.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnAdicionarCobranca.setPreferredSize(new Dimension(0, 55));
        btnAdicionarCobranca.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAdicionarCobranca.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                btnAdicionarCobranca.setBackground(HOVER_BLUE);
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                btnAdicionarCobranca.setBackground(PRIMARY_BLUE);
            }
        });
        btnAdicionarCobranca.addActionListener(e -> mostrarDialogoAdicionarCobranca());

        RoundedButton btnAdicionarDespesaMaterial = new RoundedButton("+ Adicionar nova despesa de material");
        btnAdicionarDespesaMaterial.setBackground(PRIMARY_GREEN);
        btnAdicionarDespesaMaterial.setForeground(Color.WHITE);
        btnAdicionarDespesaMaterial.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnAdicionarDespesaMaterial.setPreferredSize(new Dimension(0, 55));
        btnAdicionarDespesaMaterial.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAdicionarDespesaMaterial.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                btnAdicionarDespesaMaterial.setBackground(PRIMARY_GREEN.brighter());
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                btnAdicionarDespesaMaterial.setBackground(PRIMARY_GREEN);
            }
        });
        btnAdicionarDespesaMaterial.addActionListener(e -> mostrarDialogoAdicionarDespesaMaterial());

        buttonsContainer.add(btnAdicionarCobranca);
        buttonsContainer.add(btnAdicionarDespesaMaterial);

        add(headerPanel, BorderLayout.NORTH);
        add(tablesPanel, BorderLayout.CENTER);
        add(buttonsContainer, BorderLayout.SOUTH);
    }
    
    // --- Métodos de Dados ---
    
    public void carregarDados() {
        despesasList = controller.listarTodos();
        despesasFiltradas = new ArrayList<>(despesasList);
        atualizarTabelaCobrancas();
        carregarDespesasMateriais(); // Carregar todas as despesas materiais
    }
    
    private void filtrarCobrancas(String filtroCidade) {
        despesasFiltradas.clear();
        
        for (Despesa despesa : despesasList) {
            boolean match = true;
            
            // Filtro por cidade
            if (!filtroCidade.isEmpty()) {
                String cidade = despesa.getCidade();
                if (cidade == null || !cidade.toLowerCase().contains(filtroCidade.toLowerCase())) {
                    match = false;
                }
            }
            
            if (match) {
                despesasFiltradas.add(despesa);
            }
        }
        
        atualizarTabelaCobrancas();
    }
    
    private void atualizarTabelaCobrancas() {
        cobrancasTableModel.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        
        for (Despesa despesa : despesasFiltradas) {
            String cidade = despesa.getCidade() != null ? despesa.getCidade() : "";
            
            BigDecimal totalAdquirido = despesa.getTotal();
            BigDecimal totalDespesa = despesa.getDespesa();
            BigDecimal saldo = totalAdquirido.subtract(totalDespesa);
            
            String totalAdquiridoStr = String.format("%.2f", totalAdquirido);
            if (saldo.compareTo(BigDecimal.ZERO) < 0) {
                totalAdquiridoStr += " (" + String.format("%.2f", saldo) + ")";
            }
            
            Object[] row = {
                despesa.getData() != null ? sdf.format(despesa.getData()) : "---",
                cidade,
                despesa.getDespesa() != null ? String.format("%.2f", despesa.getDespesa()) : "0.00",
                totalAdquiridoStr,
                ""
            };
            cobrancasTableModel.addRow(row);
        }
    }
    
    private void carregarDespesasMateriais() {
        try {
            // Sempre carregar todas as despesas materiais (sem relacionamento com cobranças)
            despesasMateriaisList = controller.listarDespesasMateriais();
            
            if (despesasMateriaisList == null) {
                despesasMateriaisList = new ArrayList<>();
            }
            
            atualizarTabelaDespesasMateriais();
        } catch (Exception e) {
            System.err.println("Erro ao carregar despesas materiais: " + e.getMessage());
            e.printStackTrace();
            despesasMateriaisList = new ArrayList<>();
            atualizarTabelaDespesasMateriais();
        }
    }
    
    private void atualizarTabelaDespesasMateriais() {
        despesasMateriaisTableModel.setRowCount(0);
        
        if (despesasMateriaisList == null || despesasMateriaisList.isEmpty()) {
            return; // Não há despesas materiais para exibir
        }
        
        BigDecimal totalGasto = BigDecimal.ZERO;
        
        // Calcular total primeiro
        for (DespesaMaterial despesaMaterial : despesasMateriaisList) {
            BigDecimal gasto = despesaMaterial.getGasto() != null ? despesaMaterial.getGasto() : BigDecimal.ZERO;
            totalGasto = totalGasto.add(gasto);
        }
        
        // Adicionar linhas de despesas materiais
        for (int i = 0; i < despesasMateriaisList.size(); i++) {
            DespesaMaterial despesaMaterial = despesasMateriaisList.get(i);
            if (despesaMaterial == null) continue; // Pular se o objeto for nulo
            
            String nomeMaterial = despesaMaterial.getNome() != null ? despesaMaterial.getNome() : "---";
            BigDecimal gasto = despesaMaterial.getGasto() != null ? despesaMaterial.getGasto() : BigDecimal.ZERO;
            
            // Armazenar apenas o nome do material na primeira coluna (os botões serão renderizados pelo CellRenderer)
            Object[] row = {
                nomeMaterial,
                String.format("%.2f", gasto),
                i == 0 ? String.format("%.2f", totalGasto) : "" // Total gasto apenas na primeira linha
            };
            despesasMateriaisTableModel.addRow(row);
        }
    }
    
    // --- Classes de Renderer e Editor para Ações ---
    
    private class AcoesCobrancaCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
            panel.setBackground(isSelected ? table.getSelectionBackground() : Color.WHITE);
            panel.setOpaque(true);
            
            if (row < despesasFiltradas.size()) {
                TableActionButton btnEditar = new TableActionButton("Editar", INFO_BLUE);
                TableActionButton btnExcluir = new TableActionButton("Excluir", DANGER_RED);
                
                panel.add(btnEditar);
                panel.add(btnExcluir);
            }
            
            return panel;
        }
    }
    
    private class AcoesCobrancaCellEditor extends AbstractCellEditor implements javax.swing.table.TableCellEditor {
        private JPanel panel;
        private Despesa despesaAtual;
        
        public AcoesCobrancaCellEditor() {
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
            panel.setBackground(Color.WHITE);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            panel.removeAll();
            
            if (row < despesasFiltradas.size()) {
                despesaAtual = despesasFiltradas.get(row);

                TableActionButton btnEditar = new TableActionButton("Editar", INFO_BLUE);
                btnEditar.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        e.consume();
                        SwingUtilities.invokeLater(() -> editarCobranca(despesaAtual));
                        fireEditingStopped();
                    }
                });

                TableActionButton btnExcluir = new TableActionButton("Excluir", DANGER_RED);
                btnExcluir.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        e.consume();
                        SwingUtilities.invokeLater(() -> excluirCobranca(despesaAtual));
                        fireEditingStopped();
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
    
    // --- Classes de Renderer e Editor para Material de Despesas Materiais (com ações integradas) ---
    
    private class MaterialDespesaCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            
            JPanel panel = new JPanel(new BorderLayout(10, 0));
            panel.setBackground(isSelected ? table.getSelectionBackground() : Color.WHITE);
            panel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

            if (row < despesasMateriaisList.size()) {
                JLabel lblNome = new JLabel(value != null ? value.toString() : "---");
                lblNome.setFont(new Font("Segoe UI", Font.PLAIN, 15));
                lblNome.setForeground(Color.BLACK);
                
                JPanel acoesPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
                acoesPanel.setOpaque(false);

                JLabel lblEditar = new TableActionButton("Editar", INFO_BLUE);
                JLabel lblDeletar = new TableActionButton("Excluir", DANGER_RED);

                acoesPanel.add(lblEditar);
                acoesPanel.add(lblDeletar);

                panel.add(lblNome, BorderLayout.WEST);
                panel.add(acoesPanel, BorderLayout.EAST);
            }
            return panel;
        }
    }
    
    private class MaterialDespesaCellEditor extends AbstractCellEditor implements javax.swing.table.TableCellEditor {
        private JPanel panel;
        private DespesaMaterial despesaMaterialAtual;
        
        public MaterialDespesaCellEditor() {
            panel = new JPanel(new BorderLayout(10, 0));
            panel.setBackground(LIGHT_GREY.brighter());
            panel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            panel.removeAll();
            
            if (row < despesasMateriaisList.size()) {
                despesaMaterialAtual = despesasMateriaisList.get(row);

                JLabel lblNome = new JLabel(value != null ? value.toString() : "---");
                lblNome.setFont(new Font("Segoe UI", Font.PLAIN, 15));
                lblNome.setForeground(Color.BLACK);

                JPanel acoesPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
                acoesPanel.setOpaque(false);

                TableActionButton btnEditar = new TableActionButton("Editar", INFO_BLUE);
                btnEditar.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        e.consume();
                        SwingUtilities.invokeLater(() -> {
                            editarDespesaMaterial(despesaMaterialAtual);
                            fireEditingStopped();
                        });
                    }
                });

                TableActionButton btnDeletar = new TableActionButton("Excluir", DANGER_RED);
                btnDeletar.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        e.consume();
                        SwingUtilities.invokeLater(() -> {
                            excluirDespesaMaterial(despesaMaterialAtual);
                            fireEditingStopped();
                        });
                    }
                });

                acoesPanel.add(btnEditar);
                acoesPanel.add(btnDeletar);

                panel.add(lblNome, BorderLayout.WEST);
                panel.add(acoesPanel, BorderLayout.EAST);
            }
            
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return despesaMaterialAtual != null ? despesaMaterialAtual.getNome() : "";
        }
        
        @Override
        public boolean shouldSelectCell(EventObject anEvent) {
            return false;
        }
    }
    
    // --- Métodos de Ação ---
    
    private void editarCobranca(Despesa despesa) {
        mostrarDialogoEditarCobranca(despesa);
    }
    
    private void excluirCobranca(Despesa despesa) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Deseja realmente excluir este registro de cobrança?",
                "Confirmar exclusão", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (controller.deletar(despesa.getId())) {
                JOptionPane.showMessageDialog(this, "Registro excluído com sucesso!");
                carregarDados();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao excluir registro!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void excluirDespesaMaterial(DespesaMaterial despesaMaterial) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Deseja realmente excluir esta despesa de material?",
                "Confirmar exclusão", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (controller.deletarDespesaMaterial(despesaMaterial.getId())) {
                JOptionPane.showMessageDialog(this, "Despesa de material excluída com sucesso!");
                carregarDespesasMateriais();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao excluir despesa de material!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void mostrarDialogoAdicionarCobranca() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                "Adicionar Cobrança", true);
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

        PlaceholderTextField txtData = new PlaceholderTextField("Data (dd/MM/yyyy)");
        txtData.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtData.setPreferredSize(new Dimension(0, 40));
        txtData.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(LIGHT_GREY.darker(), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        formPanel.add(txtData, gbc);

        gbc.gridy++;
        PlaceholderTextField txtCidade = new PlaceholderTextField("Cidade");
        txtCidade.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtCidade.setPreferredSize(new Dimension(0, 40));
        txtCidade.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(LIGHT_GREY.darker(), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        formPanel.add(txtCidade, gbc);

        gbc.gridy++;
        PlaceholderTextField txtTotalDespesa = new PlaceholderTextField("Total Despesa");
        txtTotalDespesa.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtTotalDespesa.setPreferredSize(new Dimension(0, 40));
        txtTotalDespesa.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(LIGHT_GREY.darker(), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        formPanel.add(txtTotalDespesa, gbc);

        gbc.gridy++;
        PlaceholderTextField txtTotalGasto = new PlaceholderTextField("Total Gasto");
        txtTotalGasto.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtTotalGasto.setPreferredSize(new Dimension(0, 40));
        txtTotalGasto.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(LIGHT_GREY.darker(), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        formPanel.add(txtTotalGasto, gbc);
        
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
            String dataStr = txtData.getText().trim();
            String cidadeStr = txtCidade.getText().trim();
            String totalDespesaStr = txtTotalDespesa.getText().trim();
            String totalGastoStr = txtTotalGasto.getText().trim();

            if (dataStr.isEmpty() || cidadeStr.isEmpty() || totalDespesaStr.isEmpty() || totalGastoStr.isEmpty()) {
                JOptionPane.showMessageDialog(dialog,
                        "Por favor, preencha todos os campos obrigatórios!",
                        "Validação",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                java.util.Date utilDate = sdf.parse(dataStr);
                Date sqlDate = new Date(utilDate.getTime());
                
                BigDecimal totalDespesa = new BigDecimal(totalDespesaStr.replace(",", "."));
                BigDecimal totalGasto = new BigDecimal(totalGastoStr.replace(",", "."));

                if (controller.inserir(sqlDate, cidadeStr, totalDespesa, totalGasto)) {
                    JOptionPane.showMessageDialog(dialog, "Cobrança adicionada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                    carregarDados();
                } else {
                    JOptionPane.showMessageDialog(dialog,
                            "Erro ao adicionar cobrança!",
                            "Erro",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(dialog, "Data inválida! Use o formato dd/MM/yyyy", "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Valores de Total Despesa e Total Gasto devem ser números decimais!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        buttonPanel.add(btnCancelar);
        buttonPanel.add(btnSalvar);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        dialog.add(mainPanel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }
    
    private void mostrarDialogoEditarCobranca(Despesa despesa) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), 
                                 "Editar Cobrança", true);
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

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.2;
        JLabel lblData = new JLabel("Data:");
        lblData.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(lblData, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.8;
        JTextField txtData = new JTextField(despesa.getData() != null ? sdf.format(despesa.getData()) : "", 20);
        txtData.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtData.setPreferredSize(new Dimension(0, 35));
        txtData.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LIGHT_GREY.darker(), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        formPanel.add(txtData, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.2;
        JLabel lblCidade = new JLabel("Cidade:");
        lblCidade.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(lblCidade, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.8;
        String cidadeAtual = despesa.getCidade() != null ? despesa.getCidade() : "";
        JTextField txtCidade = new JTextField(cidadeAtual, 20);
        txtCidade.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtCidade.setPreferredSize(new Dimension(0, 35));
        txtCidade.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LIGHT_GREY.darker(), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        formPanel.add(txtCidade, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.2;
        JLabel lblTotalDespesa = new JLabel("Total Despesa:");
        lblTotalDespesa.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(lblTotalDespesa, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.8;
        JTextField txtTotalDespesa = new JTextField(despesa.getDespesa() != null ? String.format("%.2f", despesa.getDespesa()) : "0.00", 20);
        txtTotalDespesa.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtTotalDespesa.setPreferredSize(new Dimension(0, 35));
        txtTotalDespesa.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LIGHT_GREY.darker(), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        formPanel.add(txtTotalDespesa, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.2;
        JLabel lblTotalGasto = new JLabel("Total Gasto:");
        lblTotalGasto.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(lblTotalGasto, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.8;
        JTextField txtTotalGasto = new JTextField(despesa.getTotal() != null ? String.format("%.2f", despesa.getTotal()) : "0.00", 20);
        txtTotalGasto.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtTotalGasto.setPreferredSize(new Dimension(0, 35));
        txtTotalGasto.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LIGHT_GREY.darker(), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        formPanel.add(txtTotalGasto, gbc);

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
            String dataStr = txtData.getText().trim();
            String cidadeStr = txtCidade.getText().trim();
            String totalDespesaStr = txtTotalDespesa.getText().trim();
            String totalGastoStr = txtTotalGasto.getText().trim();

            if (dataStr.isEmpty() || cidadeStr.isEmpty() || totalDespesaStr.isEmpty() || totalGastoStr.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Por favor, preencha todos os campos obrigatórios!", "Validação",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                SimpleDateFormat sdfEdit = new SimpleDateFormat("dd/MM/yyyy");
                java.util.Date utilDate = sdfEdit.parse(dataStr);
                Date sqlDate = new Date(utilDate.getTime());
                
                BigDecimal totalDespesa = new BigDecimal(totalDespesaStr.replace(",", "."));
                BigDecimal totalGasto = new BigDecimal(totalGastoStr.replace(",", "."));
                
                despesa.setData(sqlDate);
                despesa.setCidade(cidadeStr);
                despesa.setDespesa(totalDespesa);
                despesa.setTotal(totalGasto);
                
                if (controller.atualizar(despesa)) {
                    JOptionPane.showMessageDialog(dialog, "Cobrança atualizada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                    carregarDados();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Erro ao atualizar cobrança!", "Erro",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(dialog, "Data inválida! Use o formato dd/MM/yyyy", "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Valores de Total Despesa e Total Gasto devem ser números decimais!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancelar.addActionListener(e -> dialog.dispose());

        buttonPanel.add(btnCancelar);
        buttonPanel.add(btnSalvar);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    
    private void editarDespesaMaterial(DespesaMaterial despesaMaterial) {
        mostrarDialogoEditarDespesaMaterial(despesaMaterial);
    }
    
    private void mostrarDialogoAdicionarDespesaMaterial() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                "Adicionar Despesa de Material", true);
        dialog.setSize(450, 300);
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

        PlaceholderTextField txtNome = new PlaceholderTextField("Nome do Material");
        txtNome.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtNome.setPreferredSize(new Dimension(0, 40));
        txtNome.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(LIGHT_GREY.darker(), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        formPanel.add(txtNome, gbc);

        gbc.gridy++;
        PlaceholderTextField txtGasto = new PlaceholderTextField("Gasto");
        txtGasto.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtGasto.setPreferredSize(new Dimension(0, 40));
        txtGasto.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(LIGHT_GREY.darker(), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        formPanel.add(txtGasto, gbc);
        
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
            String nome = txtNome.getText().trim();
            if (nome.isEmpty()) {
                JOptionPane.showMessageDialog(dialog,
                        "Por favor, preencha o nome do material!",
                        "Validação",
                        JOptionPane.WARNING_MESSAGE);
                txtNome.requestFocus();
                return;
            }

            String gastoStr = txtGasto.getText().trim();
            if (gastoStr.isEmpty()) {
                JOptionPane.showMessageDialog(dialog,
                        "Por favor, preencha o gasto!",
                        "Validação",
                        JOptionPane.WARNING_MESSAGE);
                txtGasto.requestFocus();
                return;
            }

            try {
                BigDecimal gasto = new BigDecimal(gastoStr.replace(",", "."));

                if (controller.inserirDespesaMaterial(nome, null, gasto)) {
                    JOptionPane.showMessageDialog(dialog, "Despesa de material adicionada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                    carregarDespesasMateriais();
                } else {
                    JOptionPane.showMessageDialog(dialog,
                            "Erro ao adicionar despesa de material!",
                            "Erro",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Valor de gasto deve ser um número decimal!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        buttonPanel.add(btnCancelar);
        buttonPanel.add(btnSalvar);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        dialog.add(mainPanel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }
    
    private void mostrarDialogoEditarDespesaMaterial(DespesaMaterial despesaMaterial) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), 
                                 "Editar Despesa de Material", true);
        dialog.setSize(450, 300); 
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
        JLabel lblMaterial = new JLabel("Material:");
        lblMaterial.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(lblMaterial, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.8;
        JTextField txtNome = new JTextField(despesaMaterial.getNome() != null ? despesaMaterial.getNome() : "", 20);
        txtNome.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtNome.setPreferredSize(new Dimension(0, 35));
        txtNome.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LIGHT_GREY.darker(), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        formPanel.add(txtNome, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.2;
        JLabel lblGasto = new JLabel("Gasto:");
        lblGasto.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(lblGasto, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.8;
        JTextField txtGasto = new JTextField(despesaMaterial.getGasto() != null ? String.format("%.2f", despesaMaterial.getGasto()) : "0.00", 20);
        txtGasto.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtGasto.setPreferredSize(new Dimension(0, 35));
        txtGasto.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LIGHT_GREY.darker(), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        formPanel.add(txtGasto, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        RoundedButton btnSalvar = new RoundedButton("Salvar");
        btnSalvar.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnSalvar.setBackground(PRIMARY_GREEN);
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.setPreferredSize(new Dimension(110, 40));
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
            String nome = txtNome.getText().trim();
            if (nome.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "O nome do material não pode estar vazio!", "Validação",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            String gastoStr = txtGasto.getText().trim();
            if (gastoStr.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "O gasto não pode estar vazio!", "Validação",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            try {
                BigDecimal gasto = new BigDecimal(gastoStr.replace(",", "."));
                
                despesaMaterial.setNome(nome);
                despesaMaterial.setGasto(gasto);
                
                if (controller.atualizarDespesaMaterial(despesaMaterial)) {
                    JOptionPane.showMessageDialog(dialog, "Despesa de material atualizada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                    carregarDespesasMateriais();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Erro ao atualizar despesa de material!", "Erro",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Valor de gasto deve ser um número decimal!", "Erro", JOptionPane.ERROR_MESSAGE);
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
