package com.bidesk.controller;

import com.bidesk.dao.ClienteDAO;
import com.bidesk.dao.RegistroFinanceiroDAO;
import com.bidesk.model.Cliente;
import com.bidesk.model.RegistroFinanceiro;
import java.util.List;

public class ClientesController {
    private ClienteDAO clienteDAO;
    private RegistroFinanceiroDAO registroFinanceiroDAO;
    
    public ClientesController() {
        clienteDAO = new ClienteDAO();
        registroFinanceiroDAO = new RegistroFinanceiroDAO();
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
    
    public boolean atualizar(Cliente cliente) {
        return clienteDAO.atualizar(cliente);
    }
    
    public boolean deletar(int id) {
        return clienteDAO.deletar(id);
    }
    
    public List<RegistroFinanceiro> listarRegistrosPorCliente(int clienteId) {
        return registroFinanceiroDAO.listarPorCliente(clienteId);
    }
}


