package com.bidesk.service.impl;

import com.bidesk.model.Cliente;
import com.bidesk.repository.BaseRepository;
import com.bidesk.repository.ClienteRepository;
import com.bidesk.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteServiceImpl extends BaseServiceImpl<Cliente, Long> implements ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    protected BaseRepository<Cliente, Long> getRepository() {
        return clienteRepository;
    }

    @Override
    public List<Cliente> buscarPorNome(String nome) {
        return clienteRepository.findByNomeContainingIgnoreCase(nome);
    }

    @Override
    public List<Cliente> buscarPorCidade(String cidade) {
        return clienteRepository.findByCidade(cidade);
    }
}


