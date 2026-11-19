package br.ufal.ic.p2.bikeshare.bicicleta;

import br.ufal.ic.p2.bikeshare.exception.BikeshareException;
import br.ufal.ic.p2.bikeshare.util.Configuracao;

public class BicicletaEletrica extends Bicicleta {

    private static final long serialVersionUID = 1L;

    public static final String TIPO = "eletrica";

    private int nivelBateria;

    public BicicletaEletrica(String id, String estacao, int nivelBateria) {
        super(id, estacao);
        this.nivelBateria = nivelBateria;
    }

    @Override
    public String getTipo() {
        return TIPO;
    }

    @Override
    public int getNivelBateria() {
        return nivelBateria;
    }

    public void setNivelBateria(int nivelBateria) {
        this.nivelBateria = nivelBateria;
    }

    @Override
    public boolean disponivelParaRetirada(Configuracao configuracao) {
        return super.disponivelParaRetirada(configuracao)
                && nivelBateria >= configuracao.getNivelMinimoBateria();
    }

    @Override
    public void validarRetirada(Configuracao configuracao) throws BikeshareException {
        if (nivelBateria < configuracao.getNivelMinimoBateria()) {
            throw new BikeshareException("Nivel de bateria insuficiente.");
        }
    }

    @Override
    public double calcularAdicional(long minutos, Configuracao configuracao) {
        return minutos * configuracao.getAdicionalEletricaPorMinuto();
    }
}
