package br.com.fiap.vale.repository;

import br.com.fiap.vale.model.Funcionario;
import br.com.fiap.vale.model.Vale;

import java.util.List;
import java.util.Optional;

/**
 * Contrato de acesso aos dados.
 * Desacopla a lógica de negócio da implementação de persistência.
 */
public interface IValeRepository {

    // ---- Funcionários ----
    List<Funcionario> listarFuncionarios();
    Optional<Funcionario> buscarFuncionarioPorId(int id);

    // ---- Vales ----
    Vale salvarVale(Vale vale);
    Optional<Vale> buscarValePorId(int id);
    List<Vale> listarVales();
    List<Vale> listarValesPorFuncionario(int funcionarioId);
    boolean possuiValeAtivoNoMes(int funcionarioId, String mesAno);
}