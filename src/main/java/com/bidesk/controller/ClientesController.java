package com.bidesk.controller;

import com.bidesk.dao.ClienteDAO;
import com.bidesk.dao.MesaDAO;
import com.bidesk.model.Cliente;
import com.bidesk.model.Mesa;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

public class ClientesController {
    private ClienteDAO clienteDAO;
    private MesaDAO mesaDAO;
    
    public ClientesController() {
        this.clienteDAO = new ClienteDAO();
        this.mesaDAO = new MesaDAO();
    }
    
    public List<Cliente> listarTodos() {
        return clienteDAO.listarTodos();
    }
    
    public Cliente buscarClientePorId(int id) {
        return clienteDAO.buscarPorId(id);
    }
    
    public boolean inserir(String nome, String endereco, String cidade) {
        Cliente cliente = new Cliente(nome, endereco, cidade);
        return clienteDAO.inserir(cliente);
    }
    
    public boolean inserirComMesa(String nome, String endereco, String cidade, String numero, Date data, String registro) {
        Cliente cliente = new Cliente(nome, endereco, cidade);
        int clienteId = clienteDAO.inserirERetornarId(cliente);
        if (clienteId > 0) {
            Mesa mesa = new Mesa(
                clienteId,
                numero != null && !numero.isEmpty() ? numero : null,
                data,
                registro != null && !registro.isEmpty() ? registro : null,
                BigDecimal.ZERO,
                BigDecimal.ZERO
            );
            return mesaDAO.inserir(mesa);
        }
        return false;
    }
    
    public boolean adicionarMesa(int clienteId, String numero, Date data, String registro) {
        Mesa mesa = new Mesa(
            clienteId,
            numero != null && !numero.isEmpty() ? numero : null,
            data,
            registro != null && !registro.isEmpty() ? registro : null,
            BigDecimal.ZERO,
            BigDecimal.ZERO
        );
        return mesaDAO.inserir(mesa);
    }
    
    public boolean atualizarMesa(Mesa mesa) {
        return mesaDAO.atualizar(mesa);
    }
    
    public boolean atualizar(Cliente cliente) {
        return clienteDAO.atualizar(cliente);
    }
    
    public boolean deletar(int id) {
        // Primeiro deleta todas as mesas do cliente
        mesaDAO.deletarPorCliente(id);
        // Depois deleta o cliente
        return clienteDAO.deletar(id);
    }
    
    public boolean deletarMesa(int mesaId) {
        return mesaDAO.deletar(mesaId);
    }
    
    public List<Mesa> listarMesasPorCliente(int clienteId) {
        return mesaDAO.listarPorCliente(clienteId);
    }
    
    public List<Mesa> listarTodasMesas() {
        return mesaDAO.listarTodas();
    }
}