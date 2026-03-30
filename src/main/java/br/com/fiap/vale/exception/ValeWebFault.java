package br.com.fiap.vale.exception;

import javax.xml.ws.WebFault;

/**
 * Exceção SOAP padronizada para o JAX-WS.
 * Anotada com @WebFault para que o JAX-WS serialze corretamente
 * a mensagem de erro no envelope SOAP retornado ao cliente.
 */
@WebFault(name = "ValeWebFault")
public class ValeWebFault extends Exception {

    private final String mensagem;

    public ValeWebFault(String mensagem) {
        super(mensagem);
        this.mensagem = mensagem;
    }

    public String getFaultInfo() {
        return mensagem;
    }
}