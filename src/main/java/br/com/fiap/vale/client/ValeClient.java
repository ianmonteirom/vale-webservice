package br.com.fiap.vale.client;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * Cliente SOAP que consome o ValeService.
 * Demonstra todas as operações disponíveis no WebService.
 *
 * Pré-requisito: ServicePublisher deve estar rodando em http://localhost:8080/vale
 */
public class ValeClient {

    private static final String ENDPOINT  = "http://localhost:8080/vale";
    private static final String NAMESPACE = "http://service.vale.fiap.com.br/";

    public static void main(String[] args) throws Exception {
        System.out.println("\n========================================");
        System.out.println("  Cliente SOAP — Vale WebService");
        System.out.println("========================================\n");

        // 1. Lista funcionários
        print("[1] Listando funcionários...");
        System.out.println(enviar(envelopeListarFuncionarios()));

        // 2. Solicita ADIANTAMENTO_MENSAL (35%) para Ana Silva (ID 1)
        print("[2] Solicitando ADIANTAMENTO_MENSAL 35% para Ana Silva (ID 1)...");
        System.out.println(enviar(envelopeSolicitarVale(1, 35.0, "ADIANTAMENTO_MENSAL")));

        // 3. Tenta segundo vale no mesmo mês → erro esperado
        print("[3] Segundo vale no mesmo mês (erro esperado)...");
        System.out.println(enviar(envelopeSolicitarVale(1, 30.0, "ADIANTAMENTO_MENSAL")));

        // 4. Lista vales do funcionário 1
        print("[4] Listando vales do funcionário ID 1...");
        System.out.println(enviar(envelopeListarValesPorFuncionario(1)));

        // 5. Lista todos os vales
        print("[5] Listando todos os vales...");
        System.out.println(enviar(envelopeListarVales()));

        // 6. Cancela vale ID 1
        print("[6] Cancelando vale ID 1...");
        System.out.println(enviar(envelopeCancelarVale(1)));

        // 7. Solicita EMERGENCIAL 15% para Carlos (ID 2)
        print("[7] Solicitando EMERGENCIAL 15% para Carlos Mendes (ID 2)...");
        System.out.println(enviar(envelopeSolicitarVale(2, 15.0, "EMERGENCIAL")));

        // 8. Tenta percentual inválido para EMERGENCIAL → erro esperado
        print("[8] EMERGENCIAL com percentual 50% (erro esperado)...");
        System.out.println(enviar(envelopeSolicitarVale(3, 50.0, "EMERGENCIAL")));

        // 9. Ana solicita novo vale após cancelamento
        print("[9] Ana solicita novo vale após cancelamento...");
        System.out.println(enviar(envelopeSolicitarVale(1, 30.0, "ADIANTAMENTO_MENSAL")));

        System.out.println("\n========================================");
        System.out.println("  Demonstração concluída!");
        System.out.println("========================================\n");
    }

    // ---- Envelopes SOAP ----

    private static String envelopeListarFuncionarios() {
        return envelope("<ser:listarFuncionarios/>");
    }

    private static String envelopeListarVales() {
        return envelope("<ser:listarVales/>");
    }

    private static String envelopeListarValesPorFuncionario(int funcionarioId) {
        return envelope("<ser:listarValesPorFuncionario>" +
                "<funcionarioId>" + funcionarioId + "</funcionarioId>" +
                "</ser:listarValesPorFuncionario>");
    }

    private static String envelopeSolicitarVale(int funcionarioId, double percentual, String tipo) {
        return envelope("<ser:solicitarVale>" +
                "<funcionarioId>" + funcionarioId + "</funcionarioId>" +
                "<percentualSolicitado>" + percentual + "</percentualSolicitado>" +
                "<tipoVale>" + tipo + "</tipoVale>" +
                "</ser:solicitarVale>");
    }

    private static String envelopeCancelarVale(int valeId) {
        return envelope("<ser:cancelarVale>" +
                "<valeId>" + valeId + "</valeId>" +
                "</ser:cancelarVale>");
    }

    private static String envelope(String body) {
        return "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
                "xmlns:ser=\"" + NAMESPACE + "\">" +
                "<soapenv:Header/><soapenv:Body>" + body +
                "</soapenv:Body></soapenv:Envelope>";
    }

    // ---- HTTP ----

    private static String enviar(String envelope) throws Exception {
        URL url = new URL(ENDPOINT);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(envelope.getBytes(StandardCharsets.UTF_8));
        }

        try (Scanner sc = new Scanner(conn.getInputStream(), StandardCharsets.UTF_8)) {
            return sc.useDelimiter("\\A").next();
        } catch (Exception e) {
            try (Scanner sc = new Scanner(conn.getErrorStream(), StandardCharsets.UTF_8)) {
                return "[SOAP FAULT] " + sc.useDelimiter("\\A").next();
            }
        }
    }

    private static void print(String msg) {
        System.out.println(">>> " + msg + "\n");
    }
}