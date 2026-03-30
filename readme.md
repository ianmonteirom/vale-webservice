# 💰 Vale WebService — Adiantamento Salarial SOAP

<p align="center">
  <img src="./logo.png" alt="Vale WebService Logo" width="300"/>
</p>

**CP1 - SOA | Turma: 3ESPX**

**Desenvolvido por:**
- Carlos Henrique — RM558003
- Mauricio Alves — RM556214
- Ian Monteiro — RM558652
- Bruno Silva — RM550416
- João Hoffmann — RM550763

---


## 📌 Contexto de Implantação

O **Vale WebService** é um serviço SOAP desenvolvido para automatizar e centralizar o processo de solicitação de adiantamento salarial (vale) em empresas. Atualmente, muitas organizações ainda gerenciam esse processo de forma manual — via e-mail, planilhas ou aprovação verbal — o que gera inconsistências, retrabalho e falta de rastreabilidade.

Este serviço oferece uma API SOAP padronizada que pode ser consumida por sistemas de RH, portais do colaborador e ERPs corporativos, garantindo integração segura e contratual via WSDL.

---

## ❗ Problemas que o sistema resolve

| Problema | Como o sistema resolve |
|---|---|
| Solicitações manuais sem controle | Registro centralizado de todas as solicitações |
| Funcionário solicita mais de um vale no mês | Validação automática: apenas 1 vale ativo por mês |
| Percentual fora do permitido | Validação por `TipoVale` com intervalos específicos por categoria |
| Funcionários inativos solicitando vale | Validação do status antes de aprovar |
| Falta de rastreabilidade dos cancelamentos | `StatusVale` com ciclo de vida: PENDENTE → APROVADO → CANCELADO |
| Strings mágicas espalhadas no código | Centralização em `Constantes.java` |
| Lógica de cálculo misturada ao service | Isolada em `ValeCalculator.java` |

---

## 🧱 Estrutura do Projeto

```
vale-webservice/
│
├── pom.xml
├── README.md
├── POSTMAN_GUIDE.md
│
└── src/
    ├── main/java/br/com/fiap/vale/
    │   │
    │   ├── client/
    │   │   └── ValeClient.java           → Consome o serviço via HTTP/SOAP
    │   │
    │   ├── exception/
    │   │   ├── ValeException.java               → Exceção base da aplicação
    │   │   ├── FuncionarioNaoEncontradoException.java
    │   │   ├── FuncionarioInativoException.java
    │   │   ├── PercentualInvalidoException.java
    │   │   ├── ValeCanceladoException.java
    │   │   ├── ValeDuplicadoException.java
    │   │   └── ValeNaoEncontradoException.java
    │   │
    │   ├── model/
    │   │   ├── Funcionario.java          → Dados do colaborador (@XmlRootElement)
    │   │   ├── Vale.java                 → Solicitação de adiantamento (@XmlRootElement)
    │   │   ├── TipoVale.java             → Enum: ADIANTAMENTO_MENSAL, EMERGENCIAL, FERIAS, DECIMO_TERCEIRO
    │   │   └── StatusVale.java           → Enum: PENDENTE, APROVADO, CANCELADO
    │   │
    │   ├── publisher/
    │   │   └── ServicePublisher.java     → Publica o endpoint SOAP
    │   │
    │   ├── repository/
    │   │   ├── IValeRepository.java      → Contrato de persistência
    │   │   └── ValeRepository.java       → Implementação em memória
    │   │
    │   ├── service/
    │   │   ├── IValeService.java         → Contrato das operações de negócio
    │   │   └── ValeService.java          → Implementação @WebService
    │   │
    │   ├── util/
    │   │   ├── Constantes.java           → Centraliza strings e mensagens de log
    │   │   └── ValeCalculator.java       → Cálculo de valor adiantado e datas
    │   │
    │   └── validation/
    │       ├── FuncionarioValidator.java → Valida existência e status ativo
    │       └── ValeValidator.java        → Valida percentual, duplicidade e cancelamento
    │
    └── test/java/br/com/fiap/vale/
        ├── service/
        │   └── ValeServiceTest.java            → 11 testes de integração do service
        ├── support/
        │   └── TestFixtures.java               → Fixtures e stubs compartilhados
        ├── util/
        │   └── ValeCalculatorTest.java         → 6 testes de cálculo e formatação
        └── validation/
            ├── FuncionarioValidatorTest.java   → 6 testes do validator de funcionários
            └── ValeValidatorTest.java          → 10 testes isolados com stub
```

