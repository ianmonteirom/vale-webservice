package br.com.fiap.vale.exception;

/**
 * Fault bean exigido pelo JAX-WS para serialização correta do SOAP Fault.
 * O getFaultInfo() do ValeWebFault deve retornar este objeto.
 */
public class ValeWebFaultInfo {

  private String mensagem;

  public ValeWebFaultInfo() {}

  public ValeWebFaultInfo(String mensagem) {
    this.mensagem = mensagem;
  }

  public String getMensagem() { return mensagem; }
  public void setMensagem(String mensagem) { this.mensagem = mensagem; }
}