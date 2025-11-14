package com.bidesk.model;

public class Material {
    private int id;
    private String nome;
    private int quantidade;
    private StatusMaterial status;
    
    public enum StatusMaterial {
        VAZIO, BAIXO, ALTO
    }
    
    public Material() {
    }
    
    public Material(String nome, int quantidade) {
        this.nome = nome;
        this.quantidade = quantidade;
        atualizarStatus();
    }
    
    public void atualizarStatus() {
        if (quantidade == 0) {
            this.status = StatusMaterial.VAZIO;
        } else if (quantidade < 5) {
            this.status = StatusMaterial.BAIXO;
        } else {
            this.status = StatusMaterial.ALTO;
        }
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
        atualizarStatus();
    }
    
    public StatusMaterial getStatus() {
        return status;
    }
    
    public void setStatus(StatusMaterial status) {
        this.status = status;
    }
}



