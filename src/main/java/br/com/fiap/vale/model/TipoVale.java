package br.com.fiap.vale.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

@XmlEnum
@XmlType(name = "TipoVale")
public enum TipoVale {

    ADIANTAMENTO_MENSAL("Adiantamento Mensal", 30.0, 40.0),

    EMERGENCIAL("Vale Emergencial", 10.0, 20.0),

    FERIAS("Vale Férias", 30.0, 50.0),

    DECIMO_TERCEIRO("Adiantamento 13º Salário", 50.0, 50.0);

    private final String descricao;
    private final double percentualMinimo;
    private final double percentualMaximo;

    TipoVale(String descricao, double percentualMinimo, double percentualMaximo) {
        this.descricao = descricao;
        this.percentualMinimo = percentualMinimo;
        this.percentualMaximo = percentualMaximo;
    }

    public String getDescricao() { return descricao; }
    public double getPercentualMinimo() { return percentualMinimo; }
    public double getPercentualMaximo() { return percentualMaximo; }

    public boolean percentualValido(double percentual) {
        return percentual >= percentualMinimo && percentual <= percentualMaximo;
    }

    public String getResumo() {
        if (percentualMinimo == percentualMaximo) {
            return String.format("%s — fixo em %.0f%% do salário bruto", descricao, percentualMinimo);
        }
        return String.format("%s — entre %.0f%% e %.0f%% do salário bruto",
                descricao, percentualMinimo, percentualMaximo);
    }
}