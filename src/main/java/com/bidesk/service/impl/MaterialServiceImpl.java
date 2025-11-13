package com.bidesk.service.impl;

import com.bidesk.exception.ResourceNotFoundException;
import com.bidesk.model.Material;
import com.bidesk.repository.BaseRepository;
import com.bidesk.repository.MaterialRepository;
import com.bidesk.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MaterialServiceImpl extends BaseServiceImpl<Material, Long> implements MaterialService {

    @Autowired
    private MaterialRepository materialRepository;

    @Override
    protected BaseRepository<Material, Long> getRepository() {
        return materialRepository;
    }

    @Override
    public List<Material> buscarPorNome(String nome) {
        return materialRepository.findByNomeContainingIgnoreCase(nome);
    }

    @Override
    public List<Material> buscarEstoqueBaixo() {
        return materialRepository.findByQuantidadeLessThan(5);
    }

    @Override
    @Transactional
    public Material adicionarQuantidade(Long id, Integer quantidade) {
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Material não encontrado com id: " + id));
        material.setQuantidade(material.getQuantidade() + quantidade);
        return materialRepository.save(material);
    }

    @Override
    @Transactional
    public Material diminuirQuantidade(Long id, Integer quantidade) {
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Material não encontrado com id: " + id));
        int novaQuantidade = material.getQuantidade() - quantidade;
        if (novaQuantidade < 0) {
            novaQuantidade = 0;
        }
        material.setQuantidade(novaQuantidade);
        return materialRepository.save(material);
    }
}


