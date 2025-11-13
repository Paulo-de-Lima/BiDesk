package com.bidesk.repository;

import com.bidesk.model.Cliente;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends BaseRepository<Cliente, Long> {
    List<Cliente> findByNomeContainingIgnoreCase(String nome);
    List<Cliente> findByCidade(String cidade);
    Optional<Cliente> findByNome(String nome);
}


