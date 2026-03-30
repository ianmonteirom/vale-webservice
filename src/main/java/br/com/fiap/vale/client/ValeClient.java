package br.com.fiap.vale.client;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Cliente SOAP que consome o ValeService.
 * Demonstra todas as operações disponíveis no WebService.
 *
 * Pre-requisito: ServicePublisher deve estar rodando em http://localhost:8080/vale
 */
public class ValeClient {

    private static final String ENDPOINT  = "http://localhost:8080/vale";
    private static final String NAMESPACE = "http://service.vale.fiap.com.br/";

    private static final Pattern FAULT_PATTERN =
            Pattern.compile("<faultstring(?:[^>]*)>(.*?)</faultstring>", Pattern.DOTALL);

    public static void main(String[] args) throws Exception {
        System.out.println("\n========================================");
        System.out.println("  Cliente SOAP - Vale WebService");
        System.out.println("========================================\n");

        print("[1] Listando funcionarios...");
        System.out.println(enviar(envelopeListarFuncionarios()));

        print("[2] Solicitando ADIANTAMENTO_MENSAL 35% para Ana Silva (ID 1)...");
        System.out.println(enviar(envelopeSolicitarVale(1, 35.0, "ADIANTAMENTO_MENSAL")));

        print("[3] Segundo vale no mesmo mes (erro esperado)...");
        System.out.println(enviar(envelopeSolicitarVale(1, 30.0, "ADIANTAMENTO_MENSAL")));

        print("[4] Listando vales do funcionario ID 1...");
        System.out.println(enviar(envelopeListarValesPorFuncionario(1)));

        print("[5] Listando todos os vales...");
        System.out.println(enviar(envelopeListarVales()));

        print("[6] Cancelando vale ID 1...");
        System.out.println(enviar(envelopeCancelarVale(1)));

        print("[7] Solicitando EMERGENCIAL 15% para Carlos Mendes (ID 2)...");
        System.out.println(enviar(envelopeSolicitarVale(2, 15.0, "EMERGENCIAL")));

        print("[8] EMERGENCIAL com percentual 50% (erro esperado)...");
        System.out.println(enviar(envelopeSolicitarVale(3, 50.0, "EMERGENCIAL")));

        print("[9] Ana solicita novo vale apos cancelamento...");
        System.out.println(enviar(envelopeSolicitarVale(1, 30.0, "ADIANTAMENTO_MENSAL")));

        print("[10] Funcionario ID 99 inexistente (erro esperado)...");
        System.out.println(enviar(envelopeSolicitarVale(99, 35.0, "ADIANTAMENTO_MENSAL")));

        System.out.println("\n========================================");
        System.out.println("  Demonstracao concluida!");
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

    // ---- HTTP + tratamento de erro ----

    private static String enviar(String envelope) throws Exception {
        URL url = URI.create(ENDPOINT).toURL(); // evita deprecation do new URL(String)
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(envelope.getBytes(StandardCharsets.UTF_8));
        }

        int statusCode = conn.getResponseCode();

        if (statusCode == 200) {
            try (Scanner sc = new Scanner(conn.getInputStream(), "UTF-8")) {
                return sc.useDelimiter("\\A").next();
            }
        } else {
            InputStream errorStream = conn.getErrorStream();
            if (errorStream == null) {
                return "[ERRO] HTTP " + statusCode + " sem detalhes.";
            }
            try (Scanner sc = new Scanner(errorStream, "UTF-8")) {
                String faultXml = sc.hasNext() ? sc.useDelimiter("\\A").next() : "";
                return "[ERRO] " + extrairFaultString(faultXml);
            }
        }
    }

    private static String extrairFaultString(String faultXml) {
        Matcher matcher = FAULT_PATTERN.matcher(faultXml);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return faultXml;
    }

    private static void print(String msg) {
        System.out.println(">>> " + msg + "\n");
    }
}