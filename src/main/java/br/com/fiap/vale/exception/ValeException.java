package br.com.fiap.vale.exception;

/**
 * Exceção base da aplicação.
 * Todas as exceções de negócio herdam desta classe,
 * permitindo captura centralizada no serviço.
 */
public class ValeException extends RuntimeException {

    public ValeException(String message) {
        super(message);
    }

    public ValeException(String message, Throwable cause) {
        super(message, cause);
    }
}