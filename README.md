# ESG Resíduos — Projeto DevOps

API RESTful para gestão de pontos de coleta e registros de coleta (tema: Gestão de Resíduos e Reciclagem), desenvolvida com Java 17 e Spring Boot. Este repositório inclui pipeline CI/CD completo com GitHub Actions e containerização com Docker.

**Integrantes:**
- Levi Bergamascki
- Lanna Carvalho

---

## Como executar localmente com Docker

### Pré-requisitos

- Docker Desktop instalado e em execução
- Java 17 (para execução sem Docker)
- Maven 3.8+

### Subindo a aplicação com Docker Compose

```bash
# Clone o repositório
git clone https://github.com/leviberga/esg-residuos.git
cd esg-residuos

# Configure as variáveis de ambiente
cp .env.example .env
# Edite o .env com suas credenciais do banco Oracle da FIAP

# Suba os containers
docker-compose up --build
```

A aplicação estará disponível em `http://localhost:8080`.

### Executando localmente sem Docker (perfil dev)

```bash
mvn -DskipTests spring-boot:run -Dspring-boot.run.profiles=dev
```

### Variáveis de ambiente necessárias

Crie um arquivo `.env` na raiz do projeto baseado no `.env.example`:

```env
SPRING_DATASOURCE_URL=jdbc:oracle:thin:@oracle.fiap.com.br:1521/ORCL
SPRING_DATASOURCE_USERNAME=SEU_RM
SPRING_DATASOURCE_PASSWORD=SUA_SENHA
SPRING_DATASOURCE_HIKARI_MAXIMUM_POOL_SIZE=1
SPRING_DATASOURCE_HIKARI_MINIMUM_IDLE=1
SPRING_DATASOURCE_HIKARI_CONNECTION_TIMEOUT=20000
SPRING_DATASOURCE_HIKARI_IDLE_TIMEOUT=300000
SPRING_DATASOURCE_HIKARI_MAX_LIFETIME=1200000
SPRING_DATASOURCE_HIKARI_LEAK_DETECTION_THRESHOLD=60000
JAVA_OPTS=-Xms256m -Xmx512m
```

---

## Pipeline CI/CD

### Ferramenta utilizada

**GitHub Actions** — plataforma de automação nativa do GitHub, integrada diretamente ao repositório.

### Arquivos de workflow

O projeto possui dois workflows em `.github/workflows/`:

| Arquivo | Gatilho | Objetivo |
|---|---|---|
| `ci-cd.yml` | Push em `main` ou `develop` | Pipeline completo de build, testes, Docker e deploy |
| `pr-check.yml` | Pull Request para `main` ou `develop` | Verificação rápida de qualidade antes do merge |

### Etapas do pipeline principal (`ci-cd.yml`)

**1. 🔨 Build & Testes**
- Sobe um banco Oracle XE como service container
- Configura o JDK 17 com cache Maven
- Executa os testes unitários com `mvn test`
- Gera o JAR com `mvn package -DskipTests`

**2. 🐳 Build & Push Docker**
- Realiza login no GitHub Container Registry (GHCR)
- Constrói a imagem Docker em multi-stage
- Faz push da imagem com a tag `latest` para `ghcr.io`

**3. 🚀 Deploy (Staging e Produção)**
- Utiliza `matrix strategy` para executar o deploy em paralelo nos dois ambientes
- Ambiente `staging`: simulado via log com IP configurado em `vars.STAGING_HOST`
- Ambiente `production`: simulado via log com IP configurado em `vars.PROD_HOST`
- Cada ambiente é um `environment` do GitHub Actions, podendo ter aprovação manual configurada

### Etapas do pipeline de PR (`pr-check.yml`)

- Executa os testes com banco H2 em memória (`-Dspring.profiles.active=test`)
- Gera relatório de cobertura com JaCoCo
- Faz upload do relatório como artefato (retido por 7 dias)
- Valida o Dockerfile com `docker build --target builder`

