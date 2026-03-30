# 🧪 Guia de Testes — vale-webservice no Postman

## ⚙️ Configuração Base (igual para todas as requisições)

| Campo        | Valor                              |
|--------------|------------------------------------|
| Método       | `POST`                             |
| URL          | `http://localhost:8080/vale`       |
| Content-Type | `text/xml;charset=UTF-8`           |

**Aba Headers:**
```
Content-Type: text/xml;charset=UTF-8
```

**Aba Body → raw → XML**

---

## 📋 Operação 1 — listarFuncionarios()

```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns:ser="http://service.vale.fiap.com.br/">
    <soapenv:Header/>
    <soapenv:Body>
        <ser:listarFuncionarios/>
    </soapenv:Body>
</soapenv:Envelope>
```

---

## 📋 Operação 2 — listarVales()

```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns:ser="http://service.vale.fiap.com.br/">
    <soapenv:Header/>
    <soapenv:Body>
        <ser:listarVales/>
    </soapenv:Body>
</soapenv:Envelope>
```

---

## 📋 Operação 3 — listarValesPorFuncionario(funcionarioId)

```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns:ser="http://service.vale.fiap.com.br/">
    <soapenv:Header/>
    <soapenv:Body>
        <ser:listarValesPorFuncionario>
            <funcionarioId>1</funcionarioId>
        </ser:listarValesPorFuncionario>
    </soapenv:Body>
</soapenv:Envelope>
```

---

## 💰 Operação 4 — solicitarVale(funcionarioId, percentualSolicitado, tipoVale)

### Tipos disponíveis de vale

| TipoVale              | Percentual permitido |
|-----------------------|----------------------|
| `ADIANTAMENTO_MENSAL` | 30% a 40%            |
| `EMERGENCIAL`         | 10% a 20%            |
| `FERIAS`              | 30% a 50%            |
| `DECIMO_TERCEIRO`     | fixo 50%             |

### Exemplo — ADIANTAMENTO_MENSAL

```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns:ser="http://service.vale.fiap.com.br/">
    <soapenv:Header/>
    <soapenv:Body>
        <ser:solicitarVale>
            <funcionarioId>1</funcionarioId>
            <percentualSolicitado>35.0</percentualSolicitado>
            <tipoVale>ADIANTAMENTO_MENSAL</tipoVale>
        </ser:solicitarVale>
    </soapenv:Body>
</soapenv:Envelope>
```

### Exemplo — EMERGENCIAL

```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns:ser="http://service.vale.fiap.com.br/">
    <soapenv:Header/>
    <soapenv:Body>
        <ser:solicitarVale>
            <funcionarioId>2</funcionarioId>
            <percentualSolicitado>15.0</percentualSolicitado>
            <tipoVale>EMERGENCIAL</tipoVale>
        </ser:solicitarVale>
    </soapenv:Body>
</soapenv:Envelope>
```

### Exemplo — DECIMO_TERCEIRO

```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns:ser="http://service.vale.fiap.com.br/">
    <soapenv:Header/>
    <soapenv:Body>
        <ser:solicitarVale>
            <funcionarioId>3</funcionarioId>
            <percentualSolicitado>50.0</percentualSolicitado>
            <tipoVale>DECIMO_TERCEIRO</tipoVale>
        </ser:solicitarVale>
    </soapenv:Body>
</soapenv:Envelope>
```

**Resposta esperada:**
```xml
<ns2:solicitarValeResponse>
    <return>
        <funcionarioId>1</funcionarioId>
        <id>1</id>
        <nomeFuncionario>Ana Silva</nomeFuncionario>
        <percentualSolicitado>35.0</percentualSolicitado>
        <salarioBruto>8500.0</salarioBruto>
        <status>APROVADO</status>
        <tipoVale>ADIANTAMENTO_MENSAL</tipoVale>
        <valorAdiantado>2975.0</valorAdiantado>
        <dataSolicitacao>2026-03-30</dataSolicitacao>
    </return>
</ns2:solicitarValeResponse>
```

---

## ❌ Operação 5 — cancelarVale(valeId)

```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns:ser="http://service.vale.fiap.com.br/">
    <soapenv:Header/>
    <soapenv:Body>
        <ser:cancelarVale>
            <valeId>1</valeId>
        </ser:cancelarVale>
    </soapenv:Body>
</soapenv:Envelope>
```

**Resposta esperada:**
```xml
<ns2:cancelarValeResponse>
    <return>Vale ID 1 do funcionário 'Ana Silva' cancelado com sucesso. Valor de R$ 2975,00 não será descontado no próximo pagamento.</return>
</ns2:cancelarValeResponse>
```

---

## ⚠️ Cenários de erro esperados

### Percentual fora do intervalo
```xml
<ser:solicitarVale>
    <funcionarioId>1</funcionarioId>
    <percentualSolicitado>50.0</percentualSolicitado>
    <tipoVale>ADIANTAMENTO_MENSAL</tipoVale>
</ser:solicitarVale>
```
→ `PercentualInvalidoException`

### Segundo vale no mesmo mês
→ `ValeDuplicadoException`

### Funcionário inexistente
→ `FuncionarioNaoEncontradoException`

### Cancelar vale já cancelado
→ `ValeCanceladoException`

---

## 🔗 WSDL

```
http://localhost:8080/vale?wsdl
```