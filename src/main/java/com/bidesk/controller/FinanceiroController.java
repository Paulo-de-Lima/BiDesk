package com.bidesk.controller;

import com.bidesk.dao.DespesaDAO;
import com.bidesk.dao.DespesaMaterialDAO;
import com.bidesk.model.Despesa;
import com.bidesk.model.DespesaMaterial;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

public class FinanceiroController {
    private DespesaDAO despesaDAO;
    private DespesaMaterialDAO despesaMaterialDAO;
    
    public FinanceiroController() {
        despesaDAO = new DespesaDAO();

        despesaMaterialDAO = new DespesaMaterialDAO();
    }
    
    public List<Despesa> listarTodos() {
        return despesaDAO.listarTodos();
    }
    
    public boolean inserir(Date data, String cidade, BigDecimal despesa, BigDecimal total) {
        Despesa despesaObj = new Despesa(data, cidade, despesa, total);
        return despesaDAO.inserir(despesaObj);
    }
    
    public boolean atualizar(Despesa despesa) {
        return despesaDAO.atualizar(despesa);
    }
    
    public boolean deletar(int id) {
        return despesaDAO.deletar(id);
    }
    
    // Métodos para Despesas Materiais
    public List<DespesaMaterial> listarDespesasMateriais() {
        return despesaMaterialDAO.listarTodos();
    }
    
    public List<DespesaMaterial> listarDespesasMateriaisPorDespesa(Integer despesaId) {
        return despesaMaterialDAO.listarPorDespesa(despesaId);
    }
    
    public boolean inserirDespesaMaterial(String nome, Integer despesaId, BigDecimal gasto) {
        DespesaMaterial despesaMaterial = new DespesaMaterial(nome, despesaId, gasto);
        return despesaMaterialDAO.inserir(despesaMaterial);
    }
    
    public boolean atualizarDespesaMaterial(DespesaMaterial despesaMaterial) {
        return despesaMaterialDAO.atualizar(despesaMaterial);
    }
    
    public boolean deletarDespesaMaterial(int id) {
        return despesaMaterialDAO.deletar(id);
    }
    
    public BigDecimal calcularTotalGastoMateriais() {
        return despesaMaterialDAO.calcularTotalGasto();
    }
}