### Lógica de funcionamento

```
Push na main
     │
     ▼
┌─────────────────────┐
│  Build & Testes     │  ← Oracle XE como service, mvn test
└────────┬────────────┘
         │ (sucesso)
         ▼
┌─────────────────────┐
│  Docker Build/Push  │  ← Imagem publicada no GHCR
└────────┬────────────┘
         │ (sucesso)
         ▼
┌──────────────────────────────────┐
│  Deploy Staging  │  Deploy Prod  │  ← Matrix paralela
└──────────────────────────────────┘
```

---

## Containerização

### Dockerfile

O projeto utiliza um **Dockerfile multi-stage** para manter a imagem final enxuta:

```dockerfile
# Stage 1: builder — compila o projeto com Maven
FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -DskipTests package

# Stage 2: runtime — apenas o JRE e o JAR final
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
```

**Estratégias adotadas:**
- Multi-stage build: imagem de build separada da imagem de produção, reduzindo o tamanho final
- Base JRE (não JDK) no stage final: apenas o necessário para executar
- Cache de dependências Maven no stage de build

### Docker Compose

O `docker-compose.yml` orquestra a aplicação e o banco Oracle XE:

- **Variáveis de ambiente**: carregadas do arquivo `.env` via `env_file`
- **Volumes**: volume nomeado para persistência dos dados do Oracle
- **Redes**: rede interna para comunicação entre os serviços
- **Health check**: verificação de saúde do banco antes de iniciar a aplicação

---

## Endpoints principais

A API utiliza autenticação Basic Auth.

| Método | Endpoint | Autenticação | Descrição |
|---|---|---|---|
| POST | `/api/ponto-coleta` | admin/admin | Cadastra ponto de coleta |
| GET | `/api/pontos-coleta` | user/user ou admin/admin | Lista todos os pontos |
| GET | `/api/ponto-coleta/{id}` | user/user ou admin/admin | Busca ponto por ID |
| PUT | `/api/ponto-coleta/{id}` | admin/admin | Atualiza ponto de coleta |
| DELETE | `/api/ponto-coleta/{id}` | admin/admin | Remove ponto de coleta |
| POST | `/api/registro-coleta` | admin/admin | Registra uma coleta |
| GET | `/api/coletas/alertas` | user/user ou admin/admin | Lista alertas de coleta |

A collection Postman com exemplos está em `docs/postman_esg_residuos.postman_collection.json`.

---

## Tecnologias utilizadas

| Camada | Tecnologia |
|---|---|
| Linguagem | Java 17 |
| Framework | Spring Boot 3 |
| Segurança | Spring Security (Basic Auth) |
| Banco de dados | Oracle XE 21 |
| Migrações | Flyway |
| Build | Maven 3.8+ |
| Containerização | Docker + Docker Compose |
| CI/CD | GitHub Actions |
| Registry | GitHub Container Registry (GHCR) |
| Testes | JUnit 5, H2 (in-memory para testes) |
| Cobertura | JaCoCo |

---

🧪 Testes de Qualidade e BDD
O projeto adota a metodologia BDD (Behavior Driven Development) para validar as regras de negócio ESG.  

Cenários Gherkin Implementados
Cadastrar ponto de coleta (Governance): Valida o fluxo completo de criação de novos locais de descarte.  
+1

Segurança e Compliance: Garante que tentativas de acesso sem as devidas permissões sejam negadas.  
+1

Alertas de Coleta (Ambiental/Eficiência): Verifica o cálculo de volume acumulado versus volume máximo para gerar alertas de coleta.  
+1

Ferramentas de Teste
Cucumber: Execução dos cenários Gherkin.  
+1

RestAssured: Validação de API e Contratos JSON.  
+1

JSON Schema Validation: Garante que a resposta da API siga o contrato ponto_coleta_schema.json.  

Executar testes localmente:

Bash
mvn test -Dtest=RunCucumberTest

