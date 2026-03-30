package br.com.fiap.vale.exception;

public class ValeException extends RuntimeException {

    public ValeException(String message) {
        super(message);
    }

    public ValeException(String message, Throwable cause) {
        super(message, cause);
    }
}