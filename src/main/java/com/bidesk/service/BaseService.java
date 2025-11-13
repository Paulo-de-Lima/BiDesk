package com.bidesk.service;

import com.bidesk.model.BaseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Interface base para todos os services.
 * Define métodos CRUD padrão.
 */
public interface BaseService<T extends BaseEntity, ID> {
    List<T> findAll();
    Page<T> findAll(Pageable pageable);
    Optional<T> findById(ID id);
    T save(T entity);
    T update(ID id, T entity);
    void deleteById(ID id);
    boolean existsById(ID id);
}

