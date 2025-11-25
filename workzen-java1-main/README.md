# Face Recognition API

API REST para análise de emoções faciais utilizando Spring Boot e integração com DeepFace.

## 📋 Sobre o Projeto

Esta aplicação permite que usuários façam upload de imagens faciais em formato base64 e recebam análises de emoções. O sistema utiliza autenticação JWT para garantir segurança e integra-se com uma API externa de análise facial (DeepFace).

## 🚀 Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Security** - Autenticação e autorização
- **Spring Data JPA** - Persistência de dados
- **PostgreSQL** - Banco de dados
- **JWT (JJWT 0.12.3)** - Tokens de autenticação
- **MapStruct** - Mapeamento de objetos
- **Lombok** - Redução de boilerplate
- **Caffeine Cache** - Cache em memória
- **Spring WebFlux** - Cliente HTTP reativo
- **SpringDoc OpenAPI** - Documentação Swagger
- **Maven** - Gerenciamento de dependências

## 📦 Pré-requisitos

Antes de começar, você precisa ter instalado:

- **Java 17** ou superior
- **Maven 3.6+**
- **PostgreSQL 12+**
- **API DeepFace** rodando (opcional, pode ser configurada via variáveis de ambiente)

## ⚙️ Configuração

### 1. Clone o repositório

```bash
git clone <url-do-repositorio>
cd face.recognization
```

### 2. Configure o banco de dados PostgreSQL

Crie um banco de dados PostgreSQL:

```sql
CREATE DATABASE face_recognition;
```

### 3. Configure as variáveis de ambiente

Crie um arquivo `.env` ou configure as variáveis de ambiente:

```bash
# Database
DATASOURCE_URL=jdbc:postgresql://localhost:5432/face_recognition
DATASOURCE_USERNAME=postgres
DATASOURCE_PASSWORD=postgres
DATASOURCE_DRIVER=org.postgresql.Driver

# JPA
JPA_DDL_AUTO=update
JPA_SHOW_SQL=true
JPA_DIALECT=org.hibernate.dialect.PostgreSQLDialect
JPA_FORMAT_SQL=true

# JWT
JWT_SECRET=mySecretKeyForJWTTokenGenerationMustBeAtLeast256BitsLongForHS512Algorithm
JWT_EXPIRATION=86400000

# DeepFace API
DEEPFACE_API_URL=http://localhost:8000

# Cache
CACHE_TYPE=caffeine
CACHE_NAMES=faceAnalyses
```

**⚠️ Importante:** Altere o `JWT_SECRET` para uma chave segura em produção!

### 4. Configure a API DeepFace (Opcional)

Se você tiver uma API DeepFace rodando, configure a URL. Caso contrário, a aplicação tentará se conectar em `http://localhost:8000`.

## 🏃 Executando a Aplicação

### Usando Maven

```bash
# Compilar o projeto
mvn clean install

# Executar a aplicação
mvn spring-boot:run
```

### Usando o wrapper Maven

```bash
# Windows
./mvnw.cmd spring-boot:run

# Linux/Mac
./mvnw spring-boot:run
```

A aplicação estará disponível em: `http://localhost:8080`

## 📚 Documentação da API

Após iniciar a aplicação, acesse a documentação Swagger:

- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **API Docs (JSON):** http://localhost:8080/v3/api-docs

## 🔐 Autenticação

A API utiliza autenticação JWT (JSON Web Token). Para acessar os endpoints protegidos:

1. **Registre um novo usuário:**
```bash
POST /api/auth/register
Content-Type: application/json

{
  "name": "João Silva",
  "email": "joao@example.com",
  "password": "senha123"
}
```

2. **Faça login:**
```bash
POST /api/auth/login
Content-Type: application/json

{
  "email": "joao@example.com",
  "password": "senha123"
}
```

3. **Use o token nas requisições:**
```bash
Authorization: Bearer <seu-token-jwt>
```

## 📡 Endpoints da API

### Autenticação

| Método | Endpoint | Descrição | Autenticação |
|--------|----------|-----------|--------------|
| POST | `/api/auth/register` | Registrar novo usuário | Não |
| POST | `/api/auth/login` | Fazer login | Não |

