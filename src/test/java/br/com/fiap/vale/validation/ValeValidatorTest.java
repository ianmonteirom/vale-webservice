package br.com.fiap.vale.validation;

import br.com.fiap.vale.exception.PercentualInvalidoException;
import br.com.fiap.vale.exception.ValeCanceladoException;
import br.com.fiap.vale.exception.ValeDuplicadoException;
import br.com.fiap.vale.exception.ValeNaoEncontradoException;
import br.com.fiap.vale.model.*;
import br.com.fiap.vale.support.TestFixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ValeValidator")
class ValeValidatorTest {

    private ValeValidator validator;
    private TestFixtures.StubRepository stub;

    @BeforeEach
    void setUp() {
        stub = new TestFixtures.StubRepository();
        validator = new ValeValidator(stub);
    }


    @Test
    @DisplayName("deve aceitar percentual no limite mínimo do ADIANTAMENTO_MENSAL")
    void validarPercentual_deveAceitarLimiteMinimo() {
        assertDoesNotThrow(() ->
                validator.validarPercentual(30.0, TipoVale.ADIANTAMENTO_MENSAL));
    }

    @Test
    @DisplayName("deve aceitar percentual no limite máximo do ADIANTAMENTO_MENSAL")
    void validarPercentual_deveAceitarLimiteMaximo() {
        assertDoesNotThrow(() ->
                validator.validarPercentual(40.0, TipoVale.ADIANTAMENTO_MENSAL));
    }

    @Test
    @DisplayName("deve lançar exceção para percentual abaixo do mínimo")
    void validarPercentual_deveLancarExcecaoAbaixoMinimo() {
        assertThrows(PercentualInvalidoException.class, () ->
                validator.validarPercentual(29.9, TipoVale.ADIANTAMENTO_MENSAL));
    }

    @Test
    @DisplayName("deve lançar exceção para percentual acima do máximo")
    void validarPercentual_deveLancarExcecaoAcimaMaximo() {
        assertThrows(PercentualInvalidoException.class, () ->
                validator.validarPercentual(40.1, TipoVale.ADIANTAMENTO_MENSAL));
    }

    @Test
    @DisplayName("DECIMO_TERCEIRO deve aceitar exatamente 50%")
    void validarPercentual_decimoTerceiroDeveAceitarExatamente50() {
        assertDoesNotThrow(() ->
                validator.validarPercentual(50.0, TipoVale.DECIMO_TERCEIRO));
    }

    @Test
    @DisplayName("DECIMO_TERCEIRO deve rejeitar qualquer valor diferente de 50%")
    void validarPercentual_decimoTerceiroDeveRejeitarValorDiferente() {
        assertThrows(PercentualInvalidoException.class, () ->
                validator.validarPercentual(49.0, TipoVale.DECIMO_TERCEIRO));
    }


    @Test
    @DisplayName("deve lançar ValeDuplicadoException se já existe vale ativo no mês")
    void validarValeUnicoPorMes_deveLancarExcecaoSeDuplicado() {
        stub.setTemValeAtivo(true);
        assertThrows(ValeDuplicadoException.class, () ->
                validator.validarValeUnicoPorMes(1, "Ana Silva", "2026-03"));
    }

    @Test
    @DisplayName("deve passar sem exceção se não existe vale ativo no mês")
    void validarValeUnicoPorMes_devePassarSemValeAtivo() {
        stub.setTemValeAtivo(false);
        assertDoesNotThrow(() ->
                validator.validarValeUnicoPorMes(1, "Ana Silva", "2026-03"));
    }


    @Test
    @DisplayName("deve lançar ValeNaoEncontradoException para ID inexistente")
    void validarValeCancelavel_deveLancarExcecaoValeNaoEncontrado() {
        stub.setVale(Optional.empty());
        assertThrows(ValeNaoEncontradoException.class, () ->
                validator.validarValeCancelavel(99));
    }

    @Test
    @DisplayName("deve lançar ValeCanceladoException para vale já cancelado")
    void validarValeCancelavel_deveLancarExcecaoValeCancelado() {
        stub.setVale(Optional.of(TestFixtures.valeCancelado()));
        assertThrows(ValeCanceladoException.class, () ->
                validator.validarValeCancelavel(1));
    }

    @Test
    @DisplayName("deve retornar vale quando está APROVADO")
    void validarValeCancelavel_deveRetornarValeAprovado() {
        stub.setVale(Optional.of(TestFixtures.valeAprovado()));
        Vale resultado = validator.validarValeCancelavel(1);
        assertEquals(StatusVale.APROVADO, resultado.getStatus());
    }
}