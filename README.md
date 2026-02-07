# JTech Tasklist - Sistema TODO List Multi-usuario

Sistema fullstack de gerenciamento de tarefas com autenticacao JWT, desenvolvido com **Spring Boot** (backend) e **Vue 3 + Vuetify** (frontend).

---

## Visao Geral da Arquitetura

### Backend - Arquitetura Hexagonal (Ports & Adapters)

O backend segue a **Arquitetura Hexagonal**, isolando a logica de negocio das dependencias externas (banco de dados, HTTP, seguranca). Cada operacao possui seu proprio conjunto de classes, garantindo o **Principio da Responsabilidade Unica (SRP)**.

```
Controller (Input Adapter)
    |
InputGateway (Input Port - interface)
    |
UseCase (Application Core - logica de negocio)
    |
OutputGateway (Output Port - interface)
    |
Adapter (Output Adapter - implementacao)
    |
Repository (Spring Data JPA)
```

**Fluxo de uma requisicao:**
1. O `Controller` recebe a requisicao HTTP e delega para o `InputGateway`
2. O `UseCase` implementa o `InputGateway` e contem a logica de negocio
3. O `UseCase` usa o `OutputGateway` (interface) para acessar dados
4. O `Adapter` implementa o `OutputGateway` e interage com o `Repository`
5. O `UseCaseConfig` faz a amarracao de dependencias via `@Configuration` + `@Bean`

**Beneficios:**
- Core de negocio sem dependencia de frameworks
- Facilidade de testes unitarios (mock apenas das interfaces)
- Troca de implementacoes sem impacto na logica de negocio

### Frontend - Arquitetura em Camadas

O frontend segue uma arquitetura modular com separacao clara de responsabilidades:

```
Views (paginas)
    |
Components (componentes reutilizaveis)
    |
Stores (Pinia - gerenciamento de estado)
    |
Services (camada HTTP - axios)
    |
API Backend (REST)
```

---

## Stack Tecnologica

### Backend

| Tecnologia | Versao | Justificativa |
|------------|--------|---------------|
| Java | 21 | LTS mais recente, records, sealed classes, pattern matching |
| Spring Boot | 3.5.5 | Framework principal, auto-configuracao, produtividade |
| Spring Security | 6.x | Autenticacao/autorizacao robusta via filtros |
| Spring Data JPA | 3.x | Abstrai acesso ao banco, query methods automaticos |
| Spring Validation | 3.x | Validacao declarativa com Bean Validation |
| PostgreSQL | - | Banco relacional robusto, ACID compliance |
| JWT (jjwt) | 0.12.6 | Autenticacao stateless, padrao industria |
| BCrypt | - | Hash de senhas com salt automatico |
| JUnit 5 | - | Framework de testes padrao Java |
| Mockito | - | Mocking para testes unitarios isolados |
| H2 | - | Banco em memoria para testes de integracao |
| JaCoCo | - | Cobertura de codigo |
| Lombok | - | Reducao de boilerplate (getters, setters, constructors) |
| SpringDoc OpenAPI | 2.0.4 | Documentacao automatica da API (Swagger UI) |
| Gradle | - | Build tool com configuracao declarativa |

### Frontend

| Tecnologia | Versao | Justificativa |
|------------|--------|---------------|
| Vue 3 | 3.5.18 | Composition API, reatividade, performance |
| TypeScript | 5.8 | Tipagem estatica, seguranca em tempo de compilacao |
| Vite | 7.0.6 | Build tool rapida, HMR instantaneo |
| Pinia | 3.0.3 | State management oficial Vue, type-safe |
| Vue Router | 4.5.1 | Roteamento SPA com guards de navegacao |
| Vuetify | 3.11.8 | Material Design, componentes ricos e acessiveis |
| Axios | 1.13.4 | HTTP client com interceptors para auth automatica |
| Vitest | 3.2.4 | Test runner integrado ao Vite, rapido |
| Vue Test Utils | 2.4.6 | Testes de componentes Vue |
| ESLint + Prettier | 9.x / 3.6.2 | Qualidade e padronizacao de codigo |

