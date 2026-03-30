package br.com.fiap.vale.repository;

import br.com.fiap.vale.model.Funcionario;
import br.com.fiap.vale.model.Vale;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ValeRepository implements IValeRepository {

    private final List<Funcionario> funcionarios = new ArrayList<>();
    private final List<Vale> vales = new ArrayList<>();
    private final AtomicInteger valeIdCounter = new AtomicInteger(1);

    public ValeRepository() {
        funcionarios.add(new Funcionario(1, "Ana Silva",       "Desenvolvedora Backend", 8500.00,  true));
        funcionarios.add(new Funcionario(2, "Carlos Mendes",   "Analista de QA",         5200.00,  true));
        funcionarios.add(new Funcionario(3, "Fernanda Rocha",  "Scrum Master",           9800.00,  true));
        funcionarios.add(new Funcionario(4, "Rafael Oliveira", "Designer UX/UI",         6300.00,  true));
        funcionarios.add(new Funcionario(5, "Juliana Costa",   "DevOps Engineer",        11000.00, true));
    }

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