package br.com.fiap.vale.exception;

import javax.xml.ws.WebFault;

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

    public ValeWebFaultInfo getFaultInfo() {
        return faultInfo;
    }
}