package com.bidesk.model;

import java.math.BigDecimal;
import java.sql.Date;

public class Despesa {
    private int id;
    private Date data;
    private String cidade;
    private BigDecimal despesa;
    private BigDecimal total;
    
    public Despesa() {
    }
    
    public Despesa(Date data, String cidade, BigDecimal despesa, BigDecimal total) {
        this.data = data;
        this.cidade = cidade;
        this.despesa = despesa;
        this.total = total;
    }
    
    // Getters e Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public Date getData() {
        return data;
    }
    
    public void setData(Date data) {
        this.data = data;
    }
    
    public String getCidade() {
        return cidade;
    }
    
    public void setCidade(String cidade) {
        this.cidade = cidade;
    }
    
    public BigDecimal getDespesa() {
        return despesa;
    }
    
    public void setDespesa(BigDecimal despesa) {
        this.despesa = despesa;
    }
    
    public BigDecimal getTotal() {
        return total;
    }
    
    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}


