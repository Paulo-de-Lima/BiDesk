package com.bidesk.model;

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
}


