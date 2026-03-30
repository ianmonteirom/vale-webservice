package br.com.fiap.vale.exception;

/**
 * Lançada quando um vale não é encontrado pelo ID informado.
 */
public class ValeNaoEncontradoException extends ValeException {

    public ValeNaoEncontradoException(int id) {
        super("Vale com ID " + id + " não encontrado.");
    }
}