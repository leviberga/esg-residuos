# ESG Resíduos — Projeto

Este projeto é uma API RESTful para gestão de pontos de coleta e registros de coleta (tema: Gestão de Resíduos e Reciclagem) desenvolvida com Java 17 e Spring Boot.

Conteúdo deste repositório:
- `src/main` — código-fonte
- `src/main/resources/db/migration` — scripts Flyway (V1/V2/V3)
- `docker-compose.yml` — serviço Oracle XE para ambiente `dev`
- `Dockerfile` — multi-stage para empacotar a aplicação
- `docs/postman_esg_residuos.postman_collection.json` — coleção Postman com exemplos

---

Requisitos
- Java 17
- Maven 3.8+
- Docker (para executar o Oracle localmente)

Como rodar localmente (usar PowerShell no Windows)

1) Iniciar o banco Oracle (opcional, para perfil `dev`):

```powershell
docker-compose up -d
docker ps
```

2) Rodar a aplicação com profile `dev` (usa o Oracle do docker):

```powershell
mvn -DskipTests spring-boot:run -Dspring-boot.run.profiles=dev
```

ou gerar o jar e executar:

```powershell
mvn -DskipTests package
java -jar target\esg-residuos-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
```

3) Importar a collection do Postman:

- Abra o Postman → Import → selecione `docs/postman_esg_residuos.postman_collection.json`.
- Teste os endpoints (ex.: POST `/api/ponto-coleta` usa credenciais admin/admin; GET usa user/user ou admin/admin).

Endpoints principais
- POST /api/ponto-coleta (admin)
- GET  /api/pontos-coleta (user/admin)
- GET  /api/ponto-coleta/{id} (user/admin)
- PUT  /api/ponto-coleta/{id} (admin)
- DELETE /api/ponto-coleta/{id} (admin)
- GET /api/coletas/alertas (user/admin)
- POST /api/registro-coleta (admin)

Testes

Executar os testes (unit + integração H2):

```powershell
mvn test
```
Teste 

Observações / Entrega
- A solução usa Oracle em produção/dev (configurado via profiles). As migrações Flyway estão em `src/main/resources/db/migration` (V1 cria tabelas e sequências, V2 cria índices, V3 insere dados de exemplo).
- Se o IntelliJ apresentar erro ao executar pelo Run, use `mvn spring-boot:run` ou ajuste o classpath do módulo no IDE (Lombok deve ser `provided` e plugin Lombok atualizado).
