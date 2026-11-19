package br.ufal.ic.p2.bikeshare.plano;

import br.ufal.ic.p2.bikeshare.util.Configuracao;

/**
 * Plano com mensalidade: franquia de minutos gratuitos por corrida e
 * cobranca apenas dos minutos excedentes.
 */
public class PlanoAssinante extends Plano {

    private static final long serialVersionUID = 1L;

    public static final String NOME = "assinante";

    private final double mensalidade;

    public PlanoAssinante(double mensalidade) {
        this.mensalidade = mensalidade;
    }

    @Override
    public String getNome() {
        return NOME;
    }

    @Override
    public double calcularValor(long minutos, Configuracao configuracao) {
        long excedente = minutos - configuracao.getFranquiaAssinanteMinutos();
        if (excedente <= 0) {
            return 0.0;
        }
        return excedente * configuracao.getValorMinutoExcedente();
    }

    @Override
    public boolean possuiMensalidade() {
        return true;
    }

    @Override
    public double getMensalidade() {
        return mensalidade;
    }
}
