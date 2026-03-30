package br.com.fiap.vale.model;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlEnum;

/**
 * Representa uma solicitação de vale (adiantamento salarial).
 *
 * Regras de negócio:
 * - O percentual solicitado deve ser entre 30% e 40% do salário bruto.
 * - Apenas um vale ativo por funcionário por mês é permitido.
 * - O vale é descontado integralmente no pagamento oficial do mês seguinte.
 */
@XmlRootElement
public class Vale {

    private int id;
    private int funcionarioId;
    private String nomeFuncionario;
    private double salarioBruto;
    private double percentualSolicitado; // entre 30 e 40
    private double valorAdiantado;       // salarioBruto * (percentualSolicitado / 100)
    private TipoVale tipoVale;           // tipo do adiantamento solicitado
    private String datasolicitacao;
    private String status;               // PENDENTE, APROVADO, CANCELADO

    public Vale() {}

    public Vale(int id, int funcionarioId, String nomeFuncionario,
                double salarioBruto, double percentualSolicitado,
                double valorAdiantado, TipoVale tipoVale,
                String dataSolicitacao, String status) {
        this.id = id;
        this.funcionarioId = funcionarioId;
        this.nomeFuncionario = nomeFuncionario;
        this.salarioBruto = salarioBruto;
        this.percentualSolicitado = percentualSolicitado;
        this.valorAdiantado = valorAdiantado;
        this.tipoVale = tipoVale;
        this.datasolicitacao = dataSolicitacao;
        this.status = status;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getFuncionarioId() { return funcionarioId; }
    public void setFuncionarioId(int funcionarioId) { this.funcionarioId = funcionarioId; }

    public String getNomeFuncionario() { return nomeFuncionario; }
    public void setNomeFuncionario(String nomeFuncionario) { this.nomeFuncionario = nomeFuncionario; }

    public double getSalarioBruto() { return salarioBruto; }
    public void setSalarioBruto(double salarioBruto) { this.salarioBruto = salarioBruto; }

    public double getPercentualSolicitado() { return percentualSolicitado; }
    public void setPercentualSolicitado(double percentualSolicitado) { this.percentualSolicitado = percentualSolicitado; }

    public double getValorAdiantado() { return valorAdiantado; }
    public void setValorAdiantado(double valorAdiantado) { this.valorAdiantado = valorAdiantado; }

    public TipoVale getTipoVale() { return tipoVale; }
    public void setTipoVale(TipoVale tipoVale) { this.tipoVale = tipoVale; }

    public String getDatasolicitacao() { return datasolicitacao; }
    public void setDatasolicitacao(String datasolicitacao) { this.datasolicitacao = datasolicitacao; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}