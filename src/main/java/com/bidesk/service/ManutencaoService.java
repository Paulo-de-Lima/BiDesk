package com.bidesk.service;

import com.bidesk.model.Manutencao;

import java.util.List;

public interface ManutencaoService extends BaseService<Manutencao, Long> {
    List<Manutencao> buscarPorClienteId(Long clienteId);
    List<Manutencao> buscarPorTitulo(String titulo);
}


