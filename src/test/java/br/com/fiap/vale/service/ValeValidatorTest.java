package br.com.fiap.vale.validation;

import br.com.fiap.vale.exception.PercentualInvalidoException;
import br.com.fiap.vale.exception.ValeCanceladoException;
import br.com.fiap.vale.exception.ValeDuplicadoException;
import br.com.fiap.vale.exception.ValeNaoEncontradoException;
import br.com.fiap.vale.model.*;
import br.com.fiap.vale.repository.IValeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ValeValidator")
class ValeValidatorTest {

    private ValeValidator validator;
    private StubValeRepository stubRepository;

    @BeforeEach
    void setUp() {
        stubRepository = new StubValeRepository();
        validator = new ValeValidator(stubRepository);
    }

    // ---- validarPercentual ----

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

    // ---- validarValeUnicoPorMes ----

    @Test
    @DisplayName("deve lançar ValeDuplicadoException se já existe vale ativo no mês")
    void validarValeUnicoPorMes_deveLancarExcecaoSeDuplicado() {
        stubRepository.setTemValeAtivo(true);
        assertThrows(ValeDuplicadoException.class, () ->
                validator.validarValeUnicoPorMes(1, "Ana Silva", "2026-03"));
    }

    @Test
    @DisplayName("deve passar sem exceção se não existe vale ativo no mês")
    void validarValeUnicoPorMes_devePassarSemValeAtivo() {
        stubRepository.setTemValeAtivo(false);
        assertDoesNotThrow(() ->
                validator.validarValeUnicoPorMes(1, "Ana Silva", "2026-03"));
    }

    // ---- validarValeCancelavel ----

    @Test
    @DisplayName("deve lançar ValeNaoEncontradoException para ID inexistente")
    void validarValeCancelavel_deveLancarExcecaoValeNaoEncontrado() {
        stubRepository.setValeParaRetornar(Optional.empty());
        assertThrows(ValeNaoEncontradoException.class, () ->
                validator.validarValeCancelavel(99));
    }

    @Test
    @DisplayName("deve lançar ValeCanceladoException para vale já cancelado")
    void validarValeCancelavel_deveLancarExcecaoValeCancelado() {
        Vale valeCancelado = criarVale(StatusVale.CANCELADO);
        stubRepository.setValeParaRetornar(Optional.of(valeCancelado));

        assertThrows(ValeCanceladoException.class, () ->
                validator.validarValeCancelavel(1));
    }

    @Test
    @DisplayName("deve retornar vale quando está APROVADO")
    void validarValeCancelavel_deveRetornarValeAprovado() {
        Vale valeAprovado = criarVale(StatusVale.APROVADO);
        stubRepository.setValeParaRetornar(Optional.of(valeAprovado));

        Vale resultado = validator.validarValeCancelavel(1);
        assertEquals(StatusVale.APROVADO, resultado.getStatus());
    }

    // ---- Helpers ----

    private Vale criarVale(StatusVale status) {
        return new Vale(1, 1, "Ana Silva", 8500.0, 35.0, 2975.0,
                TipoVale.ADIANTAMENTO_MENSAL, LocalDate.now().toString(), status);
    }

    // ---- Stub do repositório para testes isolados ----

    static class StubValeRepository implements IValeRepository {

        private boolean temValeAtivo = false;
        private Optional<Vale> valeParaRetornar = Optional.empty();

        public void setTemValeAtivo(boolean temValeAtivo) { this.temValeAtivo = temValeAtivo; }
        public void setValeParaRetornar(Optional<Vale> vale) { this.valeParaRetornar = vale; }

        @Override public List<Funcionario> listarFuncionarios() { return new ArrayList<>(); }
        @Override public Optional<Funcionario> buscarFuncionarioPorId(int id) { return Optional.empty(); }
        @Override public Vale salvarVale(Vale vale) { return vale; }
        @Override public Optional<Vale> buscarValePorId(int id) { return valeParaRetornar; }
        @Override public List<Vale> listarVales() { return new ArrayList<>(); }
        @Override public List<Vale> listarValesPorFuncionario(int id) { return new ArrayList<>(); }
        @Override public boolean possuiValeAtivoNoMes(int funcionarioId, String mesAno) { return temValeAtivo; }
    }
}