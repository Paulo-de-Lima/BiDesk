# BiDesk - Sistema de Gestão Desktop

Aplicação Desktop desenvolvida em Java Swing com MySQL, seguindo o padrão de arquitetura MVC (Model-View-Controller).

## Características

- **Estoque**: Gerenciamento de materiais com controle de quantidade e status (Vazio/Baixo/Alto)
- **Clientes**: Cadastro de clientes e visualização de registros financeiros
- **Financeiro**: Controle de despesas e cobranças
- **Manutenção**: Registro de manutenções por cliente

## Requisitos

- Java 11 ou superior
- MySQL 8.0 ou superior
- Maven 3.6 ou superior

## Configuração do Banco de Dados

1. Certifique-se de que o MySQL está rodando
2. Execute o script SQL para criar o banco de dados e as tabelas:

```bash
mysql -u root -p < database/schema.sql
```

Ou importe o arquivo `database/schema.sql` através de um cliente MySQL (phpMyAdmin, MySQL Workbench, etc.)

3. Configure as credenciais de conexão no arquivo `src/main/java/com/bidesk/database/DatabaseConnection.java`:

```java
private static final String URL = "jdbc:mysql://localhost:3306/bidesk?useSSL=false&serverTimezone=UTC&characterEncoding=UTF-8";
private static final String USER = "root";
private static final String PASSWORD = ""; // Altere conforme necessário
```

## Compilação e Execução

### Usando Maven

1. Compilar o projeto:
```bash
mvn clean compile
```

2. Executar a aplicação:
```bash
mvn exec:java -Dexec.mainClass="com.bidesk.Main"
```

### Ou criar um JAR executável:

```bash
mvn clean package
java -jar target/bidesk-1.0.0.jar
```

## Estrutura do Projeto

```
BiDesk/
├── src/main/java/com/bidesk/
│   ├── Main.java                    # Classe principal
│   ├── database/
│   │   └── DatabaseConnection.java  # Gerenciamento de conexão MySQL
│   ├── model/                       # Modelos de dados
│   │   ├── Material.java
│   │   ├── Cliente.java
│   │   ├── RegistroFinanceiro.java
│   │   ├── Despesa.java
│   │   └── Manutencao.java
│   ├── dao/                         # Data Access Objects
│   │   ├── MaterialDAO.java
│   │   ├── ClienteDAO.java
│   │   ├── RegistroFinanceiroDAO.java
│   │   ├── DespesaDAO.java
│   │   └── ManutencaoDAO.java
│   ├── controller/                  # Controladores (lógica de negócio)
│   │   ├── EstoqueController.java
│   │   ├── ClientesController.java
│   │   ├── FinanceiroController.java
│   │   └── ManutencaoController.java
│   └── view/                        # Interfaces gráficas (Swing)
│       ├── MainView.java
│       ├── EstoqueView.java
│       ├── ClientesView.java
│       ├── FinanceiroView.java
│       └── ManutencaoView.java
├── database/
│   └── schema.sql                   # Script de criação do banco
└── pom.xml                          # Configuração Maven
```

## Funcionalidades

### Estoque
- Visualizar materiais com status colorido (Vermelho=Vazio, Amarelo=Baixo, Verde=Alto)
- Adicionar novos materiais
- Editar materiais existentes
- Deletar materiais
- Adicionar quantidade aos materiais

### Clientes
- Listar todos os clientes
- Adicionar novos clientes
- Visualizar registros financeiros ao selecionar um cliente
- Filtrar e pesquisar clientes (interface preparada)

### Financeiro
- Visualizar despesas e cobranças
- Adicionar novas cobranças com data, cidade, despesa e total

### Manutenção
- Listar manutenções registradas
- Adicionar novas manutenções vinculadas a clientes
- Visualizar cliente, título e descrição de cada manutenção

## Notas

- A aplicação utiliza o padrão MVC para separação de responsabilidades
- As cores da interface seguem o esquema dos wireframes fornecidos
- O status dos materiais é calculado automaticamente baseado na quantidade:
  - 0 = VAZIO (vermelho)
  - 1-4 = BAIXO (amarelo)
  - 5+ = ALTO (verde)

## Licença

Este projeto foi desenvolvido para fins educacionais.


