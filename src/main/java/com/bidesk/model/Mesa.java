package com.bidesk.model;

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
    }
}