---

## ⚙️ Dependências Maven

| Dependência | Função |
|---|---|
| `jaxws-rt` | Criar WebServices SOAP, gerar WSDL e publicar endpoints |
| `jaxb-api` | Converter objetos Java em XML (serialização) |
| `jaxb-runtime` | Implementação do JAXB |
| `jakarta.activation` | Suporte a tipos MIME no transporte SOAP |
| `junit-jupiter` | Testes unitários com JUnit 5 |

---

## 🔧 Como executar

### Pré-requisitos
- Java 21
- Maven

---

### Passo 1 — Compilar o projeto
```bash
mvn clean install -DskipTests
```

---

### Passo 2 — Rodar os testes
```bash
mvn test
```
Resultado esperado: `Tests run: 36, Failures: 0, Errors: 0, Skipped: 0`

```bash
# Rodar uma classe específica
mvn test -Dtest=ValeServiceTest
mvn test -Dtest=ValeValidatorTest
mvn test -Dtest=FuncionarioValidatorTest
mvn test -Dtest=ValeCalculatorTest

# Rodar um método específico
mvn test -Dtest=ValeServiceTest#solicitarVale_deveAprovarComPercentualValido
```

---

### Passo 3 — Subir o servidor SOAP (Terminal 1)
```bash
mvn exec:java
```

Aguarde a mensagem de confirmação:
```
========================================
✅ Vale WebService SOAP rodando!
Endpoint:  http://localhost:8080/vale
WSDL:      http://localhost:8080/vale?wsdl
========================================
```

> ⚠️ **Mantenha este terminal aberto.** O servidor precisa continuar rodando para os próximos passos.

---

### Passo 4 — Verificar o WSDL no navegador
Abra no navegador:
```
http://localhost:8080/vale?wsdl
```
Se exibir um XML com o contrato do serviço, o servidor está no ar corretamente.

---

### Passo 5 — Rodar o cliente Java (Terminal 2)
Abra um **segundo terminal** (sem fechar o primeiro) e execute:

**No PowerShell:**
```bash
mvn exec:java "-Dexec.mainClass=br.com.fiap.vale.client.ValeClient"
```

**Ou pelo IntelliJ:** clique com botão direito no `main` do `ValeClient.java` → **Run 'ValeClient.main()'**

O cliente executa automaticamente 10 cenários de teste, incluindo sucessos e erros esperados.

---

### Passo 6 — Testar manualmente no Postman
Com o servidor ainda rodando, use os envelopes SOAP descritos no **POSTMAN_GUIDE.md**.

Configuração base para todas as requisições:

| Campo | Valor |
|---|---|
| Método | `POST` |
| URL | `http://localhost:8080/vale` |
| Header `Content-Type` | `text/xml;charset=UTF-8` |
| Body | `raw` → `XML` |

---

### Para encerrar o servidor
No Terminal 1, pressione `Ctrl+C`.



---

## 🗂️ Regras de Negócio

- ✅ O percentual solicitado deve respeitar os limites definidos pelo **TipoVale**
- ✅ Apenas **um vale ativo** (APROVADO ou PENDENTE) por funcionário por mês
- ✅ Apenas **funcionários ativos** podem solicitar vale
- ✅ O cancelamento libera nova solicitação no mesmo mês
- ✅ O valor adiantado é calculado por `ValeCalculator`: `salário × (percentual / 100)`
- ✅ Cada tipo de vale possui intervalo de percentual próprio

### Tipos de Vale disponíveis

| TipoVale | Descrição | Percentual permitido |
|---|---|---|
| `ADIANTAMENTO_MENSAL` | Adiantamento padrão entre 15º e 20º dia | 30% a 40% |
| `EMERGENCIAL` | Situações excepcionais (saúde, família) | 10% a 20% |
| `FERIAS` | Antecipação antes das férias | 30% a 50% |
| `DECIMO_TERCEIRO` | Primeira parcela do 13º salário | fixo 50% |

