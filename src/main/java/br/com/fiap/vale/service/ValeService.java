package br.com.fiap.vale.service;

import br.com.fiap.vale.model.Funcionario;
import br.com.fiap.vale.model.TipoVale;
import br.com.fiap.vale.model.Vale;
import br.com.fiap.vale.repository.IValeRepository;
import br.com.fiap.vale.repository.ValeRepository;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Implementação do WebService SOAP para gerenciamento de vales (adiantamento salarial).
 * Implementa IValeService — a lógica de negócio é independente do transporte SOAP.
 *
 * Regras de negócio aplicadas:
 * - Percentual do vale deve ser entre 30% e 40% do salário bruto.
 * - Apenas um vale ativo (PENDENTE ou APROVADO) por funcionário por mês.
 * - Apenas funcionários ativos podem solicitar vale.
 * - Vale cancelado libera nova solicitação no mesmo mês.
 */
@WebService
public class ValeService implements IValeService {

    // Injeção via construtor — facilita testes com mock do repositório
    private final IValeRepository repository;

    public ValeService() {
        this.repository = new ValeRepository();
    }

    public ValeService(IValeRepository repository) {
        this.repository = repository;
    }

    /**
     * Operação 1 — Lista todos os funcionários cadastrados.
     */
    @Override
    @WebMethod
    public List<Funcionario> listarFuncionarios() {
        return repository.listarFuncionarios();
    }

    /**
     * Operação 2 — Solicita um vale para um funcionário.
     *
     * @param funcionarioId      ID do funcionário
     * @param percentualSolicitado Percentual do salário bruto a ser adiantado (30 a 40)
     * @return Vale criado com status APROVADO
     */
    @Override
    @WebMethod
    public Vale solicitarVale(
            @WebParam(name = "funcionarioId") int funcionarioId,
            @WebParam(name = "percentualSolicitado") double percentualSolicitado,
            @WebParam(name = "tipoVale") TipoVale tipoVale
    ) {
        // Valida percentual de acordo com o tipo do vale
        if (!tipoVale.percentualValido(percentualSolicitado)) {
            throw new RuntimeException(
                    "Percentual inválido para o tipo '" + tipoVale.getDescricao() + "'. " +
                            tipoVale.getResumo()
            );
        }

        // Busca funcionário
        Optional<Funcionario> funcionarioOpt = repository.buscarFuncionarioPorId(funcionarioId);
        if (!funcionarioOpt.isPresent()) {
            throw new RuntimeException("Funcionário com ID " + funcionarioId + " não encontrado.");
        }

        Funcionario funcionario = funcionarioOpt.get();

        // Valida se funcionário está ativo
        if (!funcionario.isAtivo()) {
            throw new RuntimeException(
                    "Funcionário '" + funcionario.getNome() + "' está inativo e não pode solicitar vale."
            );
        }

        // Valida se já possui vale ativo no mês
        String mesAno = LocalDate.now().toString().substring(0, 7); // "2026-03"
        if (repository.possuiValeAtivoNoMes(funcionarioId, mesAno)) {
            throw new RuntimeException(
                    "Funcionário '" + funcionario.getNome() + "' já possui um vale ativo para o mês " + mesAno + "."
            );
        }

        // Calcula o valor adiantado
        double valorAdiantado = funcionario.getSalarioBruto() * (percentualSolicitado / 100.0);
        valorAdiantado = Math.round(valorAdiantado * 100.0) / 100.0; // arredonda 2 casas

        // Cria e salva o vale
        Vale vale = new Vale(
                0,
                funcionarioId,
                funcionario.getNome(),
                funcionario.getSalarioBruto(),
                percentualSolicitado,
                valorAdiantado,
                tipoVale,
                LocalDate.now().toString(),
                "APROVADO"
        );

        return repository.salvarVale(vale);
    }

    /**
     * Operação 3 — Cancela um vale existente.
     *
     * @param valeId ID do vale a cancelar
     * @return Mensagem de confirmação
     */
    @Override
    @WebMethod
    public String cancelarVale(
            @WebParam(name = "valeId") int valeId
    ) {
        Optional<Vale> valeOpt = repository.buscarValePorId(valeId);

        if (!valeOpt.isPresent()) {
            throw new RuntimeException("Vale com ID " + valeId + " não encontrado.");
        }

        Vale vale = valeOpt.get();

        if ("CANCELADO".equals(vale.getStatus())) {
            throw new RuntimeException("Vale com ID " + valeId + " já está cancelado.");
        }

        vale.setStatus("CANCELADO");

        return String.format(
                "Vale ID %d do funcionário '%s' cancelado com sucesso. " +
                        "Valor de R$ %.2f não será descontado no próximo pagamento.",
                valeId, vale.getNomeFuncionario(), vale.getValorAdiantado()
        );
    }
}