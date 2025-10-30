package com.bidesk.ui;

import com.bidesk.database.DatabaseInitializer;
import com.bidesk.ui.panels.*;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private JPanel sidebar;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    
    // Panels das abas
    private EstoquePanel estoquePanel;
    private ClientesPanel clientesPanel;
    private FinanceiroPanel financeiroPanel;
    private ManutencaoPanel manutencaoPanel;
    
    public MainFrame() {
        initializeDatabase();
        initializeComponents();
        setupLayout();
        setupEventListeners();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("BiDesk - Sistema de Gerenciamento");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setResizable(true);
    }
    
    private void initializeDatabase() {
        DatabaseInitializer.initializeDatabase();
    }
    
    private void initializeComponents() {
        // Inicializar painéis
        estoquePanel = new EstoquePanel();
        clientesPanel = new ClientesPanel();
        financeiroPanel = new FinanceiroPanel();
        manutencaoPanel = new ManutencaoPanel();
        
        // Layout do painel de conteúdo
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.add(estoquePanel, "ESTOQUE");
        contentPanel.add(clientesPanel, "CLIENTES");
        contentPanel.add(financeiroPanel, "FINANCEIRO");
        contentPanel.add(manutencaoPanel, "MANUTENCAO");
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Criar sidebar
        createSidebar();
        
        // Adicionar componentes ao frame
        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
        
        // Mostrar aba inicial (Estoque)
        cardLayout.show(contentPanel, "ESTOQUE");
    }
    
    private void createSidebar() {
        sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(250, 0));
        sidebar.setBackground(Color.WHITE);
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.LIGHT_GRAY));
        sidebar.setLayout(new BorderLayout());
        
        // Logo
        JPanel logoPanel = createLogoPanel();
        
        // Menu items
        JPanel menuPanel = createMenuPanel();
        
        // Botão sair
        JPanel exitPanel = createExitPanel();
        
        sidebar.add(logoPanel, BorderLayout.NORTH);
        sidebar.add(menuPanel, BorderLayout.CENTER);
        sidebar.add(exitPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createLogoPanel() {
        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(Color.WHITE);
        logoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        logoPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        JLabel logoLabel = new JLabel("bidesk");
        logoLabel.setFont(new Font("Arial", Font.BOLD, 24));
        logoLabel.setForeground(new Color(0, 150, 0)); // Verde
        
        logoPanel.add(logoLabel);
        return logoPanel;
    }
    
    private JPanel createMenuPanel() {
        JPanel menuPanel = new JPanel();
        menuPanel.setBackground(Color.WHITE);
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        
        // Botões do menu
        JButton estoqueBtn = createMenuButton("Estoque", "ESTOQUE");
        JButton clientesBtn = createMenuButton("Clientes", "CLIENTES");
        JButton financeiroBtn = createMenuButton("Financeiro", "FINANCEIRO");
        JButton manutencaoBtn = createMenuButton("Manutenção", "MANUTENCAO");
        
        // Marcar estoque como selecionado inicialmente
        estoqueBtn.setBackground(new Color(144, 238, 144)); // Verde claro
        estoqueBtn.setForeground(Color.BLACK);
        
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(estoqueBtn);
        menuPanel.add(Box.createVerticalStrut(5));
        menuPanel.add(clientesBtn);
        menuPanel.add(Box.createVerticalStrut(5));
        menuPanel.add(financeiroBtn);
        menuPanel.add(Box.createVerticalStrut(5));
        menuPanel.add(manutencaoBtn);
        menuPanel.add(Box.createVerticalGlue());
        
        return menuPanel;
    }
    
    private JButton createMenuButton(String text, String cardName) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(200, 40));
        button.setMaximumSize(new Dimension(200, 40));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setBackground(Color.WHITE);
        button.setForeground(Color.BLACK);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addActionListener(e -> {
            // Resetar todos os botões
            resetMenuButtons();
            // Destacar botão selecionado
            button.setBackground(new Color(144, 238, 144)); // Verde claro
            button.setForeground(Color.BLACK);
            // Mostrar painel correspondente
            cardLayout.show(contentPanel, cardName);
        });
        
        return button;
    }
    
    private void resetMenuButtons() {
        JPanel menuPanel = (JPanel) sidebar.getComponent(1);
        Component[] components = menuPanel.getComponents();
        for (Component comp : components) {
            if (comp instanceof JButton) {
                JButton btn = (JButton) comp;
                btn.setBackground(Color.WHITE);
                btn.setForeground(Color.BLACK);
            }
        }
    }
    
    private JPanel createExitPanel() {
        JPanel exitPanel = new JPanel();
        exitPanel.setBackground(Color.LIGHT_GRAY);
        exitPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        exitPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        JButton exitBtn = new JButton("Sair");
        exitBtn.setBackground(Color.LIGHT_GRAY);
        exitBtn.setForeground(Color.BLACK);
        exitBtn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        exitBtn.setFocusPainted(false);
        exitBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        exitBtn.addActionListener(e -> System.exit(0));
        
        exitPanel.add(exitBtn);
        return exitPanel;
    }
    
    private void setupEventListeners() {
        // Event listeners já configurados nos métodos acima
    }
}
