package br.com.fiap.vale.service;

import br.com.fiap.vale.model.Funcionario;
import br.com.fiap.vale.model.StatusVale;
import br.com.fiap.vale.model.TipoVale;
import br.com.fiap.vale.model.Vale;
import br.com.fiap.vale.repository.IValeRepository;
import br.com.fiap.vale.repository.ValeRepository;
import br.com.fiap.vale.util.Constantes;
import br.com.fiap.vale.util.ValeCalculator;
import br.com.fiap.vale.validation.FuncionarioValidator;
import br.com.fiap.vale.validation.ValeValidator;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.List;
import java.util.logging.Logger;

/**
 * Implementação do WebService SOAP para gerenciamento de vales (adiantamento salarial).
 * Implementa IValeService — a lógica de negócio é independente do transporte SOAP.
 *
 * Regras de negócio:
 * - Percentual deve respeitar os limites do TipoVale.
 * - Apenas um vale ativo por funcionário por mês.
 * - Apenas funcionários ativos podem solicitar vale.
 * - Vale cancelado libera nova solicitação no mesmo mês.
 */
@WebService
public class ValeService implements IValeService {

    private static final Logger LOGGER = Logger.getLogger(ValeService.class.getName());

    private final IValeRepository repository;
    private final FuncionarioValidator funcionarioValidator;
    private final ValeValidator valeValidator;

    public ValeService() {
        this.repository           = new ValeRepository();
        this.funcionarioValidator = new FuncionarioValidator(repository);
        this.valeValidator        = new ValeValidator(repository);
    }

    public ValeService(IValeRepository repository) {
        this.repository           = repository;
        this.funcionarioValidator = new FuncionarioValidator(repository);
        this.valeValidator        = new ValeValidator(repository);
    }

    /**
     * Operação 1 — Lista todos os funcionários cadastrados.
     */
    @Override
    @WebMethod
    public List<Funcionario> listarFuncionarios() {
        LOGGER.info(Constantes.LOG_LISTANDO_FUNCIONARIOS);
        return repository.listarFuncionarios();
    }

    /**
     * Operação 2 — Lista todos os vales registrados.
     */
    @Override
    @WebMethod
    public List<Vale> listarVales() {
        LOGGER.info(Constantes.LOG_LISTANDO_VALES);
        return repository.listarVales();
    }

    /**
     * Operação 3 — Lista todos os vales de um funcionário específico.
     */
    @Override
    @WebMethod
    public List<Vale> listarValesPorFuncionario(
            @WebParam(name = "funcionarioId") int funcionarioId
    ) {
        LOGGER.info(String.format(Constantes.LOG_LISTANDO_VALES_FUNCIONARIO, funcionarioId));
        funcionarioValidator.validarFuncionarioAtivo(funcionarioId);
        return repository.listarValesPorFuncionario(funcionarioId);
    }

    /**
     * Operação 4 — Solicita um vale para um funcionário.
     */
    @Override
    @WebMethod
    public Vale solicitarVale(
            @WebParam(name = "funcionarioId") int funcionarioId,
            @WebParam(name = "percentualSolicitado") double percentualSolicitado,
            @WebParam(name = "tipoVale") TipoVale tipoVale
    ) {
        LOGGER.info(String.format(Constantes.LOG_SOLICITANDO_VALE,
                funcionarioId, percentualSolicitado, tipoVale));

        // 1. Valida percentual pelo tipo de vale
        valeValidator.validarPercentual(percentualSolicitado, tipoVale);

        // 2. Valida funcionário (existe e está ativo)
        Funcionario funcionario = funcionarioValidator.validarFuncionarioAtivo(funcionarioId);

        // 3. Valida se já possui vale ativo no mês
        String mesAno = ValeCalculator.getMesAnoAtual();
        valeValidator.validarValeUnicoPorMes(funcionarioId, funcionario.getNome(), mesAno);

        // 4. Delega cálculo para ValeCalculator
        double valorAdiantado = ValeCalculator.calcularValorAdiantado(
                funcionario.getSalarioBruto(), percentualSolicitado);

        Vale vale = new Vale(
                0,
                funcionarioId,
                funcionario.getNome(),
                funcionario.getSalarioBruto(),
                percentualSolicitado,
                valorAdiantado,
                tipoVale,
                ValeCalculator.getDataAtual(),
                StatusVale.APROVADO
        );

        Vale valeSalvo = repository.salvarVale(vale);

        LOGGER.info(String.format(Constantes.LOG_VALE_APROVADO,
                valeSalvo.getId(), valeSalvo.getNomeFuncionario(),
                valeSalvo.getValorAdiantado(), tipoVale));

        return valeSalvo;
    }

    /**
     * Operação 5 — Cancela um vale existente pelo ID.
     */
    @Override
    @WebMethod
    public String cancelarVale(
            @WebParam(name = "valeId") int valeId
    ) {
        LOGGER.info(String.format(Constantes.LOG_CANCELANDO_VALE, valeId));

        Vale vale = valeValidator.validarValeCancelavel(valeId);
        vale.setStatus(StatusVale.CANCELADO);

        LOGGER.info(String.format(Constantes.LOG_VALE_CANCELADO,
                valeId, vale.getNomeFuncionario()));

        return String.format(Constantes.MSG_VALE_CANCELADO,
                valeId, vale.getNomeFuncionario(), vale.getValorAdiantado());
    }
}