### Análise Facial

| Método | Endpoint | Descrição | Autenticação |
|--------|----------|-----------|--------------|
| POST | `/api/face/analyse` | Criar análise facial | Sim |
| GET | `/api/face/{id}` | Buscar análise por ID | Sim |
| GET | `/api/face` | Listar análises (paginado) | Sim |
| PUT | `/api/face/{id}` | Atualizar análise | Sim |
| DELETE | `/api/face/{id}` | Deletar análise | Sim |

### Exemplos de Uso

#### Criar Análise Facial

```bash
POST /api/face/analyse
Authorization: Bearer <token>
Content-Type: application/json

{
  "image_base64": "iVBORw0KGgoAAAANSUhEUgAA..."
}
```

**Resposta:**
```json
{
  "id": 1,
  "userId": 1,
  "emotion": "happy",
  "analysisDate": "2025-11-23T10:30:00",
  "createdAt": "2025-11-23T10:30:00"
}
```

#### Listar Análises (Paginado)

```bash
GET /api/face?page=0&size=10&sort=createdAt,desc
Authorization: Bearer <token>
```

**Parâmetros de paginação:**
- `page`: Número da página (padrão: 0)
- `size`: Tamanho da página (padrão: 20)
- `sort`: Campo de ordenação (ex: `createdAt,desc`)

**Campos válidos para ordenação:**
- `id`
- `createdAt`
- `updatedAt`
- `analysisDate`
- `emotion`

## 🏗️ Estrutura do Projeto

```
src/main/java/com/ai/fiap/face/recognization/
├── Application.java                 # Classe principal
├── client/                          # Cliente HTTP para DeepFace
│   ├── DeepFaceClient.java
│   └── impl/
│       └── DeepFaceClientImpl.java
├── config/                          # Configurações
│   ├── CacheConfig.java
│   ├── JwtConfig.java
│   ├── SecurityConfig.java
│   ├── SwaggerConfig.java
│   └── WebClientConfig.java
├── controller/                      # Controllers REST
│   ├── AuthController.java
│   └── FaceAnalysisController.java
├── dto/                             # Data Transfer Objects
│   ├── request/
│   └── response/
├── exception/                       # Tratamento de exceções
│   ├── BusinessException.java
│   ├── GlobalExceptionHandler.java
│   ├── ResourceNotFoundException.java
│   └── UnauthorizedException.java
├── mapper/                          # MapStruct mappers
│   ├── FaceAnalysisMapper.java
│   └── UserMapper.java
├── model/                           # Entidades JPA
│   ├── FaceAnalysis.java
│   └── User.java
├── repository/                      # Repositórios JPA
│   ├── FaceAnalysisRepository.java
│   └── UserRepository.java
└── service/                         # Lógica de negócio
    ├── AuthService.java
    ├── FaceAnalysisService.java
    ├── JwtService.java
    └── impl/
        ├── AuthServiceImpl.java
        ├── FaceAnalysisServiceImpl.java
        └── JwtServiceImpl.java
```

## 🔧 Configurações Avançadas

### Cache

A aplicação utiliza Caffeine Cache para melhorar a performance. O cache é configurado para armazenar análises faciais e é invalidado automaticamente quando há operações de escrita.

### Logging

Os logs são configurados via `logback-spring.xml` e são salvos em `logs/application.log`.

### Perfis

A aplicação suporta perfis Spring. O perfil padrão é `dev`, configurado em `application.properties`.

## 🧪 Testes

Para executar os testes:

```bash
mvn test
```

## 🐛 Troubleshooting

### Erro de conexão com banco de dados

Verifique se o PostgreSQL está rodando e se as credenciais estão corretas nas variáveis de ambiente.

### Erro ao acessar LOB no PostgreSQL

Se você encontrar o erro "Unable to access lob stream", certifique-se de que:
- O campo `imageBase64` não está marcado com `@Lob` (já corrigido no código)
- As transações estão configuradas corretamente

### Erro de autenticação

Verifique se:
- O token JWT está sendo enviado no header `Authorization`
- O token não expirou (padrão: 24 horas)
- O formato está correto: `Bearer <token>`



