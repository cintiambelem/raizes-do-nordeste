# Diagramas da solução

Os diagramas abaixo estão em Mermaid para permanecerem versionáveis e reproduzíveis. Podem
ser renderizados no GitHub, no VS Code com uma extensão Mermaid ou no Mermaid Live.

## Casos de uso

```mermaid
flowchart LR
  cliente([Cliente<br/>App/Web/Totem])
  atendente([Atendente<br/>Balcão])
  cozinha([Cozinha])
  gerente([Gerente/Admin])
  gateway([Gateway externo<br/>de pagamento mock])
  api((API Raízes do Nordeste))
  cliente -->|autenticar, consultar cardápio,<br/>criar pedido, pagar| api
  atendente -->|consultar pedidos| api
  cozinha -->|atualizar preparo| api
  gerente -->|auditoria e status| api
  api -->|solicitar pagamento| gateway
```

## DER

```mermaid
erDiagram
  USUARIOS ||--o{ PEDIDOS : realiza
  UNIDADES ||--o{ PEDIDOS : recebe
  UNIDADES ||--o{ ESTOQUES : possui
  PRODUTOS ||--o{ ESTOQUES : controla
  PEDIDOS ||--|{ ITENS_PEDIDO : contem
  PRODUTOS ||--o{ ITENS_PEDIDO : compoe
  PEDIDOS ||--o| PAGAMENTOS : possui
  USUARIOS {
    bigint id PK
    varchar nome
    varchar email UK
    varchar senha_hash
    varchar role
    boolean consentimento_fidelidade
  }
  UNIDADES {
    bigint id PK
    varchar nome
    varchar endereco
  }
  PRODUTOS {
    bigint id PK
    varchar nome
    decimal preco
    boolean ativo
  }
  ESTOQUES {
    bigint id PK
    bigint unidade_id FK
    bigint produto_id FK
    int quantidade
  }
  PEDIDOS {
    bigint id PK
    bigint unidade_id FK
    bigint cliente_id FK
    varchar canal_pedido
    varchar status
    decimal total
    timestamp created_at
  }
  ITENS_PEDIDO {
    bigint id PK
    bigint pedido_id FK
    bigint produto_id FK
    int quantidade
    decimal preco_unitario
  }
  PAGAMENTOS {
    bigint id PK
    bigint pedido_id FK
    varchar status
    varchar payload
    timestamp created_at
  }
```

## Classes de domínio

```mermaid
classDiagram
  class User { +Long id +String email +Role role }
  class Unit { +Long id +String nome }
  class Product { +Long id +String nome +BigDecimal preco }
  class Stock { +Unit unidade +Product produto +int quantidade }
  class Order { +Channel canalPedido +OrderStatus status +BigDecimal total }
  class OrderItem { +Product produto +int quantidade +BigDecimal precoUnitario }
  class Payment { +PaymentStatus status +String payload }
  User "1" --> "0..*" Order : cliente
  Unit "1" --> "0..*" Order : recebe
  Order "1" --> "1..*" OrderItem : itens
  Product "1" --> "0..*" OrderItem
  Unit "1" --> "0..*" Stock
  Product "1" --> "0..*" Stock
  Order "1" --> "0..1" Payment
```

## Sequência do fluxo crítico

```mermaid
sequenceDiagram
  participant C as Cliente
  participant A as API
  participant DB as Banco
  participant G as Pagamento mock
  C->>A: POST /pedidos (canalPedido + itens)
  A->>DB: valida unidade, produtos e estoque
  DB-->>A: dados válidos
  A->>DB: grava pedido AGUARDANDO_PAGAMENTO e baixa estoque
  A-->>C: 201 pedidoId
  C->>A: POST /pagamentos/mock (aprovado)
  A->>G: solicita simulação
  G-->>A: APPROVED/DECLINED
  A->>DB: grava pagamento e atualiza status
  A-->>C: status do pagamento e pedido
```

## Arquitetura em camadas

```mermaid
flowchart TB
  API[API: Controllers, DTOs, Swagger]
  APP[Application: OrderService e casos de uso]
  DOMAIN[Domain: entidades, enums e regras]
  INFRA[Infrastructure: JPA, H2, JWT, seed]
  API --> APP --> DOMAIN
  APP --> INFRA
  INFRA --> DOMAIN
```
