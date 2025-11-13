package com.bidesk.controller;

import com.bidesk.model.Material;
import com.bidesk.model.dto.ApiResponse;
import com.bidesk.service.MaterialService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/materiais")
public class MaterialController extends BaseController<Material, Long> {

    @Autowired
    private MaterialService materialService;

    @Override
    protected MaterialService getService() {
        return materialService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Material>>> listarTodos() {
        return super.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Material>> buscarPorId(@PathVariable Long id) {
        return super.findById(id);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Material>> criar(@Valid @RequestBody Material material) {
        return super.create(material);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Material>> atualizar(@PathVariable Long id, @Valid @RequestBody Material material) {
        return super.update(id, material);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> deletar(@PathVariable Long id) {
        return super.delete(id);
    }

    @GetMapping("/buscar")
    public ResponseEntity<ApiResponse<List<Material>>> buscarPorNome(@RequestParam String nome) {
        List<Material> materiais = materialService.buscarPorNome(nome);
        return ResponseEntity.ok(ApiResponse.success(materiais));
    }

    @GetMapping("/estoque-baixo")
    public ResponseEntity<ApiResponse<List<Material>>> buscarEstoqueBaixo() {
        List<Material> materiais = materialService.buscarEstoqueBaixo();
        return ResponseEntity.ok(ApiResponse.success(materiais));
    }

    @PostMapping("/{id}/adicionar")
    public ResponseEntity<ApiResponse<Material>> adicionarQuantidade(
            @PathVariable Long id,
            @RequestParam Integer quantidade) {
        Material material = materialService.adicionarQuantidade(id, quantidade);
        return ResponseEntity.ok(ApiResponse.success("Quantidade adicionada com sucesso", material));
    }

    @PostMapping("/{id}/diminuir")
    public ResponseEntity<ApiResponse<Material>> diminuirQuantidade(
            @PathVariable Long id,
            @RequestParam Integer quantidade) {
        Material material = materialService.diminuirQuantidade(id, quantidade);
        return ResponseEntity.ok(ApiResponse.success("Quantidade diminuída com sucesso", material));
    }
}


