package com.bidesk.controller;

import com.bidesk.model.Cliente;
import com.bidesk.model.dto.ApiResponse;
import com.bidesk.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController extends BaseController<Cliente, Long> {

    @Autowired
    private ClienteService clienteService;

    @Override
    protected ClienteService getService() {
        return clienteService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Cliente>>> listarTodos() {
        return super.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Cliente>> buscarPorId(@PathVariable Long id) {
        return super.findById(id);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Cliente>> criar(@Valid @RequestBody Cliente cliente) {
        return super.create(cliente);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Cliente>> atualizar(@PathVariable Long id, @Valid @RequestBody Cliente cliente) {
        return super.update(id, cliente);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> deletar(@PathVariable Long id) {
        return super.delete(id);
    }

    @GetMapping("/buscar")
    public ResponseEntity<ApiResponse<List<Cliente>>> buscarPorNome(@RequestParam String nome) {
        List<Cliente> clientes = clienteService.buscarPorNome(nome);
        return ResponseEntity.ok(ApiResponse.success(clientes));
    }

    @GetMapping("/cidade/{cidade}")
    public ResponseEntity<ApiResponse<List<Cliente>>> buscarPorCidade(@PathVariable String cidade) {
        List<Cliente> clientes = clienteService.buscarPorCidade(cidade);
        return ResponseEntity.ok(ApiResponse.success(clientes));
    }
}

