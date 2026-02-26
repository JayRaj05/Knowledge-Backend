## Knowledge Backend – Spring Boot (OpenAI Integration)

This is the **backend service** for the Knowledge application, built with **Spring Boot**.  
It exposes REST APIs and integrates with the **OpenAI API** to improve and process textual content (e.g., articles, answers, or documentation).

This README is written for handover to a company or another team and explains:

- What the backend does
- Which AI tools were used:
  - For **development assistance** (this README and some code were created with help from OpenAI’s GPT-based assistant)
  - For **runtime API calls** (the backend calls the **OpenAI API**)
- How to set up, run, and test the backend locally

---

### 1. Overview

The backend provides REST endpoints that:

- Accept input text (for example: articles, explanations, answers)
- Call the **OpenAI API** to:
  - Improve clarity, grammar, and tone
  - Optionally summarize or rephrase text
- Return the improved/processed text as JSON to the client (frontend or other services)

The project is implemented as a standard **Spring Boot** application.

---

### 2. Tech Stack

- **Language**: Java (version defined in `pom.xml`, currently Java 17)
- **Framework**: Spring Boot
- **Build Tool**: Maven
- **Database**: MySQL (via Spring Data JPA)
- **Security & Auth**: Spring Security + JWT
- **External AI Service**: **OpenAI API** (for text / article improvement)
- **HTTP Client**: Spring’s HTTP facilities (`RestTemplate` / `WebClient` or similar)
- **Config**: Externalized via application properties / environment variables

---

### 3. AI & Tools Used

#### 3.1. AI Used for Development Help

- This README and some parts of the backend design/documentation were created with assistance from **OpenAI’s GPT-based coding assistant** (via Cursor).
- The AI was used as a **reference and writing assistant** only; all final decisions and code are under the control of the development team.

#### 3.2. AI Used at Runtime (API Calls)

- The backend **calls the OpenAI API** at runtime to improve or process text.
- Typical usage:
  - Receive text from the client
  - Build a prompt with instructions (e.g., “Improve grammar, keep meaning, keep technical terms”)
  - Send it to an OpenAI model (e.g., `gpt-4.1`, `gpt-4o`, or similar)
  - Return the improved text (and optionally summaries or tags) to the client

All OpenAI calls are made **from the backend only**; the API key is never exposed to the frontend.

---

### 4. Project Structure (high level)

Main directories of interest (packages may vary slightly):

- `src/main/java/com/example/knowledge/`
  - `KnowledgeSharingApplication.java` – Spring Boot main class
  - `ai/` – AI-related controllers, DTOs, and services for OpenAI integration
  - `article/` – article entities, repositories, services, controllers
  - `auth/` – authentication controllers and DTOs
  - `user/` – user entities and repositories
  - `config/` – security and application configuration (including JWT)
  - `common/` – shared classes such as global exception handling
- `src/main/resources/application.yml` – application configuration (DB, ports, etc.)
- `pom.xml` – Maven build and dependency configuration

---

### 5. Prerequisites

- **Java**: 17 (as specified in `pom.xml`)
- **Maven**: Installed and available on your PATH (`mvn -v`)
- **MySQL**:
  - Running instance
  - Database created for this project
- **OpenAI Account**:
  - Valid **OpenAI API key** with access to text models

---

### 6. Configuration & Environment Variables

Core configuration is in `src/main/resources/application.yml` and/or environment variables.

Typical configuration:

- **Database** (example only – match to your real values):

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/knowledge_db
    username: your_db_user
    password: your_db_password
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
```

- **OpenAI** (pattern – actual mapping depends on how `AiService` is implemented):

```yaml
openai:
  api-key: ${OPENAI_API_KEY}
  model: gpt-4.1
  temperature: 0.3
```

Set the environment variables before running (example for macOS/Linux):

```bash
export OPENAI_API_KEY=your_openai_api_key_here
export SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/knowledge_db
export SPRING_DATASOURCE_USERNAME=your_db_user
export SPRING_DATASOURCE_PASSWORD=your_db_password
```

Or configure the same values directly in `application.yml` for local development (avoid committing secrets).

**Important**:  
Do **not** commit your API key. Keep it in environment variables or a secure secrets manager.

---

### 7. How to Build and Run (Maven)

From the backend project root (this folder, where `pom.xml` is located):

```bash
# Clean and build the project
mvn clean package

# Run the Spring Boot application
mvn spring-boot:run
```

Alternatively, run the generated JAR:

```bash
java -jar target/knowledge-sharing-backend-0.0.1-SNAPSHOT.jar
```

*(Adapt the JAR name if it changes.)*

By default, the backend is available on:

- `http://localhost:8080`

unless a different port is configured in `application.yml` or via `SERVER_PORT` / `server.port`.

---

### 8. Main API Endpoints (Examples)

Your project contains multiple controllers (auth, articles, AI).  
Below is a typical pattern for the AI-related endpoints (adjust path names to match the real controller mappings in `AiController`).

#### 8.1. Improve Text / Article

**POST** `/api/ai/improve`

**Request (JSON example)**:

```json
{
  "text": "Original article or paragraph goes here...",
  "tone": "professional",
  "instructions": "Improve clarity and grammar but keep the meaning the same."
}
```

**Response (JSON example)**:

```json
{
  "originalText": "Original article or paragraph goes here...",
  "improvedText": "Improved version of the article with better grammar and clarity.",
  "meta": {
    "model": "gpt-4.1",
    "temperature": 0.3
  }
}
```

If an error occurs (for example, invalid key or OpenAI outage), the backend returns a JSON error with an appropriate HTTP status code (e.g., `4xx` or `5xx`), handled by the global exception handler.

---

### 9. How the Company Can Use This Backend

- **Locally (developers)**:
  - Configure database and OpenAI API key
  - Build and run with Maven
  - Call the REST endpoints from Postman, frontend, or other services

- **In a Company / Production Environment**:
  - Deploy the Spring Boot JAR (or Docker image) to your preferred environment:
    - On-premise server
    - Cloud VM
    - Container platform (Kubernetes, ECS, etc.)
  - Set `OPENAI_API_KEY` and other sensitive values as **secure environment variables** or via a **secrets manager**
  - Configure logging, monitoring, and backups following company standards

---

### 10. Summary

- **Backend**: Spring Boot (Java 17, Maven)
- **Core Features**: Authentication, article management, AI-assisted text processing
- **AI Help for Development**: Documentation and some code were created/assisted by **OpenAI GPT-based assistant**.
- **Runtime AI Integration**: Backend **calls the OpenAI API** to improve and process text.
- **Usage**: Start the Spring Boot app, configure database and `OPENAI_API_KEY`, and call the REST endpoints (e.g., `/api/ai/improve`).

This README is backend-only and clearly documents which AI was used and how the OpenAI API is integrated.

