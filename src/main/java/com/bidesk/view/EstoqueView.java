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

// IMPORTANTE: Você precisa da classe auxiliar PlaceholderTextField para esta View compilar.

public class EstoqueView extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private EstoqueController controller;
    private java.util.List<Material> materiaisList;

    public EstoqueView() {
        controller = new EstoqueController();
        materiaisList = new java.util.ArrayList<>();
        initializeComponents();
        setupLayout();
        carregarDados(); 
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);

        // Título
        JLabel titleLabel = new JLabel("Estoque");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Tabela
        String[] columns = { "Materiais", "Quantidade" }; 
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // A Coluna Materiais (0) é editável para ativar o Editor (botões de ação)
                return column == 0; 
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(50);
        table.getTableHeader().setBackground(new Color(200, 230, 200));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.setFont(new Font("Arial", Font.PLAIN, 14));

        DefaultTableCellRenderer centerRenderer = (DefaultTableCellRenderer) table.getTableHeader()
                .getDefaultRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        // Ajustar largura das colunas
        table.getColumnModel().getColumn(0).setPreferredWidth(500);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        JButton btnAdicionar = new JButton("+ Adicionar novo material");
        btnAdicionar.setBackground(new Color(51, 171, 118));
        btnAdicionar.setForeground(Color.WHITE);
        btnAdicionar.setFont(new Font("Arial", Font.PLAIN, 14));
        btnAdicionar.setPreferredSize(new Dimension(0, 50));
        btnAdicionar.setOpaque(true);
        btnAdicionar.setBorderPainted(false);
        btnAdicionar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAdicionar.addActionListener(e -> mostrarDialogoAdicionar()); // Chave para o erro resolvido

        // Efeito hover (correto)
        btnAdicionar.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnAdicionar.setBackground(new Color(39, 140, 98));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnAdicionar.setBackground(new Color(51, 171, 118));
            }
        });

        // Legenda (correta)
        JPanel legendPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        legendPanel.setBackground(Color.WHITE);
        legendPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel legendLabel = new JLabel("Legenda: ");
        JLabel redLabel = new JLabel("■ Vazio");
        redLabel.setForeground(Color.RED);
        JLabel yellowLabel = new JLabel("■ Baixo");
        yellowLabel.setForeground(new Color(255, 200, 0));
        JLabel greenLabel = new JLabel("■ Alto");
        greenLabel.setForeground(new Color(0, 150, 0));

        legendPanel.add(legendLabel);
        legendPanel.add(redLabel);
        legendPanel.add(Box.createHorizontalStrut(10));
        legendPanel.add(yellowLabel);
        legendPanel.add(Box.createHorizontalStrut(10));
        legendPanel.add(greenLabel);

        add(titleLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(legendPanel, BorderLayout.EAST);
        bottomPanel.add(btnAdicionar, BorderLayout.CENTER);
        bottomPanel.setBackground(Color.WHITE);

        add(bottomPanel, BorderLayout.SOUTH);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }

    private void setupLayout() {
        // Layout já configurado em initializeComponents
    }

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

        // Configurar renderer e editor customizados
        table.getColumn("Materiais").setCellRenderer(new MateriaisCellRenderer());
        table.getColumn("Materiais").setCellEditor(new MateriaisCellEditor());
        table.getColumn("Quantidade").setCellRenderer(new StatusColorRenderer());
    }

    private Color getStatusColor(Material.StatusMaterial status) {
        switch (status) {
            case VAZIO:
                return Color.RED;
            case BAIXO:
                return new Color(255, 200, 0); 
            case ALTO:
                return new Color(0, 150, 0); 
            default:
                return Color.BLACK;
        }
    }

    // Renderer para a coluna Materiais (com ações integradas)
    private class MateriaisCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            JPanel panel = new JPanel(new BorderLayout(10, 0));
            panel.setBackground(isSelected ? table.getSelectionBackground() : Color.WHITE);
            panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

            if (row < materiaisList.size()) {
                // Nome do material à esquerda
                JLabel lblNome = new JLabel(value.toString());
                lblNome.setFont(new Font("Arial", Font.PLAIN, 14));
                
                // Painel de ações à direita (apenas para visualização do Render)
                JPanel acoesPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
                acoesPanel.setOpaque(false);

                JLabel lblAdicionar = new JLabel("+");
                lblAdicionar.setFont(new Font("Arial", Font.BOLD, 20));
                lblAdicionar.setForeground(new Color(0, 150, 0));
                
                JLabel lblRemover = new JLabel("-");
                lblRemover.setFont(new Font("Arial", Font.BOLD, 20));
                lblRemover.setForeground(new Color(255, 100, 0));
                
                JLabel lblEditar = new JLabel("Editar");
                lblEditar.setFont(new Font("Arial", Font.PLAIN, 14));
                lblEditar.setForeground(new Color(0, 100, 200));
                
                JLabel lblDeletar = new JLabel("Excluir");
                lblDeletar.setFont(new Font("Arial", Font.PLAIN, 14));
                lblDeletar.setForeground(Color.RED);

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

    // Editor para a coluna Materiais (com ações clicáveis)
    private class MateriaisCellEditor extends AbstractCellEditor implements TableCellEditor {
        private JPanel panel;
        private Material materialAtual;
        private int rowAtual;

        public MateriaisCellEditor() {
            panel = new JPanel(new BorderLayout(10, 0));
            panel.setBackground(Color.WHITE);
            panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
                int column) {
            panel.removeAll();
            
            if (row < materiaisList.size()) {
                materialAtual = materiaisList.get(row);
                rowAtual = row;

                // Nome do material à esquerda
                JLabel lblNome = new JLabel(value.toString());
                lblNome.setFont(new Font("Arial", Font.PLAIN, 14));

                // Painel de ações à direita
                JPanel acoesPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
                acoesPanel.setOpaque(false);

                // Botão Adicionar (+1 direto)
                JLabel btnAdicionar = new JLabel("+");
                btnAdicionar.setFont(new Font("Arial", Font.BOLD, 20));
                btnAdicionar.setForeground(new Color(0, 150, 0));
                btnAdicionar.setToolTipText("Adicionar +1");
                btnAdicionar.setCursor(new Cursor(Cursor.HAND_CURSOR));
                btnAdicionar.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        fireEditingStopped();
                        adicionarUm(materialAtual);
                    }
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        btnAdicionar.setFont(new Font("Arial", Font.BOLD, 24));
                    }
                    @Override
                    public void mouseExited(MouseEvent e) {
                        btnAdicionar.setFont(new Font("Arial", Font.BOLD, 20));
                    }
                });

                // Botão Remover (-1 direto)
                JLabel btnRemover = new JLabel("-");
                btnRemover.setFont(new Font("Arial", Font.BOLD, 20));
                btnRemover.setForeground(new Color(255, 100, 0));
                btnRemover.setToolTipText("Remover -1");
                btnRemover.setCursor(new Cursor(Cursor.HAND_CURSOR));
                btnRemover.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        fireEditingStopped();
                        removerUm(materialAtual);
                    }
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        btnRemover.setFont(new Font("Arial", Font.BOLD, 24));
                    }
                    @Override
                    public void mouseExited(MouseEvent e) {
                        btnRemover.setFont(new Font("Arial", Font.BOLD, 20));
                    }
                });

                // Botão Editar (nome e quantidade)
                JLabel btnEditar = new JLabel("Editar");
                btnEditar.setFont(new Font("Arial", Font.PLAIN, 14));
                btnEditar.setForeground(new Color(0, 100, 200));
                btnEditar.setToolTipText("Editar nome e quantidade");
                btnEditar.setCursor(new Cursor(Cursor.HAND_CURSOR));
                btnEditar.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        fireEditingStopped();
                        // CHAMA O NOVO MÉTODO DE EDIÇÃO DE NOME E QUANTIDADE
                        mostrarDialogoEditarMaterial(materialAtual); 
                    }
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        btnEditar.setFont(new Font("Arial", Font.BOLD, 14));
                    }
                    @Override
                    public void mouseExited(MouseEvent e) {
                        btnEditar.setFont(new Font("Arial", Font.PLAIN, 14));
                    }
                });

                // Botão Excluir
                JLabel btnDeletar = new JLabel("Excluir");
                btnDeletar.setFont(new Font("Arial", Font.PLAIN, 14));
                btnDeletar.setForeground(Color.RED);
                btnDeletar.setToolTipText("Excluir material");
                btnDeletar.setCursor(new Cursor(Cursor.HAND_CURSOR));
                btnDeletar.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        fireEditingStopped();
                        deletarMaterial(materialAtual);
                    }
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        btnDeletar.setFont(new Font("Arial", Font.BOLD, 14));
                    }
                    @Override
                    public void mouseExited(MouseEvent e) {
                        btnDeletar.setFont(new Font("Arial", Font.PLAIN, 14));
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
    }

    // Renderer para colorir a coluna de quantidade baseado no status
    private class StatusColorRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            setHorizontalAlignment(JLabel.CENTER);

            if (row < materiaisList.size()) {
                Material material = materiaisList.get(row);
                Material.StatusMaterial status = material.getStatus();
                Color statusColor = getStatusColor(status);
                
                if (isSelected) {
                    cell.setBackground(table.getSelectionBackground());
                    cell.setForeground(statusColor.brighter().brighter()); 
                } else {
                    cell.setBackground(table.getBackground());
                    cell.setForeground(statusColor); 
                }
            } else {
                cell.setForeground(Color.BLACK); 
                cell.setBackground(table.getBackground());
            }

            return cell;
        }
    }

    // --- Métodos de Diálogo e Ações ---

    private void mostrarDialogoAdicionar() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                "Adicionar Material", true);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(Color.WHITE);

        // Painel principal com padding
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // Painel do formulário
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // CAMPO NOME (PlaceholderTextField)
        PlaceholderTextField txtNome = new PlaceholderTextField("Nome");
        txtNome.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtNome.setPreferredSize(new Dimension(0, 40));
        txtNome.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        formPanel.add(txtNome, gbc);

        // CAMPO QUANTIDADE
        gbc.gridy++;
        JSpinner spinnerQuantidade = new JSpinner(
                new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
        spinnerQuantidade.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        spinnerQuantidade.setPreferredSize(new Dimension(0, 40));

        JComponent editor = spinnerQuantidade.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            JTextField spinnerField = ((JSpinner.DefaultEditor) editor).getTextField();
            spinnerField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                    BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        }

        formPanel.add(spinnerQuantidade, gbc);
        mainPanel.add(formPanel, BorderLayout.CENTER);

        // BOTÕES
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));

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
                JOptionPane.showMessageDialog(dialog, "Material adicionado com sucesso!");
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

    // MÉTODO MODIFICADO: Edita nome e quantidade.
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

        // CAMPO NOME
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.2;
        formPanel.add(new JLabel("Nome:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.8;
        JTextField txtNome = new JTextField(material.getNome(), 20);
        txtNome.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(txtNome, gbc);

        // CAMPO QUANTIDADE
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.2;
        formPanel.add(new JLabel("Quantidade:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.8;
        JSpinner spinnerQuantidade = new JSpinner(
            new SpinnerNumberModel(material.getQuantidade(), 0, Integer.MAX_VALUE, 1));
        spinnerQuantidade.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        JComponent editor = spinnerQuantidade.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            JTextField spinnerField = ((JSpinner.DefaultEditor) editor).getTextField();
            spinnerField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                    BorderFactory.createEmptyBorder(2, 5, 2, 5)));
        }
        formPanel.add(spinnerQuantidade, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSalvar.setBackground(new Color(51, 171, 118));
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.setPreferredSize(new Dimension(100, 35));
        btnSalvar.setBorderPainted(false);
        btnSalvar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCancelar.setBackground(new Color(232, 236, 240));
        btnCancelar.setForeground(new Color(41, 50, 65));
        btnCancelar.setPreferredSize(new Dimension(100, 35));
        btnCancelar.setBorderPainted(false);
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnSalvar.addActionListener(e -> {
            String novoNome = txtNome.getText().trim();
            int novaQuantidade = (Integer) spinnerQuantidade.getValue();

            if (novoNome.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "O nome não pode estar vazio!", "Validação",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Aplica as novas alterações
            material.setNome(novoNome);
            material.setQuantidade(novaQuantidade); 
            
            if (controller.atualizar(material)) {
                JOptionPane.showMessageDialog(dialog, "Material atualizado com sucesso!");
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
                JOptionPane.showMessageDialog(this, "Material excluído com sucesso!");
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
        } else {
            JOptionPane.showMessageDialog(this, "A quantidade não pode ser negativa!", "Aviso",
                    JOptionPane.WARNING_MESSAGE);
        }
    }
}