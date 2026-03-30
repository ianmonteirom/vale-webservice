package br.com.fiap.vale.repository;

import br.com.fiap.vale.model.Funcionario;
import br.com.fiap.vale.model.Vale;

import java.util.List;
import java.util.Optional;

/**
 * Contrato de acesso aos dados.
 * Desacopla a lógica de negócio da implementação de persistência.
 * Permite trocar a implementação (memória, banco, API externa) sem alterar o service.
 */
public interface IValeRepository {

    // ---- Funcionários ----
    List<Funcionario> listarFuncionarios();
    Optional<Funcionario> buscarFuncionarioPorId(int id);

    // ---- Vales ----
    Vale salvarVale(Vale vale);
    Optional<Vale> buscarValePorId(int id);
    List<Vale> listarVales();
    boolean possuiValeAtivoNoMes(int funcionarioId, String mesAno);
}