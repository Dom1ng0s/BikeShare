package br.ufal.ic.p2.bikeshare.usuario;

import java.io.Serializable;

import br.ufal.ic.p2.bikeshare.exception.BikeshareException;
import br.ufal.ic.p2.bikeshare.pagamento.MetodoPagamento;
import br.ufal.ic.p2.bikeshare.pagamento.SaldoPreCarregado;
import br.ufal.ic.p2.bikeshare.pagamento.SemMetodoPagamento;
import br.ufal.ic.p2.bikeshare.plano.Plano;
import br.ufal.ic.p2.bikeshare.util.Texto;

/**
 * Usuario do sistema. Os dados especificos de cada tipo ficam nas subclasses.
 */
public abstract class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String ESTADO_ATIVO = "ativo";
    public static final String ESTADO_INADIMPLENTE = "inadimplente";

    private final String id;
    private String nome;
    private String email;
    private String senha;
    private Plano plano;
    private MetodoPagamento metodoPagamento = new SemMetodoPagamento();
    private SaldoPreCarregado carteira;
    private String estado = ESTADO_ATIVO;
    private double totalPendencias;
    private String aluguelAtivo;

    protected Usuario(String id, String nome, String email, String senha, Plano plano) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.plano = plano;
    }

    public abstract String getTipo();

    public String getCpf() throws BikeshareException {
        throw new BikeshareException("Usuario nao eh pessoa fisica.");
    }

    public String getCnpj() throws BikeshareException {
        throw new BikeshareException("Usuario nao eh pessoa juridica.");
    }

    public String getRazaoSocial() throws BikeshareException {
        throw new BikeshareException("Usuario nao eh pessoa juridica.");
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Plano getPlano() {
        return plano;
    }

    public void setPlano(Plano plano) {
        this.plano = plano;
    }

    public MetodoPagamento getMetodoPagamento() {
        return metodoPagamento;
    }

    public void setMetodoPagamento(MetodoPagamento metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }

    /**
     * Carteira de saldo pre-carregado do usuario, criada na primeira vez que
     * esse metodo de pagamento e escolhido e preservada depois disso.
     */
    public SaldoPreCarregado getCarteira() {
        if (carteira == null) {
            carteira = new SaldoPreCarregado();
        }
        return carteira;
    }

    public String getEstado() {
        return estado;
    }

    public boolean isInadimplente() {
        return ESTADO_INADIMPLENTE.equals(estado);
    }

    public double getTotalPendencias() {
        return Texto.arredondar(totalPendencias);
    }

    public void lancarPendencia(double valor) {
        totalPendencias = Texto.arredondar(totalPendencias + valor);
        if (totalPendencias > 0) {
            estado = ESTADO_INADIMPLENTE;
        }
    }

    public void quitarPendencias(double valor) {
        totalPendencias = Texto.arredondar(Math.max(0.0, totalPendencias - valor));
        if (totalPendencias == 0.0) {
            estado = ESTADO_ATIVO;
        }
    }

    public String getAluguelAtivo() {
        return aluguelAtivo;
    }

    public void setAluguelAtivo(String aluguelAtivo) {
        this.aluguelAtivo = aluguelAtivo;
    }
}
