# SGS - Sistema de Gestão de Solicitações

Aplicação web para registro, consulta e acompanhamento de solicitações de pagamento de uma organização.

---

## Tecnologias utilizadas

| Camada     | Tecnologia                        |
|------------|-----------------------------------|
| Backend    | Java 21 + Spring Boot             |
| Frontend   | Thymeleaf + HTML/CSS/JS puro      |
| Banco      | MySQL 8+                          |
| Build      | Maven (Maven Wrapper incluso)     |

**Justificativas técnicas:**
- **Spring Boot**: Ecossistema para APIs REST e integração com JPA e Thymeleaf.
- **Thymeleaf**: Renderização server-side com templates.
- **SQL Nativo com filtros dinâmicos**: Implementado em `SolicitacaoRepositoryCustomImpl`, utilizando `EntityManager` e `createNativeQuery` com `StringBuilder` e parâmetros nomeados.
- **Arquitetura em camadas**: Controller -> Service -> Repository, utilizada para separar de maneira clara as responsabilidades.

---

## Pré-requisitos

- Java 21+
- Maven 3.9+ (ou use o `mvnw` incluso no projeto)
- MySQL 8+

---

## Configuração do banco de dados

### 1. Criar o banco e as tabelas

Execute o script DDL no seu cliente MySQL (MySQL Workbench, DBeaver, terminal etc.):

```sql
CREATE DATABASE IF NOT EXISTS sgs_db;
USE sgs_db;

CREATE TABLE solicitante (
    id        INT AUTO_INCREMENT PRIMARY KEY,
    nome      VARCHAR(100) NOT NULL,
    cpf_cnpj  VARCHAR(20)  NOT NULL UNIQUE
);

CREATE TABLE categoria (
    id   INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(50) NOT NULL
);

CREATE TABLE solicitacao (
    id               INT AUTO_INCREMENT PRIMARY KEY,
    solicitante_id   INT            NOT NULL,
    categoria_id     INT            NOT NULL,
    descricao        TEXT           NOT NULL,
    valor            DECIMAL(10,2)  NOT NULL,
    data_solicitacao DATETIME       NOT NULL,
    status           VARCHAR(15)    NOT NULL,
    CONSTRAINT fk_solicitacao_solicitante FOREIGN KEY (solicitante_id) REFERENCES solicitante(id),
    CONSTRAINT fk_solicitacao_categoria   FOREIGN KEY (categoria_id)   REFERENCES categoria(id)
);
```

> O arquivo completo está em `src/main/resources/schema.sql`.

### 2. Popular as tabelas com dados iniciais

```sql
USE sgs_db;

INSERT INTO solicitante (nome, cpf_cnpj) VALUES
('SergipeTec',                                    '06938508000111'),
('CONTABILIZE ASSESSORIA CONTABIL LTDA - ME',     '19801458000178'),
('L & A Informatica Limitada',                    '10526704000156'),
('COLEGIO MAGNUS LTDA - ME',                      '04545587000175'),
('Fundação Universidade Federal de Sergipe',       '13031547000104');

INSERT INTO categoria (nome) VALUES
('Software'),
('Contabilidade'),
('Ensino'),
('Hardware'),
('Licenças de Programas');
```

> O arquivo completo está em `src/main/resources/data.sql`.

---

## Configuração da aplicação

Edite o arquivo `src/main/resources/application.properties` com as suas credenciais do MySQL:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/sgs_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=SUA_SENHA_AQUI
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

> **Importante:** `ddl-auto=validate` significa que o Hibernate apenas valida o schema - as tabelas devem ser criadas manualmente pelos scripts acima antes de iniciar a aplicação.

---

## Como executar

Na raiz do projeto, execute:

```bash
# Linux/macOS
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
```

A aplicação estará disponível em: **http://localhost:8080**

---

## Estrutura do projeto

