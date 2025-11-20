package com.bidesk.model;

import java.math.BigDecimal;
import java.sql.Date;

public class ClienteMesa {
    private int id; // ID do relacionamento (cliente_mesas)
    private int clienteId;
    private int mesaId;
    private String identificacaoMesa; // Para exibir no View
    private String localizacaoMesa;   // Para exibir no View
    private Date dataInicioAluguel;
    private BigDecimal valorAluguelMensal;

    public ClienteMesa() {}
    
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

    public int getMesaId() {
        return mesaId;
    }

    public void setMesaId(int mesaId) {
        this.mesaId = mesaId;
    }

    public String getIdentificacaoMesa() {
        return identificacaoMesa;
    }

    public void setIdentificacaoMesa(String identificacaoMesa) {
        this.identificacaoMesa = identificacaoMesa;
    }

    public String getLocalizacaoMesa() {
        return localizacaoMesa;
    }

    public void setLocalizacaoMesa(String localizacaoMesa) {
        this.localizacaoMesa = localizacaoMesa;
    }

    public Date getDataInicioAluguel() {
        return dataInicioAluguel;
    }

    public void setDataInicioAluguel(Date dataInicioAluguel) {
        this.dataInicioAluguel = dataInicioAluguel;
    }

    public BigDecimal getValorAluguelMensal() {
        return valorAluguelMensal;
    }

    public void setValorAluguelMensal(BigDecimal valorAluguelMensal) {
        this.valorAluguelMensal = valorAluguelMensal;
    }
}