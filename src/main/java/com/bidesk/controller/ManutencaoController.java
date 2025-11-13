package com.bidesk.controller;

<<<<<<< HEAD
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
=======
import com.bidesk.dao.ManutencaoDAO;
import com.bidesk.dao.ClienteDAO;
import com.bidesk.model.Manutencao;
import com.bidesk.model.Cliente;
import java.util.List;
import java.util.ArrayList;

public class ManutencaoController {
    private ManutencaoDAO manutencaoDAO;
    private ClienteDAO clienteDAO;
    
    public ManutencaoController() {
        manutencaoDAO = new ManutencaoDAO();
        clienteDAO = new ClienteDAO();
    }
    
    public List<Manutencao> listarTodos() {
        return manutencaoDAO.listarTodos();
    }
    
    public String getNomeCliente(Integer clienteId) {
        if (clienteId == null) {
            return null;
        }
        Cliente cliente = clienteDAO.buscarPorId(clienteId);
        return cliente != null ? cliente.getNome() : null;
    }
    
    public List<String> listarNomesClientes() {
        List<String> nomes = new ArrayList<>();
        List<Cliente> clientes = clienteDAO.listarTodos();
        for (Cliente cliente : clientes) {
            nomes.add(cliente.getNome());
        }
        return nomes;
    }
    
    public Integer getIdClientePorNome(String nome) {
        List<Cliente> clientes = clienteDAO.listarTodos();
        for (Cliente cliente : clientes) {
            if (cliente.getNome().equals(nome)) {
                return cliente.getId();
            }
        }
        return null;
    }
    
    public boolean inserir(Integer clienteId, String titulo, String descricao) {
        Manutencao manutencao = new Manutencao(clienteId, titulo, descricao);
        return manutencaoDAO.inserir(manutencao);
    }
    
    public boolean atualizar(Manutencao manutencao) {
        return manutencaoDAO.atualizar(manutencao);
    }
    
    public boolean deletar(int id) {
        return manutencaoDAO.deletar(id);
>>>>>>> 2df6e2acf9e0ba6c9fa0effb77c2aa4db3983ed2
    }
}