---

## Como Rodar Localmente

### Pre-requisitos

- Java 21+
- Node.js 20.19+ ou 22.12+
- PostgreSQL rodando localmente
- npm ou yarn

### 1. Banco de Dados

```bash
# Criar o banco no PostgreSQL
createdb tasklist_db
```

Ou via psql:
```sql
CREATE DATABASE tasklist_db;
```

> O backend usa `postgres:postgres` como credenciais padrao. Ajuste em `application.yml` se necessario.

### 2. Backend

```bash
cd fullstack2-jtech-main/jtech-tasklist-backend

# Rodar a aplicacao (porta 8080)
./gradlew bootRun
```

A API estara disponivel em `http://localhost:8080/api/v1`

Swagger UI: `http://localhost:8080/doc/tasklist/v1/api.html`

### 3. Frontend

```bash
cd jtech-tasklist-frontend

# Instalar dependencias
npm install

# Rodar em desenvolvimento (porta 5173)
npm run dev
```

Acesse `http://localhost:5173`

### 4. Fluxo de Uso

1. Acesse `http://localhost:5173` -> sera redirecionado para `/login`
2. Clique em "Cadastre-se" -> preencha nome, email e senha
3. Faca login com as credenciais cadastradas
4. Crie, edite, marque como concluida e exclua tarefas
5. Use os filtros (Todas, Pendentes, Concluidas) para organizar
6. Clique no icone de logout para sair

---

## Como Rodar os Testes

### Backend (39 testes)

```bash
cd fullstack2-jtech-main/jtech-tasklist-backend

# Rodar todos os testes
./gradlew test

# Rodar com relatorio de cobertura (JaCoCo)
./gradlew test jacocoTestReport
```

O relatorio de cobertura sera gerado em `build/reports/jacoco/test/html/index.html`

### Frontend (36 testes)

```bash
cd jtech-tasklist-frontend

# Rodar todos os testes
npm run test:unit

# Rodar com watch mode
npx vitest

# Verificar tipos + build
npm run build

# Lint
npm run lint
```

---

## Estrutura de Pastas

### Backend

