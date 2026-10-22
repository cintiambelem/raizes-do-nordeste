# Raízes do Nordeste — API Back-End

API REST da rede de lanchonetes **Raízes do Nordeste**, desenvolvida em Java 25 e Spring Boot
para o projeto multidisciplinar Back-End 2026. O MVP implementa o Fluxo A definido no roteiro:
**Pedido → Pagamento mock → Atualização de status**.

## O que foi implementado

- Autenticação JWT com perfis `CLIENTE`, `COZINHA`, `GERENTE` e `ADMIN`.
- Persistência real em H2 por arquivo, com criação automática do modelo JPA e seed inicial.
- Cardápio e estoque individual por unidade.
- Criação de pedido com `canalPedido` obrigatório (`APP`, `TOTEM`, `BALCAO`, `PICKUP`, `WEB`).
- Validação de produtos, unidade e estoque; baixa do estoque na criação do pedido.
- Pagamento externo simulado, com retorno aprovado/recusado e registro persistido.
- Atualização de status por perfil autorizado e filtro de pedidos por canal/status.
- Auditoria persistida para criação, pagamento e atualização de status.
- Respostas de validação e regras de negócio em formato JSON consistente.
- Swagger/OpenAPI e coleção Postman com 10 cenários.

Fidelidade, promoções e campanhas ficam como proposta documentada do domínio, sem alterar o
fluxo MVP escolhido. Não há pagamento real nem armazenamento de senha em texto puro.

## Requisitos

- JDK 25 LTS
- Maven 3.9+
- Nenhum banco externo: H2 é criado localmente em `./data/raizes`

## Configuração e execução

1. Copie `.env.example` para `.env` e substitua `JWT_SECRET` por uma chave privada de pelo
   menos 256 bits. O Spring também aceita as variáveis diretamente no ambiente do processo.
2. No Windows, configure o `JAVA_HOME` para um JDK 25 antes de executar o Maven. O Maven usa
   `JAVA_HOME` para selecionar o JDK, mesmo que outro `java` apareça primeiro no `PATH`:

```powershell
$env:JAVA_HOME = 'C:\Program Files\Eclipse Adoptium\jdk-25.0.4.101-hotspot'
java -version
mvn -version
```

Os dois comandos de versão devem indicar Java 25. Para tornar a configuração permanente para
novos terminais, defina `JAVA_HOME` nas variáveis de ambiente do Windows com o caminho do JDK
25 instalado na máquina.

3. Execute:

```powershell
mvn spring-boot:run
```

A API iniciará em `http://localhost:8080`. O seed cria:

| Perfil | E-mail | Senha |
|---|---|---|
| CLIENTE | cliente@raizes.com | Senha@123 |
| GERENTE | gerente@raizes.com | Senha@123 |

Para apagar os dados locais e recriar o seed, pare a aplicação e remova somente a pasta
`data/`.

## Swagger/OpenAPI

- UI: `http://localhost:8080/swagger-ui.html`
- JSON: `http://localhost:8080/v3/api-docs`
- Console H2 local: `http://localhost:8080/h2-console`
- JDBC URL do H2: `jdbc:h2:file:./data/raizes`

O Swagger é gerado a partir dos endpoints implementados e deve ser considerado o contrato
executável da API.

## Endpoints principais

| Recurso | Método e rota | Permissão |
|---|---|---|
| Auth | `POST /auth/login` | público |
| Unidades | `GET /unidades` | JWT |
| Cardápio | `GET /unidades/{id}/cardapio` | JWT |
| Pedidos | `POST /pedidos` | `CLIENTE` |
| Pedidos | `GET /pedidos?canalPedido=TOTEM&status=AGUARDANDO_PAGAMENTO` | JWT |
| Pedido | `GET /pedidos/{id}` | JWT |
| Status | `PATCH /pedidos/{id}/status` | `COZINHA`, `GERENTE`, `ADMIN` |
| Pagamento | `POST /pagamentos/mock` | JWT |
| Auditoria | `GET /auditoria` | `GERENTE`, `ADMIN` |

Todas as falhas de validação e negócio usam:

```json
{
  "error": "ESTOQUE_INSUFICIENTE",
  "message": "Não há quantidade suficiente para um ou mais itens.",
  "details": [],
  "timestamp": "2026-10-13T12:00:00Z",
  "path": "/pedidos"
}
```

## Coleção Postman e plano de testes

Importe [`postman/raizes-do-nordeste.postman_collection.json`](postman/raizes-do-nordeste.postman_collection.json)
no Postman. A coleção usa as variáveis `baseUrl`, `tokenCliente`, `tokenGerente`, `pedidoId` e
`unidadeId`. Execute primeiro os dois logins e depois as pastas na ordem apresentada.

Os cenários são:

| ID | Cenário | Esperado |
|---|---|---|
| T01 | Login válido | 200 + `accessToken` |
| T02 | Login inválido | 401 + erro padrão |
| T03 | Listagem sem token | 401 |
| T04 | Login de gerente | 200 + perfil `GERENTE` |
| T05 | Listar unidades | 200 |
| T06 | Consultar cardápio | 200 + produtos/estoque |
| T07 | Criar pedido com canal e itens válidos | 201 |
| T08 | Campo `canalPedido` ausente | 422 |
| T09 | Produto inexistente | 404 |
| T10 | Estoque insuficiente | 409 |
| T11 | Pagamento mock aprovado | 200 + `EM_PREPARO` |
| T12 | Criar pedido para testar recusa | 201 |
| T13 | Pagamento mock recusado | 200 + `RECUSADO` |
| T14 | Cliente tentando atualizar status | 403 |
| T15 | Gerente consulta auditoria | 200 + registros |

Os scripts da coleção salvam automaticamente tokens, IDs e verificam os códigos principais.

## Diagramas e documentação

Os diagramas de casos de uso, DER, classes, sequência e arquitetura estão em
[`docs/diagramas.md`](docs/diagramas.md). O DER usa as mesmas entidades, chaves e cardinalidades
do modelo JPA executado pela aplicação.

## LGPD e segurança

O sistema coleta nome e e-mail para identificação/autenticação e registra consentimento
específico para fidelidade. A senha é armazenada somente com BCrypt; tokens são JWT; respostas
não expõem `senhaHash`. O acesso é autorizado por perfil e ações sensíveis geram registros em
`auditoria`. A retenção/anônimização de dados pessoais após encerramento da finalidade é uma
atividade operacional futura, explicitada para não criar regra não prevista no MVP.

## Testes automatizados

```powershell
mvn test
```

## Limites do escopo

Não foi implementado frontend, gateway de pagamento real, integração externa de fidelidade,
promoções automáticas ou deploy público. Essas partes não são necessárias para fechar o fluxo
MVP escolhido e são identificadas como propostas, sem inventar regras adicionais.