### Ciclo de vida do Vale

```
PENDENTE → APROVADO → CANCELADO
```

---

## 🧪 Operações do WebService

| # | Operação | Descrição |
|---|---|---|
| 1 | `listarFuncionarios()` | Lista todos os funcionários cadastrados |
| 2 | `listarVales()` | Lista todos os vales registrados |
| 3 | `listarValesPorFuncionario(funcionarioId)` | Histórico de vales de um funcionário |
| 4 | `solicitarVale(funcionarioId, percentual, tipoVale)` | Solicita adiantamento salarial |
| 5 | `cancelarVale(valeId)` | Cancela um vale ativo |

### Endpoint
```
POST http://localhost:8080/vale
Content-Type: text/xml;charset=UTF-8
```

> Os envelopes SOAP de cada operação estão detalhados no arquivo **POSTMAN_GUIDE.md**

---

## ✅ Boas Práticas Aplicadas

| Prática | Descrição |
|---|---|
| **Arquitetura em camadas** | client, exception, model, publisher, repository, service, util, validation |
| **Interfaces desacopladas** | `IValeService` e `IValeRepository` permitem trocar implementações sem impacto |
| **Injeção de dependência** | `ValeService` recebe repositório via construtor — facilita testes com mocks |
| **Enums tipados** | `TipoVale` e `StatusVale` com `@XmlEnum` para serialização JAXB correta |
| **Validações isoladas** | `FuncionarioValidator` e `ValeValidator` mantêm o service limpo |
| **Exceções específicas** | 6 exceções com hierarquia — cada cenário tem sua própria classe |
| **Cálculo isolado** | `ValeCalculator` centraliza lógica financeira e formatação de datas |
| **Constantes centralizadas** | `Constantes.java` elimina strings e mensagens espalhadas no código |
| **Logging consistente** | `Logger` em service e validators com mensagens padronizadas via `Constantes` |
| **Anotações JAX-WS** | `@WebService`, `@WebMethod`, `@WebParam` aplicadas corretamente |
| **Anotações JAXB** | `@XmlRootElement` nos models, `@XmlEnum`/`@XmlType` nos enums |
| **`@XmlAccessorType`** | Evita dupla serialização de campos pelo JAXB em todos os models |
| **`toString()` nos models** | `Funcionario` e `Vale` com representação legível para logs e debug |
| **Repositório por instância** | `ValeRepository` sem `static` — sem compartilhamento de estado entre testes |
| **`TestFixtures` centralizado** | Stubs e fixtures reutilizados em todos os testes — sem duplicação |
| **Testes unitários** | 33 testes cobrindo service, validators e calculator com JUnit 5 |
| **Cliente independente** | `ValeClient` consome via HTTP puro, sem dependência do servidor |

---

## 🔄 Fluxo do WebService

```
Cliente (Postman / ValeClient)
            ↓
    SOAP Request XML
            ↓
      ValeService.java
            ↓
    ┌───────────────────┐
    │   ValeValidator   │ ← valida percentual, duplicidade, cancelamento
    │ FuncValidator     │ ← valida existência e status ativo
    └───────────────────┘
            ↓
    ValeCalculator.java  ← calcula valor adiantado
            ↓
    ValeRepository.java  ← persiste em memória
            ↓
  Objeto Java → JAXB → XML
            ↓
    SOAP Response XML
            ↓
         Cliente
```

---

## 🚀 Próximas Features

- [ ] **Persistência real** com banco de dados (PostgreSQL / H2)
- [ ] **Autenticação** via WS-Security para proteção do endpoint
- [ ] **Histórico com filtro por período** — listarValesPorMes(funcionarioId, mesAno)
- [ ] **Notificação por e-mail** ao aprovar ou cancelar um vale
- [ ] **Integração com folha de pagamento** para desconto automático no mês seguinte
- [ ] **Limite configurável por empresa** — percentual mínimo e máximo personalizável
- [ ] **Dashboard de aprovações** com relatório mensal por departamento

---

## 👨‍💻 Tecnologias

- Java 21
- JAX-WS 2.3.7
- JAXB 2.3.1
- JUnit Jupiter 5.10.0
- Maven
- Postman (testes manuais)