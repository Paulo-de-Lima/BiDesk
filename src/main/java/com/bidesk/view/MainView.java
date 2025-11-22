package com.bidesk.view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.net.URL;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class MainView extends JFrame {
    private static final long serialVersionUID = 1L;

    // Cores do protótipo - Sidebar verde escuro
    private static final Color SIDEBAR_BACKGROUND = new Color(31, 151, 98); // Verde escuro
    private static final Color SIDEBAR_BORDER = new Color(25, 130, 85); // Verde mais escuro para borda
    private static final Color BUTTON_BACKGROUND = new Color(31, 151, 98); // Verde escuro
    private static final Color BUTTON_HOVER = new Color(36, 163, 92); // Verde intermediário no hover
    private static final Color BUTTON_SELECTED = new Color(39, 174, 96); // Verde mais claro para item selecionado
    private static final Color TEXT_PRIMARY = Color.WHITE; // Texto branco no sidebar
    private static final Color TEXT_SELECTED = Color.WHITE; // Texto branco quando selecionado
    private static final Font MENU_FONT = new Font("Segoe UI", Font.BOLD, 15);
    
    // Variável para rastrear qual botão está selecionado
    private JButton selectedButton;

    private JPanel sidebar;
    private JPanel contentArea;
    private CardLayout cardLayout;
    private JPanel cardPanel;

    // Views dos módulos
    private HomeView homeView;
    private EstoqueView estoqueView;
    private ClientesView clientesView;
    private FinanceiroView financeiroView;
    private ManutencaoView manutencaoView;

    // Botões do menu
    private JButton btnHome;
    private JButton btnEstoque;
    private JButton btnClientes;
    private JButton btnFinanceiro;
    private JButton btnManutencao;

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

        try {
            // Usa o class loader para encontrar o recurso no classpath
            URL iconURL = getClass().getResource("/images/favicon.png"); 
            
            if (iconURL != null) {
                // Carrega a imagem e a define como ícone da janela (JFrame)
                Image icon = new ImageIcon(iconURL).getImage();
                this.setIconImage(icon); // Método do JFrame
            } else {
                System.err.println("Atenção: Ícone 'favicon.png' não encontrado. Verifique a pasta src/main/resources.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erro ao carregar o ícone da aplicação.");
        }

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setOpaque(false);
        cardPanel.setBackground(Color.WHITE);
        // Inicializar views
        homeView = new HomeView();
        estoqueView = new EstoqueView();
        clientesView = new ClientesView();
        financeiroView = new FinanceiroView();
        manutencaoView = new ManutencaoView();
        
        // Configurar callbacks de navegação da HomeView
        homeView.setOnNavigateToEstoque(() -> showView("ESTOQUE"));
        homeView.setOnNavigateToClientes(() -> showView("CLIENTES"));
        homeView.setOnNavigateToFinanceiro(() -> showView("FINANCEIRO"));
        homeView.setOnNavigateToManutencao(() -> showView("MANUTENCAO"));
    }

    private void setupLayout() {
        sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBackground(SIDEBAR_BACKGROUND);
        sidebar.setLayout(new BorderLayout());
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, SIDEBAR_BORDER));

        contentArea = new JPanel(new BorderLayout());
        contentArea.setBackground(new Color(51, 171, 118));

        add(sidebar, BorderLayout.WEST);
        add(contentArea, BorderLayout.CENTER);
    }

    private void setupSidebar() {
        JPanel logoPanel = new JPanel(new BorderLayout());
        logoPanel.setPreferredSize(new Dimension(220, 100));
        logoPanel.setOpaque(true);
        logoPanel.setBackground(SIDEBAR_BACKGROUND); // Mesmo verde escuro do sidebar
        logoPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Carregar e exibir a logo
        try {
            java.net.URL imgURL = getClass().getResource("/images/bidesk_logo1.png");
            
            if (imgURL != null) {
                ImageIcon originalIcon = new ImageIcon(imgURL);
                int desiredWidth = 161;
                int desiredHeight = 60;
                
                Image img = originalIcon.getImage();
                Image resizedImg = img.getScaledInstance(
                    desiredWidth, 
                    desiredHeight, 
                    Image.SCALE_SMOOTH
                );
                
                ImageIcon logoIcon = new ImageIcon(resizedImg);
                JLabel logoLabel = new JLabel(logoIcon);
                logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
                logoPanel.add(logoLabel, BorderLayout.CENTER);
            } else {
                // Fallback: Exibe apenas o texto caso a imagem não seja encontrada
                System.err.println("Atenção: Imagem da logo bidesk_logo1.png não encontrada. Usando texto.");
                JLabel fallbackLabel = new JLabel("bidesk");
                fallbackLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
                fallbackLabel.setForeground(Color.WHITE); // Texto branco sobre fundo verde escuro
                fallbackLabel.setHorizontalAlignment(SwingConstants.CENTER);
                fallbackLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
                logoPanel.add(fallbackLabel, BorderLayout.CENTER);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setOpaque(false);
        menuPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0)); // Pequeno espaçamento vertical

        btnHome = createMenuButton("Home");
        btnEstoque = createMenuButton("Estoque");
        btnClientes = createMenuButton("Clientes");
        btnFinanceiro = createMenuButton("Financeiro");
        btnManutencao = createMenuButton("Manutenção");

        btnHome.addActionListener(e -> showView("HOME"));
        btnEstoque.addActionListener(e -> showView("ESTOQUE"));
        btnClientes.addActionListener(e -> showView("CLIENTES"));
        btnFinanceiro.addActionListener(e -> showView("FINANCEIRO"));
        btnManutencao.addActionListener(e -> showView("MANUTENCAO"));

        selectedButton = btnHome;
        highlightButton(btnHome);

        menuPanel.add(btnHome);
        menuPanel.add(btnEstoque);
        menuPanel.add(btnClientes);
        menuPanel.add(btnFinanceiro);
        menuPanel.add(btnManutencao);

        // Painel inferior com botão Sair ocupando toda a área
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        JButton btnSair = new JButton("Sair");
        btnSair.setBackground(new Color(232, 236, 240)); // Cinza claro conforme protótipo - mantém cor
        btnSair.setForeground(new Color(41, 50, 65)); // Texto escuro no botão Sair
        btnSair.setFont(new Font("Segoe UI", Font.BOLD, 15)); // Mesma fonte dos outros botões
        btnSair.setBorder(BorderFactory.createEmptyBorder(12, 0, 12, 0)); // Padding harmônico com outros botões
        btnSair.setBorderPainted(false);
        btnSair.addActionListener(e -> System.exit(0));
        btnSair.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSair.setFocusPainted(false);
        btnSair.setOpaque(true);
        btnSair.setPreferredSize(new Dimension(220, 48)); // Mesma altura dos outros botões
        btnSair.setMaximumSize(new Dimension(220, 48));
        btnSair.setMinimumSize(new Dimension(220, 48));
        btnSair.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnSair.setBackground(new Color(214, 222, 228)); // Hover mais escuro
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnSair.setBackground(new Color(232, 236, 240)); // Volta ao cinza claro original
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
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15)); // Padding mais harmônico
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(220, 48)); // Altura ligeiramente menor para mais harmonia
        button.setMaximumSize(new Dimension(220, 48));
        button.setMinimumSize(new Dimension(220, 48));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                // Só aplica hover se não for o botão selecionado
                if (button != selectedButton) {
                    button.setBackground(BUTTON_HOVER);
                }
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                // Volta para o background padrão se não for o botão selecionado
                if (button != selectedButton) {
                    button.setBackground(BUTTON_BACKGROUND);
                }
            }
        });

        return button;
    }

    private void setupContentArea() {
        cardPanel.add(homeView, "HOME");
        cardPanel.add(estoqueView, "ESTOQUE");
        cardPanel.add(clientesView, "CLIENTES");
        cardPanel.add(financeiroView, "FINANCEIRO");
        cardPanel.add(manutencaoView, "MANUTENCAO");

        contentArea.add(cardPanel, BorderLayout.CENTER);

        showView("HOME");
    }

    private void showView(String viewName) {
        cardLayout.show(cardPanel, viewName);

        resetButtons();
        switch (viewName) {
            case "HOME":
                highlightButton(btnHome);
                homeView.carregarDados();
                break;
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
        // Reseta todos os botões para o estado padrão
        btnHome.setBackground(BUTTON_BACKGROUND);
        btnEstoque.setBackground(BUTTON_BACKGROUND);
        btnClientes.setBackground(BUTTON_BACKGROUND);
        btnFinanceiro.setBackground(BUTTON_BACKGROUND);
        btnManutencao.setBackground(BUTTON_BACKGROUND);

        btnHome.setForeground(TEXT_PRIMARY);
        btnEstoque.setForeground(TEXT_PRIMARY);
        btnClientes.setForeground(TEXT_PRIMARY);
        btnFinanceiro.setForeground(TEXT_PRIMARY);
        btnManutencao.setForeground(TEXT_PRIMARY);
        
        // Remove borda de todos
        btnHome.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));
        btnEstoque.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));
        btnClientes.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));
        btnFinanceiro.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));
        btnManutencao.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));
    }

    private void highlightButton(JButton button) {
        // Atualiza a referência do botão selecionado
        selectedButton = button;
        
        // Aplica estilo de selecionado
        button.setBackground(BUTTON_SELECTED);
        button.setForeground(TEXT_SELECTED);
        
        // Adiciona uma borda esquerda sutil para indicar seleção
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 4, 0, 0, new Color(255, 255, 255, 180)), // Borda esquerda branca semi-transparente
            BorderFactory.createEmptyBorder(12, 11, 12, 15) // Padding ajustado
        ));
    }
}

