package br.com.hbsis.util;

public enum UnidadeMedida {
    Kg,
    g,
    mg;

    private String unidadeMedida;

    public String getUnidadeMedida() {
        return unidadeMedida;
    }

    public void setUnidadeMedida(String unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }
}
