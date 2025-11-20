package com.bidesk.view;

import com.bidesk.controller.EstoqueController;
import com.bidesk.model.Material;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.AbstractCellEditor;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.EventObject;

public class EstoqueView extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private EstoqueController controller;
    private java.util.List<Material> materiaisList;

    // Constantes de Cores
    private static final Color PRIMARY_GREEN = new Color(39, 174, 96);
    private static final Color PRIMARY_BLUE = new Color(41, 128, 185);
    private static final Color HOVER_BLUE = new Color(52, 152, 219);
    private static final Color DANGER_RED = new Color(231, 76, 60);
    private static final Color WARNING_ORANGE = new Color(243, 156, 18);
    private static final Color INFO_BLUE = new Color(52, 152, 219);
    private static final Color LIGHT_GREY = new Color(236, 240, 241);

    public EstoqueView() {
        controller = new EstoqueController();
        materiaisList = new java.util.ArrayList<>();
        initializeComponents();
        carregarDados();
    }

    // --- Componentes Auxiliares de Estilo ---

    private class RoundedPanel extends JPanel {
        private int cornerRadius = 15;
        private Color backgroundColor = Color.WHITE;

        public RoundedPanel(LayoutManager layout, Color bgColor, int radius) {
            super(layout);
            this.backgroundColor = bgColor;
            this.cornerRadius = radius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(backgroundColor);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius));
            g2.dispose();
        }
    }

    /**
     * Botão arredondado com sombreamento
     */
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
                    setToolTipText(getToolTipText());
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
        JLabel titleLabel = new JLabel("Estoque");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        headerPanel.add(titleLabel);

        // Tabela
        String[] columns = { "Materiais", "Quantidade" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0;
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(45);
        table.getTableHeader().setBackground(LIGHT_GREY);
        table.getTableHeader().setForeground(new Color(41, 50, 65));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setBackground(Color.WHITE); 
        table.setSelectionBackground(LIGHT_GREY.brighter());
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, LIGHT_GREY.darker()));
        
        DefaultTableCellRenderer centerRenderer = (DefaultTableCellRenderer) table.getTableHeader()
                .getDefaultRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        table.getColumnModel().getColumn(0).setPreferredWidth(500);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setBackground(Color.WHITE);

        // Botão Principal com sombreamento
        RoundedButton btnAdicionar = new RoundedButton("+ Adicionar novo material");
        btnAdicionar.setBackground(PRIMARY_BLUE); 
        btnAdicionar.setForeground(Color.WHITE);
        btnAdicionar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnAdicionar.setPreferredSize(new Dimension(0, 55));
        btnAdicionar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnAdicionar.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnAdicionar.setBackground(HOVER_BLUE);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnAdicionar.setBackground(PRIMARY_BLUE);
            }
        });
        btnAdicionar.addActionListener(e -> mostrarDialogoAdicionar()); 

        // Painel inferior apenas com o botão
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        bottomPanel.add(btnAdicionar, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        
        setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
    }
    
    // --- Métodos de Dados e Configuração de Tabela ---

    public void carregarDados() {
        tableModel.setRowCount(0);
        materiaisList = controller.listarTodos();

        for (Material material : materiaisList) {
            Object[] row = {
                    material.getNome(),
                    material.getQuantidade()
            };
            tableModel.addRow(row);
        }

        table.getColumn("Materiais").setCellRenderer(new MateriaisCellRenderer());
        table.getColumn("Materiais").setCellEditor(new MateriaisCellEditor());
        table.getColumn("Quantidade").setCellRenderer(new StatusColorRenderer());
    }

    private Color getStatusColor(Material.StatusMaterial status) {
        switch (status) {
            case VAZIO:
                return DANGER_RED;
            case BAIXO:
                return WARNING_ORANGE; 
            case ALTO:
                return PRIMARY_GREEN; 
            default:
                return Color.DARK_GRAY;
        }
    }

    private class MateriaisCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            
            JPanel panel = new JPanel(new BorderLayout(10, 0));
            panel.setBackground(isSelected ? table.getSelectionBackground() : Color.WHITE);
            panel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

            if (row < materiaisList.size()) {
                JLabel lblNome = new JLabel(value.toString());
                lblNome.setFont(new Font("Segoe UI", Font.PLAIN, 15));
                lblNome.setForeground(Color.BLACK);
                
                JPanel acoesPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
                acoesPanel.setOpaque(false);

                JLabel lblAdicionar = new TableActionButton("+", PRIMARY_GREEN);
                JLabel lblRemover = new TableActionButton("-", WARNING_ORANGE.darker());
                JLabel lblEditar = new TableActionButton("Editar", INFO_BLUE);
                JLabel lblDeletar = new TableActionButton("Excluir", DANGER_RED);

                lblAdicionar.setFont(new Font("Segoe UI", Font.BOLD, 20));
                lblRemover.setFont(new Font("Segoe UI", Font.BOLD, 20));

                acoesPanel.add(lblAdicionar);
                acoesPanel.add(lblRemover);
                acoesPanel.add(lblEditar);
                acoesPanel.add(lblDeletar);

                panel.add(lblNome, BorderLayout.WEST);
                panel.add(acoesPanel, BorderLayout.EAST);
            }
            return panel;
        }
    }

    private class MateriaisCellEditor extends AbstractCellEditor implements TableCellEditor {
        private JPanel panel;
        private Material materialAtual;
        
        public MateriaisCellEditor() {
            panel = new JPanel(new BorderLayout(10, 0));
            panel.setBackground(LIGHT_GREY.brighter());
            panel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
                int column) {
            panel.removeAll();
            
            if (row < materiaisList.size()) {
                materialAtual = materiaisList.get(row);

                JLabel lblNome = new JLabel(value.toString());
                lblNome.setFont(new Font("Segoe UI", Font.PLAIN, 15));
                lblNome.setForeground(Color.BLACK);

                JPanel acoesPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
                acoesPanel.setOpaque(false);

                TableActionButton btnAdicionar = new TableActionButton("+", PRIMARY_GREEN);
                btnAdicionar.setFont(new Font("Segoe UI", Font.BOLD, 20));
                btnAdicionar.setToolTipText("Adicionar +1");
                btnAdicionar.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        fireEditingStopped();
                        adicionarUm(materialAtual);
                    }
                });

                TableActionButton btnRemover = new TableActionButton("-", WARNING_ORANGE.darker());
                btnRemover.setFont(new Font("Segoe UI", Font.BOLD, 20));
                btnRemover.setToolTipText("Remover -1");
                btnRemover.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        fireEditingStopped();
                        removerUm(materialAtual);
                    }
                });

                TableActionButton btnEditar = new TableActionButton("Editar", INFO_BLUE);
                btnEditar.setToolTipText("Editar nome e quantidade");
                btnEditar.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        fireEditingStopped();
                        mostrarDialogoEditarMaterial(materialAtual); 
                    }
                });

                TableActionButton btnDeletar = new TableActionButton("Excluir", DANGER_RED);
                btnDeletar.setToolTipText("Excluir material");
                btnDeletar.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        fireEditingStopped();
                        deletarMaterial(materialAtual);
                    }
                });

                acoesPanel.add(btnAdicionar);
                acoesPanel.add(btnRemover);
                acoesPanel.add(btnEditar);
                acoesPanel.add(btnDeletar);

                panel.add(lblNome, BorderLayout.WEST);
                panel.add(acoesPanel, BorderLayout.EAST);
            }
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return materialAtual != null ? materialAtual.getNome() : "";
        }
        
        @Override
        public boolean shouldSelectCell(EventObject anEvent) {
            return false;
        }
    }

    private class StatusColorRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            
            Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setHorizontalAlignment(JLabel.CENTER);
            setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, LIGHT_GREY)); 
            
            if (row < materiaisList.size()) {
                Material material = materiaisList.get(row);
                Color statusColor = getStatusColor(material.getStatus());
                
                if (isSelected) {
                    cell.setBackground(table.getSelectionBackground());
                    cell.setForeground(statusColor.darker());
                } else {
                    cell.setBackground(Color.WHITE);
                    cell.setForeground(statusColor); 
                }
            } else {
                cell.setForeground(Color.BLACK); 
                cell.setBackground(Color.WHITE);
            }

            return cell;
        }
    }

    // --- Métodos de Diálogo e Ações ---

    private void mostrarDialogoAdicionar() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                "Adicionar Material", true);
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
        JSpinner spinnerQuantidade = new JSpinner(
                new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
        spinnerQuantidade.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        spinnerQuantidade.setPreferredSize(new Dimension(0, 40));

        JComponent editor = spinnerQuantidade.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            JTextField spinnerField = ((JSpinner.DefaultEditor) editor).getTextField();
            spinnerField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(LIGHT_GREY.darker(), 1),
                    BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        }

        formPanel.add(spinnerQuantidade, gbc);
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
        btnCancelar.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCancelar.setBackground(LIGHT_GREY.darker());
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
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
        btnSalvar.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSalvar.setBackground(HOVER_BLUE);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSalvar.setBackground(PRIMARY_BLUE);
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

            int quantidade = (Integer) spinnerQuantidade.getValue();
            String resultado = controller.inserir(nome, quantidade); 

            if (resultado == null || resultado.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Material adicionado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
                carregarDados(); 
            } else {
                JOptionPane.showMessageDialog(dialog,
                        "Erro ao adicionar material:\n" + resultado,
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

    private void mostrarDialogoEditarMaterial(Material material) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), 
                                 "Editar Material", true);
        dialog.setSize(450, 280); 
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
        JTextField txtNome = new JTextField(material.getNome(), 20);
        txtNome.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtNome.setPreferredSize(new Dimension(0, 35));
        txtNome.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LIGHT_GREY.darker(), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        formPanel.add(txtNome, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.2;
        JLabel lblQuantidade = new JLabel("Quantidade:");
        lblQuantidade.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(lblQuantidade, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.8;
        JSpinner spinnerQuantidade = new JSpinner(
            new SpinnerNumberModel(material.getQuantidade(), 0, Integer.MAX_VALUE, 1));
        spinnerQuantidade.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        spinnerQuantidade.setPreferredSize(new Dimension(0, 35));
        
        JComponent editor = spinnerQuantidade.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            JTextField spinnerField = ((JSpinner.DefaultEditor) editor).getTextField();
            spinnerField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(LIGHT_GREY.darker(), 1),
                    BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        }
        formPanel.add(spinnerQuantidade, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        RoundedButton btnSalvar = new RoundedButton("Salvar");
        btnSalvar.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnSalvar.setBackground(PRIMARY_BLUE);
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.setPreferredSize(new Dimension(110, 40));
        btnSalvar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSalvar.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSalvar.setBackground(HOVER_BLUE);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSalvar.setBackground(PRIMARY_BLUE);
            }
        });
        
        RoundedButton btnCancelar = new RoundedButton("Cancelar");
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnCancelar.setBackground(LIGHT_GREY);
        btnCancelar.setForeground(new Color(41, 50, 65));
        btnCancelar.setPreferredSize(new Dimension(110, 40));
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancelar.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCancelar.setBackground(LIGHT_GREY.darker());
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCancelar.setBackground(LIGHT_GREY);
            }
        });

        btnSalvar.addActionListener(e -> {
            String novoNome = txtNome.getText().trim();
            int novaQuantidade = (Integer) spinnerQuantidade.getValue();

            if (novoNome.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "O nome não pode estar vazio!", "Validação",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            material.setNome(novoNome);
            material.setQuantidade(novaQuantidade); 
            
            if (controller.atualizar(material)) {
                JOptionPane.showMessageDialog(dialog, "Material atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
                carregarDados();
            } else {
                JOptionPane.showMessageDialog(dialog, "Erro ao atualizar material!", "Erro",
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

    private void deletarMaterial(Material material) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Deseja realmente excluir o material \"" + material.getNome() + "\"?",
                "Confirmar exclusão", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            if (controller.deletar(material.getId())) {
                JOptionPane.showMessageDialog(this, "Material excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                carregarDados();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao excluir material!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void adicionarUm(Material material) {
        material.setQuantidade(material.getQuantidade() + 1);
        if (controller.atualizar(material)) {
            carregarDados();
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar quantidade!", "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removerUm(Material material) {
        if (material.getQuantidade() > 0) {
            material.setQuantidade(material.getQuantidade() - 1);
            if (controller.atualizar(material)) {
                carregarDados();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao atualizar quantidade!", "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}