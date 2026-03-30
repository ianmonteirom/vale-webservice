package br.com.fiap.vale.validation;

import br.com.fiap.vale.exception.FuncionarioInativoException;
import br.com.fiap.vale.exception.FuncionarioNaoEncontradoException;
import br.com.fiap.vale.model.Funcionario;
import br.com.fiap.vale.support.TestFixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("FuncionarioValidator")
class FuncionarioValidatorTest {

    private FuncionarioValidator validator;
    private TestFixtures.StubRepository stub;

    @BeforeEach
    void setUp() {
        stub = new TestFixtures.StubRepository();
        validator = new FuncionarioValidator(stub);
    }

    @Test
    @DisplayName("deve retornar funcionário quando existe e está ativo")
    void validarFuncionarioAtivo_deveRetornarFuncionarioValido() {
        stub.setFuncionario(Optional.of(TestFixtures.funcionarioAtivo()));

        Funcionario resultado = validator.validarFuncionarioAtivo(1);

        assertNotNull(resultado);
        assertEquals("Ana Silva", resultado.getNome());
        assertTrue(resultado.isAtivo());
    }

    @Test
    @DisplayName("deve lançar FuncionarioNaoEncontradoException quando ID não existe")
    void validarFuncionarioAtivo_deveLancarExcecaoQuandoNaoEncontrado() {
        stub.setFuncionario(Optional.empty());

        FuncionarioNaoEncontradoException ex = assertThrows(
                FuncionarioNaoEncontradoException.class,
                () -> validator.validarFuncionarioAtivo(99)
        );

        assertTrue(ex.getMessage().contains("99"));
    }

    @Test
    @DisplayName("deve lançar FuncionarioInativoException quando funcionário está inativo")
    void validarFuncionarioAtivo_deveLancarExcecaoQuandoInativo() {
        stub.setFuncionario(Optional.of(TestFixtures.funcionarioInativo()));

        FuncionarioInativoException ex = assertThrows(
                FuncionarioInativoException.class,
                () -> validator.validarFuncionarioAtivo(6)
        );

        assertTrue(ex.getMessage().contains("Funcionário Inativo"));
    }

    @Test
    @DisplayName("mensagem de erro deve conter o ID quando funcionário não encontrado")
    void validarFuncionarioAtivo_mensagemDeveConterId() {
        stub.setFuncionario(Optional.empty());

        Exception ex = assertThrows(FuncionarioNaoEncontradoException.class,
                () -> validator.validarFuncionarioAtivo(42));

        assertTrue(ex.getMessage().contains("42"));
    }

    @Test
    @DisplayName("mensagem de erro deve conter o nome quando funcionário inativo")
    void validarFuncionarioAtivo_mensagemDeveConterNome() {
        Funcionario inativo = new Funcionario(3, "João Inativo", "QA", 4000.0, false);
        stub.setFuncionario(Optional.of(inativo));

        Exception ex = assertThrows(FuncionarioInativoException.class,
                () -> validator.validarFuncionarioAtivo(3));

        assertTrue(ex.getMessage().contains("João Inativo"));
    }

    @Test
    @DisplayName("deve aceitar funcionário com salário zero quando ativo")
    void validarFuncionarioAtivo_deveAceitarFuncionarioComSalarioZero() {
        Funcionario estagiario = new Funcionario(4, "Estagiário", "Estágio", 0.0, true);
        stub.setFuncionario(Optional.of(estagiario));

        assertDoesNotThrow(() -> validator.validarFuncionarioAtivo(4));
    }
}