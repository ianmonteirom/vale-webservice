package br.com.fiap.vale.exception;

public class FuncionarioNaoEncontradoException extends ValeException {

    public FuncionarioNaoEncontradoException(int id) {
        super("Funcionário com ID " + id + " não encontrado.");
    }
}