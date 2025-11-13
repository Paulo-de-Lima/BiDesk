package com.bidesk.model;

<<<<<<< HEAD
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Entidade Material - Representa um material/estoque.
 * Baseado no wireframe da Aba Estoque.
 * Status: Vazio (quantidade = 0), Baixo (quantidade < 5), Alto (quantidade >= 5)
 */
@Entity
@Table(name = "materiais")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Material extends BaseEntity {

    @NotBlank(message = "Nome do material é obrigatório")
    @Column(nullable = false, unique = true, length = 100)
    private String nome;

    @Min(value = 0, message = "Quantidade não pode ser negativa")
    @Column(nullable = false)
    private Integer quantidade = 0;

    @Column(length = 50)
    private String unidade = "unidade";

    /**
     * Retorna o status do estoque baseado na quantidade.
     * @return "VAZIO" se quantidade = 0, "BAIXO" se < 5, "ALTO" se >= 5
     */
    public String getStatusEstoque() {
        if (quantidade == 0) {
            return "VAZIO";
        } else if (quantidade < 5) {
            return "BAIXO";
        } else {
            return "ALTO";
        }
    }
=======
public class Material {
    private int id;
    private String nome;
    private String unidade;
    private int quantidade;
    private StatusMaterial status;
    
    public enum StatusMaterial {
        VAZIO, BAIXO, ALTO
    }
    
    public Material() {
    }
    
    public Material(String nome, String unidade, int quantidade) {
        this.nome = nome;
        this.unidade = unidade;
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
    
    public String getUnidade() {
        return unidade;
    }
    
    public void setUnidade(String unidade) {
        this.unidade = unidade;
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
>>>>>>> 2df6e2acf9e0ba6c9fa0effb77c2aa4db3983ed2
}


