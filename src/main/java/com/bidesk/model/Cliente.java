package com.bidesk.model;

<<<<<<< HEAD
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Entidade Cliente - Representa um cliente do sistema.
 * Baseado no wireframe da Aba Clientes.
 */
@Entity
@Table(name = "clientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Cliente extends BaseEntity {

    @NotBlank(message = "Nome é obrigatório")
    @Column(nullable = false, length = 100)
    private String nome;

    @Column(length = 200)
    private String endereco;

    @Column(length = 100)
    private String cidade;

    @JsonIgnore
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transacao> transacoes;

    @JsonIgnore
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Manutencao> manutencoes;
}

=======
public class Cliente {
    private int id;
    private String nome;
    private String endereco;
    private String cidade;
    
    public Cliente() {
    }
    
    public Cliente(String nome, String endereco, String cidade) {
        this.nome = nome;
        this.endereco = endereco;
        this.cidade = cidade;
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
}


>>>>>>> 2df6e2acf9e0ba6c9fa0effb77c2aa4db3983ed2
