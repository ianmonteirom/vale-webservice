package br.com.fiap.vale.util;

/**
 * Centraliza constantes utilizadas na aplicação.
 * Evita strings e números mágicos espalhados pelo código.
 */
public final class Constantes {

    private Constantes() {
        // Utilitária — não deve ser instanciada
    }

    // ---- Formato de data ----
    public static final String FORMATO_MES_ANO = "yyyy-MM"; // ex: "2026-03"

    // ---- Mensagens de sucesso ----
    public static final String MSG_VALE_CANCELADO =
            "Vale ID %d do funcionário '%s' cancelado com sucesso. " +
                    "Valor de R$ %.2f não será descontado no próximo pagamento.";

    // ---- Mensagens de log ----
    public static final String LOG_SOLICITANDO_VALE =
            "Solicitando vale: funcionarioId=%d, percentual=%.1f%%, tipo=%s";

    public static final String LOG_VALE_APROVADO =
            "Vale aprovado: id=%d, funcionario=%s, valor=R$ %.2f, tipo=%s";

    public static final String LOG_CANCELANDO_VALE =
            "Cancelando vale: id=%d";

    public static final String LOG_VALE_CANCELADO =
            "Vale cancelado: id=%d, funcionario=%s";
}