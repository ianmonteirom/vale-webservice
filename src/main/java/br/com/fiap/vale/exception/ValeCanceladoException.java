package br.com.fiap.vale.exception;

/**
 * Lançada quando se tenta cancelar um vale que já está cancelado.
 */
public class ValeCanceladoException extends ValeException {

    public ValeCanceladoException(int id) {
        super("Vale com ID " + id + " já está cancelado.");
    }
}