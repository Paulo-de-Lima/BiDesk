package com.bidesk.controller;

import com.bidesk.model.BaseEntity;
import com.bidesk.model.dto.ApiResponse;
import com.bidesk.service.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * Controller base com endpoints CRUD padrão.
 */
public abstract class BaseController<T extends BaseEntity, ID> {

    protected abstract BaseService<T, ID> getService();

    /**
     * Lista todos os recursos.
     */
    public ResponseEntity<ApiResponse<List<T>>> findAll() {
        List<T> entities = getService().findAll();
        return ResponseEntity.ok(ApiResponse.success(entities));
    }

    /**
     * Lista recursos com paginação.
     */
    public ResponseEntity<ApiResponse<Page<T>>> findAll(Pageable pageable) {
        Page<T> entities = getService().findAll(pageable);
        return ResponseEntity.ok(ApiResponse.success(entities));
    }

    /**
     * Busca um recurso por ID.
     */
    public ResponseEntity<ApiResponse<T>> findById(ID id) {
        T entity = getService().findById(id)
                .orElseThrow(() -> new com.bidesk.exception.ResourceNotFoundException("Recurso não encontrado com id: " + id));
        return ResponseEntity.ok(ApiResponse.success(entity));
    }

    /**
     * Cria um novo recurso.
     */
    public ResponseEntity<ApiResponse<T>> create(T entity) {
        T savedEntity = getService().save(entity);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Recurso criado com sucesso", savedEntity));
    }

    /**
     * Atualiza um recurso existente.
     */
    public ResponseEntity<ApiResponse<T>> update(ID id, T entity) {
        T updatedEntity = getService().update(id, entity);
        return ResponseEntity.ok(ApiResponse.success("Recurso atualizado com sucesso", updatedEntity));
    }

    /**
     * Deleta um recurso.
     */
    public ResponseEntity<ApiResponse<Object>> delete(ID id) {
        getService().deleteById(id);
        return ResponseEntity.ok(ApiResponse.success("Recurso deletado com sucesso", null));
    }
}

