# Notification Patterns — Strategy + Registry em Java

Projeto de estudo criado para compreender, implementar e testar **Strategy Pattern** e um **Registry de implementações** utilizando Java puro, antes de introduzir frameworks como Spring Boot.

A motivação surgiu de uma refatoração em outro projeto: um agente de IA propôs uma arquitetura com Strategy e Registry, mas eu queria entender as decisões por trás do código. Para isso, reconstruí os conceitos em um exemplo menor, escrevendo as implementações e os testes na prática.

## Objetivo

Simular o envio de notificações por canais com regras distintas, sem concentrar as validações em uma única classe ou espalhar condicionais pelo fluxo de seleção.

Atualmente, há dois canais:

- **E-mail:** exige destinatário preenchido e, em uma validação simplificada para estudo, contendo `@`.
- **SMS:** exige destinatário preenchido com exatamente 11 dígitos numéricos (regra didática, não uma validação completa de telefone).

Ambos exigem mensagem não vazia. **Nenhuma notificação real é enviada:** o resultado é exibido no console.

## Tecnologias

- Java 21
- Maven
- JUnit Jupiter 5.14.3
- Maven Surefire Plugin 3.6.1

Não utiliza Spring Boot nesta etapa.

## Arquitetura

```text
                   NotificationSender (interface)
                     channel() | send(...)
                              ^
                      implementada por
                         /          \
        EmailNotificationSender    SmsNotificationSender
                         \          /
                         objetos registrados
                                |
                   NotificationSenderRegistry
                     Map<String, NotificationSender>
                                |
                        getSender("sms")
                                |
                    SmsNotificationSender.send(...)
```

**`NotificationSender`** define o contrato comum. Cada implementação contém as regras de seu canal.

**`NotificationSenderRegistry`** recebe uma `List<NotificationSender>` no construtor, consulta `channel()` para registrar cada implementação em um mapa e recupera o objeto correspondente com `getSender(channel)`.

Durante a construção, o Registry:

1. Usa `putIfAbsent()` para detectar identificadores duplicados e lança `IllegalStateException` se houver conflito.
2. Usa `Map.copyOf()` para manter uma cópia não modificável dos registros.
3. Ao receber um canal não registrado em `getSender()`, lança `IllegalArgumentException`.

**`MessageTemplateUtil`** concentra a saída comum das notificações no console. É apenas uma ajuda de apresentação, não um serviço de envio externo.

### Exemplo de uso

```java
NotificationSender email = new EmailNotificationSender();
NotificationSender sms = new SmsNotificationSender();

NotificationSenderRegistry registry = new NotificationSenderRegistry(
    List.of(email, sms)
);

NotificationSender selected = registry.getSender("sms");
selected.send("11987654321", "Seu código é 123456");
```

> Imports e declaração de `package` foram omitidos para destacar o fluxo. A montagem da lista é manual nesta versão; a descoberta automática de implementações pelo Spring é um próximo passo de estudo.

## Organização do código

Estrutura Maven convencional sugerida para o repositório:

```text
notification-patterns/
├── pom.xml
└── src/
    ├── main/java/com/joilson/notification/
    │   ├── Main.java
    │   ├── NotificationSender.java
    │   ├── EmailNotificationSender.java
    │   ├── SmsNotificationSender.java
    │   ├── NotificationSenderRegistry.java
    │   └── MessageTemplateUtil.java
    └── test/java/com/joilson/notification/
        └── NotificationSenderRegistryTest.java
```

As pastas e as declarações `package` das classes devem corresponder à estrutura real do projeto. `src/main/java` e `src/test/java` são raízes de código-fonte, não partes do nome do pacote.

## Como executar

**Pré-requisitos:** JDK 21 e Maven 3 instalado ou acessível pela integração Maven do IntelliJ IDEA.

No terminal, a partir da pasta que contém o `pom.xml`:

```bash
mvn clean test
```

Para compilar apenas o código principal:

```bash
mvn clean compile
```

Se o comando `mvn` não estiver configurado no PATH do Windows, utilize o painel **Maven** do IntelliJ IDEA e execute **Lifecycle → test** (ou **compile**).

Para executar a classe `Main`, use a opção **Run** da IDE. O comando `mvn test` executa os testes, não o `main()`.

## Testes automatizados

A classe `NotificationSenderRegistryTest` contém três testes:

| Teste | Comportamento verificado |
| --- | --- |
| `shouldRejectDuplicateChannels()` | Impede duas Strategies com o mesmo identificador (`assertThrows`) |
| `shouldReturnRegisteredSender()` | Retorna a mesma instância registrada (`assertSame`) |
| `shouldRejectUnsupportedChannel()` | Rejeita a consulta a um canal ausente (`assertThrows`) |

Resultado registrado na execução do Maven Surefire em **09/11/2026**:

```text
Tests run: 3, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

Esse resultado comprova a execução bem-sucedida dos três testes existentes; não significa cobertura completa de todos os casos possíveis.

## Decisões e aprendizados

- **Strategy:** diferentes regras de envio ficam encapsuladas atrás do mesmo contrato.
- **Registry:** a seleção por identificador fica separada do comportamento específico dos canais.
- **Polimorfismo:** o consumidor trabalha com `NotificationSender`, sem precisar fazer `instanceof` ou um `if` para cada implementação.
- **Fail Fast:** identificadores duplicados são rejeitados na construção do Registry, em vez de gerar uma substituição silenciosa.
- **Coleção não modificável:** `Map.copyOf()` evita mudanças posteriores no registro, embora não torne os objetos armazenados imutáveis.
- **Testes de comportamento:** JUnit valida o contrato público sem acessar a estrutura interna do mapa.

## Próximos estudos

- Validar identificadores nulos, vazios e fora do padrão.
- Ampliar os testes, incluindo a mensagem das exceções.
- Introduzir Spring Boot e entender como `@Component` e a injeção por construtor podem fornecer uma `List<NotificationSender>` automaticamente.
- Comparar a montagem manual do Registry com o gerenciamento de beans pelo Spring.

---

Este repositório tem finalidade educacional. As validações e os envios são intencionalmente simplificados para manter o foco em arquitetura, orientação a objetos e testes.

[Strategy + Registry Pattern com Java: aprendendo arquitetura além dos frameworks](https://lnkd.in/p/dsZN3g4U)