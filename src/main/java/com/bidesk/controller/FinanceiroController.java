package com.bidesk.controller;

import com.bidesk.dao.DespesaDAO;
import com.bidesk.model.Despesa;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

public class FinanceiroController {
    private DespesaDAO despesaDAO;
    
    public FinanceiroController() {
        despesaDAO = new DespesaDAO();
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
}


