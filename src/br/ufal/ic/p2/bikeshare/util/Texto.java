package br.ufal.ic.p2.bikeshare.util;

import java.util.Locale;

/**
 * Utilitarios de validacao e formatacao de texto e de valores monetarios.
 */
public final class Texto {

    private Texto() {
    }

    public static boolean vazio(String valor) {
        return valor == null || valor.trim().isEmpty();
    }

    public static String limpar(String valor) {
        return valor == null ? null : valor.trim();
    }

    public static boolean emailValido(String email) {
        String limpo = limpar(email);
        return limpo != null && limpo.matches("[^@\\s]+@[^@\\s]+");
    }

    /**
     * Converte texto para numero aceitando virgula ou ponto como separador decimal.
     * Retorna null quando o texto nao e numerico.
     */
    public static Double numero(String valor) {
        String limpo = limpar(valor);
        if (limpo == null) {
            return null;
        }
        try {
            return Double.valueOf(limpo.replace(",", "."));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Converte texto para inteiro. Apenas digitos (com sinal) sao aceitos.
     */
    public static Integer inteiro(String valor) {
        String limpo = limpar(valor);
        if (limpo == null || !limpo.matches("[+-]?\\d+")) {
            return null;
        }
        try {
            return Integer.valueOf(limpo);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static double arredondar(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }

    public static String moeda(double valor) {
        return String.format(Locale.US, "%.2f", arredondar(valor)).replace('.', ',');
    }
}
