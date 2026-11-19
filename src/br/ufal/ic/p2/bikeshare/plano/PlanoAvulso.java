package br.ufal.ic.p2.bikeshare.plano;

import br.ufal.ic.p2.bikeshare.util.Configuracao;

/**
 * Plano padrao: o usuario paga por minuto a cada corrida.
 */
public class PlanoAvulso extends Plano {

    private static final long serialVersionUID = 1L;

    public static final String NOME = "avulso";

    @Override
    public String getNome() {
        return NOME;
    }

    @Override
    public double calcularValor(long minutos, Configuracao configuracao) {
        return minutos * configuracao.getTarifaPorMinuto();
    }
}
