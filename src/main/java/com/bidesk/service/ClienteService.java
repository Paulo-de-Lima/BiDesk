package com.bidesk.service;

import com.bidesk.model.Cliente;

import java.util.List;

public interface ClienteService extends BaseService<Cliente, Long> {
    List<Cliente> buscarPorNome(String nome);
    List<Cliente> buscarPorCidade(String cidade);
}


