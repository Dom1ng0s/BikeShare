package br.ufal.ic.p2.bikeshare.bicicleta;

import java.io.Serializable;

import br.ufal.ic.p2.bikeshare.exception.BikeshareException;
import br.ufal.ic.p2.bikeshare.util.Configuracao;

/**
 * Bicicleta compartilhada. O nivel de bateria e o adicional por minuto sao
 * responsabilidade apenas da subclasse eletrica.
 */
public abstract class Bicicleta implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String ESTADO_DISPONIVEL = "disponivel";
    public static final String ESTADO_EM_USO = "emUso";
    public static final String ESTADO_EM_MANUTENCAO = "emManutencao";

    private final String id;
    private String estado = ESTADO_DISPONIVEL;
    private String estacao;

    protected Bicicleta(String id, String estacao) {
        this.id = id;
        this.estacao = estacao;
    }

    public abstract String getTipo();

    public int getNivelBateria() throws BikeshareException {
        throw new BikeshareException("Bicicleta nao possui bateria.");
    }

    /**
     * Indica se a bicicleta pode ser contada como disponivel para retirada.
     */
    public boolean disponivelParaRetirada(Configuracao configuracao) {
        return ESTADO_DISPONIVEL.equals(estado);
    }

    /**
     * Valida as condicoes proprias do tipo de bicicleta no momento da retirada.
     */
    public void validarRetirada(Configuracao configuracao) throws BikeshareException {
    }

    /**
     * Adicional cobrado pelo tipo de bicicleta, somado ao valor do plano.
     */
    public double calcularAdicional(long minutos, Configuracao configuracao) {
        return 0.0;
    }

    public String getId() {
        return id;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getEstacao() {
        return estacao;
    }

    public void setEstacao(String estacao) {
        this.estacao = estacao;
    }
}
