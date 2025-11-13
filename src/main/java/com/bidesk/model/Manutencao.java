package com.bidesk.model;

<<<<<<< HEAD
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Entidade Manutencao - Representa uma manutenção registrada.
 * Baseado no wireframe da Aba Manutenção.
 */
@Entity
@Table(name = "manutencoes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Manutencao extends BaseEntity {

    @NotNull(message = "Cliente é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @NotBlank(message = "Título é obrigatório")
    @Column(nullable = false, length = 100)
    private String titulo;

    @Column(length = 500)
    private String descricao;
=======
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
>>>>>>> 2df6e2acf9e0ba6c9fa0effb77c2aa4db3983ed2
}


