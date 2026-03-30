package br.com.fiap.vale.exception;

import br.com.fiap.vale.model.TipoVale;

public class PercentualInvalidoException extends ValeException {

    public PercentualInvalidoException(double percentual, TipoVale tipoVale) {
        super(String.format(
                "Percentual inválido: %.1f%% para o tipo '%s'. %s",
                percentual,
                tipoVale.getDescricao(),
                tipoVale.getResumo()
        ));
    }
}