package com.bidesk.repository;

import com.bidesk.model.Transacao;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransacaoRepository extends BaseRepository<Transacao, Long> {
    List<Transacao> findByClienteId(Long clienteId);
    List<Transacao> findByClienteIdOrderByDataDesc(Long clienteId);
}


