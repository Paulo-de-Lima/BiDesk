# Estrutura MVC - BiDesk

## 📋 Visão Geral

Estrutura MVC completa criada baseada nos wireframes do projeto BiDesk - Sistema de Gerenciamento para Mesa de Bilhar.

## 🏗️ Estrutura de Diretórios

```
src/main/java/com/bidesk/
├── BiDeskApplication.java          (Classe principal Spring Boot)
├── config/                          (Configurações)
│   └── WebConfig.java              (Configuração CORS)
├── controller/                      (Controllers REST)
│   ├── BaseController.java         (Controller base)
│   ├── ClienteController.java      (API de Clientes)
│   ├── ManutencaoController.java   (API de Manutenções)
│   ├── MaterialController.java     (API de Materiais/Estoque)
│   ├── TransacaoController.java    (API de Transações)
│   └── CobrancaController.java     (API de Cobranças)
├── service/                         (Services - Lógica de negócio)
│   ├── BaseService.java            (Interface base)
│   ├── ClienteService.java
│   ├── ManutencaoService.java
│   ├── MaterialService.java
│   ├── TransacaoService.java
│   ├── CobrancaService.java
│   └── impl/                        (Implementações)
│       ├── BaseServiceImpl.java
│       ├── ClienteServiceImpl.java
│       ├── ManutencaoServiceImpl.java
│       ├── MaterialServiceImpl.java
│       ├── TransacaoServiceImpl.java
│       └── CobrancaServiceImpl.java
├── repository/                      (Repositories - Acesso a dados)
│   ├── BaseRepository.java         (Repository base)
│   ├── ClienteRepository.java
│   ├── ManutencaoRepository.java
│   ├── MaterialRepository.java
│   ├── TransacaoRepository.java
│   └── CobrancaRepository.java
├── model/                           (Entities - Modelos de dados)
│   ├── BaseEntity.java             (Entidade base)
│   ├── Cliente.java                (Cliente: nome, endereco, cidade)
│   ├── Manutencao.java             (Manutenção: cliente, titulo, descricao)
│   ├── Material.java               (Material: nome, quantidade, unidade)
│   ├── Transacao.java              (Transação: cliente, numero, data, registro, pago, deve)
│   ├── Cobranca.java               (Cobrança: data, cidade, despesa, total)
│   └── dto/                         (DTOs)
│       └── ApiResponse.java        (Resposta padrão da API)
└── exception/                       (Tratamento de exceções)
    ├── GlobalExceptionHandler.java (Handler global)
    └── ResourceNotFoundException.java (Exceção customizada)
```

## 📦 Entidades Criadas

### 1. Cliente
- **Campos**: nome, endereco, cidade
- **Relacionamentos**: 
  - OneToMany com Transacao
  - OneToMany com Manutencao
- **Endpoints**: `/api/clientes`

### 2. Manutencao
- **Campos**: cliente, titulo, descricao
- **Relacionamentos**: ManyToOne com Cliente
- **Endpoints**: `/api/manutencoes`

### 3. Material
- **Campos**: nome, quantidade, unidade
- **Métodos**: getStatusEstoque() - retorna VAZIO, BAIXO ou ALTO
- **Endpoints**: `/api/materiais`
- **Endpoints Especiais**: 
  - POST `/api/materiais/{id}/adicionar` - Adiciona quantidade
  - POST `/api/materiais/{id}/diminuir` - Diminui quantidade
  - GET `/api/materiais/estoque-baixo` - Lista materiais com estoque baixo

### 4. Transacao
- **Campos**: cliente, numero, data, registro, pago, deve
- **Relacionamentos**: ManyToOne com Cliente
- **Endpoints**: `/api/transacoes`
- **Endpoints Especiais**: 
  - GET `/api/transacoes/cliente/{clienteId}` - Lista transações de um cliente

### 5. Cobranca
- **Campos**: data, cidade, despesa, total
- **Endpoints**: `/api/cobrancas`
- **Endpoints Especiais**: 
  - GET `/api/cobrancas/cidade/{cidade}` - Lista cobranças por cidade
  - GET `/api/cobrancas/periodo?dataInicio=...&dataFim=...` - Lista cobranças por período

