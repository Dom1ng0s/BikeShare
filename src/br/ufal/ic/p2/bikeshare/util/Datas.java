package br.ufal.ic.p2.bikeshare.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

import br.ufal.ic.p2.bikeshare.exception.BikeshareException;

/**
 * Conversao das datas no formato usado pelos scripts de aceitacao (d/M/aaaa H:mm).
 */
public final class Datas {

    private static final DateTimeFormatter FORMATO = DateTimeFormatter
            .ofPattern("d/M/uuuu H:mm")
            .withResolverStyle(ResolverStyle.STRICT);

    private Datas() {
    }

    public static LocalDateTime converter(String data) throws BikeshareException {
        if (Texto.vazio(data)) {
            throw new BikeshareException("Data nao pode ser nula.");
        }
        try {
            return LocalDateTime.parse(Texto.limpar(data), FORMATO);
        } catch (DateTimeParseException e) {
            throw new BikeshareException("Data invalida.");
        }
    }

    public static String formatar(LocalDateTime data) {
        return data == null ? null : data.format(FORMATO);
    }
}
