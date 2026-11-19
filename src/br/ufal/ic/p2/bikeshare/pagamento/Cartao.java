package br.ufal.ic.p2.bikeshare.pagamento;

public class Cartao extends MetodoPagamento {

    private static final long serialVersionUID = 1L;

    public static final String NOME = "cartao";

    private final String numero;
    private final String validade;

    public Cartao(String numero, String validade) {
        this.numero = numero;
        this.validade = validade;
    }

    @Override
    public String getNome() {
        return NOME;
    }

    @Override
    public String getNumeroCartao() {
        return numero;
    }

    @Override
    public String getValidadeCartao() {
        return validade;
    }
}
