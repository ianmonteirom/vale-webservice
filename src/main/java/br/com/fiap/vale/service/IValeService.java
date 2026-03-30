package br.com.fiap.vale.service;

import br.com.fiap.vale.model.Funcionario;
import br.com.fiap.vale.model.TipoVale;
import br.com.fiap.vale.model.Vale;

import java.util.List;

/**
 * Contrato das operações de negócio do ValeService.
 * Desacopla o WebService da implementação concreta,
 * facilitando testes, mocks e futuras versões do serviço.
 */
public interface IValeService {

    /**
     * Retorna todos os funcionários cadastrados.
     */
    List<Funcionario> listarFuncionarios();

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