## 🔧 Funcionalidades Implementadas

### CRUD Completo
Todas as entidades possuem operações CRUD completas:
- **GET** `/api/{entidade}` - Lista todos
- **GET** `/api/{entidade}/{id}` - Busca por ID
- **POST** `/api/{entidade}` - Cria novo
- **PUT** `/api/{entidade}/{id}` - Atualiza existente
- **DELETE** `/api/{entidade}/{id}` - Deleta

### Funcionalidades Especiais

#### Cliente
- Buscar por nome: `GET /api/clientes/buscar?nome=...`
- Buscar por cidade: `GET /api/clientes/cidade/{cidade}`

#### Manutenção
- Buscar por cliente: `GET /api/manutencoes/cliente/{clienteId}`
- Buscar por título: `GET /api/manutencoes/buscar?titulo=...`

#### Material
- Buscar por nome: `GET /api/materiais/buscar?nome=...`
- Listar estoque baixo: `GET /api/materiais/estoque-baixo`
- Adicionar quantidade: `POST /api/materiais/{id}/adicionar?quantidade=...`
- Diminuir quantidade: `POST /api/materiais/{id}/diminuir?quantidade=...`

#### Transação
- Buscar por cliente: `GET /api/transacoes/cliente/{clienteId}`

#### Cobrança
- Buscar por cidade: `GET /api/cobrancas/cidade/{cidade}`
- Buscar por período: `GET /api/cobrancas/periodo?dataInicio=...&dataFim=...`

## 🗄️ Banco de Dados

### Configuração
- **Banco**: MySQL
- **URL**: `jdbc:mysql://localhost:3306/bidesk_db`
- **DDL**: `update` (cria/atualiza tabelas automaticamente)
- **Dialeto**: MySQL

### Tabelas Criadas
- `clientes` - Tabela de clientes
- `manutencoes` - Tabela de manutenções
- `materiais` - Tabela de materiais/estoque
- `transacoes` - Tabela de transações
- `cobrancas` - Tabela de cobranças

## 🚀 Como Usar

### 1. Configurar Banco de Dados
Certifique-se de que o MySQL está rodando e crie o banco de dados:
```sql
CREATE DATABASE bidesk_db;
```

### 2. Atualizar application.properties
Verifique as credenciais do banco de dados em `src/main/resources/application.properties`

### 3. Executar a Aplicação
```bash
mvn clean install
mvn spring-boot:run
```

### 4. Testar os Endpoints
A aplicação estará disponível em: `http://localhost:8080`

Exemplos de requisições:
```bash
# Listar clientes
GET http://localhost:8080/api/clientes

# Criar cliente
POST http://localhost:8080/api/clientes
Content-Type: application/json
{
  "nome": "João Silva",
  "endereco": "Rua 1 - Bairro 2 - 123",
  "cidade": "Paulo Afonso - BA"
}

# Listar materiais
GET http://localhost:8080/api/materiais

# Adicionar quantidade a material
POST http://localhost:8080/api/materiais/1/adicionar?quantidade=5
```

## 📝 Notas Importantes

1. **Validações**: Todas as entidades possuem validações básicas usando Bean Validation
2. **Tratamento de Erros**: Exceções são tratadas globalmente pelo `GlobalExceptionHandler`
3. **Respostas Padronizadas**: Todas as respostas seguem o padrão `ApiResponse<T>`
4. **Relacionamentos**: Relacionamentos bidirecionais estão configurados com `@JsonIgnore` para evitar serialização circular
5. **Timestamps**: Todas as entidades possuem `createdAt` e `updatedAt` automaticamente

## 🔍 Próximos Passos

1. Adicionar autenticação e autorização (se necessário)
2. Implementar paginação nas listagens
3. Adicionar filtros avançados
4. Implementar testes unitários e de integração
5. Adicionar documentação Swagger/OpenAPI
6. Implementar validações de negócio mais complexas
