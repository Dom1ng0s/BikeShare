package br.ufal.ic.p2.bikeshare.util;

import java.io.Serializable;

/**
 * Parametros de tarifacao e limites de negocio. Requisito nao funcional BS04:
 * nada disso pode ficar embutido diretamente na logica.
 */
public class Configuracao implements Serializable {

    private static final long serialVersionUID = 1L;

    private double tarifaPorMinuto = 0.50;
    private double adicionalEletricaPorMinuto = 0.20;
    private int franquiaAssinanteMinutos = 60;
    private double valorMinutoExcedente = 0.30;
    private double mensalidadePadrao = 30.00;
    private int prazoCarenciaMinutos = 2;
    private int tempoMaximoAluguelMinutos = 1440;
    private int nivelMinimoBateria = 15;
    private double penalidadeAtraso = 10.00;

    public double getTarifaPorMinuto() {
        return tarifaPorMinuto;
    }

    public void setTarifaPorMinuto(double tarifaPorMinuto) {
        this.tarifaPorMinuto = tarifaPorMinuto;
    }

    public double getAdicionalEletricaPorMinuto() {
        return adicionalEletricaPorMinuto;
    }

    public void setAdicionalEletricaPorMinuto(double adicionalEletricaPorMinuto) {
        this.adicionalEletricaPorMinuto = adicionalEletricaPorMinuto;
    }

    public int getFranquiaAssinanteMinutos() {
        return franquiaAssinanteMinutos;
    }

    public void setFranquiaAssinanteMinutos(int franquiaAssinanteMinutos) {
        this.franquiaAssinanteMinutos = franquiaAssinanteMinutos;
    }

    public double getValorMinutoExcedente() {
        return valorMinutoExcedente;
    }

    public void setValorMinutoExcedente(double valorMinutoExcedente) {
        this.valorMinutoExcedente = valorMinutoExcedente;
    }

    public double getMensalidadePadrao() {
        return mensalidadePadrao;
    }

    public void setMensalidadePadrao(double mensalidadePadrao) {
        this.mensalidadePadrao = mensalidadePadrao;
    }

    public int getPrazoCarenciaMinutos() {
        return prazoCarenciaMinutos;
    }

    public void setPrazoCarenciaMinutos(int prazoCarenciaMinutos) {
        this.prazoCarenciaMinutos = prazoCarenciaMinutos;
    }

    public int getTempoMaximoAluguelMinutos() {
        return tempoMaximoAluguelMinutos;
    }

    public void setTempoMaximoAluguelMinutos(int tempoMaximoAluguelMinutos) {
        this.tempoMaximoAluguelMinutos = tempoMaximoAluguelMinutos;
    }

    public int getNivelMinimoBateria() {
        return nivelMinimoBateria;
    }

    public void setNivelMinimoBateria(int nivelMinimoBateria) {
        this.nivelMinimoBateria = nivelMinimoBateria;
    }

    public double getPenalidadeAtraso() {
        return penalidadeAtraso;
    }

    public void setPenalidadeAtraso(double penalidadeAtraso) {
        this.penalidadeAtraso = penalidadeAtraso;
    }
}
