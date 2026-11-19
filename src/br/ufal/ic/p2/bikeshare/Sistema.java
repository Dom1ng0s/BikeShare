package br.ufal.ic.p2.bikeshare;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import br.ufal.ic.p2.bikeshare.aluguel.Aluguel;
import br.ufal.ic.p2.bikeshare.bicicleta.Bicicleta;
import br.ufal.ic.p2.bikeshare.bicicleta.BicicletaConvencional;
import br.ufal.ic.p2.bikeshare.bicicleta.BicicletaEletrica;
import br.ufal.ic.p2.bikeshare.estacao.Estacao;
import br.ufal.ic.p2.bikeshare.exception.BikeshareException;
import br.ufal.ic.p2.bikeshare.pagamento.Cartao;
import br.ufal.ic.p2.bikeshare.pagamento.MetodoPagamento;
import br.ufal.ic.p2.bikeshare.pagamento.Pix;
import br.ufal.ic.p2.bikeshare.pagamento.SaldoPreCarregado;
import br.ufal.ic.p2.bikeshare.pagamento.SemMetodoPagamento;
import br.ufal.ic.p2.bikeshare.plano.Plano;
import br.ufal.ic.p2.bikeshare.plano.PlanoAssinante;
import br.ufal.ic.p2.bikeshare.plano.PlanoAvulso;
import br.ufal.ic.p2.bikeshare.usuario.PessoaFisica;
import br.ufal.ic.p2.bikeshare.usuario.PessoaJuridica;
import br.ufal.ic.p2.bikeshare.usuario.Usuario;
import br.ufal.ic.p2.bikeshare.util.Configuracao;
import br.ufal.ic.p2.bikeshare.util.Datas;
import br.ufal.ic.p2.bikeshare.util.Texto;

/**
 * Nucleo da logica de negocio do BIKESHARE. A Facade apenas delega para esta
 * classe, que concentra o estado do sistema e as regras de negocio.
 */
