package br.com.fiap.vale.service;

import br.com.fiap.vale.exception.*;
import br.com.fiap.vale.model.*;
import br.com.fiap.vale.repository.IValeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários do ValeService.
 * Usa implementação in-memory real — sem mocks externos.
 */
@DisplayName("ValeService")
class ValeServiceTest {

    private ValeService service;

    @BeforeEach
    void setUp() {
        // Instância fresh a cada teste — dados isolados
        service = new ValeService(new InMemoryValeRepository());
    }

    // ---- listarFuncionarios ----

    @Test
    @DisplayName("listarFuncionarios deve retornar lista não vazia")
    void listarFuncionarios_deveRetornarListaNaoVazia() {
        List<Funcionario> lista = service.listarFuncionarios();
        assertFalse(lista.isEmpty());
    }

    // ---- solicitarVale ----

    @Test
    @DisplayName("solicitarVale deve aprovar vale com percentual válido")
    void solicitarVale_deveAprovarComPercentualValido() {
        Vale vale = service.solicitarVale(1, 35.0, TipoVale.ADIANTAMENTO_MENSAL);

        assertNotNull(vale);
        assertEquals(StatusVale.APROVADO, vale.getStatus());
        assertEquals(35.0, vale.getPercentualSolicitado());
        assertEquals(TipoVale.ADIANTAMENTO_MENSAL, vale.getTipoVale());
    }

    @Test
    @DisplayName("solicitarVale deve calcular valor adiantado corretamente")
    void solicitarVale_deveCalcularValorAdiantadoCorreto() {
        // Funcionário ID 1 tem salário de 8500.00
        Vale vale = service.solicitarVale(1, 40.0, TipoVale.ADIANTAMENTO_MENSAL);
        assertEquals(3400.00, vale.getValorAdiantado(), 0.01);
    }

    @Test
    @DisplayName("solicitarVale deve lançar PercentualInvalidoException se percentual fora do tipo")
    void solicitarVale_deveLancarExcecaoPercentualInvalido() {
        assertThrows(PercentualInvalidoException.class,
                () -> service.solicitarVale(1, 50.0, TipoVale.ADIANTAMENTO_MENSAL));
    }

    @Test
    @DisplayName("solicitarVale deve lançar FuncionarioNaoEncontradoException para ID inexistente")
    void solicitarVale_deveLancarExcecaoFuncionarioNaoEncontrado() {
        assertThrows(FuncionarioNaoEncontradoException.class,
                () -> service.solicitarVale(99, 35.0, TipoVale.ADIANTAMENTO_MENSAL));
    }

    @Test
    @DisplayName("solicitarVale deve lançar FuncionarioInativoException para funcionário inativo")
    void solicitarVale_deveLancarExcecaoFuncionarioInativo() {
        assertThrows(FuncionarioInativoException.class,
                () -> service.solicitarVale(6, 35.0, TipoVale.ADIANTAMENTO_MENSAL));
    }

    @Test
    @DisplayName("solicitarVale deve lançar ValeDuplicadoException se já existe vale no mês")
    void solicitarVale_deveLancarExcecaoValeDuplicado() {
        service.solicitarVale(2, 30.0, TipoVale.ADIANTAMENTO_MENSAL);

        assertThrows(ValeDuplicadoException.class,
                () -> service.solicitarVale(2, 30.0, TipoVale.ADIANTAMENTO_MENSAL));
    }

    @Test
    @DisplayName("solicitarVale EMERGENCIAL deve aceitar percentual entre 10 e 20")
    void solicitarVale_emergencialDeveAceitarPercentualCorreto() {
        Vale vale = service.solicitarVale(3, 15.0, TipoVale.EMERGENCIAL);
        assertEquals(TipoVale.EMERGENCIAL, vale.getTipoVale());
        assertEquals(StatusVale.APROVADO, vale.getStatus());
    }

    // ---- cancelarVale ----

    @Test
    @DisplayName("cancelarVale deve cancelar vale aprovado com sucesso")
    void cancelarVale_deveCancelarValeAprovado() {
        Vale vale = service.solicitarVale(1, 35.0, TipoVale.ADIANTAMENTO_MENSAL);
        String mensagem = service.cancelarVale(vale.getId());

        assertNotNull(mensagem);
        assertTrue(mensagem.contains("cancelado com sucesso"));
        assertEquals(StatusVale.CANCELADO, vale.getStatus());
    }

