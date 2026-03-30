package br.com.fiap.vale.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Representa um funcionário elegível a solicitar vale (adiantamento salarial).
 */
@XmlRootElement
public class Funcionario {

    private int id;
    private String nome;
    private String cargo;
    private double salarioBruto;
    private boolean ativo;

    public Funcionario() {}

    public Funcionario(int id, String nome, String cargo, double salarioBruto, boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.cargo = cargo;
        this.salarioBruto = salarioBruto;
        this.ativo = ativo;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }

    public double getSalarioBruto() { return salarioBruto; }
    public void setSalarioBruto(double salarioBruto) { this.salarioBruto = salarioBruto; }

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
}