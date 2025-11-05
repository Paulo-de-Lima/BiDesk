package com.bidesk.models;

public class Manutencao {
    private int id;
    private int clienteId;
    private String titulo;
    private String descricao;
    private String clienteNome; // Para exibição
    
    public Manutencao(int id, int clienteId, String titulo, String descricao) {
        this.id = id;
        this.clienteId = clienteId;
        this.titulo = titulo;
        this.descricao = descricao;
    }
    
    public Manutencao(int id, int clienteId, String titulo, String descricao, String clienteNome) {
        this.id = id;
        this.clienteId = clienteId;
        this.titulo = titulo;
        this.descricao = descricao;
        this.clienteNome = clienteNome;
    }
    
    // Getters e Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getClienteId() {
        return clienteId;
    }
    
    public void setClienteId(int clienteId) {
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
    
    public String getClienteNome() {
        return clienteNome;
    }
    
    public void setClienteNome(String clienteNome) {
        this.clienteNome = clienteNome;
    }
}


