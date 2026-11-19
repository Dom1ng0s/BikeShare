package br.ufal.ic.p2.bikeshare.exception;

/**
 * Excecao unica de negocio do sistema. A mensagem transportada e exatamente
 * a mensagem esperada pelos testes de aceitacao.
 */
public class BikeshareException extends Exception {

    private static final long serialVersionUID = 1L;

    public BikeshareException(String mensagem) {
        super(mensagem);
    }
}
