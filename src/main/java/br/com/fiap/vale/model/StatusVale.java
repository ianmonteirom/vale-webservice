package br.com.fiap.vale.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

/**
 * Status possíveis de um Vale durante seu ciclo de vida.
 */
@XmlEnum
@XmlType(name = "StatusVale")
public enum StatusVale {

    /**
     * Solicitação registrada, aguardando processamento.
     */
    PENDENTE("Pendente"),

    /**
     * Vale aprovado e valor liberado para o funcionário.
     */
    APROVADO("Aprovado"),

    /**
     * Vale cancelado — valor não será descontado no próximo pagamento.
     */
    CANCELADO("Cancelado");

    private final String descricao;

    StatusVale(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    /**
     * Verifica se o vale ainda pode ser cancelado.
     */
    public boolean isCancelavel() {
        return this == PENDENTE || this == APROVADO;
    }
}