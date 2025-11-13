package com.bidesk.controller;

import com.bidesk.model.Cobranca;
import com.bidesk.model.dto.ApiResponse;
import com.bidesk.service.CobrancaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/cobrancas")
public class CobrancaController extends BaseController<Cobranca, Long> {

    @Autowired
    private CobrancaService cobrancaService;

    @Override
    protected CobrancaService getService() {
        return cobrancaService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Cobranca>>> listarTodos() {
        return super.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Cobranca>> buscarPorId(@PathVariable Long id) {
        return super.findById(id);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Cobranca>> criar(@Valid @RequestBody Cobranca cobranca) {
        return super.create(cobranca);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Cobranca>> atualizar(@PathVariable Long id, @Valid @RequestBody Cobranca cobranca) {
        return super.update(id, cobranca);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> deletar(@PathVariable Long id) {
        return super.delete(id);
    }

    @GetMapping("/cidade/{cidade}")
    public ResponseEntity<ApiResponse<List<Cobranca>>> buscarPorCidade(@PathVariable String cidade) {
        List<Cobranca> cobrancas = cobrancaService.buscarPorCidade(cidade);
        return ResponseEntity.ok(ApiResponse.success(cobrancas));
    }

    @GetMapping("/periodo")
    public ResponseEntity<ApiResponse<List<Cobranca>>> buscarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        List<Cobranca> cobrancas = cobrancaService.buscarPorPeriodo(dataInicio, dataFim);
        return ResponseEntity.ok(ApiResponse.success(cobrancas));
    }
}


