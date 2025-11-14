package com.bidesk.view;

import java.awt.Image;
import java.awt.Dimension;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.GeneralPath;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class MainView extends JFrame {
    private static final long serialVersionUID = 1L;

    private static final Color PRIMARY_COLOR = new Color(51, 171, 118);
    private static final Color PRIMARY_DARK = new Color(39, 140, 98);
    private static final Color SIDEBAR_BACKGROUND = new Color(246, 248, 251);
    private static final Color SIDEBAR_BORDER = new Color(226, 232, 240);
    private static final Color BUTTON_BACKGROUND = new Color(246, 248, 251);
    private static final Color BUTTON_HOVER = new Color(229, 243, 234);
    private static final Color BUTTON_SELECTED = new Color(210, 236, 219);
    private static final Color TEXT_PRIMARY = new Color(41, 50, 65);
    private static final Font MENU_FONT = new Font("Segoe UI", Font.BOLD, 15);

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

    private Image logoImage;

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
        setMinimumSize(new Dimension(1024, 620));

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setOpaque(false);
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        // Inicializar views
        estoqueView = new EstoqueView();
        clientesView = new ClientesView();
        financeiroView = new FinanceiroView();
        manutencaoView = new ManutencaoView();
        
        // Carregar logo
        logoImage = loadLogoImage();
    }

    private void setupLayout() {
        sidebar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                GradientPaint gradient = new GradientPaint(
                        0, 0, Color.WHITE,
                        0, getHeight(), SIDEBAR_BACKGROUND);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                GradientPaint softGlow = new GradientPaint(
                        0, 0, new Color(255, 255, 255, 80),
                        getWidth(), getHeight(), new Color(255, 255, 255, 0));
                g2d.setPaint(softGlow);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBackground(SIDEBAR_BACKGROUND);
        sidebar.setLayout(new BorderLayout());
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, SIDEBAR_BORDER));

        contentArea = new JPanel(new BorderLayout());
        contentArea.setBackground(Color.WHITE);
        contentArea.setBorder(BorderFactory.createEmptyBorder(0, 12, 12, 12));

        add(sidebar, BorderLayout.WEST);
        add(contentArea, BorderLayout.CENTER);
    }

    // O método setupSidebar() modificado:

        private void setupSidebar() {
            // 1. O painel agora é um JPanel simples (não mais anônimo com paintComponent)
            JPanel logoPanel = new JPanel(new BorderLayout());
            logoPanel.setPreferredSize(new Dimension(220, 100));
            logoPanel.setOpaque(false);
            logoPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

            // 2. Novo código para carregar e exibir a imagem
            try {
                // Tenta carregar a imagem a partir dos recursos do projeto
                // Altere o caminho "images/bidesk_logo.png" para o caminho real da sua imagem
                java.net.URL imgURL = getClass().getResource("/images/bidesk_logo.png");
                
                // Dentro do seu método setupSidebar(), no bloco 'try':

                if (imgURL != null) {
                    ImageIcon originalIcon = new ImageIcon(imgURL);
                    
                    // --- Início do código de redimensionamento ---
                    
                    // Defina a largura e altura desejadas para o ícone
                    int desiredWidth = 161; // Exemplo: 32 pixels de largura (tamanho padrão para ícones de barra de título)
                    int desiredHeight = 60; // 32 pixels de altura
                    
                    // Obtém a imagem original, redimensiona e cria um novo ícone
                    Image img = originalIcon.getImage();
                    Image resizedImg = img.getScaledInstance(
                        desiredWidth, 
                        desiredHeight, 
                        Image.SCALE_SMOOTH // Usa um algoritmo de redimensionamento de alta qualidade
                    );
                    
                    ImageIcon logoIcon = new ImageIcon(resizedImg);
                    // --- Fim do código de redimensionamento ---

                    JLabel logoLabel = new JLabel(logoIcon);
                    logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
                    
                    logoPanel.add(logoLabel, BorderLayout.CENTER);
                } else {
                    // Fallback: Exibe apenas o texto caso a imagem não seja encontrada
                    System.err.println("Atenção: Imagem da logo bidesk_logo.png não encontrada. Usando texto.");
                    JLabel fallbackLabel = new JLabel("bidesk");
                    fallbackLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
                    fallbackLabel.setForeground(TEXT_PRIMARY); // Cor de texto já definida
                    fallbackLabel.setHorizontalAlignment(SwingConstants.CENTER);
                    logoPanel.add(fallbackLabel, BorderLayout.CENTER);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            // ... O restante do seu método setupSidebar() continua aqui...
            
            JPanel menuPanel = new JPanel();
            menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
            menuPanel.setOpaque(false);
            menuPanel.setBorder(BorderFactory.createEmptyBorder(24, 18, 24, 18));
            
            // ... (o restante do código dos botões)
            
            // ...
        
        logoPanel.setPreferredSize(new Dimension(220, 100));
        logoPanel.setOpaque(false);
        logoPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setOpaque(false);
        menuPanel.setBorder(BorderFactory.createEmptyBorder(24, 18, 24, 18));

        btnEstoque = createMenuButton("Estoque");
        btnClientes = createMenuButton("Clientes");
        btnFinanceiro = createMenuButton("Financeiro");
        btnManutencao = createMenuButton("Manutenção");

        btnEstoque.addActionListener(e -> showView("ESTOQUE"));
        btnClientes.addActionListener(e -> showView("CLIENTES"));
        btnFinanceiro.addActionListener(e -> showView("FINANCEIRO"));
        btnManutencao.addActionListener(e -> showView("MANUTENCAO"));

        highlightButton(btnEstoque);

        menuPanel.add(btnEstoque);
        menuPanel.add(Box.createVerticalStrut(12));
        menuPanel.add(btnClientes);
        menuPanel.add(Box.createVerticalStrut(12));
        menuPanel.add(btnFinanceiro);
        menuPanel.add(Box.createVerticalStrut(12));
        menuPanel.add(btnManutencao);
        menuPanel.add(Box.createVerticalGlue());

        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 18, 32, 18));

        JButton btnSair = new JButton("Sair");
        btnSair.setBackground(new Color(232, 236, 240));
        btnSair.setForeground(TEXT_PRIMARY);
        btnSair.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSair.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 224, 229)),
                BorderFactory.createEmptyBorder(12, 18, 12, 18)
        ));
        btnSair.addActionListener(e -> System.exit(0));
        btnSair.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSair.setFocusPainted(false);
        btnSair.setOpaque(true);
        btnSair.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnSair.setBackground(new Color(214, 222, 228));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnSair.setBackground(new Color(232, 236, 240));
            }
        });

        bottomPanel.add(btnSair, BorderLayout.CENTER);

        sidebar.add(logoPanel, BorderLayout.NORTH);
        sidebar.add(menuPanel, BorderLayout.CENTER);
        sidebar.add(bottomPanel, BorderLayout.SOUTH);
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFont(MENU_FONT);
        button.setBackground(BUTTON_BACKGROUND);
        button.setForeground(TEXT_PRIMARY);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 12));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(180, 44));
        button.setMaximumSize(new Dimension(180, 44));
        button.setMinimumSize(new Dimension(180, 44));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                if (!button.getBackground().equals(BUTTON_SELECTED)) {
                    button.setBackground(BUTTON_HOVER);
                }
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                if (!button.getBackground().equals(BUTTON_SELECTED)) {
                    button.setBackground(BUTTON_BACKGROUND);
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

        showView("ESTOQUE");
    }

    private void showView(String viewName) {
        cardLayout.show(cardPanel, viewName);

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
        btnEstoque.setBackground(BUTTON_BACKGROUND);
        btnClientes.setBackground(BUTTON_BACKGROUND);
        btnFinanceiro.setBackground(BUTTON_BACKGROUND);
        btnManutencao.setBackground(BUTTON_BACKGROUND);

        btnEstoque.setForeground(TEXT_PRIMARY);
        btnClientes.setForeground(TEXT_PRIMARY);
        btnFinanceiro.setForeground(TEXT_PRIMARY);
        btnManutencao.setForeground(TEXT_PRIMARY);
    }

    private void highlightButton(JButton button) {
        button.setBackground(BUTTON_SELECTED);
        button.setForeground(PRIMARY_DARK);
    }

    private Image loadLogoImage() {
        URL resource = getClass().getResource("/images/bidesk-logo.png");
        if (resource == null) {
            return null;
        }
        try {
            BufferedImage bufferedImage = ImageIO.read(resource);
            if (bufferedImage == null) {
                return null;
            }
            return bufferedImage;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

