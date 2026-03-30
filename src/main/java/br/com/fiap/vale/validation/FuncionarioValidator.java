package br.com.fiap.vale.validation;

import br.com.fiap.vale.exception.FuncionarioInativoException;
import br.com.fiap.vale.exception.FuncionarioNaoEncontradoException;
import br.com.fiap.vale.model.Funcionario;
import br.com.fiap.vale.repository.IValeRepository;
import br.com.fiap.vale.util.Constantes;

import java.util.Optional;
import java.util.logging.Logger;

/**
 * Responsável por validar regras relacionadas ao Funcionario.
 * Centraliza as validações para evitar duplicação no ValeService.
 */
public class FuncionarioValidator {

    private static final Logger LOGGER = Logger.getLogger(FuncionarioValidator.class.getName());

    private final IValeRepository repository;

    public FuncionarioValidator(IValeRepository repository) {
        this.repository = repository;
    }

    /**
     * Busca e valida o funcionário — lança exceção se não encontrado ou inativo.
     *
     * @param funcionarioId ID do funcionário
     * @return Funcionario validado
     */
    public Funcionario validarFuncionarioAtivo(int funcionarioId) {
        LOGGER.info(String.format(Constantes.LOG_VALIDANDO_FUNCIONARIO, funcionarioId));

        Optional<Funcionario> funcionarioOpt = repository.buscarFuncionarioPorId(funcionarioId);

        if (!funcionarioOpt.isPresent()) {
            throw new FuncionarioNaoEncontradoException(funcionarioId);
        }

        Funcionario funcionario = funcionarioOpt.get();

        if (!funcionario.isAtivo()) {
            throw new FuncionarioInativoException(funcionario.getNome());
        }

        return funcionario;
    }
}