package br.com.fiap.vale.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ValeCalculator")
class ValeCalculatorTest {

    @Test
    @DisplayName("deve calcular 35% de 8500 corretamente")
    void calcularValorAdiantado_deveCalcularCorretamente() {
        double resultado = ValeCalculator.calcularValorAdiantado(8500.00, 35.0);
        assertEquals(2975.00, resultado, 0.01);
    }

    @Test
    @DisplayName("deve calcular 40% de 5200 corretamente")
    void calcularValorAdiantado_deveCalcular40Porcento() {
        double resultado = ValeCalculator.calcularValorAdiantado(5200.00, 40.0);
        assertEquals(2080.00, resultado, 0.01);
    }

    @Test
    @DisplayName("deve arredondar resultado em 2 casas decimais")
    void calcularValorAdiantado_deveArredondar() {
        // 3333.33 * 30% = 999.999 → arredonda para 1000.00
        double resultado = ValeCalculator.calcularValorAdiantado(3333.33, 30.0);
        assertEquals(1000.00, resultado, 0.01);
    }

    @Test
    @DisplayName("getMesAnoAtual deve retornar formato yyyy-MM")
    void getMesAnoAtual_deveRetornarFormatoCorreto() {
        String mesAno = ValeCalculator.getMesAnoAtual();
        assertTrue(mesAno.matches("\\d{4}-\\d{2}"),
                "Formato esperado: yyyy-MM, recebido: " + mesAno);
    }

    @Test
    @DisplayName("getDataAtual deve retornar formato yyyy-MM-dd")
    void getDataAtual_deveRetornarFormatoCorreto() {
        String data = ValeCalculator.getDataAtual();
        assertTrue(data.matches("\\d{4}-\\d{2}-\\d{2}"),
                "Formato esperado: yyyy-MM-dd, recebido: " + data);
    }

    @Test
    @DisplayName("getMesAnoAtual deve ser prefixo de getDataAtual")
    void getMesAnoAtual_deveSerPrefixoDeGetDataAtual() {
        assertTrue(ValeCalculator.getDataAtual().startsWith(ValeCalculator.getMesAnoAtual()));
    }
}