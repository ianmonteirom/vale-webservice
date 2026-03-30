package br.com.fiap.vale.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

@XmlEnum
@XmlType(name = "StatusVale")
public enum StatusVale {

    PENDENTE("Pendente"),

    APROVADO("Aprovado"),

    CANCELADO("Cancelado");

    private final String descricao;

    StatusVale(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public boolean isCancelavel() {
        return this == PENDENTE || this == APROVADO;
    }
}