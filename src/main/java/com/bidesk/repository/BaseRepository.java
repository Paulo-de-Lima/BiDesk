package com.bidesk.repository;

import com.bidesk.model.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Interface base para todos os repositórios.
 * Estende JpaRepository com funcionalidades comuns.
 */
@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity, ID> extends JpaRepository<T, ID> {
    // Métodos comuns podem ser adicionados aqui
}


