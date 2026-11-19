package br.ufal.ic.p2.bikeshare.pagamento;

public class Pix extends MetodoPagamento {

    private static final long serialVersionUID = 1L;

    public static final String NOME = "pix";

    private final String chave;

    public Pix(String chave) {
        this.chave = chave;
    }

    @Override
    public String getNome() {
        return NOME;
    }

    @Override
    public String getChavePix() {
        return chave;
    }
}
