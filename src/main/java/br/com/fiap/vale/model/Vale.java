package br.com.fiap.vale.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Representa uma solicitação de vale (adiantamento salarial).
 *
 * Regras de negócio:
 * - O percentual solicitado deve respeitar os limites do TipoVale.
 * - Apenas um vale ativo por funcionário por mês é permitido.
 * - O vale é descontado integralmente no pagamento oficial do mês seguinte.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Vale", propOrder = {
        "id", "funcionarioId", "nomeFuncionario", "salarioBruto",
        "percentualSolicitado", "valorAdiantado", "tipoVale",
        "dataSolicitacao", "status"
})
public class Vale {

    private int id;
    private int funcionarioId;
    private String nomeFuncionario;
    private double salarioBruto;
    private double percentualSolicitado;
    private double valorAdiantado;
    private TipoVale tipoVale;
    private String dataSolicitacao;
    private StatusVale status;

    public Vale() {}

    public Vale(int id, int funcionarioId, String nomeFuncionario,
                double salarioBruto, double percentualSolicitado,
                double valorAdiantado, TipoVale tipoVale,
                String dataSolicitacao, StatusVale status) {
        this.id = id;
        this.funcionarioId = funcionarioId;
        this.nomeFuncionario = nomeFuncionario;
        this.salarioBruto = salarioBruto;
        this.percentualSolicitado = percentualSolicitado;
        this.valorAdiantado = valorAdiantado;
        this.tipoVale = tipoVale;
        this.dataSolicitacao = dataSolicitacao;
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

    public String getDataSolicitacao() { return dataSolicitacao; }
    public void setDataSolicitacao(String dataSolicitacao) { this.dataSolicitacao = dataSolicitacao; }

    public StatusVale getStatus() { return status; }
    public void setStatus(StatusVale status) { this.status = status; }

    @Override
    public String toString() {
        return String.format(
                "Vale{id=%d, funcionarioId=%d, nome='%s', tipo=%s, percentual=%.1f%%, valor=R$ %.2f, status=%s, data='%s'}",
                id, funcionarioId, nomeFuncionario, tipoVale, percentualSolicitado,
                valorAdiantado, status, dataSolicitacao);
    }
}