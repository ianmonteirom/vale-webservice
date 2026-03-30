package br.com.fiap.vale.util;

/**
 * Utilitário responsável pelos cálculos financeiros do vale.
 * Isola a lógica de cálculo do ValeService, facilitando testes e reutilização.
 */
public final class ValeCalculator {

    private ValeCalculator() {
        // Utilitária — não deve ser instanciada
    }

    /**
     * Calcula o valor adiantado com base no salário bruto e percentual solicitado.
     * O resultado é arredondado em 2 casas decimais.
     *
     * @param salarioBruto         Salário bruto do funcionário
     * @param percentualSolicitado Percentual solicitado (ex: 35.0 para 35%)
     * @return Valor adiantado arredondado em 2 casas decimais
     */
    public static double calcularValorAdiantado(double salarioBruto, double percentualSolicitado) {
        double valor = salarioBruto * (percentualSolicitado / 100.0);
        return Math.round(valor * 100.0) / 100.0;
    }

    /**
     * Retorna o mês/ano atual no formato yyyy-MM (ex: "2026-03").
     * Centraliza o uso de LocalDate para facilitar testes futuros.
     */
    public static String getMesAnoAtual() {
        return java.time.LocalDate.now().toString().substring(0, 7);
    }

    /**
     * Retorna a data atual no formato yyyy-MM-dd (ex: "2026-03-30").
     */
    public static String getDataAtual() {
        return java.time.LocalDate.now().toString();
    }
}