public class Sistema implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Map<String, Usuario> usuarios = new LinkedHashMap<>();
    private final Map<String, Estacao> estacoes = new LinkedHashMap<>();
    private final Map<String, Bicicleta> bicicletas = new LinkedHashMap<>();
    private final Map<String, Aluguel> alugueis = new LinkedHashMap<>();

    private final Configuracao configuracao = new Configuracao();

    private int proximoUsuario = 1;
    private int proximaEstacao = 1;
    private int proximoAluguel = 1;

    public Configuracao getConfiguracao() {
        return configuracao;
    }

    // =====================================================================
    // US1 - Cadastro de usuario
    // =====================================================================

    public String criarPessoaFisica(String nome, String email, String senha, String tipo, String cpf,
                                    String plano, String mensalidade, boolean planoInformado)
            throws BikeshareException {
        validarDadosBasicos(nome, email, senha);
        exigirTipo(tipo, PessoaFisica.TIPO);
        if (Texto.vazio(cpf)) {
            throw new BikeshareException("CPF nao pode ser nulo.");
        }
        Plano planoContratado = construirPlano(plano, mensalidade, planoInformado);
        exigirEmailLivre(email, null);

        String id = "U" + proximoUsuario++;
        Usuario usuario = new PessoaFisica(id, Texto.limpar(nome), Texto.limpar(email),
                Texto.limpar(senha), planoContratado, Texto.limpar(cpf));
        usuarios.put(id, usuario);
        return id;
    }

    public String criarPessoaJuridica(String nome, String email, String senha, String tipo, String cnpj,
                                      String razaoSocial, String plano, String mensalidade,
                                      boolean planoInformado) throws BikeshareException {
        validarDadosBasicos(nome, email, senha);
        exigirTipo(tipo, PessoaJuridica.TIPO);
        if (Texto.vazio(cnpj)) {
            throw new BikeshareException("CNPJ nao pode ser nulo.");
        }
        if (Texto.vazio(razaoSocial)) {
            throw new BikeshareException("Razao social nao pode ser nula.");
        }
        Plano planoContratado = construirPlano(plano, mensalidade, planoInformado);
        exigirEmailLivre(email, null);

        String id = "U" + proximoUsuario++;
        Usuario usuario = new PessoaJuridica(id, Texto.limpar(nome), Texto.limpar(email),
                Texto.limpar(senha), planoContratado, Texto.limpar(cnpj), Texto.limpar(razaoSocial));
        usuarios.put(id, usuario);
        return id;
    }

    private void validarDadosBasicos(String nome, String email, String senha) throws BikeshareException {
        if (Texto.vazio(nome)) {
            throw new BikeshareException("Nome nao pode ser nulo.");
        }
        if (Texto.vazio(email)) {
            throw new BikeshareException("Email nao pode ser nulo.");
        }
        if (!Texto.emailValido(email)) {
            throw new BikeshareException("Email invalido.");
        }
        if (Texto.vazio(senha)) {
            throw new BikeshareException("Senha nao pode ser nula.");
        }
    }

    private void exigirTipo(String tipo, String esperado) throws BikeshareException {
        if (tipo == null || !tipo.equals(esperado)) {
            throw new BikeshareException("Tipo invalido.");
        }
    }

    private void exigirEmailLivre(String email, String idIgnorado) throws BikeshareException {
        if (buscarPorEmail(email, idIgnorado) != null) {
            throw new BikeshareException("Email ja cadastrado.");
        }
    }

    private Usuario buscarPorEmail(String email, String idIgnorado) {
        String alvo = Texto.limpar(email);
        for (Usuario usuario : usuarios.values()) {
            if (usuario.getId().equals(idIgnorado)) {
                continue;
            }
            if (usuario.getEmail().equalsIgnoreCase(alvo)) {
                return usuario;
            }
        }
        return null;
    }

    private Plano construirPlano(String plano, String mensalidade, boolean planoInformado)
            throws BikeshareException {
        if (!planoInformado) {
            return new PlanoAvulso();
        }
        String nome = Texto.limpar(plano);
        if (PlanoAvulso.NOME.equals(nome)) {
            if (!Texto.vazio(mensalidade)) {
                throw new BikeshareException("Plano nao possui mensalidade.");
            }
            return new PlanoAvulso();
        }
        if (PlanoAssinante.NOME.equals(nome)) {
            return new PlanoAssinante(converterMensalidade(mensalidade));
        }
        throw new BikeshareException("Plano invalido.");
    }

    private double converterMensalidade(String mensalidade) throws BikeshareException {
        if (Texto.vazio(mensalidade)) {
            throw new BikeshareException("Mensalidade nao pode ser nula.");
        }
        Double valor = Texto.numero(mensalidade);
        if (valor == null) {
            throw new BikeshareException("Mensalidade deve ser numerica.");
        }
        if (valor < 0) {
            throw new BikeshareException("Mensalidade deve ser nao-negativa.");
        }
        return valor;
    }

    // =====================================================================
    // US2 - Consulta de usuario
    // =====================================================================

    public String getAtributoUsuario(String usuarioId, String atributo) throws BikeshareException {
        Usuario usuario = exigirUsuario(usuarioId);
        String chave = atributo == null ? "" : atributo;
        switch (chave) {
            case "nome":
                return usuario.getNome();
            case "email":
                return usuario.getEmail();
            case "senha":
                return usuario.getSenha();
            case "tipo":
                return usuario.getTipo();
            case "estado":
                return usuario.getEstado();
            case "plano":
                return usuario.getPlano().getNome();
            case "mensalidade":
                return Texto.moeda(usuario.getPlano().getMensalidade());
            case "cpf":
                return usuario.getCpf();
            case "cnpj":
                return usuario.getCnpj();
            case "razaoSocial":
                return usuario.getRazaoSocial();
            case "metodoPagamento":
                return usuario.getMetodoPagamento().getNome();
            case "numeroCartao":
                return usuario.getMetodoPagamento().getNumeroCartao();
            case "validadeCartao":
                return usuario.getMetodoPagamento().getValidadeCartao();
            case "chavePix":
                return usuario.getMetodoPagamento().getChavePix();
            case "saldo":
                return Texto.moeda(usuario.getMetodoPagamento().getSaldo());
            case "aluguelAtivo":
                return usuario.getAluguelAtivo() == null ? "nenhum" : usuario.getAluguelAtivo();
            case "totalPendencias":
                return Texto.moeda(usuario.getTotalPendencias());
            default:
                throw new BikeshareException("Atributo nao existe.");
        }
    }

    public String getUsuarioPorEmail(String email) throws BikeshareException {
        if (Texto.vazio(email)) {
            throw new BikeshareException("Email nao pode ser nulo.");
        }
        Usuario usuario = buscarPorEmail(email, null);
        if (usuario == null) {
            throw new BikeshareException("Usuario nao existe.");
        }
        return usuario.getId();
    }

    private Usuario exigirUsuario(String usuarioId) throws BikeshareException {
        if (Texto.vazio(usuarioId)) {
            throw new BikeshareException("Identificacao do usuario nao pode ser nula.");
        }
        Usuario usuario = usuarios.get(Texto.limpar(usuarioId));
        if (usuario == null) {
            throw new BikeshareException("Usuario nao existe.");
        }
        return usuario;
    }

    // =====================================================================
    // US3 - Alteracao de usuario
    // =====================================================================

    public void alterarUsuario(String usuarioId, String atributo, String valor) throws BikeshareException {
        Usuario usuario = exigirUsuario(usuarioId);
        String chave = atributo == null ? "" : atributo;
        switch (chave) {
            case "nome":
                if (Texto.vazio(valor)) {
                    throw new BikeshareException("Nome nao pode ser nulo.");
                }
                usuario.setNome(Texto.limpar(valor));
                return;
            case "email":
                if (Texto.vazio(valor)) {
                    throw new BikeshareException("Email nao pode ser nulo.");
                }
                if (!Texto.emailValido(valor)) {
                    throw new BikeshareException("Email invalido.");
                }
                exigirEmailLivre(valor, usuario.getId());
                usuario.setEmail(Texto.limpar(valor));
                return;
            case "senha":
                if (Texto.vazio(valor)) {
                    throw new BikeshareException("Senha nao pode ser nula.");
                }
                usuario.setSenha(Texto.limpar(valor));
                return;
            case "plano":
                alterarPlano(usuario, valor);
                return;
            case "saldo":
                recarregarSaldo(usuario, valor);
                return;
            case "metodoPagamento":
                definirMetodoPagamento(usuario, valor, null, null, 1);
                return;
            default:
                throw new BikeshareException("Atributo nao pode ser alterado.");
        }
    }

    public void alterarUsuario(String usuarioId, String atributo, String valor, String chave)
            throws BikeshareException {
        Usuario usuario = exigirUsuario(usuarioId);
        if (!"metodoPagamento".equals(atributo)) {
            throw new BikeshareException("Atributo nao pode ser alterado.");
        }
        definirMetodoPagamento(usuario, valor, chave, null, 2);
    }

    public void alterarUsuario(String usuarioId, String atributo, String valor, String numero, String validade)
            throws BikeshareException {
        Usuario usuario = exigirUsuario(usuarioId);
        if (!"metodoPagamento".equals(atributo)) {
            throw new BikeshareException("Atributo nao pode ser alterado.");
        }
        definirMetodoPagamento(usuario, valor, numero, validade, 3);
    }

    private void alterarPlano(Usuario usuario, String valor) throws BikeshareException {
        String nome = Texto.limpar(valor);
        if (PlanoAvulso.NOME.equals(nome)) {
            if (!usuario.getPlano().possuiMensalidade()) {
                return;
            }
            usuario.setPlano(new PlanoAvulso());
            return;
        }
        if (PlanoAssinante.NOME.equals(nome)) {
            if (usuario.getPlano().possuiMensalidade()) {
                return;
            }
            usuario.setPlano(new PlanoAssinante(configuracao.getMensalidadePadrao()));
            return;
        }
        throw new BikeshareException("Plano invalido.");
    }

    private void recarregarSaldo(Usuario usuario, String valor) throws BikeshareException {
        MetodoPagamento metodo = usuario.getMetodoPagamento();
        if (!(metodo instanceof SaldoPreCarregado)) {
            throw new BikeshareException("Usuario nao usa saldo pre-carregado.");
        }
        if (Texto.vazio(valor)) {
            throw new BikeshareException("Valor nao pode ser nulo.");
        }
        Double numero = Texto.numero(valor);
        if (numero == null) {
            throw new BikeshareException("Valor deve ser numerico.");
        }
        if (numero <= 0) {
            throw new BikeshareException("Valor deve ser positivo.");
        }
        metodo.recarregar(numero);
    }

    /**
     * Define o metodo de pagamento do usuario. A quantidade de parametros
     * informada no script precisa combinar com o metodo escolhido.
     */
    private void definirMetodoPagamento(Usuario usuario, String valor, String segundo, String terceiro,
                                        int parametros) throws BikeshareException {
        String escolha = Texto.limpar(valor);
        if (Pix.NOME.equals(escolha) && parametros == 2) {
            if (Texto.vazio(segundo)) {
                throw new BikeshareException("Chave pix nao pode ser nula.");
            }
            usuario.setMetodoPagamento(new Pix(Texto.limpar(segundo)));
            return;
        }
        if (Cartao.NOME.equals(escolha) && parametros == 3) {
            if (Texto.vazio(segundo)) {
                throw new BikeshareException("Numero do cartao nao pode ser nulo.");
            }
            if (Texto.vazio(terceiro)) {
                throw new BikeshareException("Validade do cartao nao pode ser nula.");
            }
            if (!Texto.limpar(terceiro).matches("(0[1-9]|1[0-2])/\\d{4}")) {
                throw new BikeshareException("Validade do cartao invalida.");
            }
            usuario.setMetodoPagamento(new Cartao(Texto.limpar(segundo), Texto.limpar(terceiro)));
            return;
        }
        if (SaldoPreCarregado.NOME.equals(escolha) && parametros == 1) {
            usuario.setMetodoPagamento(usuario.getCarteira());
            return;
        }
        throw new BikeshareException("Valor de metodo de pagamento invalido.");
    }

    // =====================================================================
    // US4 - Remocao de usuario
    // =====================================================================

    public void removerUsuario(String usuarioId) throws BikeshareException {
        Usuario usuario = exigirUsuario(usuarioId);
        if (usuario.getAluguelAtivo() != null) {
            throw new BikeshareException("Nao e possivel remover usuario com aluguel em andamento.");
        }
        usuarios.remove(usuario.getId());
    }

    // =====================================================================
    // US5 - Cadastro de estacao
    // =====================================================================

    public String criarEstacao(String nome, String endereco, String capacidade) throws BikeshareException {
        if (Texto.vazio(nome)) {
            throw new BikeshareException("Nome nao pode ser nulo.");
        }
        if (Texto.vazio(endereco)) {
            throw new BikeshareException("Endereco nao pode ser nulo.");
        }
        if (Texto.vazio(capacidade)) {
            throw new BikeshareException("Capacidade nao pode ser nula.");
        }
        Integer vagas = Texto.inteiro(capacidade);
        if (vagas == null) {
            throw new BikeshareException("Capacidade deve ser numerica.");
        }
        if (vagas <= 0) {
            throw new BikeshareException("Capacidade deve ser positiva.");
        }
        String id = "E" + proximaEstacao++;
        estacoes.put(id, new Estacao(id, Texto.limpar(nome), Texto.limpar(endereco), vagas));
        return id;
    }

    private Estacao exigirEstacao(String estacaoId) throws BikeshareException {
        if (Texto.vazio(estacaoId)) {
            throw new BikeshareException("Identificacao da estacao nao pode ser nula.");
        }
        Estacao estacao = estacoes.get(Texto.limpar(estacaoId));
        if (estacao == null) {
            throw new BikeshareException("Estacao nao existe.");
        }
        return estacao;
    }

    // =====================================================================
    // US6 - Gestao de bicicletas na estacao
    // =====================================================================

    public void adicionarBicicleta(String estacaoId, String bicicletaId, String tipo, String nivelBateria,
                                   boolean bateriaInformada) throws BikeshareException {
        Estacao estacao = exigirEstacao(estacaoId);
        if (Texto.vazio(bicicletaId)) {
            throw new BikeshareException("Identificacao da bicicleta nao pode ser nula.");
        }
        String id = Texto.limpar(bicicletaId);
        if (bicicletas.containsKey(id)) {
            throw new BikeshareException("Bicicleta ja existe.");
        }

        Bicicleta bicicleta;
        if (BicicletaConvencional.TIPO.equals(tipo)) {
            if (bateriaInformada) {
                throw new BikeshareException("Bicicleta nao possui bateria.");
            }
            bicicleta = new BicicletaConvencional(id, estacao.getId());
        } else if (BicicletaEletrica.TIPO.equals(tipo)) {
            bicicleta = new BicicletaEletrica(id, estacao.getId(), converterBateria(nivelBateria));
        } else {
            throw new BikeshareException("Tipo invalido.");
        }

        if (estacao.estaCheia()) {
            throw new BikeshareException("Estacao sem vagas disponiveis.");
        }
        bicicletas.put(id, bicicleta);
        estacao.receber(id);
    }

    private int converterBateria(String nivelBateria) throws BikeshareException {
        if (Texto.vazio(nivelBateria)) {
            throw new BikeshareException("Nivel de bateria nao pode ser nulo.");
        }
        Integer nivel = Texto.inteiro(nivelBateria);
        if (nivel == null) {
            throw new BikeshareException("Nivel de bateria deve ser numerico.");
        }
        if (nivel < 0 || nivel > 100) {
            throw new BikeshareException("Nivel de bateria deve estar entre 0 e 100.");
        }
        return nivel;
    }

    public void removerBicicleta(String estacaoId, String bicicletaId) throws BikeshareException {
        Estacao estacao = exigirEstacao(estacaoId);
        Bicicleta bicicleta = exigirBicicleta(bicicletaId);
        if (!estacao.contem(bicicleta.getId())) {
            throw new BikeshareException("Bicicleta nao esta nesta estacao.");
        }
        estacao.retirar(bicicleta.getId());
        bicicletas.remove(bicicleta.getId());
    }

    public String getAtributoBicicleta(String bicicletaId, String atributo) throws BikeshareException {
        Bicicleta bicicleta = exigirBicicleta(bicicletaId);
        String chave = atributo == null ? "" : atributo;
        switch (chave) {
            case "tipo":
                return bicicleta.getTipo();
            case "estado":
                return bicicleta.getEstado();
            case "estacao":
                return bicicleta.getEstacao() == null ? "nenhuma" : bicicleta.getEstacao();
            case "nivelBateria":
                return String.valueOf(bicicleta.getNivelBateria());
            default:
                throw new BikeshareException("Atributo nao existe.");
        }
    }

    private Bicicleta exigirBicicleta(String bicicletaId) throws BikeshareException {
        if (Texto.vazio(bicicletaId)) {
            throw new BikeshareException("Identificacao da bicicleta nao pode ser nula.");
        }
        Bicicleta bicicleta = bicicletas.get(Texto.limpar(bicicletaId));
        if (bicicleta == null) {
            throw new BikeshareException("Bicicleta nao existe.");
        }
        return bicicleta;
    }

    // =====================================================================
    // US7 - Consulta de disponibilidade
    // =====================================================================

    public String getBicicletasDisponiveis(String estacaoId) throws BikeshareException {
        Estacao estacao = exigirEstacao(estacaoId);
        int total = 0;
        for (String id : estacao.getBicicletas()) {
            Bicicleta bicicleta = bicicletas.get(id);
            if (bicicleta != null && bicicleta.disponivelParaRetirada(configuracao)) {
                total++;
            }
        }
        return String.valueOf(total);
    }

    public String getVagasDisponiveis(String estacaoId) throws BikeshareException {
        return String.valueOf(exigirEstacao(estacaoId).getVagasDisponiveis());
    }

    public String getEstadoBicicleta(String bicicletaId) throws BikeshareException {
        return exigirBicicleta(bicicletaId).getEstado();
    }

    // =====================================================================
    // US8 - Retirada de bicicleta
    // =====================================================================

    public String retirarBicicleta(String usuarioId, String estacaoId, String bicicletaId, String data)
            throws BikeshareException {
        Usuario usuario = exigirUsuario(usuarioId);
        Estacao estacao = exigirEstacao(estacaoId);
        Bicicleta bicicleta = exigirBicicleta(bicicletaId);
        LocalDateTime inicio = Datas.converter(data);

        if (usuario.isInadimplente()) {
            throw new BikeshareException("Usuario inadimplente.");
        }
        if (usuario.getAluguelAtivo() != null) {
            throw new BikeshareException("Usuario ja possui um aluguel em andamento.");
        }
        if (!estacao.contem(bicicleta.getId())) {
            throw new BikeshareException("Bicicleta nao esta nesta estacao.");
        }
        if (!Bicicleta.ESTADO_DISPONIVEL.equals(bicicleta.getEstado())) {
            throw new BikeshareException("Bicicleta nao esta disponivel.");
        }
        bicicleta.validarRetirada(configuracao);
        usuario.getMetodoPagamento().autorizarRetirada();

        String id = "A" + proximoAluguel++;
        alugueis.put(id, new Aluguel(id, usuario.getId(), bicicleta.getId(), estacao.getId(), inicio));
        estacao.retirar(bicicleta.getId());
        bicicleta.setEstacao(null);
        bicicleta.setEstado(Bicicleta.ESTADO_EM_USO);
        usuario.setAluguelAtivo(id);
        return id;
    }

    // =====================================================================
    // US9 - Devolucao de bicicleta
    // =====================================================================

    public void devolverBicicleta(String aluguelId, String estacaoId, String data) throws BikeshareException {
        Aluguel aluguel = exigirAluguel(aluguelId);
        Estacao destino = exigirEstacao(estacaoId);
        LocalDateTime fim = Datas.converter(data);

        if (!aluguel.estaEmAndamento()) {
            throw new BikeshareException("Aluguel nao esta em andamento.");
        }
        if (fim.isBefore(aluguel.getInicio())) {
            throw new BikeshareException("Data de devolucao anterior a data de retirada.");
        }
        if (destino.estaCheia()) {
            throw new BikeshareException("Estacao cheia.");
        }

        Usuario usuario = usuarios.get(aluguel.getUsuario());
        Bicicleta bicicleta = bicicletas.get(aluguel.getBicicleta());
        long minutos = aluguel.getDuracaoEmMinutos(fim);

        if (minutos > configuracao.getTempoMaximoAluguelMinutos()) {
            expirar(aluguel, usuario, bicicleta, destino, fim);
            return;
        }
        if (destino.getId().equals(aluguel.getEstacaoOrigem()) && minutos <= configuracao.getPrazoCarenciaMinutos()) {
            cancelar(aluguel, usuario, bicicleta, destino, fim);
            return;
        }
        finalizar(aluguel, usuario, bicicleta, destino, fim, minutos);
    }

    private void expirar(Aluguel aluguel, Usuario usuario, Bicicleta bicicleta, Estacao destino,
                         LocalDateTime fim) {
        aluguel.encerrar(destino.getId(), fim, Aluguel.ESTADO_EXPIRADO, 0.0);
        devolverParaEstacao(bicicleta, destino, Bicicleta.ESTADO_EM_MANUTENCAO);
        if (usuario != null) {
            usuario.lancarPendencia(configuracao.getPenalidadeAtraso());
            usuario.setAluguelAtivo(null);
        }
    }

    private void cancelar(Aluguel aluguel, Usuario usuario, Bicicleta bicicleta, Estacao destino,
                          LocalDateTime fim) {
        aluguel.encerrar(destino.getId(), fim, Aluguel.ESTADO_CANCELADO, 0.0);
        devolverParaEstacao(bicicleta, destino, Bicicleta.ESTADO_DISPONIVEL);
        if (usuario != null) {
            usuario.setAluguelAtivo(null);
        }
    }

    private void finalizar(Aluguel aluguel, Usuario usuario, Bicicleta bicicleta, Estacao destino,
                           LocalDateTime fim, long minutos) throws BikeshareException {
        double valor = calcularValor(usuario, bicicleta, minutos);
        if (usuario != null) {
            usuario.getMetodoPagamento().autorizarCobranca(valor);
            usuario.getMetodoPagamento().cobrar(valor);
            usuario.setAluguelAtivo(null);
        }
        aluguel.encerrar(destino.getId(), fim, Aluguel.ESTADO_FINALIZADO, valor);
        devolverParaEstacao(bicicleta, destino, Bicicleta.ESTADO_DISPONIVEL);
    }

    private double calcularValor(Usuario usuario, Bicicleta bicicleta, long minutos) {
        double valor = 0.0;
        if (usuario != null) {
            valor += usuario.getPlano().calcularValor(minutos, configuracao);
        }
        if (bicicleta != null) {
            valor += bicicleta.calcularAdicional(minutos, configuracao);
        }
        return Texto.arredondar(valor);
    }

    private void devolverParaEstacao(Bicicleta bicicleta, Estacao destino, String estado) {
        if (bicicleta == null) {
            return;
        }
        destino.receber(bicicleta.getId());
        bicicleta.setEstacao(destino.getId());
        bicicleta.setEstado(estado);
    }

    // =====================================================================
    // Consulta de aluguel (usada a partir da US8)
    // =====================================================================

    public String getAtributoAluguel(String aluguelId, String atributo) throws BikeshareException {
        Aluguel aluguel = exigirAluguel(aluguelId);
        String chave = atributo == null ? "" : atributo;
        switch (chave) {
            case "estado":
                return aluguel.getEstado();
            case "usuario":
                return aluguel.getUsuario();
            case "bicicleta":
                return aluguel.getBicicleta();
            case "estacaoOrigem":
                return aluguel.getEstacaoOrigem();
            case "inicio":
                return Datas.formatar(aluguel.getInicio());
            case "fim":
                exigirEncerrado(aluguel);
                return Datas.formatar(aluguel.getFim());
            case "estacaoDestino":
                exigirEncerrado(aluguel);
                return aluguel.getEstacaoDestino();
            case "valorCobrado":
                exigirEncerrado(aluguel);
                return Texto.moeda(aluguel.getValorCobrado());
            default:
                throw new BikeshareException("Atributo nao existe.");
        }
    }

    private void exigirEncerrado(Aluguel aluguel) throws BikeshareException {
        if (aluguel.estaEmAndamento()) {
            throw new BikeshareException("Aluguel ainda esta em andamento.");
        }
    }

    private Aluguel exigirAluguel(String aluguelId) throws BikeshareException {
        if (Texto.vazio(aluguelId)) {
            throw new BikeshareException("Identificacao do aluguel nao pode ser nula.");
        }
        Aluguel aluguel = alugueis.get(Texto.limpar(aluguelId));
        if (aluguel == null) {
            throw new BikeshareException("Aluguel nao existe.");
        }
        return aluguel;
    }

    // =====================================================================
    // Ciclo de vida
    // =====================================================================

    public void zerar() {
        usuarios.clear();
        estacoes.clear();
        bicicletas.clear();
        alugueis.clear();
        proximoUsuario = 1;
        proximaEstacao = 1;
        proximoAluguel = 1;
    }
}
