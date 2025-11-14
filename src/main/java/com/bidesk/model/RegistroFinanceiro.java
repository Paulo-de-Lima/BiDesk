package com.bidesk.model;

import java.math.BigDecimal;
import java.sql.Date;

public class RegistroFinanceiro {
    private int id;
    private Integer clienteId;
    private String numero;
    private Date data;
    private String registro;
    private BigDecimal pago;
    private BigDecimal deve;
    
    public RegistroFinanceiro() {
        this.pago = BigDecimal.ZERO;
        this.deve = BigDecimal.ZERO;
    }
    
    public RegistroFinanceiro(Integer clienteId, String numero, Date data, String registro, BigDecimal pago, BigDecimal deve) {
        this.clienteId = clienteId;
        this.numero = numero;
        this.data = data;
        this.registro = registro;
        this.pago = pago != null ? pago : BigDecimal.ZERO;
        this.deve = deve != null ? deve : BigDecimal.ZERO;
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
    
    public String getNumero() {
        return numero;
    }
    
    public void setNumero(String numero) {
        this.numero = numero;
    }
    
    public Date getData() {
        return data;
    }
    
    public void setData(Date data) {
        this.data = data;
    }
    
    public String getRegistro() {
        return registro;
    }
    
    public void setRegistro(String registro) {
        this.registro = registro;
    }
    
    public BigDecimal getPago() {
        return pago;
    }
    
    public void setPago(BigDecimal pago) {
        this.pago = pago != null ? pago : BigDecimal.ZERO;
    }
    
    public BigDecimal getDeve() {
        return deve;
    }
    
    public void setDeve(BigDecimal deve) {
        this.deve = deve != null ? deve : BigDecimal.ZERO;
    }
}


