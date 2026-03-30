package br.com.fiap.vale.exception;

public class ValeDuplicadoException extends ValeException {

    public ValeDuplicadoException(String nomeFuncionario, String mesAno) {
        super("Funcionário '" + nomeFuncionario + "' já possui um vale ativo para o mês " + mesAno + ".");
    }
}