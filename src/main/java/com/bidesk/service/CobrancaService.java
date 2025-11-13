package com.bidesk.service;

import com.bidesk.model.Cobranca;

import java.time.LocalDate;
import java.util.List;

public interface CobrancaService extends BaseService<Cobranca, Long> {
    List<Cobranca> buscarPorCidade(String cidade);
    List<Cobranca> buscarPorPeriodo(LocalDate dataInicio, LocalDate dataFim);
}


