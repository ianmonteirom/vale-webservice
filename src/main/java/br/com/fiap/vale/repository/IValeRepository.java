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

    /**
     * Retorna todos os funcionários cadastrados.
     */
    List<Funcionario> listarFuncionarios();

    /**
     * Busca um funcionário pelo ID.
     *
     * @param id ID do funcionário
     * @return Optional contendo o funcionário, ou vazio se não encontrado
     */
    Optional<Funcionario> buscarFuncionarioPorId(int id);

    // ---- Vales ----

    /**
     * Persiste um novo vale e retorna o objeto com ID gerado.
     *
     * @param vale Vale a ser salvo (sem ID)
     * @return Vale salvo com ID preenchido
     */
    Vale salvarVale(Vale vale);

    /**
     * Busca um vale pelo ID.
     *
     * @param id ID do vale
     * @return Optional contendo o vale, ou vazio se não encontrado
     */
    Optional<Vale> buscarValePorId(int id);

    /**
     * Retorna todos os vales registrados.
     */
    List<Vale> listarVales();

    /**
     * Retorna todos os vales de um funcionário específico.
     *
     * @param funcionarioId ID do funcionário
     * @return Lista de vales do funcionário (pode ser vazia)
     */
    List<Vale> listarValesPorFuncionario(int funcionarioId);

    /**
     * Verifica se o funcionário já possui um vale ativo (APROVADO ou PENDENTE)
     * no mês/ano informado.
     *
     * @param funcionarioId ID do funcionário
     * @param mesAno        Mês e ano no formato "yyyy-MM" (ex: "2026-03")
     * @return true se já existe vale ativo no mês, false caso contrário
     */
    boolean possuiValeAtivoNoMes(int funcionarioId, String mesAno);
}