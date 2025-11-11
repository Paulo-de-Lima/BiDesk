package com.bidesk.model;

public class Manutencao {
    private int id;
    private Integer clienteId;
    private String titulo;
    private String descricao;
    
    public Manutencao() {
    }
    
    public Manutencao(Integer clienteId, String titulo, String descricao) {
        this.clienteId = clienteId;
        this.titulo = titulo;
        this.descricao = descricao;
    }
    
    // Getters e Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public Integer getClienteId() {
        return clienteId;
    }
    
    public void setClienteId(Integer clienteId) {
        this.clienteId = clienteId;
    }
    
    public String getTitulo() {
        return titulo;
    }
    
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}


