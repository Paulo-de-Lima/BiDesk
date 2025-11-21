package com.bidesk.model;

import java.math.BigDecimal;
import java.sql.Date;

public class Mesa {
    private int id;
    private int clienteId;
    private String numero;
    private Date data;
    private String registro;
    private BigDecimal pago;
    private BigDecimal deve;
    
    public Mesa() {
    }
    
    public Mesa(int clienteId, String numero, Date data, String registro, BigDecimal pago, BigDecimal deve) {
        this.clienteId = clienteId;
        this.numero = numero;
        this.data = data;
        this.registro = registro;
        this.pago = pago;
        this.deve = deve;
    }
    
    // Getters e Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getClienteId() {
        return clienteId;
    }
    
    public void setClienteId(int clienteId) {
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
        this.pago = pago;
    }
    
    public BigDecimal getDeve() {
        return deve;
    }
    
    public void setDeve(BigDecimal deve) {
        this.deve = deve;
    }
}
