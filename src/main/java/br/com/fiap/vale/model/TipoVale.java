package br.com.fiap.vale.model;

/**
 * Tipos de vale (adiantamento) disponíveis na empresa.
 *
 * Cada tipo possui:
 * - descricao: texto amigável para exibição
 * - percentualMinimo: menor percentual do salário bruto permitido
 * - percentualMaximo: maior percentual do salário bruto permitido
 */
public enum TipoVale {

    /**
     * Adiantamento padrão — pago entre o 15º e o 20º dia do mês.
     * Corresponde a 30% a 40% do salário bruto.
     */
    ADIANTAMENTO_MENSAL(
            "Adiantamento Mensal",
            30.0,
            40.0
    ),

    /**
     * Adiantamento emergencial — liberado em situações excepcionais
     * como emergências médicas ou familiares. Percentual reduzido.
     */
    EMERGENCIAL(
            "Vale Emergencial",
            10.0,
            20.0
    ),

    /**
     * Vale para férias — antecipação de parte do salário antes do
     * período de férias do colaborador.
     */
    FERIAS(
            "Vale Férias",
            30.0,
            50.0
    ),

    /**
     * Vale para décimo terceiro — antecipação da primeira parcela
     * do 13º salário, conforme legislação trabalhista.
     * Fixado em 50% do salário bruto.
     */
    DECIMO_TERCEIRO(
            "Adiantamento 13º Salário",
            50.0,
            50.0
    );

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

    /**
     * Valida se o percentual informado está dentro do intervalo permitido para este tipo.
     */
    public boolean percentualValido(double percentual) {
        return percentual >= percentualMinimo && percentual <= percentualMaximo;
    }

    /**
     * Retorna uma descrição completa do tipo com o intervalo permitido.
     */
    public String getResumo() {
        if (percentualMinimo == percentualMaximo) {
            return String.format("%s — fixo em %.0f%% do salário bruto", descricao, percentualMinimo);
        }
        return String.format("%s — entre %.0f%% e %.0f%% do salário bruto",
                descricao, percentualMinimo, percentualMaximo);
    }
}