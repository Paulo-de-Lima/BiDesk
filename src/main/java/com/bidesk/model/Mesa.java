package com.bidesk.model;

<<<<<<< HEAD
import java.util.ArrayList;
import java.util.List;

public class Mesa {
    public enum TipoContrato {
        ALUGADA("Mesa Alugada"),
        VENDIDA("Mesa Vendida");

        private final String descricao;
        TipoContrato(String descricao) {
            this.descricao = descricao;
        }
        public String getDescricao() {
            return descricao;
        }
    }

    private int id;
    private TipoContrato tipoContrato;
    private String numero;
    private List<RegistroFinanceiro> registrosFinanceiros;

    public Mesa() {
        registrosFinanceiros = new ArrayList<>();
    }

    public Mesa(String numero, TipoContrato tipoContrato) {
        this.numero = numero;
        this.tipoContrato = tipoContrato;
        this.registrosFinanceiros = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TipoContrato getTipoContrato() {
        return tipoContrato;
    }

    public void setTipoContrato(TipoContrato tipoContrato) {
        this.tipoContrato = tipoContrato;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public List<RegistroFinanceiro> getRegistrosFinanceiros() {
        return registrosFinanceiros;
    }

    public void setRegistrosFinanceiros(List<RegistroFinanceiro> registrosFinanceiros) {
        this.registrosFinanceiros = registrosFinanceiros;
    }

    public void addRegistroFinanceiro(RegistroFinanceiro registro) {
        this.registrosFinanceiros.add(registro);
=======
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
>>>>>>> 190a2ab4b8b8f911ccf454c4b9d43e97d5dfdf8a
    }
}