```
src/
└── main/
    ├── java/com/desafio/sgs/
    │   ├── controller/
    │   │   ├── SgsViewController.java        # Rotas das páginas Thymeleaf
    │   │   ├── SolicitacaoController.java    # API REST de solicitações
    │   │   ├── SolicitanteController.java    # API REST de solicitantes
    │   │   └── CategoriaController.java      # API REST de categorias
    │   ├── dto/
    │   │   ├── SolicitacaoListaDTO.java       # Projeção para listagem
    │   │   └── SolicitacaoDetalheDTO.java     # Projeção para detalhamento
    │   ├── model/
    │   │   ├── Solicitacao.java
    │   │   ├── Solicitante.java
    │   │   ├── Categoria.java
    │   │   └── StatusSolicitacao.java         # Enum com os status possíveis
    │   ├── repository/
    │   │   ├── SolicitacaoRepository.java
    │   │   ├── SolicitacaoRepositoryCustom.java
    │   │   ├── SolicitacaoRepositoryCustomImpl.java  # SQL nativo com filtros dinâmicos
    │   │   ├── SolicitanteRepository.java
    │   │   └── CategoriaRepository.java
    │   ├── service/
    │   │   ├── SolicitacaoService.java
    │   │   ├── SolicitanteService.java
    │   │   └── CategoriaService.java
    │   └── SgsApplication.java
    └── resources/
        ├── templates/
        │   ├── index.html      # Listagem com filtros e atualização de status
        │   ├── cadastro.html   # Cadastro de nova solicitação
        │   └── detalhe.html    # Detalhamento completo
        ├── sql/
        │   ├── schema.sql 
        │   ├── data.sql
        └── application.properties
```

---

## Endpoints da API REST

| Método  | Endpoint                            | Descrição                              |
|---------|-------------------------------------|----------------------------------------|
| GET     | `/api/solicitacoes`                 | Lista solicitações com filtros opcionais |
| GET     | `/api/solicitacoes/{id}`            | Retorna detalhes de uma solicitação    |
| POST    | `/api/solicitacoes`                 | Cadastra nova solicitação              |
| PATCH   | `/api/solicitacoes/{id}/status`     | Atualiza status de uma solicitação     |
| GET     | `/api/solicitantes`                 | Lista todos os solicitantes            |
| GET     | `/api/categorias`                   | Lista todas as categorias              |

### Parâmetros de filtro (GET `/api/solicitacoes`)

| Parâmetro    | Tipo       | Exemplo            |
|--------------|------------|--------------------|
| `status`     | String     | `SOLICITADO`       |
| `categoriaId`| Integer    | `2`                |
| `dataInicio` | yyyy-MM-dd | `2025-01-01`       |
| `dataFim`    | yyyy-MM-dd | `2025-12-31`       |

### Parâmetros de cadastro (POST `/api/solicitacoes`)

| Parâmetro      | Tipo       | Obrigatório |
|----------------|------------|-------------|
| `descricao`    | String     | Sim         |
| `solicitanteId`| Integer    | Sim         |
| `categoriaId`  | Integer    | Sim         |
| `valor`        | BigDecimal | Sim (> 0)   |

---

## Regras de negócio - Transições de status

Toda solicitação inicia com status **SOLICITADO**. As transições permitidas são:

```
SOLICITADO -> LIBERADO
SOLICITADO -> REJEITADO
LIBERADO   -> APROVADO
LIBERADO   -> REJEITADO
APROVADO   -> CANCELADO
```

> **REJEITADO** e **CANCELADO** são estados finais, ou seja, nenhuma alteração é permitida a partir deles.

---

## Telas da aplicação

| Rota                  | Descrição                                        |
|-----------------------|--------------------------------------------------|
| `/`                   | Listagem de solicitações com filtros e atualização de status inline |
| `/cadastro`           | Formulário de cadastro de nova solicitação       |
| `/solicitacoes/{id}`  | Detalhamento completo com fluxo visual de status |
