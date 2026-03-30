package br.com.fiap.vale.service;

import br.com.fiap.vale.model.Funcionario;
import br.com.fiap.vale.model.TipoVale;
import br.com.fiap.vale.model.Vale;

import java.util.List;

/**
 * Contrato das operações de negócio do ValeService.
 */
public interface IValeService {

    /** Retorna todos os funcionários cadastrados. */
    List<Funcionario> listarFuncionarios();

    /** Retorna todos os vales registrados. */
    List<Vale> listarVales();

    /** Retorna todos os vales de um funcionário específico. */
    List<Vale> listarValesPorFuncionario(int funcionarioId);

    /**
     * Solicita um adiantamento salarial para o funcionário.
     *
     * @param funcionarioId        ID do funcionário
     * @param percentualSolicitado Percentual do salário bruto (validado pelo TipoVale)
     * @param tipoVale             Tipo do vale que define as regras de percentual
     * @return Vale aprovado
     */
    Vale solicitarVale(int funcionarioId, double percentualSolicitado, TipoVale tipoVale);

    /**
     * Cancela um vale existente pelo ID.
     *
     * @param valeId ID do vale
     * @return Mensagem de confirmação
     */
    String cancelarVale(int valeId);
}