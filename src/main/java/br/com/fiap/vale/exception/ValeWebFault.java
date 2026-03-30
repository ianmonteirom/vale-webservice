package br.com.fiap.vale.exception;

import javax.xml.ws.WebFault;

/**
 * Exceção SOAP padronizada para o JAX-WS.
 * Anotada com @WebFault e com fault bean ValeWebFaultInfo para que
 * o JAX-WS serialize corretamente a mensagem de erro no envelope SOAP.
 *
 * Resposta esperada ao cliente:
 * <S:Fault>
 *   <faultcode>S:Server</faultcode>
 *   <faultstring>Mensagem do erro aqui</faultstring>
 *   <detail><ValeWebFault><mensagem>Mensagem do erro aqui</mensagem></ValeWebFault></detail>
 * </S:Fault>
 */
@WebFault(name = "ValeWebFault", targetNamespace = "http://service.vale.fiap.com.br/")
public class ValeWebFault extends Exception {

    private final ValeWebFaultInfo faultInfo;

    public ValeWebFault(String mensagem) {
        super(mensagem);
        this.faultInfo = new ValeWebFaultInfo(mensagem);
    }

    public ValeWebFault(String mensagem, ValeWebFaultInfo faultInfo) {
        super(mensagem);
        this.faultInfo = faultInfo;
    }

    /**
     * Obrigatório pelo contrato do @WebFault.
     * O JAX-WS usa este método para serializar o <detail> do SOAP Fault.
     */
    public ValeWebFaultInfo getFaultInfo() {
        return faultInfo;
    }
}