package br.com.fiap.vale.exception;

public class ValeCanceladoException extends ValeException {

    public ValeCanceladoException(int id) {
        super("Vale com ID " + id + " já está cancelado.");
    }
}