package br.com.fiap.vale.exception;

/**
 * Lançada quando um funcionário inativo tenta solicitar um vale.
 */
public class FuncionarioInativoException extends ValeException {

    public FuncionarioInativoException(String nomeFuncionario) {
        super("Funcionário '" + nomeFuncionario + "' está inativo e não pode solicitar vale.");
    }
}