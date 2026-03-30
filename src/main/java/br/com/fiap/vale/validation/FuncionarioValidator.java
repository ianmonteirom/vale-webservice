package br.com.fiap.vale.validation;

import br.com.fiap.vale.exception.FuncionarioInativoException;
import br.com.fiap.vale.exception.FuncionarioNaoEncontradoException;
import br.com.fiap.vale.model.Funcionario;
import br.com.fiap.vale.repository.IValeRepository;

import java.util.Optional;

/**
 * Responsável por validar regras relacionadas ao Funcionario.
 * Centraliza as validações para evitar duplicação no ValeService.
 */
public class FuncionarioValidator {

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