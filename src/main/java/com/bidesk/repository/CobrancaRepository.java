package com.bidesk.repository;

import com.bidesk.model.Cobranca;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CobrancaRepository extends BaseRepository<Cobranca, Long> {
    List<Cobranca> findByCidade(String cidade);
    List<Cobranca> findByDataBetween(LocalDate dataInicio, LocalDate dataFim);
    List<Cobranca> findByData(LocalDate data);
}


