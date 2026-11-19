package br.ufal.ic.p2.bikeshare.pagamento;

import br.ufal.ic.p2.bikeshare.exception.BikeshareException;
import br.ufal.ic.p2.bikeshare.util.Texto;

/**
 * Carteira com saldo recarregavel. O saldo acompanha o usuario mesmo quando
 * outro metodo de pagamento passa a ser o escolhido.
 */
public class SaldoPreCarregado extends MetodoPagamento {

    private static final long serialVersionUID = 1L;

    public static final String NOME = "saldoPreCarregado";

    private double saldo;

    @Override
    public String getNome() {
        return NOME;
    }

    @Override
    public double getSaldo() {
        return Texto.arredondar(saldo);
    }

    @Override
    public void recarregar(double valor) {
        saldo = Texto.arredondar(saldo + valor);
    }

    @Override
    public void autorizarRetirada() throws BikeshareException {
        if (getSaldo() <= 0.0) {
            throw new BikeshareException("Saldo insuficiente.");
        }
    }

    @Override
    public void autorizarCobranca(double valor) throws BikeshareException {
        if (Texto.arredondar(valor) > getSaldo()) {
            throw new BikeshareException("Saldo insuficiente.");
        }
    }

    @Override
    public void cobrar(double valor) {
        saldo = Texto.arredondar(saldo - valor);
    }
}
