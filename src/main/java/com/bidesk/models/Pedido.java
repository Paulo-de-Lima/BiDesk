package com.bidesk.models;

public class Pedido {
    private int id;
    private int clienteId;
    private String numero;
    private String data;
    private String registro;
    private double pago;
    private double deve;
    
    public Pedido(int id, int clienteId, String numero, String data, String registro, double pago, double deve) {
        this.id = id;
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
    
    public String getData() {
        return data;
    }
    
    public void setData(String data) {
        this.data = data;
    }
    
    public String getRegistro() {
        return registro;
    }
    
    public void setRegistro(String registro) {
        this.registro = registro;
    }
    
    public double getPago() {
        return pago;
    }
    
    public void setPago(double pago) {
        this.pago = pago;
    }
    
    public double getDeve() {
        return deve;
    }
    
    public void setDeve(double deve) {
        this.deve = deve;
    }
}

