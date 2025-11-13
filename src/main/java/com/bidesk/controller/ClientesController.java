package com.bidesk.controller;

import com.bidesk.dao.ClienteDAO;
import com.bidesk.dao.RegistroFinanceiroDAO;
import com.bidesk.model.Cliente;
import com.bidesk.model.RegistroFinanceiro;
import java.math.BigDecimal;
import java.sql.Date;
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
    
    public boolean inserirComRegistro(String nome, String endereco, String cidade, String numero, Date data, String registro) {
        Cliente cliente = new Cliente(nome, endereco, cidade);
        int clienteId = clienteDAO.inserirERetornarId(cliente);
        if (clienteId > 0) {
            RegistroFinanceiro registroFinanceiro = new RegistroFinanceiro(
                clienteId,
                numero.isEmpty() ? null : numero,
                data,
                registro.isEmpty() ? null : registro,
                BigDecimal.ZERO,
                BigDecimal.ZERO
            );
            return registroFinanceiroDAO.inserir(registroFinanceiro);
        }
        return false;
    }
    
    public boolean atualizarRegistroFinanceiro(RegistroFinanceiro registro) {
        return registroFinanceiroDAO.atualizar(registro);
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


