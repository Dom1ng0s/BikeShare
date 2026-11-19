package br.ufal.ic.p2.bikeshare.usuario;

import br.ufal.ic.p2.bikeshare.plano.Plano;

public class PessoaJuridica extends Usuario {

    private static final long serialVersionUID = 1L;

    public static final String TIPO = "pessoaJuridica";

    private final String cnpj;
    private final String razaoSocial;

    public PessoaJuridica(String id, String nome, String email, String senha, Plano plano,
                          String cnpj, String razaoSocial) {
        super(id, nome, email, senha, plano);
        this.cnpj = cnpj;
        this.razaoSocial = razaoSocial;
    }

    @Override
    public String getTipo() {
        return TIPO;
    }

    @Override
    public String getCnpj() {
        return cnpj;
    }

    @Override
    public String getRazaoSocial() {
        return razaoSocial;
    }
}
