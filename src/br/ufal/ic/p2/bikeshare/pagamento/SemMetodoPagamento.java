package br.ufal.ic.p2.bikeshare.pagamento;

import br.ufal.ic.p2.bikeshare.exception.BikeshareException;

/**
 * Estado inicial do usuario: nenhum metodo de pagamento cadastrado.
 */
public class SemMetodoPagamento extends MetodoPagamento {

    private static final long serialVersionUID = 1L;

    public static final String NOME = "nenhum";

    @Override
    public String getNome() {
        return NOME;
    }

    @Override
    public void autorizarRetirada() throws BikeshareException {
        throw new BikeshareException("Metodo de pagamento nao cadastrado.");
    }
}
