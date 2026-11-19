package br.ufal.ic.p2.bikeshare.usuario;

import br.ufal.ic.p2.bikeshare.plano.Plano;

public class PessoaFisica extends Usuario {

    private static final long serialVersionUID = 1L;

    public static final String TIPO = "pessoaFisica";

    private final String cpf;

    public PessoaFisica(String id, String nome, String email, String senha, Plano plano, String cpf) {
        super(id, nome, email, senha, plano);
        this.cpf = cpf;
    }

    @Override
    public String getTipo() {
        return TIPO;
    }

    @Override
    public String getCpf() {
        return cpf;
    }
}
