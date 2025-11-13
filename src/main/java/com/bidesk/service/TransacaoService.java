package com.bidesk.service;

import com.bidesk.model.Transacao;

import java.util.List;

public interface TransacaoService extends BaseService<Transacao, Long> {
    List<Transacao> buscarPorClienteId(Long clienteId);
}


