package br.com.fiap.vale.support;

import br.com.fiap.vale.model.*;
import br.com.fiap.vale.repository.IValeRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Centraliza fixtures e stubs reutilizados nos testes.
 *
 * Elimina a duplicação de repositórios in-memory espalhados
 * em ValeServiceTest, ValeValidatorTest e FuncionarioValidatorTest.
 */
public final class TestFixtures {

    private TestFixtures() {}

    // ---- Funcionários de teste ----

    public static Funcionario funcionarioAtivo() {
        return new Funcionario(1, "Ana Silva", "Desenvolvedora Backend", 8500.00, true);
    }

    public static Funcionario funcionarioAtivo2() {
        return new Funcionario(2, "Carlos Mendes", "Analista de QA", 5200.00, true);
    }

    public static Funcionario funcionarioInativo() {
        return new Funcionario(6, "Funcionário Inativo", "Cargo", 3000.00, false);
    }

    // ---- Vales de teste ----

    public static Vale valeAprovado() {
        return new Vale(1, 1, "Ana Silva", 8500.00, 35.0, 2975.00,
                TipoVale.ADIANTAMENTO_MENSAL, LocalDate.now().toString(), StatusVale.APROVADO);
    }

    public static Vale valeCancelado() {
        return new Vale(2, 1, "Ana Silva", 8500.00, 35.0, 2975.00,
                TipoVale.ADIANTAMENTO_MENSAL, LocalDate.now().toString(), StatusVale.CANCELADO);
    }

    // ---- Repositório in-memory para testes de integração ----

    /**
     * Repositório completo com dados pré-carregados.
     * Usado em ValeServiceTest para testes de integração reais.
     */
    public static class InMemoryValeRepository implements IValeRepository {

        private final List<Funcionario> funcionarios = new ArrayList<>(java.util.Arrays.asList(
                funcionarioAtivo(),
                funcionarioAtivo2(),
                new Funcionario(3, "Fernanda Rocha",  "Scrum Master",    9800.00, true),
                funcionarioInativo()
        ));

        private final List<Vale> vales = new ArrayList<>();
        private final AtomicInteger counter = new AtomicInteger(1);

        @Override
        public List<Funcionario> listarFuncionarios() { return new ArrayList<>(funcionarios); }

        @Override
        public Optional<Funcionario> buscarFuncionarioPorId(int id) {
            return funcionarios.stream().filter(f -> f.getId() == id).findFirst();
        }

        @Override
        public Vale salvarVale(Vale vale) {
            vale.setId(counter.getAndIncrement());
            vales.add(vale);
            return vale;
        }

        @Override
        public Optional<Vale> buscarValePorId(int id) {
            return vales.stream().filter(v -> v.getId() == id).findFirst();
        }

        @Override
        public List<Vale> listarVales() { return new ArrayList<>(vales); }

        @Override
        public List<Vale> listarValesPorFuncionario(int funcionarioId) {
            return vales.stream()
                    .filter(v -> v.getFuncionarioId() == funcionarioId)
                    .collect(Collectors.toList());
        }

        @Override
        public boolean possuiValeAtivoNoMes(int funcionarioId, String mesAno) {
            return vales.stream()
                    .anyMatch(v -> v.getFuncionarioId() == funcionarioId
                            && v.getDataSolicitacao().startsWith(mesAno)
                            && v.getStatus().isCancelavel());
        }
    }

    // ---- Stub configurável para testes unitários isolados ----

    /**
     * Stub simples com comportamento configurável via setters.
     * Usado em ValeValidatorTest e FuncionarioValidatorTest.
     */
    public static class StubRepository implements IValeRepository {

        private Optional<Funcionario> funcionario = Optional.empty();
        private Optional<Vale> vale = Optional.empty();
        private boolean temValeAtivo = false;

        public void setFuncionario(Optional<Funcionario> funcionario) { this.funcionario = funcionario; }
        public void setVale(Optional<Vale> vale) { this.vale = vale; }
        public void setTemValeAtivo(boolean temValeAtivo) { this.temValeAtivo = temValeAtivo; }

        @Override public Optional<Funcionario> buscarFuncionarioPorId(int id) { return funcionario; }
        @Override public Optional<Vale> buscarValePorId(int id) { return vale; }
        @Override public boolean possuiValeAtivoNoMes(int f, String m) { return temValeAtivo; }
        @Override public List<Funcionario> listarFuncionarios() { return new ArrayList<>(); }
        @Override public Vale salvarVale(Vale v) { return v; }
        @Override public List<Vale> listarVales() { return new ArrayList<>(); }
        @Override public List<Vale> listarValesPorFuncionario(int id) { return new ArrayList<>(); }
    }
}