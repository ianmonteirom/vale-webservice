package br.com.fiap.vale.exception;

import br.com.fiap.vale.model.TipoVale;

/**
 * Lançada quando o percentual solicitado está fora do intervalo
 * permitido para o tipo de vale informado.
 */
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