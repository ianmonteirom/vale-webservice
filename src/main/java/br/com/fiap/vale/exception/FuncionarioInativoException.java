package br.com.fiap.vale.exception;

public class FuncionarioInativoException extends ValeException {

    public FuncionarioInativoException(String nomeFuncionario) {
        super("Funcionário '" + nomeFuncionario + "' está inativo e não pode solicitar vale.");
    }
}