```
jtech-tasklist-backend/
├── src/
│   ├── main/java/br/com/jtech/tasklist/
│   │   ├── StartTasklist.java                          # Entry point
│   │   ├── config/
│   │   │   ├── infra/
│   │   │   │   ├── exceptions/                         # Excecoes customizadas
│   │   │   │   │   ├── ApiError.java                   # Modelo de erro padrao
│   │   │   │   │   ├── ApiSubError.java                # Interface de sub-erros
│   │   │   │   │   ├── ApiValidationError.java         # Erros de validacao
│   │   │   │   │   ├── TaskNotFoundException.java      # 404 - Tarefa nao encontrada
│   │   │   │   │   ├── UserAlreadyExistsException.java # 409 - Email duplicado
│   │   │   │   │   ├── UnauthorizedAccessException.java# 403 - Sem permissao
│   │   │   │   │   └── InvalidCredentialsException.java# 401 - Credenciais invalidas
│   │   │   │   ├── handlers/
│   │   │   │   │   └── GlobalExceptionHandler.java     # Tratamento centralizado
│   │   │   │   ├── swagger/
│   │   │   │   │   └── OpenAPI30Configuration.java     # Config Swagger/OpenAPI
│   │   │   │   └── utils/                              # GenId, Jsons, ReadyListener
│   │   │   ├── security/
│   │   │   │   ├── JwtTokenProvider.java               # Gerar/validar JWT
│   │   │   │   ├── JwtAuthenticationFilter.java        # Filtro Spring Security
│   │   │   │   └── SecurityConfig.java                 # Config Spring Security
│   │   │   └── usecases/                               # 8 @Configuration beans
│   │   │       ├── CreateTaskUseCaseConfig.java
│   │   │       ├── FindTasksByUserUseCaseConfig.java
│   │   │       ├── FindTaskByIdUseCaseConfig.java
│   │   │       ├── UpdateTaskUseCaseConfig.java
│   │   │       ├── DeleteTaskUseCaseConfig.java
│   │   │       ├── RegisterUserUseCaseConfig.java
│   │   │       ├── LoginUserUseCaseConfig.java
│   │   │       └── RefreshTokenUseCaseConfig.java
│   │   ├── application/
│   │   │   ├── core/
│   │   │   │   ├── domains/
│   │   │   │   │   ├── User.java                       # Modelo de dominio User
│   │   │   │   │   └── Task.java                       # Modelo de dominio Task
│   │   │   │   └── usecases/                           # 8 use cases
│   │   │   │       ├── CreateTaskUseCase.java
│   │   │   │       ├── FindTasksByUserUseCase.java
│   │   │   │       ├── FindTaskByIdUseCase.java
│   │   │   │       ├── UpdateTaskUseCase.java
│   │   │   │       ├── DeleteTaskUseCase.java
│   │   │   │       ├── RegisterUserUseCase.java
│   │   │   │       ├── LoginUserUseCase.java
│   │   │   │       └── RefreshTokenUseCase.java
│   │   │   └── ports/
│   │   │       ├── input/                              # 8 input gateways (interfaces)
│   │   │       └── output/                             # 7 output gateways (interfaces)
│   │   └── adapters/
│   │       ├── input/
│   │       │   ├── controllers/                        # 8 REST controllers
│   │       │   │   ├── RegisterUserController.java     # POST /auth/register
│   │       │   │   ├── LoginUserController.java        # POST /auth/login
│   │       │   │   ├── RefreshTokenController.java     # POST /auth/refresh
│   │       │   │   ├── CreateTaskController.java       # POST /tasks
│   │       │   │   ├── FindTasksByUserController.java  # GET  /tasks
│   │       │   │   ├── FindTaskByIdController.java     # GET  /tasks/{id}
│   │       │   │   ├── UpdateTaskController.java       # PUT  /tasks/{id}
│   │       │   │   └── DeleteTaskController.java       # DELETE /tasks/{id}
│   │       │   └── protocols/                          # DTOs (request/response)
│   │       │       ├── TaskRequest.java
│   │       │       ├── TaskResponse.java
│   │       │       ├── RegisterRequest.java
│   │       │       ├── LoginRequest.java
│   │       │       ├── AuthResponse.java
│   │       │       ├── UserResponse.java
│   │       │       └── RefreshTokenRequest.java
│   │       └── output/
│   │           ├── CreateTaskAdapter.java              # 7 output adapters
│   │           ├── FindTasksByUserAdapter.java
│   │           ├── FindTaskByIdAdapter.java
│   │           ├── UpdateTaskAdapter.java
│   │           ├── DeleteTaskAdapter.java
│   │           ├── RegisterUserAdapter.java
│   │           ├── FindUserByEmailAdapter.java
│   │           └── repositories/
│   │               ├── entities/
│   │               │   ├── UserEntity.java             # JPA Entity - users
│   │               │   └── TaskEntity.java             # JPA Entity - tasks
│   │               ├── UserRepository.java             # Spring Data JPA
│   │               └── TaskRepository.java             # Spring Data JPA
│   ├── main/resources/
│   │   └── application.yml                             # Configuracao principal
│   └── test/
│       ├── java/br/com/jtech/tasklist/
│       │   ├── application/core/usecases/              # 7 testes unitarios
│       │   │   ├── CreateTaskUseCaseTest.java
│       │   │   ├── FindTasksByUserUseCaseTest.java
│       │   │   ├── FindTaskByIdUseCaseTest.java
│       │   │   ├── UpdateTaskUseCaseTest.java
│       │   │   ├── DeleteTaskUseCaseTest.java
│       │   │   ├── RegisterUserUseCaseTest.java
│       │   │   └── LoginUserUseCaseTest.java
│       │   ├── config/security/
│       │   │   └── JwtTokenProviderTest.java           # Teste JWT
│       │   └── adapters/input/controllers/
│       │       ├── AuthControllerIntegrationTest.java  # 6 testes integracao
│       │       └── TaskControllerIntegrationTest.java  # 8 testes integracao
│       └── resources/
│           └── application-test.properties             # Config H2 para testes
```

