package com.bidesk.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entidade Cobranca - Representa uma cobrança/despesa financeira.
 * Baseado no wireframe da Aba Financeiro.
 */
@Entity
@Table(name = "cobrancas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Cobranca extends BaseEntity {

    @NotNull(message = "Data é obrigatória")
    @Column(name = "data", nullable = false)
    private LocalDate data;

    @Column(name = "cidade", length = 100)
    private String cidade;

    @NotNull(message = "Despesa é obrigatória")
    @Min(value = 0, message = "Despesa não pode ser negativa")
    @Column(name = "despesa", nullable = false, precision = 10, scale = 2)
    private BigDecimal despesa;

    @Column(name = "total", precision = 10, scale = 2)
    private BigDecimal total;
}


