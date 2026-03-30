package br.com.fiap.vale.service;

import br.com.fiap.vale.exception.*;
import br.com.fiap.vale.model.*;
import br.com.fiap.vale.support.TestFixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ValeService")
class ValeServiceTest {

    private ValeService service;

    @BeforeEach
    void setUp() {
        service = new ValeService(new TestFixtures.InMemoryValeRepository());
    }

    // ---- listarFuncionarios ----

    @Test
    @DisplayName("listarFuncionarios deve retornar lista nao vazia")
    void listarFuncionarios_deveRetornarListaNaoVazia() {
        List<Funcionario> lista = service.listarFuncionarios();
        assertFalse(lista.isEmpty());
    }

    // ---- solicitarVale ----

    @Test
    @DisplayName("solicitarVale deve aprovar vale com percentual valido")
    void solicitarVale_deveAprovarComPercentualValido() throws ValeWebFault {
        Vale vale = service.solicitarVale(1, 35.0, TipoVale.ADIANTAMENTO_MENSAL);
        assertNotNull(vale);
        assertEquals(StatusVale.APROVADO, vale.getStatus());
        assertEquals(35.0, vale.getPercentualSolicitado());
        assertEquals(TipoVale.ADIANTAMENTO_MENSAL, vale.getTipoVale());
    }

    @Test
    @DisplayName("solicitarVale deve calcular valor adiantado corretamente")
    void solicitarVale_deveCalcularValorAdiantadoCorreto() throws ValeWebFault {
        Vale vale = service.solicitarVale(1, 40.0, TipoVale.ADIANTAMENTO_MENSAL);
        assertEquals(3400.00, vale.getValorAdiantado(), 0.01);
    }

    @Test
    @DisplayName("solicitarVale deve lancar ValeWebFault se percentual invalido")
    void solicitarVale_deveLancarExcecaoPercentualInvalido() {
        assertThrows(ValeWebFault.class,
                () -> service.solicitarVale(1, 50.0, TipoVale.ADIANTAMENTO_MENSAL));
    }

    @Test
    @DisplayName("solicitarVale deve lancar ValeWebFault para ID inexistente")
    void solicitarVale_deveLancarExcecaoFuncionarioNaoEncontrado() {
        assertThrows(ValeWebFault.class,
                () -> service.solicitarVale(99, 35.0, TipoVale.ADIANTAMENTO_MENSAL));
    }

    @Test
    @DisplayName("solicitarVale deve lancar ValeWebFault para funcionario inativo")
    void solicitarVale_deveLancarExcecaoFuncionarioInativo() {
        assertThrows(ValeWebFault.class,
                () -> service.solicitarVale(6, 35.0, TipoVale.ADIANTAMENTO_MENSAL));
    }

    @Test
    @DisplayName("solicitarVale deve lancar ValeWebFault se ja existe vale no mes")
    void solicitarVale_deveLancarExcecaoValeDuplicado() throws ValeWebFault {
        service.solicitarVale(2, 30.0, TipoVale.ADIANTAMENTO_MENSAL);
        assertThrows(ValeWebFault.class,
                () -> service.solicitarVale(2, 30.0, TipoVale.ADIANTAMENTO_MENSAL));
    }

    @Test
    @DisplayName("solicitarVale EMERGENCIAL deve aceitar percentual entre 10 e 20")
    void solicitarVale_emergencialDeveAceitarPercentualCorreto() throws ValeWebFault {
        Vale vale = service.solicitarVale(3, 15.0, TipoVale.EMERGENCIAL);
        assertEquals(TipoVale.EMERGENCIAL, vale.getTipoVale());
        assertEquals(StatusVale.APROVADO, vale.getStatus());
    }

    // ---- cancelarVale ----

    @Test
    @DisplayName("cancelarVale deve cancelar vale aprovado com sucesso")
    void cancelarVale_deveCancelarValeAprovado() throws ValeWebFault {
        Vale vale = service.solicitarVale(1, 35.0, TipoVale.ADIANTAMENTO_MENSAL);
        String mensagem = service.cancelarVale(vale.getId());
        assertNotNull(mensagem);
        assertTrue(mensagem.contains("cancelado com sucesso"));
        assertEquals(StatusVale.CANCELADO, vale.getStatus());
    }

    @Test
    @DisplayName("cancelarVale deve lancar ValeWebFault para ID inexistente")
    void cancelarVale_deveLancarExcecaoValeNaoEncontrado() {
        assertThrows(ValeWebFault.class,
                () -> service.cancelarVale(999));
    }

    @Test
    @DisplayName("cancelarVale deve lancar ValeWebFault ao cancelar duas vezes")
    void cancelarVale_deveLancarExcecaoValeCanceladoDuasVezes() throws ValeWebFault {
        Vale vale = service.solicitarVale(1, 35.0, TipoVale.ADIANTAMENTO_MENSAL);
        service.cancelarVale(vale.getId());
        assertThrows(ValeWebFault.class,
                () -> service.cancelarVale(vale.getId()));
    }

    @Test
    @DisplayName("apos cancelar vale, funcionario pode solicitar novo no mesmo mes")
    void cancelarVale_deveLiberarNovasolicitacaoNoMesmo() throws ValeWebFault {
        Vale vale = service.solicitarVale(1, 35.0, TipoVale.ADIANTAMENTO_MENSAL);
        service.cancelarVale(vale.getId());
        Vale novoVale = service.solicitarVale(1, 30.0, TipoVale.ADIANTAMENTO_MENSAL);
        assertEquals(StatusVale.APROVADO, novoVale.getStatus());
    }

    // ---- listarValesPorFuncionario ----

    @Test
    @DisplayName("listarValesPorFuncionario deve retornar apenas vales do funcionario")
    void listarValesPorFuncionario_deveRetornarApenasDoFuncionario() throws ValeWebFault {
        service.solicitarVale(1, 35.0, TipoVale.ADIANTAMENTO_MENSAL);
        service.solicitarVale(2, 30.0, TipoVale.ADIANTAMENTO_MENSAL);
        List<Vale> valesFuncionario1 = service.listarValesPorFuncionario(1);
        assertEquals(1, valesFuncionario1.size());
        assertTrue(valesFuncionario1.stream().allMatch(v -> v.getFuncionarioId() == 1));
    }
}