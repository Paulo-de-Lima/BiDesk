package com.bidesk.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entidade Transacao - Representa uma transação financeira de um cliente.
 * Baseado no wireframe da Aba Clientes (tabela de transações).
 */
@Entity
@Table(name = "transacoes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Transacao extends BaseEntity {

    @NotNull(message = "Cliente é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Column(name = "numero")
    private Long numero;

    @Column(name = "data")
    private LocalDate data;

    @Column(name = "registro")
    private Long registro;

    @Column(name = "pago", precision = 10, scale = 2)
    private BigDecimal pago = BigDecimal.ZERO;

    @Column(name = "deve", precision = 10, scale = 2)
    private BigDecimal deve = BigDecimal.ZERO;
}


