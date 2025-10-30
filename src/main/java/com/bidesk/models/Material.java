package com.bidesk.models;

public class Material {
    private int id;
    private String nome;
    private int quantidade;
    private String status;
    
    public Material(int id, String nome, int quantidade, String status) {
        this.id = id;
        this.nome = nome;
        this.quantidade = quantidade;
        this.status = status;
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
    
    public int getQuantidade() {
        return quantidade;
    }
    
    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    @Override
    public String toString() {
        return nome;
    }
}