### Frontend

```
jtech-tasklist-frontend/
├── src/
│   ├── main.ts                        # Entry point (Vue + Pinia + Router + Vuetify)
│   ├── App.vue                        # Root component (v-app + router-view)
│   ├── test-setup.ts                  # Polyfills jsdom (ResizeObserver, visualViewport)
│   ├── types/
│   │   └── index.ts                   # Interfaces TypeScript (User, Task, Request, Response)
│   ├── plugins/
│   │   └── vuetify.ts                 # Configuracao Vuetify (tema, icones MDI)
│   ├── services/
│   │   ├── api.ts                     # Axios instance + interceptors (auth, 401)
│   │   ├── authService.ts             # login(), register(), refresh()
│   │   └── taskService.ts             # getTasks(), createTask(), updateTask(), deleteTask()
│   ├── stores/
│   │   ├── auth.ts                    # Pinia: user, token, login, logout, refresh
│   │   ├── tasks.ts                   # Pinia: tasks[], CRUD, toggleComplete, filtros
│   │   └── __tests__/
│   │       ├── auth.spec.ts           # 8 testes (login, logout, storage, refresh)
│   │       └── tasks.spec.ts          # 9 testes (CRUD, toggle, loading, errors)
│   ├── router/
│   │   └── index.ts                   # Rotas + guards (requiresAuth, requiresGuest)
│   ├── components/
│   │   ├── AppBar.vue                 # Barra superior (titulo, email, logout)
│   │   ├── TaskItem.vue               # Card de tarefa (checkbox, edit, delete)
│   │   ├── TaskList.vue               # Lista + filtros (Todas, Pendentes, Concluidas)
│   │   ├── TaskForm.vue               # Dialog criar/editar tarefa
│   │   ├── ConfirmDialog.vue          # Dialog generico de confirmacao
│   │   └── __tests__/
│   │       ├── TaskItem.spec.ts       # 7 testes (render, emits, line-through)
│   │       ├── TaskList.spec.ts       # 4 testes (empty state, loading, filtros)
│   │       └── TaskForm.spec.ts       # 4 testes (titulos, cancel, fields)
│   ├── views/
│   │   ├── LoginView.vue              # Tela de login (email, senha, validacao)
│   │   ├── RegisterView.vue           # Tela de cadastro (nome, email, senha, confirmar)
│   │   ├── TasksView.vue              # Tela principal (CRUD tarefas, snackbar)
│   │   └── __tests__/
│   │       └── LoginView.spec.ts      # 4 testes (render, fields, button, link)
│   └── assets/
│       └── main.css                   # Reset CSS basico
├── package.json
├── vite.config.ts                     # Vite + plugins (Vue, DevTools, Vuetify)
├── vitest.config.ts                   # Vitest (jsdom, setup, vuetify inline)
├── tsconfig.json                      # TypeScript config raiz
├── tsconfig.app.json                  # TS config producao (exclui testes)
└── tsconfig.node.json                 # TS config Node (configs)
```

---

## Endpoints da API

### Autenticacao (publicos)

| Metodo | Rota | Descricao |
|--------|------|-----------|
| POST | `/api/v1/auth/register` | Cadastrar usuario (name, email, password) |
| POST | `/api/v1/auth/login` | Login (email, password) -> JWT tokens |
| POST | `/api/v1/auth/refresh` | Renovar tokens (refreshToken) |

### Tarefas (protegidos - requer Bearer token)

| Metodo | Rota | Descricao |
|--------|------|-----------|
| POST | `/api/v1/tasks` | Criar tarefa |
| GET | `/api/v1/tasks` | Listar tarefas do usuario |
| GET | `/api/v1/tasks/{id}` | Buscar tarefa por ID |
| PUT | `/api/v1/tasks/{id}` | Atualizar tarefa |
| DELETE | `/api/v1/tasks/{id}` | Excluir tarefa |

