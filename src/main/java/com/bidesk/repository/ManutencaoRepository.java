package com.bidesk.repository;

import com.bidesk.model.Manutencao;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManutencaoRepository extends BaseRepository<Manutencao, Long> {
    List<Manutencao> findByClienteId(Long clienteId);
    List<Manutencao> findByTituloContainingIgnoreCase(String titulo);
}


