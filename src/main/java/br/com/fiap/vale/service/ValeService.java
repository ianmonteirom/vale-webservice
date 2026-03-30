package br.com.fiap.vale.service;

import br.com.fiap.vale.model.Funcionario;
import br.com.fiap.vale.model.TipoVale;
import br.com.fiap.vale.model.Vale;
import br.com.fiap.vale.repository.IValeRepository;
import br.com.fiap.vale.repository.ValeRepository;
import br.com.fiap.vale.validation.FuncionarioValidator;
import br.com.fiap.vale.validation.ValeValidator;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.time.LocalDate;
import java.util.List;

/**
 * Implementação do WebService SOAP para gerenciamento de vales (adiantamento salarial).
 * Implementa IValeService — a lógica de negócio é independente do transporte SOAP.
 *
 * Regras de negócio aplicadas:
 * - Percentual do vale deve respeitar os limites definidos no TipoVale.
 * - Apenas um vale ativo (PENDENTE ou APROVADO) por funcionário por mês.
 * - Apenas funcionários ativos podem solicitar vale.
 * - Vale cancelado libera nova solicitação no mesmo mês.
 */
@WebService
public class ValeService implements IValeService {

    // Injeção via construtor — facilita testes com mock do repositório
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

    @Override
    @WebMethod
    public List<Funcionario> listarFuncionarios() {
        return repository.listarFuncionarios();
    }

    @Override
    @WebMethod
    public Vale solicitarVale(
            @WebParam(name = "funcionarioId") int funcionarioId,
            @WebParam(name = "percentualSolicitado") double percentualSolicitado,
            @WebParam(name = "tipoVale") TipoVale tipoVale
    ) {
        // 1. Valida percentual pelo tipo de vale
        valeValidator.validarPercentual(percentualSolicitado, tipoVale);

        // 2. Valida funcionário (existe e está ativo)
        Funcionario funcionario = funcionarioValidator.validarFuncionarioAtivo(funcionarioId);

        // 3. Valida se já possui vale ativo no mês
        String mesAno = LocalDate.now().toString().substring(0, 7);
        valeValidator.validarValeUnicoPorMes(funcionarioId, funcionario.getNome(), mesAno);

        // 4. Calcula valor adiantado e cria o vale
        double valorAdiantado = Math.round(
                funcionario.getSalarioBruto() * (percentualSolicitado / 100.0) * 100.0
        ) / 100.0;

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

    @Override
    @WebMethod
    public String cancelarVale(
            @WebParam(name = "valeId") int valeId
    ) {
        Vale vale = valeValidator.validarValeCancelavel(valeId);

        vale.setStatus("CANCELADO");

        return String.format(
                "Vale ID %d do funcionário '%s' cancelado com sucesso. " +
                        "Valor de R$ %.2f não será descontado no próximo pagamento.",
                valeId, vale.getNomeFuncionario(), vale.getValorAdiantado()
        );
    }
}