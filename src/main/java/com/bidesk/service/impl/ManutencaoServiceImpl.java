package com.bidesk.service.impl;

import com.bidesk.model.Manutencao;
import com.bidesk.repository.BaseRepository;
import com.bidesk.repository.ManutencaoRepository;
import com.bidesk.service.ManutencaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManutencaoServiceImpl extends BaseServiceImpl<Manutencao, Long> implements ManutencaoService {

    @Autowired
    private ManutencaoRepository manutencaoRepository;

    @Override
    protected BaseRepository<Manutencao, Long> getRepository() {
        return manutencaoRepository;
    }

    @Override
    public List<Manutencao> buscarPorClienteId(Long clienteId) {
        return manutencaoRepository.findByClienteId(clienteId);
    }

    @Override
    public List<Manutencao> buscarPorTitulo(String titulo) {
        return manutencaoRepository.findByTituloContainingIgnoreCase(titulo);
    }
}


