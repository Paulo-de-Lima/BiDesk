package com.bidesk.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainView extends JFrame {
    private JPanel sidebar;
    private JPanel contentArea;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    
    // Views dos módulos
    private EstoqueView estoqueView;
    private ClientesView clientesView;
    private FinanceiroView financeiroView;
    private ManutencaoView manutencaoView;
    
    // Botões do menu
    private JButton btnEstoque;
    private JButton btnClientes;
    private JButton btnFinanceiro;
    private JButton btnManutencao;
    private String viewAtual = "ESTOQUE";
    
    public MainView() {
        initializeComponents();
        setupLayout();
        setupSidebar();
        setupContentArea();
    }
    
    private void initializeComponents() {
        setTitle("BiDesk");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        
        // Inicializar views
        estoqueView = new EstoqueView();
        clientesView = new ClientesView();
        financeiroView = new FinanceiroView();
        manutencaoView = new ManutencaoView();
    }
    
    private void setupLayout() {
        sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.setBackground(new Color(245, 245, 245));
        sidebar.setLayout(new BorderLayout());
        
        contentArea = new JPanel(new BorderLayout());
        contentArea.setBackground(Color.WHITE);
        
        add(sidebar, BorderLayout.WEST);
        add(contentArea, BorderLayout.CENTER);
    }
    
    private void setupSidebar() {
        // Logo
        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(new Color(76, 175, 80));
        logoPanel.setPreferredSize(new Dimension(200, 80));
        logoPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        
        JLabel logoLabel = new JLabel("bidesk");
        logoLabel.setFont(new Font("Arial", Font.BOLD, 24));
        logoLabel.setForeground(Color.BLACK);
        logoPanel.add(logoLabel);
        
        // Menu de navegação
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(new Color(245, 245, 245));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        
        btnEstoque = createMenuButton("Estoque");
        btnClientes = createMenuButton("Clientes");
        btnFinanceiro = createMenuButton("Financeiro");
        btnManutencao = createMenuButton("Manutenção");
        
        btnEstoque.addActionListener(e -> showView("ESTOQUE"));
        btnClientes.addActionListener(e -> showView("CLIENTES"));
        btnFinanceiro.addActionListener(e -> showView("FINANCEIRO"));
        btnManutencao.addActionListener(e -> showView("MANUTENCAO"));
        
        // Destacar botão inicial
        highlightButton(btnEstoque);
        
        menuPanel.add(btnEstoque);
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(btnClientes);
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(btnFinanceiro);
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(btnManutencao);
        
        // Botão Sair
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(245, 245, 245));
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JButton btnSair = new JButton("Sair");
        btnSair.setBackground(new Color(200, 200, 200));
        btnSair.setForeground(Color.BLACK);
        btnSair.setFont(new Font("Arial", Font.PLAIN, 14));
        btnSair.setPreferredSize(new Dimension(180, 40));
        btnSair.addActionListener(e -> System.exit(0));
        
        bottomPanel.add(btnSair, BorderLayout.CENTER);
        
        sidebar.add(logoPanel, BorderLayout.NORTH);
        sidebar.add(menuPanel, BorderLayout.CENTER);
        sidebar.add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setBackground(new Color(245, 245, 245));
        button.setForeground(Color.BLACK);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(180, 40));
        button.setMaximumSize(new Dimension(180, 40));
        button.setMinimumSize(new Dimension(180, 40));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (!button.getBackground().equals(new Color(200, 230, 200))) {
                    button.setBackground(new Color(220, 240, 220));
                }
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!button.getBackground().equals(new Color(200, 230, 200))) {
                    button.setBackground(new Color(245, 245, 245));
                }
            }
        });
        
        return button;
    }
    
    private void setupContentArea() {
        cardPanel.add(estoqueView, "ESTOQUE");
        cardPanel.add(clientesView, "CLIENTES");
        cardPanel.add(financeiroView, "FINANCEIRO");
        cardPanel.add(manutencaoView, "MANUTENCAO");
        
        contentArea.add(cardPanel, BorderLayout.CENTER);
        
        // Mostrar estoque por padrão
        showView("ESTOQUE");
    }
    
    private void showView(String viewName) {
        viewAtual = viewName;
        cardLayout.show(cardPanel, viewName);
        
        // Destacar botão selecionado
        resetButtons();
        switch (viewName) {
            case "ESTOQUE":
                highlightButton(btnEstoque);
                estoqueView.carregarDados();
                break;
            case "CLIENTES":
                highlightButton(btnClientes);
                clientesView.carregarDados();
                break;
            case "FINANCEIRO":
                highlightButton(btnFinanceiro);
                financeiroView.carregarDados();
                break;
            case "MANUTENCAO":
                highlightButton(btnManutencao);
                manutencaoView.carregarDados();
                break;
        }
    }
    
    private void resetButtons() {
        btnEstoque.setBackground(new Color(245, 245, 245));
        btnClientes.setBackground(new Color(245, 245, 245));
        btnFinanceiro.setBackground(new Color(245, 245, 245));
        btnManutencao.setBackground(new Color(245, 245, 245));
    }
    
    private void highlightButton(JButton button) {
        button.setBackground(new Color(200, 230, 200));
    }
}

