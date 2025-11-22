package com.bidesk.view;

import com.bidesk.controller.EstoqueController;
import com.bidesk.controller.ClientesController;
import com.bidesk.controller.FinanceiroController;
import com.bidesk.controller.ManutencaoController;
import com.bidesk.model.Material;
import com.bidesk.model.Mesa;
import com.bidesk.model.Despesa;
import com.bidesk.model.Cliente;
import com.bidesk.model.Manutencao;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HomeView extends JPanel {
    // Controllers
    private EstoqueController estoqueController;
    private ClientesController clientesController;
    private FinanceiroController financeiroController;
    private ManutencaoController manutencaoController;
    
    // Callback para navegação
    private Runnable onNavigateToEstoque;
    private Runnable onNavigateToClientes;
    private Runnable onNavigateToFinanceiro;
    private Runnable onNavigateToManutencao;
    
    // Constantes de Cores
    private static final Color PRIMARY_GREEN = new Color(39, 174, 96);
    private static final Color LIGHT_GREY = new Color(236, 240, 241);
    private static final Color WARNING_ORANGE = new Color(243, 156, 18);
    private static final Color DANGER_RED = new Color(231, 76, 60);
    private static final Color INFO_BLUE = new Color(52, 152, 219);
    private static final Color SUCCESS_GREEN = new Color(46, 204, 113);
    private static final Color PURPLE = new Color(155, 89, 182);
    
    // Componentes
    private JLabel lblMateriaisEmFaltaNumero;
    private JLabel lblMateriaisAcabandoNumero;
    private JLabel lblTiposMateriaisNumero;
    private JLabel lblClientesDevendoNumero;
    private JLabel lblTotalClientesNumero;
    private JLabel lblTotalMesasNumero;
    private JLabel lblUltimaViagemTotalNumero;
    private JLabel lblUltimaViagemDespesaNumero;
    private JLabel lblTotalDespesasMateriaisNumero;
    private JLabel lblManutencoesPendentesNumero;
    
    public HomeView() {
        estoqueController = new EstoqueController();
        clientesController = new ClientesController();
        financeiroController = new FinanceiroController();
        manutencaoController = new ManutencaoController();
        
        initializeComponents();
        carregarDados();
    }
    
    public void setOnNavigateToEstoque(Runnable callback) {
        this.onNavigateToEstoque = callback;
    }
    
    public void setOnNavigateToClientes(Runnable callback) {
        this.onNavigateToClientes = callback;
    }
    
    public void setOnNavigateToFinanceiro(Runnable callback) {
        this.onNavigateToFinanceiro = callback;
    }
    
    public void setOnNavigateToManutencao(Runnable callback) {
        this.onNavigateToManutencao = callback;
    }
    
    private void initializeComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        // Painel do cabeçalho
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(PRIMARY_GREEN);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 15, 20));
        
        JLabel titleLabel = new JLabel("Home");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        headerPanel.add(titleLabel);
        
        // Painel principal com seções em grid
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(2, 2, 15, 15));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Seção Estoque
        mainPanel.add(criarSecaoEstoque());
        
        // Seção Clientes
        mainPanel.add(criarSecaoClientes());
        
        // Seção Financeiro
        mainPanel.add(criarSecaoFinanceiro());
        
        // Seção Manutenção
        mainPanel.add(criarSecaoManutencao());
        
        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private JPanel criarSecaoEstoque() {
        JPanel secaoPanel = criarSecaoBase("Estoque", WARNING_ORANGE, () -> {
            if (onNavigateToEstoque != null) onNavigateToEstoque.run();
        });
        
        JPanel contentPanel = (JPanel) ((BorderLayout) secaoPanel.getLayout()).getLayoutComponent(BorderLayout.CENTER);
        
        // Card: Materiais em falta
        JPanel card1 = criarCardInfo("Materiais em falta", lblMateriaisEmFaltaNumero = new JLabel("0"), DANGER_RED);
        contentPanel.add(card1);
        contentPanel.add(Box.createVerticalStrut(8));
        
        // Card: Materiais acabando
        JPanel card2 = criarCardInfo("Materiais acabando", lblMateriaisAcabandoNumero = new JLabel("0"), WARNING_ORANGE);
        contentPanel.add(card2);
        contentPanel.add(Box.createVerticalStrut(8));
        
        // Card: Tipos de materiais
        JPanel card3 = criarCardInfo("Tipos de materiais", lblTiposMateriaisNumero = new JLabel("0"), INFO_BLUE);
        contentPanel.add(card3);
        
        return secaoPanel;
    }
    
    private JPanel criarSecaoClientes() {
        JPanel secaoPanel = criarSecaoBase("Clientes", INFO_BLUE, () -> {
            if (onNavigateToClientes != null) onNavigateToClientes.run();
        });
        
        JPanel contentPanel = (JPanel) ((BorderLayout) secaoPanel.getLayout()).getLayoutComponent(BorderLayout.CENTER);
        
        // Card: Clientes devendo
        JPanel card1 = criarCardInfo("Pagamentos pendentes", lblClientesDevendoNumero = new JLabel("0"), DANGER_RED);
        contentPanel.add(card1);
        contentPanel.add(Box.createVerticalStrut(8));
        
        // Card: Total de clientes
        JPanel card2 = criarCardInfo("Total de clientes", lblTotalClientesNumero = new JLabel("0"), INFO_BLUE);
        contentPanel.add(card2);
        contentPanel.add(Box.createVerticalStrut(8));
        
        // Card: Total de mesas
        JPanel card3 = criarCardInfo("Total de mesas", lblTotalMesasNumero = new JLabel("0"), SUCCESS_GREEN);
        contentPanel.add(card3);
        
        return secaoPanel;
    }
    
    private JPanel criarSecaoFinanceiro() {
        JPanel secaoPanel = criarSecaoBase("Financeiro", SUCCESS_GREEN, () -> {
            if (onNavigateToFinanceiro != null) onNavigateToFinanceiro.run();
        });
        
        JPanel contentPanel = (JPanel) ((BorderLayout) secaoPanel.getLayout()).getLayoutComponent(BorderLayout.CENTER);
        
        // Card: Última viagem - Total adquirido
        JPanel card1 = criarCardInfo("Total adquirido (última viagem)", lblUltimaViagemTotalNumero = new JLabel("R$ 0,00"), SUCCESS_GREEN);
        contentPanel.add(card1);
        contentPanel.add(Box.createVerticalStrut(8));
        
        // Card: Última viagem - Despesas
        JPanel card2 = criarCardInfo("Despesas (última viagem)", lblUltimaViagemDespesaNumero = new JLabel("R$ 0,00"), WARNING_ORANGE);
        contentPanel.add(card2);
        contentPanel.add(Box.createVerticalStrut(8));
        
        // Card: Total despesas materiais
        JPanel card3 = criarCardInfo("Total despesas materiais", lblTotalDespesasMateriaisNumero = new JLabel("R$ 0,00"), PURPLE);
        contentPanel.add(card3);
        
        return secaoPanel;
    }
    
    private JPanel criarSecaoManutencao() {
        JPanel secaoPanel = criarSecaoBase("Manutenção", WARNING_ORANGE, () -> {
            if (onNavigateToManutencao != null) onNavigateToManutencao.run();
        });
        
        JPanel contentPanel = (JPanel) ((BorderLayout) secaoPanel.getLayout()).getLayoutComponent(BorderLayout.CENTER);
        
        // Card: Manutenções pendentes
        JPanel card = criarCardInfo("Pendentes", lblManutencoesPendentesNumero = new JLabel("0"), DANGER_RED);
        contentPanel.add(card);
        
        return secaoPanel;
    }
    
    private JPanel criarSecaoBase(String titulo, Color corTitulo, Runnable onButtonClick) {
        JPanel secaoPanel = new JPanel(new BorderLayout());
        secaoPanel.setBackground(Color.WHITE);
        secaoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LIGHT_GREY.darker(), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        // Título
        JPanel tituloPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        tituloPanel.setBackground(Color.WHITE);
        tituloPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));
        
        JLabel tituloLabel = new JLabel(titulo);
        tituloLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        tituloLabel.setForeground(corTitulo);
        tituloPanel.add(tituloLabel);
        
        // Painel de conteúdo (cards)
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));
        
        // Botão de redirecionamento
        RoundedButton btnRedirect = new RoundedButton("Ver detalhes");
        btnRedirect.setBackground(corTitulo);
        btnRedirect.setForeground(Color.WHITE);
        btnRedirect.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnRedirect.setPreferredSize(new Dimension(Integer.MAX_VALUE, 35));
        btnRedirect.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRedirect.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnRedirect.setBackground(corTitulo.darker());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                btnRedirect.setBackground(corTitulo);
            }
        });
        btnRedirect.addActionListener(e -> onButtonClick.run());
        
        secaoPanel.add(tituloPanel, BorderLayout.NORTH);
        secaoPanel.add(contentPanel, BorderLayout.CENTER);
        secaoPanel.add(btnRedirect, BorderLayout.SOUTH);
        
        return secaoPanel;
    }
    
    private JPanel criarCardInfo(String descricao, JLabel numeroLabel, Color corDestaque) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(corDestaque.getRed(), corDestaque.getGreen(), corDestaque.getBlue(), 10));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(corDestaque.getRed(), corDestaque.getGreen(), corDestaque.getBlue(), 50), 1),
            BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 65));
        card.setPreferredSize(new Dimension(Integer.MAX_VALUE, 65));
        
        // Descrição
        JLabel descLabel = new JLabel(descricao);
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descLabel.setForeground(new Color(100, 100, 100));
        
        // Número destacado
        numeroLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        numeroLabel.setForeground(corDestaque);
        numeroLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        
        card.add(descLabel, BorderLayout.WEST);
        card.add(numeroLabel, BorderLayout.EAST);
        
        return card;
    }
    
    public void carregarDados() {
        atualizarDadosEstoque();
        atualizarDadosClientes();
        atualizarDadosFinanceiro();
        atualizarDadosManutencao();
    }
    
    private void atualizarDadosEstoque() {
        try {
            List<Material> materiais = estoqueController.listarTodos();
            
            int materiaisEmFalta = 0;
            int materiaisAcabando = 0;
            Set<String> tiposMateriais = new HashSet<>();
            
            for (Material material : materiais) {
                if (material.getStatus() == Material.StatusMaterial.VAZIO) {
                    materiaisEmFalta++;
                } else if (material.getStatus() == Material.StatusMaterial.BAIXO) {
                    materiaisAcabando++;
                }
                tiposMateriais.add(material.getNome());
            }
            
            if (lblMateriaisEmFaltaNumero != null) {
                lblMateriaisEmFaltaNumero.setText(String.valueOf(materiaisEmFalta));
            }
            
            if (lblMateriaisAcabandoNumero != null) {
                lblMateriaisAcabandoNumero.setText(String.valueOf(materiaisAcabando));
            }
            
            if (lblTiposMateriaisNumero != null) {
                lblTiposMateriaisNumero.setText(String.valueOf(tiposMateriais.size()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (lblMateriaisEmFaltaNumero != null) {
                lblMateriaisEmFaltaNumero.setText("--");
            }
            if (lblMateriaisAcabandoNumero != null) {
                lblMateriaisAcabandoNumero.setText("--");
            }
        }
    }
    
    private void atualizarDadosClientes() {
        try {
            List<Cliente> clientes = clientesController.listarTodos();
            Set<Integer> clientesDevendo = new HashSet<>();
            Set<String> mesasUnicas = new HashSet<>();
            
            for (Cliente cliente : clientes) {
                List<Mesa> mesas = clientesController.listarMesasPorCliente(cliente.getId());
                // Contar mesas únicas pelo número da mesa
                for (Mesa mesa : mesas) {
                    String numeroMesa = mesa.getNumero() != null ? mesa.getNumero() : "Sem número";
                    mesasUnicas.add(cliente.getId() + "-" + numeroMesa); // Usar clienteId-numero para garantir unicidade
                }
                for (Mesa mesa : mesas) {
                    if (mesa.getDeve() != null && mesa.getDeve().compareTo(BigDecimal.ZERO) > 0) {
                        clientesDevendo.add(cliente.getId());
                        break;
                    }
                }
            }
            
            int totalMesas = mesasUnicas.size();
            
            if (lblClientesDevendoNumero != null) {
                lblClientesDevendoNumero.setText(String.valueOf(clientesDevendo.size()));
            }
            
            if (lblTotalClientesNumero != null) {
                lblTotalClientesNumero.setText(String.valueOf(clientes.size()));
            }
            
            if (lblTotalMesasNumero != null) {
                lblTotalMesasNumero.setText(String.valueOf(totalMesas));
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (lblClientesDevendoNumero != null) {
                lblClientesDevendoNumero.setText("--");
            }
            if (lblTotalClientesNumero != null) {
                lblTotalClientesNumero.setText("--");
            }
            if (lblTotalMesasNumero != null) {
                lblTotalMesasNumero.setText("--");
            }
        }
    }
    
    private void atualizarDadosFinanceiro() {
        try {
            List<Despesa> despesas = financeiroController.listarTodos();
            
            Despesa ultimaViagem = null;
            if (!despesas.isEmpty()) {
                ultimaViagem = despesas.get(0);
                for (Despesa despesa : despesas) {
                    if (despesa.getData() != null && ultimaViagem.getData() != null) {
                        if (despesa.getData().after(ultimaViagem.getData())) {
                            ultimaViagem = despesa;
                        }
                    }
                }
            }
            
            if (ultimaViagem != null) {
                BigDecimal totalAdquirido = ultimaViagem.getTotal() != null ? ultimaViagem.getTotal() : BigDecimal.ZERO;
                BigDecimal totalDespesa = ultimaViagem.getDespesa() != null ? ultimaViagem.getDespesa() : BigDecimal.ZERO;
                
                if (lblUltimaViagemTotalNumero != null) {
                    lblUltimaViagemTotalNumero.setText("R$ " + String.format("%.2f", totalAdquirido));
                }
                
                if (lblUltimaViagemDespesaNumero != null) {
                    lblUltimaViagemDespesaNumero.setText("R$ " + String.format("%.2f", totalDespesa));
                }
            } else {
                if (lblUltimaViagemTotalNumero != null) {
                    lblUltimaViagemTotalNumero.setText("--");
                }
                
                if (lblUltimaViagemDespesaNumero != null) {
                    lblUltimaViagemDespesaNumero.setText("--");
                }
            }
            
            BigDecimal totalDespesasMateriais = financeiroController.calcularTotalGastoMateriais();
            if (totalDespesasMateriais == null) {
                totalDespesasMateriais = BigDecimal.ZERO;
            }
            
            if (lblTotalDespesasMateriaisNumero != null) {
                lblTotalDespesasMateriaisNumero.setText("R$ " + String.format("%.2f", totalDespesasMateriais));
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (lblUltimaViagemTotalNumero != null) {
                lblUltimaViagemTotalNumero.setText("--");
            }
        }
    }
    
    private void atualizarDadosManutencao() {
        try {
            List<Manutencao> pendentes = manutencaoController.listarPendentes();
            int quantidadePendentes = pendentes != null ? pendentes.size() : 0;
            
            if (lblManutencoesPendentesNumero != null) {
                lblManutencoesPendentesNumero.setText(String.valueOf(quantidadePendentes));
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (lblManutencoesPendentesNumero != null) {
                lblManutencoesPendentesNumero.setText("--");
            }
        }
    }
    
    // Classe interna para botão arredondado
    private class RoundedButton extends JButton {
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
            
            g2.setColor(getBackground());
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 8, 8));
            
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
