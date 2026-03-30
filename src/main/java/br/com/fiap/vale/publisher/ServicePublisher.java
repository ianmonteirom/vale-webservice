package br.com.fiap.vale.publisher;

import br.com.fiap.vale.model.TipoVale;
import br.com.fiap.vale.service.ValeService;

import javax.xml.ws.Endpoint;

public class ServicePublisher {

    public static void main(String[] args) {
        String url = "http://localhost:8080/vale";

        Endpoint.publish(url, new ValeService());

        System.out.println("\n========================================");
        System.out.println("✅ Vale WebService SOAP rodando!");
        System.out.println("Endpoint:  " + url);
        System.out.println("WSDL:      " + url + "?wsdl");
        System.out.println("========================================\n");

        System.out.println("Operações disponíveis:");
        System.out.println("  • listarFuncionarios()");
        System.out.println("  • listarVales()");
        System.out.println("  • listarValesPorFuncionario(funcionarioId)");
        System.out.println("  • solicitarVale(funcionarioId, percentualSolicitado, tipoVale)");
        System.out.println("  • cancelarVale(valeId)");

        System.out.println("\nTipos de vale disponíveis:");
        for (TipoVale tipo : TipoVale.values()) {
            System.out.println("  • " + tipo.name() + " → " + tipo.getResumo());
        }

        System.out.println("\nRegras de negócio:");
        System.out.println("  • Apenas 1 vale ativo por funcionário por mês");
        System.out.println("  • Apenas funcionários ativos podem solicitar");
        System.out.println("  • Cancelamento libera nova solicitação no mesmo mês");
        System.out.println("\nPressione Ctrl+C para encerrar.\n");
    }
}