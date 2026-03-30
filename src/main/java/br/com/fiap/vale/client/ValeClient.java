package br.com.fiap.vale.client;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * Cliente SOAP que consome o ValeService.
 *
 * Demonstra as 3 operações disponíveis no WebService:
 *   1. listarFuncionarios()
 *   2. solicitarVale(funcionarioId, percentualSolicitado)
 *   3. cancelarVale(valeId)
 *
 * Para rodar: certifique-se de que o ServicePublisher está ativo em
 * http://localhost:8080/vale antes de executar este cliente.
 */
public class ValeClient {

    private static final String ENDPOINT = "http://localhost:8080/vale";
    private static final String NAMESPACE = "http://service.vale.fiap.com.br/";

    public static void main(String[] args) throws Exception {
        System.out.println("\n========================================");
        System.out.println("  Cliente SOAP — Vale WebService");
        System.out.println("========================================\n");

        // 1. Lista todos os funcionários
        System.out.println(">>> [1] Listando funcionários...\n");
        String respostaLista = enviarRequisicao(montarEnvelopeListarFuncionarios());
        System.out.println(respostaLista);

        // 2. Solicita vale de 35% para a Ana Silva (ID 1)
        System.out.println(">>> [2] Solicitando vale de 35% para a Ana Silva (ID 1)...\n");
        String respostaSolicitar = enviarRequisicao(montarEnvelopeSolicitarVale(1, 35.0));
        System.out.println(respostaSolicitar);

        // 3. Tenta solicitar segundo vale no mesmo mês (deve retornar erro)
        System.out.println(">>> [3] Tentando solicitar segundo vale para Ana Silva no mesmo mês...\n");
        String respostaSegundoVale = enviarRequisicao(montarEnvelopeSolicitarVale(1, 30.0));
        System.out.println(respostaSegundoVale);

        // 4. Cancela o vale de ID 1
        System.out.println(">>> [4] Cancelando vale ID 1...\n");
        String respostaCancelar = enviarRequisicao(montarEnvelopeCancelarVale(1));
        System.out.println(respostaCancelar);

        // 5. Tenta solicitar com percentual inválido (deve retornar erro)
        System.out.println(">>> [5] Tentando solicitar vale com percentual inválido (50%)...\n");
        String respostaInvalida = enviarRequisicao(montarEnvelopeSolicitarVale(2, 50.0));
        System.out.println(respostaInvalida);

        System.out.println("\n========================================");
        System.out.println("  Demonstração concluída!");
        System.out.println("========================================\n");
    }

    // ---- Envelopes SOAP ----

    private static String montarEnvelopeListarFuncionarios() {
        return "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
                "xmlns:ser=\"" + NAMESPACE + "\">" +
                "<soapenv:Header/>" +
                "<soapenv:Body>" +
                "<ser:listarFuncionarios/>" +
                "</soapenv:Body>" +
                "</soapenv:Envelope>";
    }

    private static String montarEnvelopeSolicitarVale(int funcionarioId, double percentual) {
        return "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
                "xmlns:ser=\"" + NAMESPACE + "\">" +
                "<soapenv:Header/>" +
                "<soapenv:Body>" +
                "<ser:solicitarVale>" +
                "<funcionarioId>" + funcionarioId + "</funcionarioId>" +
                "<percentualSolicitado>" + percentual + "</percentualSolicitado>" +
                "</ser:solicitarVale>" +
                "</soapenv:Body>" +
                "</soapenv:Envelope>";
    }

    private static String montarEnvelopeCancelarVale(int valeId) {
        return "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
                "xmlns:ser=\"" + NAMESPACE + "\">" +
                "<soapenv:Header/>" +
                "<soapenv:Body>" +
                "<ser:cancelarVale>" +
                "<valeId>" + valeId + "</valeId>" +
                "</ser:cancelarVale>" +
                "</soapenv:Body>" +
                "</soapenv:Envelope>";
    }

    // ---- HTTP ----

    private static String enviarRequisicao(String envelope) throws Exception {
        URL url = new URL(ENDPOINT);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            os.write(envelope.getBytes(StandardCharsets.UTF_8));
        }

        try (Scanner scanner = new Scanner(connection.getInputStream(), StandardCharsets.UTF_8)) {
            return scanner.useDelimiter("\\A").next();
        } catch (Exception e) {
            // Lê o erro do servidor (fault SOAP)
            try (Scanner scanner = new Scanner(connection.getErrorStream(), StandardCharsets.UTF_8)) {
                return "[SOAP FAULT] " + scanner.useDelimiter("\\A").next();
            }
        }
    }
}