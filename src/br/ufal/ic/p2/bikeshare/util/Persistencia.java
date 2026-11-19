package br.ufal.ic.p2.bikeshare.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import br.ufal.ic.p2.bikeshare.Sistema;

/**
 * Persistencia do estado do sistema entre execucoes, usando a serializacao
 * nativa do Java (java.io.Serializable), como exige o requisito BS03.
 */
public final class Persistencia {

    private static final String ARQUIVO = "bikeshare_data.ser";

    private Persistencia() {
    }

    public static Sistema carregar() {
        File arquivo = new File(ARQUIVO);
        if (!arquivo.exists()) {
            return new Sistema();
        }
        try (ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(arquivo))) {
            return (Sistema) entrada.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new Sistema();
        }
    }

    public static void salvar(Sistema sistema) {
        try (ObjectOutputStream saida = new ObjectOutputStream(new FileOutputStream(ARQUIVO))) {
            saida.writeObject(sistema);
        } catch (IOException e) {
            throw new IllegalStateException("Nao foi possivel salvar o estado do sistema.", e);
        }
    }

    public static void apagar() {
        File arquivo = new File(ARQUIVO);
        if (arquivo.exists()) {
            arquivo.delete();
        }
    }
}
