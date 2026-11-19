package br.ufal.ic.p2.bikeshare.pagamento;

import java.io.Serializable;

import br.ufal.ic.p2.bikeshare.exception.BikeshareException;

/**
 * Forma pela qual o usuario paga as suas cobrancas. As consultas especificas
 * de cada subtipo falham com mensagem propria nos demais subtipos.
 */
public abstract class MetodoPagamento implements Serializable {

    private static final long serialVersionUID = 1L;

    public abstract String getNome();

    public String getNumeroCartao() throws BikeshareException {
        throw new BikeshareException("Usuario nao usa cartao.");
    }

    public String getValidadeCartao() throws BikeshareException {
        throw new BikeshareException("Usuario nao usa cartao.");
    }

    public String getChavePix() throws BikeshareException {
        throw new BikeshareException("Usuario nao usa pix.");
    }

    public double getSaldo() throws BikeshareException {
        throw new BikeshareException("Usuario nao usa saldo pre-carregado.");
    }

    public void recarregar(double valor) throws BikeshareException {
        throw new BikeshareException("Usuario nao usa saldo pre-carregado.");
    }

    /**
     * Verifica se o metodo autoriza o inicio de um aluguel.
     */
    public void autorizarRetirada() throws BikeshareException {
    }

    /**
     * Verifica se o metodo cobre o valor a ser cobrado.
     */
    public void autorizarCobranca(double valor) throws BikeshareException {
    }

    /**
     * Efetiva a cobranca do valor ja autorizado.
     */
    public void cobrar(double valor) {
    }
}
