package com.bidesk.controller;

import com.bidesk.model.Transacao;
import com.bidesk.model.dto.ApiResponse;
import com.bidesk.service.TransacaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transacoes")
public class TransacaoController extends BaseController<Transacao, Long> {

    @Autowired
    private TransacaoService transacaoService;

    @Override
    protected TransacaoService getService() {
        return transacaoService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Transacao>>> listarTodos() {
        return super.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Transacao>> buscarPorId(@PathVariable Long id) {
        return super.findById(id);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Transacao>> criar(@Valid @RequestBody Transacao transacao) {
        return super.create(transacao);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Transacao>> atualizar(@PathVariable Long id, @Valid @RequestBody Transacao transacao) {
        return super.update(id, transacao);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> deletar(@PathVariable Long id) {
        return super.delete(id);
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<ApiResponse<List<Transacao>>> buscarPorCliente(@PathVariable Long clienteId) {
        List<Transacao> transacoes = transacaoService.buscarPorClienteId(clienteId);
        return ResponseEntity.ok(ApiResponse.success(transacoes));
    }
}


