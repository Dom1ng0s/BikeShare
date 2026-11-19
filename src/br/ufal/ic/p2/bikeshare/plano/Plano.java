package br.ufal.ic.p2.bikeshare.plano;

import java.io.Serializable;

import br.ufal.ic.p2.bikeshare.exception.BikeshareException;
import br.ufal.ic.p2.bikeshare.util.Configuracao;

/**
 * Modalidade de cobranca contratada pelo usuario.
 */
public abstract class Plano implements Serializable {

    private static final long serialVersionUID = 1L;

    public abstract String getNome();

    /**
     * Valor da corrida considerando apenas o plano (sem adicionais da bicicleta).
     */
    public abstract double calcularValor(long minutos, Configuracao configuracao);

    public boolean possuiMensalidade() {
        return false;
    }

    public double getMensalidade() throws BikeshareException {
        throw new BikeshareException("Plano nao possui mensalidade.");
    }
}
