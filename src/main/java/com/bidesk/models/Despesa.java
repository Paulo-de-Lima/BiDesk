package com.bidesk.models;

public class Despesa {
    private int id;
    private String data;
    private String cidade;
    private double despesa;
    private double total;
    
    public Despesa(int id, String data, String cidade, double despesa, double total) {
        this.id = id;
        this.data = data;
        this.cidade = cidade;
        this.despesa = despesa;
        this.total = total;
    }
    
    // Getters e Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getData() {
        return data;
    }
    
    public void setData(String data) {
        this.data = data;
    }
    
    public String getCidade() {
        return cidade;
    }
    
    public void setCidade(String cidade) {
        this.cidade = cidade;
    }
    
    public double getDespesa() {
        return despesa;
    }
    
    public void setDespesa(double despesa) {
        this.despesa = despesa;
    }
    
    public double getTotal() {
        return total;
    }
    
    public void setTotal(double total) {
        this.total = total;
    }
}

