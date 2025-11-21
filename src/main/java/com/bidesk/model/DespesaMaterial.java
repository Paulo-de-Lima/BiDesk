package com.bidesk.model;

import java.math.BigDecimal;

public class DespesaMaterial {
    private int id;
    private String nome;
    private Integer despesaId;
    private BigDecimal gasto;
    
    public DespesaMaterial() {
    }
    
    public DespesaMaterial(String nome, Integer despesaId, BigDecimal gasto) {
        this.nome = nome;
        this.despesaId = despesaId;
        this.gasto = gasto;
    }
    
    // Getters e Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public Integer getDespesaId() {
        return despesaId;
    }
    
    public void setDespesaId(Integer despesaId) {
        this.despesaId = despesaId;
    }
    
    public BigDecimal getGasto() {
        return gasto;
    }
    
    public void setGasto(BigDecimal gasto) {
        this.gasto = gasto;
    }
}

