package com.bidesk.service.impl;

import com.bidesk.exception.ResourceNotFoundException;
import com.bidesk.model.BaseEntity;
import com.bidesk.repository.BaseRepository;
import com.bidesk.service.BaseService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Implementação base para todos os services.
 * Fornece implementação padrão dos métodos CRUD.
 */
public abstract class BaseServiceImpl<T extends BaseEntity, ID> implements BaseService<T, ID> {

    protected abstract BaseRepository<T, ID> getRepository();

    @Override
    public List<T> findAll() {
        return getRepository().findAll();
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        return getRepository().findAll(pageable);
    }

    @Override
    public Optional<T> findById(ID id) {
        return getRepository().findById(id);
    }

    @Override
    public T save(T entity) {
        return getRepository().save(entity);
    }

    @Override
    public T update(ID id, T entity) {
        T existingEntity = getRepository().findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recurso não encontrado com id: " + id));
        // Copiar propriedades do entity para existingEntity, ignorando id, createdAt e updatedAt
        BeanUtils.copyProperties(entity, existingEntity, "id", "createdAt", "updatedAt");
        return getRepository().save(existingEntity);
    }

    @Override
    public void deleteById(ID id) {
        if (!getRepository().existsById(id)) {
            throw new ResourceNotFoundException("Recurso não encontrado com id: " + id);
        }
        getRepository().deleteById(id);
    }

    @Override
    public boolean existsById(ID id) {
        return getRepository().existsById(id);
    }
}
