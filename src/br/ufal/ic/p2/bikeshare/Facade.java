package br.ufal.ic.p2.bikeshare;

import br.ufal.ic.p2.bikeshare.exception.BikeshareException;
import br.ufal.ic.p2.bikeshare.util.Persistencia;

/**
 Facade
 */
public class Facade {

    private static final String AINDA_NAO_IMPLEMENTADO = "Ainda não implementado.";

    private Sistema sistema = Persistencia.carregar();

    // =====================================================================
    // US1 - Cadastro de usuario
    // =====================================================================

    public String criarUsuario(String nome, String email, String senha, String tipo, String cpfOuCnpj)
            throws BikeshareException {
        return sistema.criarPessoaFisica(nome, email, senha, tipo, cpfOuCnpj, null, null, false);
    }

    public String criarUsuario(String nome, String email, String senha, String tipo, String cnpj,
                               String razaoSocial) throws BikeshareException {
        return sistema.criarPessoaJuridica(nome, email, senha, tipo, cnpj, razaoSocial, null, null, false);
    }

    public String criarUsuario(String nome, String email, String senha, String tipo, String cpf,
                                String plano, String mensalidade) throws BikeshareException {
        return sistema.criarPessoaFisica(nome, email, senha, tipo, cpf, plano, mensalidade, true);
    }

    public String criarUsuario(String nome, String email, String senha, String tipo, String cnpj,
                                String razaoSocial, String plano, String mensalidade)
            throws BikeshareException {
        return sistema.criarPessoaJuridica(nome, email, senha, tipo, cnpj, razaoSocial, plano, mensalidade, true);
    }

    // =====================================================================
    // US2 - Consulta de usuario
    // =====================================================================

    public String getAtributoUsuario(String usuario, String atributo) throws BikeshareException {
        return sistema.getAtributoUsuario(usuario, atributo);
    }

    public String getUsuarioPorEmail(String email) throws BikeshareException {
        return sistema.getUsuarioPorEmail(email);
    }

    // =====================================================================
    // US3 - Alteracao de usuario (inclui metodo de pagamento)
    // =====================================================================

    public void alterarUsuario(String usuario, String atributo, String valor) throws BikeshareException {
        sistema.alterarUsuario(usuario, atributo, valor);
    }

    public void alterarUsuario(String usuario, String atributo, String valor1, String chave)
            throws BikeshareException {
        sistema.alterarUsuario(usuario, atributo, valor1, chave);
    }

    public void alterarUsuario(String usuario, String atributo, String valor1, String numero, String validade)
            throws BikeshareException {
        sistema.alterarUsuario(usuario, atributo, valor1, numero, validade);
    }

    // =====================================================================
    // US4 - Remocao de usuario
    // =====================================================================

    public void removerUsuario(String usuario) throws BikeshareException {
        sistema.removerUsuario(usuario);
    }

    // =====================================================================
    // US5 - Cadastro de estacao
    // =====================================================================

    public String criarEstacao(String nome, String endereco, String capacidade) throws BikeshareException {
        return sistema.criarEstacao(nome, endereco, capacidade);
    }

    // =====================================================================
    // US6 - Gestao de bicicletas na estacao
    // =====================================================================

    public void adicionarBicicleta(String estacao, String bicicleta, String tipo) throws BikeshareException {
        sistema.adicionarBicicleta(estacao, bicicleta, tipo, null, false);
    }

    public void adicionarBicicleta(String estacao, String bicicleta, String tipo, String nivelBateria)
            throws BikeshareException {
        sistema.adicionarBicicleta(estacao, bicicleta, tipo, nivelBateria, true);
    }

    public void removerBicicleta(String estacao, String bicicleta) throws BikeshareException {
        sistema.removerBicicleta(estacao, bicicleta);
    }

    public String getAtributoBicicleta(String bicicleta, String atributo) throws BikeshareException {
        return sistema.getAtributoBicicleta(bicicleta, atributo);
    }

    // =====================================================================
    // US7 - Consulta de disponibilidade
    // =====================================================================

    public String getBicicletasDisponiveis(String estacao) throws BikeshareException {
        return sistema.getBicicletasDisponiveis(estacao);
    }

    public String getVagasDisponiveis(String estacao) throws BikeshareException {
        return sistema.getVagasDisponiveis(estacao);
    }

    public String getEstadoBicicleta(String bicicleta) throws BikeshareException {
        return sistema.getEstadoBicicleta(bicicleta);
    }

    // =====================================================================
    // US8 - Retirada de bicicleta
    // =====================================================================

    public String retirarBicicleta(String usuario, String estacao, String bicicleta, String data)
            throws BikeshareException {
        return sistema.retirarBicicleta(usuario, estacao, bicicleta, data);
    }

    // =====================================================================
    // US9 - Devolucao de bicicleta
    // =====================================================================

    public void devolverBicicleta(String aluguel, String estacao, String data) throws BikeshareException {
        sistema.devolverBicicleta(aluguel, estacao, data);
    }

    // =====================================================================
    // US10 - Consulta de historico de alugueis
    // =====================================================================

    public String getHistoricoAlugueis(String usuario) {
        throw new UnsupportedOperationException(AINDA_NAO_IMPLEMENTADO);
    }

    public String getHistoricoAlugueis(String usuario, String atributo) {
        throw new UnsupportedOperationException(AINDA_NAO_IMPLEMENTADO);
    }

    public String getAtributoAluguel(String aluguel, String atributo) throws BikeshareException {
        return sistema.getAtributoAluguel(aluguel, atributo);
    }

    // =====================================================================
    // US11/US12 - Planos e tarifacao / criacao de novos planos
    // =====================================================================

    public String criarPlano(String nome, String franquiaMinutos, String valorMinutoExcedente, String mensalidade) {
        throw new UnsupportedOperationException(AINDA_NAO_IMPLEMENTADO);
    }

    // =====================================================================
    // US13 - Regras de bloqueio / lancamento manual de penalidade
    // =====================================================================

    public void lancaPenalidade(String usuario, String tipo, String valor, String motivo) {
        throw new UnsupportedOperationException(AINDA_NAO_IMPLEMENTADO);
    }

    // =====================================================================
    // US14 - Lancamento e conclusao de manutencao
    // =====================================================================

    public void lancaDefeito(String bicicleta, String descricao) {
        throw new UnsupportedOperationException(AINDA_NAO_IMPLEMENTADO);
    }

    public void concluiManutencao(String bicicleta) {
        throw new UnsupportedOperationException(AINDA_NAO_IMPLEMENTADO);
    }

    // =====================================================================
    // US15 - Relatorios
    // =====================================================================

    public String getTotalFaturado(String dataInicial, String dataFinal) {
        throw new UnsupportedOperationException(AINDA_NAO_IMPLEMENTADO);
    }

    // =====================================================================
    // US16 - Undo/redo
    // =====================================================================

    public void undo() {
        throw new UnsupportedOperationException(AINDA_NAO_IMPLEMENTADO);
    }

    public void redo() {
        throw new UnsupportedOperationException(AINDA_NAO_IMPLEMENTADO);
    }

    // =====================================================================
    // Ciclo de vida do sistema
    // =====================================================================

    public void zerarSistema() {
        sistema.zerar();
        Persistencia.apagar();
    }

    public void encerrarSistema() {
        Persistencia.salvar(sistema);
    }
}
