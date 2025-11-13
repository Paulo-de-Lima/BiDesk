package com.bidesk.controller;

import com.bidesk.model.Manutencao;
import com.bidesk.model.dto.ApiResponse;
import com.bidesk.service.ManutencaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/manutencoes")
public class ManutencaoController extends BaseController<Manutencao, Long> {

    @Autowired
    private ManutencaoService manutencaoService;

    @Override
    protected ManutencaoService getService() {
        return manutencaoService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Manutencao>>> listarTodos() {
        return super.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Manutencao>> buscarPorId(@PathVariable Long id) {
        return super.findById(id);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Manutencao>> criar(@Valid @RequestBody Manutencao manutencao) {
        return super.create(manutencao);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Manutencao>> atualizar(@PathVariable Long id, @Valid @RequestBody Manutencao manutencao) {
        return super.update(id, manutencao);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> deletar(@PathVariable Long id) {
        return super.delete(id);
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<ApiResponse<List<Manutencao>>> buscarPorCliente(@PathVariable Long clienteId) {
        List<Manutencao> manutencoes = manutencaoService.buscarPorClienteId(clienteId);
        return ResponseEntity.ok(ApiResponse.success(manutencoes));
    }

    @GetMapping("/buscar")
    public ResponseEntity<ApiResponse<List<Manutencao>>> buscarPorTitulo(@RequestParam String titulo) {
        List<Manutencao> manutencoes = manutencaoService.buscarPorTitulo(titulo);
        return ResponseEntity.ok(ApiResponse.success(manutencoes));
    }
}


