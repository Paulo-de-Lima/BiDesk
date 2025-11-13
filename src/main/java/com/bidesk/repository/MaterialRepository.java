package com.bidesk.repository;

import com.bidesk.model.Material;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MaterialRepository extends BaseRepository<Material, Long> {
    Optional<Material> findByNome(String nome);
    List<Material> findByNomeContainingIgnoreCase(String nome);
    List<Material> findByQuantidadeLessThan(Integer quantidade);
}


