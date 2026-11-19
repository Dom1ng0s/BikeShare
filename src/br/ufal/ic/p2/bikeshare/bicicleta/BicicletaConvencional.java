package br.ufal.ic.p2.bikeshare.bicicleta;

public class BicicletaConvencional extends Bicicleta {

    private static final long serialVersionUID = 1L;

    public static final String TIPO = "convencional";

    public BicicletaConvencional(String id, String estacao) {
        super(id, estacao);
    }

    @Override
    public String getTipo() {
        return TIPO;
    }
}
