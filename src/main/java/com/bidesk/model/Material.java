package com.bidesk.model;

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
}


