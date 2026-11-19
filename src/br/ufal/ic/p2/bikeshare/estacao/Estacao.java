package br.ufal.ic.p2.bikeshare.estacao;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Ponto de retirada e devolucao. Guarda apenas os identificadores das
 * bicicletas presentes; os objetos ficam no repositorio do sistema.
 */
public class Estacao implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String id;
    private final String nome;
    private final String endereco;
    private final int capacidade;
    private final Set<String> bicicletas = new LinkedHashSet<>();

    public Estacao(String id, String nome, String endereco, int capacidade) {
        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
        this.capacidade = capacidade;
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public Collection<String> getBicicletas() {
        return bicicletas;
    }

    public int getVagasDisponiveis() {
        return capacidade - bicicletas.size();
    }

    public boolean estaCheia() {
        return getVagasDisponiveis() <= 0;
    }

    public boolean contem(String bicicleta) {
        return bicicletas.contains(bicicleta);
    }

    public void receber(String bicicleta) {
        bicicletas.add(bicicleta);
    }

    public void retirar(String bicicleta) {
        bicicletas.remove(bicicleta);
    }
}
