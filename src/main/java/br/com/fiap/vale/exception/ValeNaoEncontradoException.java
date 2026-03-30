package br.com.fiap.vale.exception;

public class ValeNaoEncontradoException extends ValeException {

    public ValeNaoEncontradoException(int id) {
        super("Vale com ID " + id + " não encontrado.");
    }
}