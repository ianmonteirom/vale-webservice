package br.com.fiap.vale.exception;

/**
 * Lançada quando um funcionário não é encontrado pelo ID informado.
 */
public class FuncionarioNaoEncontradoException extends ValeException {

    public FuncionarioNaoEncontradoException(int id) {
        super("Funcionário com ID " + id + " não encontrado.");
    }
}