    @Test
    @DisplayName("cancelarVale deve lançar ValeNaoEncontradoException para ID inexistente")
    void cancelarVale_deveLancarExcecaoValeNaoEncontrado() {
        assertThrows(ValeNaoEncontradoException.class,
                () -> service.cancelarVale(999));
    }

    @Test
    @DisplayName("cancelarVale deve lançar ValeCanceladoException ao cancelar duas vezes")
    void cancelarVale_deveLancarExcecaoValeCanceladoDuasVezes() {
        Vale vale = service.solicitarVale(1, 35.0, TipoVale.ADIANTAMENTO_MENSAL);
        service.cancelarVale(vale.getId());

        assertThrows(ValeCanceladoException.class,
                () -> service.cancelarVale(vale.getId()));
    }

    @Test
    @DisplayName("após cancelar vale, funcionário pode solicitar novo vale no mesmo mês")
    void cancelarVale_deveLiberarNovasolicitacaoNoMesmo() {
        Vale vale = service.solicitarVale(1, 35.0, TipoVale.ADIANTAMENTO_MENSAL);
        service.cancelarVale(vale.getId());

        Vale novoVale = service.solicitarVale(1, 30.0, TipoVale.ADIANTAMENTO_MENSAL);
        assertEquals(StatusVale.APROVADO, novoVale.getStatus());
    }

    // ---- listarValesPorFuncionario ----

    @Test
    @DisplayName("listarValesPorFuncionario deve retornar apenas vales do funcionário")
    void listarValesPorFuncionario_deveRetornarApenasDoFuncionario() {
        service.solicitarVale(1, 35.0, TipoVale.ADIANTAMENTO_MENSAL);
        service.solicitarVale(2, 30.0, TipoVale.ADIANTAMENTO_MENSAL);

        List<Vale> valesFuncionario1 = service.listarValesPorFuncionario(1);

        assertEquals(1, valesFuncionario1.size());
        assertTrue(valesFuncionario1.stream().allMatch(v -> v.getFuncionarioId() == 1));
    }

    // ---- Repositório in-memory para testes ----

    /**
     * Repositório isolado para testes — dados independentes a cada execução.
     */
    static class InMemoryValeRepository implements IValeRepository {

        private final List<Funcionario> funcionarios = new java.util.ArrayList<>(List.of(
                new Funcionario(1, "Ana Silva",       "Desenvolvedora Backend", 8500.00,  true),
                new Funcionario(2, "Carlos Mendes",   "Analista de QA",         5200.00,  true),
                new Funcionario(3, "Fernanda Rocha",  "Scrum Master",           9800.00,  true),
                new Funcionario(6, "Funcionario Inativo", "Cargo",              3000.00,  false)
        ));

        private final List<Vale> vales = new java.util.ArrayList<>();
        private int counter = 1;

        @Override
        public List<Funcionario> listarFuncionarios() { return new java.util.ArrayList<>(funcionarios); }

        @Override
        public Optional<Funcionario> buscarFuncionarioPorId(int id) {
            return funcionarios.stream().filter(f -> f.getId() == id).findFirst();
        }

        @Override
        public Vale salvarVale(Vale vale) {
            vale.setId(counter++);
            vales.add(vale);
            return vale;
        }

        @Override
        public Optional<Vale> buscarValePorId(int id) {
            return vales.stream().filter(v -> v.getId() == id).findFirst();
        }

        @Override
        public List<Vale> listarVales() { return new java.util.ArrayList<>(vales); }

        @Override
        public List<Vale> listarValesPorFuncionario(int funcionarioId) {
            return vales.stream()
                    .filter(v -> v.getFuncionarioId() == funcionarioId)
                    .collect(java.util.stream.Collectors.toList());
        }

        @Override
        public boolean possuiValeAtivoNoMes(int funcionarioId, String mesAno) {
            return vales.stream()
                    .anyMatch(v -> v.getFuncionarioId() == funcionarioId
                            && v.getDataSolicitacao().startsWith(mesAno)
                            && v.getStatus().isCancelavel());
        }
    }
}