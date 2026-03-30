package br.com.fiap.vale.util;

public final class ValeCalculator {

    private ValeCalculator() {
    }

    public static double calcularValorAdiantado(double salarioBruto, double percentualSolicitado) {
        double valor = salarioBruto * (percentualSolicitado / 100.0);
        return Math.round(valor * 100.0) / 100.0;
    }

    public static String getMesAnoAtual() {
        return java.time.LocalDate.now().toString().substring(0, 7);
    }

    public static String getDataAtual() {
        return java.time.LocalDate.now().toString();
    }
}