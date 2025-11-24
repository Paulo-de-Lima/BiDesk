package com.bidesk.model;

public class Cliente {
    private int id;
    private String nome;
    private String endereco;
    private String cidade;
    private String telefone;  // Adicionar este campo
    
    // Construtor vazio
    public Cliente() {
    }
    
    // Construtor com parâmetros (atualizar)
    public Cliente(String nome, String endereco, String cidade) {
        this.nome = nome;
        this.endereco = endereco;
        this.cidade = cidade;
    }
    
    // Construtor com telefone (opcional - adicionar se necessário)
    public Cliente(String nome, String endereco, String cidade, String telefone) {
        this.nome = nome;
        this.endereco = endereco;
        this.cidade = cidade;
        this.telefone = telefone;
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
    
    public String getEndereco() {
        return endereco;
    }
    
    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
    
    public String getCidade() {
        return cidade;
    }
    
    public void setCidade(String cidade) {
        this.cidade = cidade;
    }
    
    // Adicionar getter e setter para telefone
    public String getTelefone() {
        return telefone;
    }
    
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}