### Exemplos de Requisicao

**Registro:**
```json
POST /api/v1/auth/register
{
  "name": "Joao Silva",
  "email": "joao@email.com",
  "password": "123456"
}
```

**Login:**
```json
POST /api/v1/auth/login
{
  "email": "joao@email.com",
  "password": "123456"
}
// Resposta:
{
  "token": "eyJhbG...",
  "refreshToken": "eyJhbG...",
  "type": "Bearer"
}
```

**Criar Tarefa:**
```json
POST /api/v1/tasks
Authorization: Bearer eyJhbG...
{
  "title": "Estudar Vue 3",
  "description": "Revisar Composition API"
}
```

---

## Cobertura de Testes

### Backend - 39 testes (10 arquivos)

| Categoria | Arquivo | Testes | Tipo |
|-----------|---------|--------|------|
| Use Cases | CreateTaskUseCaseTest | 3 | Unitario |
| Use Cases | FindTasksByUserUseCaseTest | 2 | Unitario |
| Use Cases | FindTaskByIdUseCaseTest | 3 | Unitario |
| Use Cases | UpdateTaskUseCaseTest | 3 | Unitario |
| Use Cases | DeleteTaskUseCaseTest | 3 | Unitario |
| Use Cases | RegisterUserUseCaseTest | 3 | Unitario |
| Use Cases | LoginUserUseCaseTest | 3 | Unitario |
| Security | JwtTokenProviderTest | 5 | Unitario |
| Controllers | AuthControllerIntegrationTest | 6 | Integracao |
| Controllers | TaskControllerIntegrationTest | 8 | Integracao |

**Cenarios cobertos:**
- Fluxos de sucesso e falha para cada operacao
- Validacao de propriedade de recursos (usuario A nao acessa tarefa de B)
- Validacao de campos obrigatorios (titulo vazio, email invalido)
- Autenticacao (credenciais invalidas, email duplicado, token expirado)
- Geracao e validacao de tokens JWT

### Frontend - 36 testes (6 arquivos)

| Categoria | Arquivo | Testes | O que valida |
|-----------|---------|--------|-------------|
| Store | auth.spec.ts | 8 | Login, logout, loadFromStorage, refresh, estado |
| Store | tasks.spec.ts | 9 | CRUD, toggle, loading, errors, computed |
| Component | TaskItem.spec.ts | 7 | Render, line-through, emits (edit, delete) |
| Component | TaskList.spec.ts | 4 | Empty state, loading, render, filtros |
| Component | TaskForm.spec.ts | 4 | Titulos dialog, cancel, fields |
| View | LoginView.spec.ts | 4 | Render, fields, botao, link registro |

---

## Decisoes Tecnicas

### 1. Arquitetura Hexagonal no Backend

Escolhida em vez de uma arquitetura em camadas tradicional (Controller → Service → Repository) para:
- **Isolamento do core**: A logica de negocio (UseCases) nao depende de Spring, JPA ou qualquer framework
- **Testabilidade**: Testes unitarios de use cases mockam apenas interfaces simples
- **Flexibilidade**: Trocar banco de dados ou framework HTTP requer apenas novos adapters
- **Um use case por operacao**: Cada classe tem uma unica responsabilidade (SRP), facilitando manutencao

### 2. JWT com Refresh Token

- **Access Token**: Curta duracao (24h) para requisicoes autenticadas
- **Refresh Token**: Longa duracao (7 dias) para renovar sessao sem re-login
- **Stateless**: Nao requer armazenamento de sessao no servidor
- **Seguranca**: BCrypt para hash de senhas, HMAC-SHA256 para assinatura JWT

### 3. Vue 3 Composition API + Pinia

- **Composition API**: Logica reutilizavel, melhor TypeScript support, composables
- **Pinia**: Substituicao oficial do Vuex, tipagem nativa, mais simples
- **Persistencia**: Tokens armazenados em localStorage para sobreviver refresh do browser
- **Interceptors Axios**: Token JWT adicionado automaticamente a cada requisicao

### 4. Vuetify 3 como UI Framework

