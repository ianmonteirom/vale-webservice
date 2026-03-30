package br.com.fiap.vale.service;

import br.com.fiap.vale.model.Funcionario;
import br.com.fiap.vale.model.TipoVale;
import br.com.fiap.vale.model.Vale;

import java.util.List;

public interface IValeService {

    List<Funcionario> listarFuncionarios();

    List<Vale> listarVales();

    List<Vale> listarValesPorFuncionario(int funcionarioId);

    Vale solicitarVale(int funcionarioId, double percentualSolicitado, TipoVale tipoVale);

    String cancelarVale(int valeId);
}