package com.bidesk.service.impl;

import com.bidesk.model.Transacao;
import com.bidesk.repository.BaseRepository;
import com.bidesk.repository.TransacaoRepository;
import com.bidesk.service.TransacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransacaoServiceImpl extends BaseServiceImpl<Transacao, Long> implements TransacaoService {

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Override
    protected BaseRepository<Transacao, Long> getRepository() {
        return transacaoRepository;
    }

    @Override
    public List<Transacao> buscarPorClienteId(Long clienteId) {
        return transacaoRepository.findByClienteIdOrderByDataDesc(clienteId);
    }
}


