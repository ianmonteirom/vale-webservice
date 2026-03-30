package br.com.fiap.vale.util;

/**
 * Centraliza constantes utilizadas na aplicação.
 * Evita strings e números mágicos espalhados pelo código.
 */
public final class Constantes {

    private Constantes() {
        // Utilitária — não deve ser instanciada
    }

    // ---- Mensagens de sucesso ----
    public static final String MSG_VALE_CANCELADO =
            "Vale ID %d do funcionário '%s' cancelado com sucesso. " +
                    "Valor de R$ %.2f não será descontado no próximo pagamento.";

    // ---- Logs — ValeService ----
    public static final String LOG_LISTANDO_FUNCIONARIOS =
            "Listando todos os funcionários.";

    public static final String LOG_LISTANDO_VALES =
            "Listando todos os vales.";

    public static final String LOG_LISTANDO_VALES_FUNCIONARIO =
            "Listando vales do funcionário ID: %d";

    public static final String LOG_SOLICITANDO_VALE =
            "Solicitando vale: funcionarioId=%d, percentual=%.1f%%, tipo=%s";

    public static final String LOG_VALE_APROVADO =
            "Vale aprovado: id=%d, funcionario=%s, valor=R$ %.2f, tipo=%s";

    public static final String LOG_CANCELANDO_VALE =
            "Cancelando vale: id=%d";

    public static final String LOG_VALE_CANCELADO =
            "Vale cancelado: id=%d, funcionario=%s";

    // ---- Logs — FuncionarioValidator ----
    public static final String LOG_VALIDANDO_FUNCIONARIO =
            "Validando funcionário ID: %d";

    // ---- Logs — ValeValidator ----
    public static final String LOG_VALIDANDO_PERCENTUAL =
            "Validando percentual %.1f%% para tipo %s";

    public static final String LOG_VALIDANDO_VALE_MES =
            "Validando duplicidade de vale para funcionário ID: %d no mês %s";

    public static final String LOG_VALIDANDO_VALE_CANCELAVEL =
            "Validando se vale ID %d pode ser cancelado";
}