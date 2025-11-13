package com.bidesk.service;

import com.bidesk.model.Material;

import java.util.List;

public interface MaterialService extends BaseService<Material, Long> {
    List<Material> buscarPorNome(String nome);
    List<Material> buscarEstoqueBaixo();
    Material adicionarQuantidade(Long id, Integer quantidade);
    Material diminuirQuantidade(Long id, Integer quantidade);
}


