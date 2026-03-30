package br.com.fiap.vale.repository;

import br.com.fiap.vale.model.Funcionario;
import br.com.fiap.vale.model.Vale;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Implementação em memória do IValeRepository.
 * Pode ser substituída por uma implementação JPA/JDBC sem alterar nenhuma outra camada.
 */
public class ValeRepository implements IValeRepository {

    private static final List<Funcionario> funcionarios = new ArrayList<>();
    private static final List<Vale> vales = new ArrayList<>();
    private static final AtomicInteger valeIdCounter = new AtomicInteger(1);

    // Pré-popula funcionários com salários reais de mercado
    static {
        funcionarios.add(new Funcionario(1, "Ana Silva",       "Desenvolvedora Backend", 8500.00, true));
        funcionarios.add(new Funcionario(2, "Carlos Mendes",   "Analista de QA",         5200.00, true));
        funcionarios.add(new Funcionario(3, "Fernanda Rocha",  "Scrum Master",           9800.00, true));
        funcionarios.add(new Funcionario(4, "Rafael Oliveira", "Designer UX/UI",         6300.00, true));
        funcionarios.add(new Funcionario(5, "Juliana Costa",   "DevOps Engineer",        11000.00, true));
    }

    // ---- Funcionários ----

    @Override
    public List<Funcionario> listarFuncionarios() {
        return new ArrayList<>(funcionarios);
    }

    @Override
    public Optional<Funcionario> buscarFuncionarioPorId(int id) {
        return funcionarios.stream()
                .filter(f -> f.getId() == id)
                .findFirst();
    }

    // ---- Vales ----

    @Override
    public Vale salvarVale(Vale vale) {
        vale.setId(valeIdCounter.getAndIncrement());
        vales.add(vale);
        return vale;
    }

    @Override
    public Optional<Vale> buscarValePorId(int id) {
        return vales.stream()
                .filter(v -> v.getId() == id)
                .findFirst();
    }

    @Override
    public List<Vale> listarVales() {
        return new ArrayList<>(vales);
    }

    /**
     * Verifica se o funcionário já possui um vale APROVADO ou PENDENTE no mês corrente.
     */
    @Override
    public boolean possuiValeAtivoNoMes(int funcionarioId, String mesAno) {
        return vales.stream()
                .anyMatch(v -> v.getFuncionarioId() == funcionarioId
                        && v.getDatasolicitacao().startsWith(mesAno)
                        && (v.getStatus().equals("APROVADO") || v.getStatus().equals("PENDENTE")));
    }
}