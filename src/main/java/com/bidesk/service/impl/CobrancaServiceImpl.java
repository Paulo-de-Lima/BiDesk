package com.bidesk.service.impl;

import com.bidesk.model.Cobranca;
import com.bidesk.repository.BaseRepository;
import com.bidesk.repository.CobrancaRepository;
import com.bidesk.service.CobrancaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CobrancaServiceImpl extends BaseServiceImpl<Cobranca, Long> implements CobrancaService {

    @Autowired
    private CobrancaRepository cobrancaRepository;

    @Override
    protected BaseRepository<Cobranca, Long> getRepository() {
        return cobrancaRepository;
    }

    @Override
    public List<Cobranca> buscarPorCidade(String cidade) {
        return cobrancaRepository.findByCidade(cidade);
    }

    @Override
    public List<Cobranca> buscarPorPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        return cobrancaRepository.findByDataBetween(dataInicio, dataFim);
    }
}