- **Material Design**: Padrao visual consistente e profissional
- **Componentes ricos**: Dialogs, forms, chips, snackbars, progress bars built-in
- **Acessibilidade**: ARIA attributes automaticos
- **Validacao de formularios**: v-form com rules nativas

### 5. Configuracao de Testes

- **Backend**: H2 em memoria para testes de integracao (nao depende de PostgreSQL externo)
- **Frontend**: jsdom com polyfills (ResizeObserver, visualViewport) para suportar Vuetify
- **Mocks**: Servicos HTTP mockados nos testes de store e componentes
- **Vuetify global**: Setup file registra Vuetify globalmente para todos os testes

---

## Principios SOLID Aplicados

| Principio | Aplicacao no Projeto |
|-----------|---------------------|
| **SRP** (Single Responsibility) | Cada UseCase faz uma unica operacao; cada Controller expoe um unico endpoint; cada Adapter implementa uma unica interface |
| **OCP** (Open/Closed) | Novos use cases podem ser adicionados sem modificar os existentes; novos adapters podem ser criados para diferentes bancos |
| **LSP** (Liskov Substitution) | Interfaces (InputGateway, OutputGateway) podem ter suas implementacoes trocadas sem quebrar o sistema |
| **ISP** (Interface Segregation) | Gateways sao interfaces especificas (CreateTaskOutputGateway, FindTaskByIdOutputGateway) em vez de uma unica interface generica |
| **DIP** (Dependency Inversion) | UseCases dependem de interfaces (OutputGateway), nao de implementacoes concretas (Adapters); inversao feita via @Configuration beans |

---

## Modelagem de Dados

### Entidade User

| Campo | Tipo | Restricao |
|-------|------|-----------|
| id | UUID | PK, auto-gerado |
| name | String | NOT NULL |
| email | String | NOT NULL, UNIQUE |
| password | String | NOT NULL (BCrypt hash) |
| createdAt | LocalDateTime | Auto (PrePersist) |

### Entidade Task

| Campo | Tipo | Restricao |
|-------|------|-----------|
| id | UUID | PK, auto-gerado |
| title | String | NOT NULL |
| description | String | Opcional |
| completed | Boolean | Default: false |
| createdAt | LocalDateTime | Auto (PrePersist) |
| updatedAt | LocalDateTime | Auto (PreUpdate) |
| user_id | UUID | FK -> User (ManyToOne) |

**Relacionamento**: Um User possui muitas Tasks (1:N). Cada Task pertence a exatamente um User.

---

## Melhorias e Roadmap

### Curto Prazo
- **Multiplas Listas**: Implementar modelo `TaskList` para categorizar tarefas (Trabalho, Estudos, Pessoal)
- **Paginacao**: Adicionar paginacao na listagem de tarefas para escalar com muitos registros
- **Ordenacao**: Permitir ordenar tarefas por data, titulo ou status
- **Refresh Token automatico**: Interceptor que renova token expirado automaticamente sem perder a requisicao original

### Medio Prazo
- **Drag & Drop**: Reordenar tarefas via arrastar e soltar
- **Busca**: Campo de busca para filtrar tarefas por titulo/descricao
- **Data de Vencimento**: Adicionar campo de prazo com notificacoes visuais
- **Subtarefas**: Hierarquia de tarefas (tarefas podem ter sub-itens)
- **Cache**: Redis para cache de listas de tarefas frequentemente acessadas

### Longo Prazo
- **Compartilhamento**: Tarefas compartilhadas entre usuarios (colaboracao)
- **Notificacoes**: WebSocket para atualizacoes em tempo real
- **PWA**: Progressive Web App para acesso offline
- **CI/CD**: Pipeline com GitHub Actions (lint, test, build, deploy)
- **Docker**: Containerizacao com docker-compose (backend + frontend + PostgreSQL)
- **Monitoramento**: Grafana + Prometheus para metricas de performance
- **Rate Limiting**: Protecao contra abuso de API
- **Auditoria**: Log de acoes dos usuarios para compliance
