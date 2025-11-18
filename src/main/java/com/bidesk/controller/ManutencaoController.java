package com.bidesk.controller;

import com.bidesk.dao.ManutencaoDAO;
import com.bidesk.dao.ClienteDAO;
import com.bidesk.model.Manutencao;
import com.bidesk.model.Cliente;
import java.util.List;
import java.util.ArrayList;

public class ManutencaoController {
    private ManutencaoDAO manutencaoDAO;
    private ClienteDAO clienteDAO;
    
    public ManutencaoController() {
        manutencaoDAO = new ManutencaoDAO();
        clienteDAO = new ClienteDAO();
    }
    
    public List<Manutencao> listarTodos() {
        return manutencaoDAO.listarTodos();
    }
    
    public String getNomeCliente(Integer clienteId) {
        if (clienteId == null) {
            return null;
        }
        Cliente cliente = clienteDAO.buscarPorId(clienteId);
        return cliente != null ? cliente.getNome() : null;
    }
    
    public List<String> listarNomesClientes() {
        List<String> nomes = new ArrayList<>();
        List<Cliente> clientes = clienteDAO.listarTodos();
        for (Cliente cliente : clientes) {
            nomes.add(cliente.getNome());
        }
        return nomes;
    }
    
    public Integer getIdClientePorNome(String nome) {
        List<Cliente> clientes = clienteDAO.listarTodos();
        for (Cliente cliente : clientes) {
            if (cliente.getNome().equals(nome)) {
                return cliente.getId();
            }
        }
        return null;
    }
    
    public boolean inserir(Integer clienteId, String titulo, String descricao) {
        Manutencao manutencao = new Manutencao(clienteId, titulo, descricao);
        return manutencaoDAO.inserir(manutencao);
    }
    
    public boolean atualizar(Manutencao manutencao) {
        return manutencaoDAO.atualizar(manutencao);
    }
    
    public boolean deletar(int id) {
        return manutencaoDAO.deletar(id);
    }
}











