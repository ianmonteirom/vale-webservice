package br.com.fiap.vale.exception;

public class ValeWebFaultInfo {

  private String mensagem;

  public ValeWebFaultInfo() {}

  public ValeWebFaultInfo(String mensagem) {
    this.mensagem = mensagem;
  }

  public String getMensagem() { return mensagem; }
  public void setMensagem(String mensagem) { this.mensagem = mensagem; }
}