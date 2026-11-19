package br.ufal.ic.p2.bikeshare.aluguel;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import br.ufal.ic.p2.bikeshare.util.Texto;

/**
 * Corrida de um usuario: do momento da retirada ao da devolucao.
 */
public class Aluguel implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String ESTADO_EM_ANDAMENTO = "emAndamento";
    public static final String ESTADO_FINALIZADO = "finalizado";
    public static final String ESTADO_CANCELADO = "cancelado";
    public static final String ESTADO_EXPIRADO = "expirado";

    private final String id;
    private final String usuario;
    private final String bicicleta;
    private final String estacaoOrigem;
    private final LocalDateTime inicio;

    private String estacaoDestino;
    private LocalDateTime fim;
    private String estado = ESTADO_EM_ANDAMENTO;
    private double valorCobrado;

    public Aluguel(String id, String usuario, String bicicleta, String estacaoOrigem, LocalDateTime inicio) {
        this.id = id;
        this.usuario = usuario;
        this.bicicleta = bicicleta;
        this.estacaoOrigem = estacaoOrigem;
        this.inicio = inicio;
    }

    public void encerrar(String estacaoDestino, LocalDateTime fim, String estado, double valorCobrado) {
        this.estacaoDestino = estacaoDestino;
        this.fim = fim;
        this.estado = estado;
        this.valorCobrado = Texto.arredondar(valorCobrado);
    }

    public long getDuracaoEmMinutos(LocalDateTime devolucao) {
        return ChronoUnit.MINUTES.between(inicio, devolucao);
    }

    public boolean estaEmAndamento() {
        return ESTADO_EM_ANDAMENTO.equals(estado);
    }

    public String getId() {
        return id;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getBicicleta() {
        return bicicleta;
    }

    public String getEstacaoOrigem() {
        return estacaoOrigem;
    }

    public LocalDateTime getInicio() {
        return inicio;
    }

    public String getEstacaoDestino() {
        return estacaoDestino;
    }

    public LocalDateTime getFim() {
        return fim;
    }

    public String getEstado() {
        return estado;
    }

    public double getValorCobrado() {
        return Texto.arredondar(valorCobrado);
    }
}
