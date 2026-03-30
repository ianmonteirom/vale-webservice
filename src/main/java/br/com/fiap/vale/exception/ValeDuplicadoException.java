package br.com.fiap.vale.exception;

/**
 * Lançada quando o funcionário já possui um vale ativo no mês corrente.
 */
public class ValeDuplicadoException extends ValeException {

    public ValeDuplicadoException(String nomeFuncionario, String mesAno) {
        super("Funcionário '" + nomeFuncionario + "' já possui um vale ativo para o mês " + mesAno + ".");
    }
}