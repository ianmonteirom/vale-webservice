package br.com.fiap.vale.publisher;

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
        System.out.println("  • solicitarVale(funcionarioId, percentualSolicitado)");
        System.out.println("  • cancelarVale(valeId)");
        System.out.println("\nRegras de negócio:");
        System.out.println("  • Percentual entre 30% e 40% do salário bruto");
        System.out.println("  • Apenas um vale ativo por funcionário por mês");
        System.out.println("  • Apenas funcionários ativos podem solicitar");
        System.out.println("\nPressione Ctrl+C para encerrar.\n");
    }
}