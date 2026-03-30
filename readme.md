# 💰 Vale WebService — Adiantamento Salarial SOAP

## 📌 Contexto de Implantação

O **Vale WebService** é um serviço SOAP desenvolvido para automatizar e centralizar o processo de solicitação de adiantamento salarial (vale) em empresas. Atualmente, muitas organizações ainda gerenciam esse processo de forma manual — via e-mail, planilhas ou aprovação verbal — o que gera inconsistências, retrabalho e falta de rastreabilidade.

Este serviço oferece uma API SOAP padronizada que pode ser consumida por sistemas de RH, portais do colaborador e ERPs corporativos, garantindo integração segura e contratual via WSDL.

---

## ❗ Problemas que o sistema resolve

| Problema | Como o sistema resolve |
|---|---|
| Solicitações manuais sem controle | Registro centralizado de todas as solicitações |
| Funcionário solicita mais de um vale no mês | Validação automática: apenas 1 vale ativo por mês |
| Percentual fora do permitido pela CLT | Validação do percentual entre 30% e 40% do salário bruto |
| Funcionários inativos solicitando vale | Validação do status do funcionário antes de aprovar |
| Falta de rastreabilidade dos cancelamentos | Status do vale atualizado (APROVADO, CANCELADO) com histórico |

---

## 🧱 Estrutura do Projeto

```
vale-webservice/
│
├── pom.xml
│
└── src/main/java/br/com/fiap/vale/
    │
    ├── model/
    │   ├── Funcionario.java       → Dados do colaborador (@XmlRootElement)
    │   └── Vale.java              → Solicitação de adiantamento (@XmlRootElement)
    │
    ├── repository/
    │   └── ValeRepository.java    → Persistência em memória
    │
    ├── service/
    │   └── ValeService.java       → Operações SOAP (@WebService)
    │
    ├── publisher/
    │   └── ServicePublisher.java  → Publicação do endpoint
    │
    └── client/
        └── ValeClient.java        → Cliente Java que consome o serviço
```

---

## ⚙️ Dependências Maven

| Dependência | Função |
|---|---|
| `jaxws-rt` | Criar WebServices SOAP, gerar WSDL e publicar endpoints |
| `jaxb-api` | Converter objetos Java em XML (serialização) |
| `jaxb-runtime` | Implementação do JAXB |
| `jakarta.activation` | Suporte a tipos MIME no transporte SOAP |

---

## 🔧 Como executar

### Pré-requisitos
- Java 21
- Maven

### 1. Compilar o projeto
```bash
mvn clean install -DskipTests
```

### 2. Subir o servidor SOAP
```bash
mvn exec:java
```

O serviço estará disponível em:
```
http://localhost:8080/vale
http://localhost:8080/vale?wsdl
```

### 3. Rodar o cliente Java
Com o servidor rodando, abra outro terminal e execute:
```bash
mvn exec:java -Dexec.mainClass="br.com.fiap.vale.client.ValeClient"
```

---

## 🗂️ Regras de Negócio

- ✅ O percentual solicitado deve ser entre **30% e 40%** do salário bruto
- ✅ Apenas **um vale ativo** (APROVADO ou PENDENTE) por funcionário por mês
- ✅ Apenas **funcionários ativos** podem solicitar vale
- ✅ O cancelamento libera nova solicitação no mesmo mês
- ✅ O valor adiantado é calculado automaticamente: `salário × (percentual / 100)`

---

## 🧪 Operações do WebService

### Endpoint
```
POST http://localhost:8080/vale
Content-Type: text/xml;charset=UTF-8
```

---

### 1. listarFuncionarios()
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

### 2. solicitarVale(funcionarioId, percentualSolicitado)
```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns:ser="http://service.vale.fiap.com.br/">
    <soapenv:Header/>
    <soapenv:Body>
        <ser:solicitarVale>
            <funcionarioId>1</funcionarioId>
            <percentualSolicitado>35.0</percentualSolicitado>
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
        <valorAdiantado>2975.0</valorAdiantado>
    </return>
</ns2:solicitarValeResponse>
```

---

### 3. cancelarVale(valeId)
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

---

## ✅ Boas Práticas Aplicadas

| Prática | Descrição |
|---|---|
| **Separação de camadas** | Model, Repository, Service, Publisher e Client em pacotes distintos |
| **Anotações JAX-WS** | `@WebService`, `@WebMethod`, `@WebParam` aplicadas corretamente |
| **Anotações JAXB** | `@XmlRootElement` em todos os modelos trafegados no XML |
| **Validações de negócio no service** | Percentual, funcionário ativo e limite mensal validados antes de aprovar |
| **Repositório desacoplado** | A camada de dados é separada da lógica de negócio |
| **Cliente independente** | `ValeClient` consome o serviço via HTTP puro, sem dependência do servidor |
| **Mensagens de erro descritivas** | Cada validação retorna uma mensagem clara ao consumidor |

---

## 🚀 Próximas Features

- [ ] **Persistência real** com banco de dados (PostgreSQL / H2)
- [ ] **Autenticação** via WS-Security para proteção do endpoint
- [ ] **Histórico de vales** por funcionário com filtro por período
- [ ] **Notificação por e-mail** ao aprovar ou cancelar um vale
- [ ] **Integração com folha de pagamento** para desconto automático no mês seguinte
- [ ] **Limite configurável por empresa** (percentual mínimo e máximo personalizável)
- [ ] **Dashboard de aprovações** com relatório mensal por departamento

---

## 🔄 Fluxo do WebService

```
Cliente (Postman / ValeClient)
        ↓
  SOAP Request XML
        ↓
   ValeService.java
        ↓
  Validações de negócio
        ↓
  ValeRepository (memória)
        ↓
  Objeto Java → JAXB → XML
        ↓
  SOAP Response XML
        ↓
     Cliente
```

---

## 👨‍💻 Tecnologias

- Java 21
- JAX-WS 2.3.7
- JAXB 2.3.1
- Maven
- Postman (testes manuais)