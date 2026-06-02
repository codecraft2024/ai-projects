# PatientHub Portal

PatientHub Portal is a monorepo-style project with a **Next.js** frontend and a **Spring Boot** backend. Each application lives in its own folder and can be developed, built, and run independently.

## Project structure

```
PatientHub/
├── frontend/          # Next.js + React + TypeScript + Tailwind CSS
├── backend/           # Java Spring Boot + Maven
└── README.md
```

### Frontend (`frontend/`)

- **Stack:** Next.js (App Router), React, TypeScript, ESLint, Tailwind CSS
- **Purpose:** Patient-facing landing page and future portal UI
- **Key folders:**
  - `src/app/` — routes and layout
  - `src/components/` — layout, sections, UI, icons
  - `src/constants/` — site content and configuration

### Backend (`backend/`)

- **Stack:** Java 21, Spring Boot 3, Maven
- **Purpose:** REST API for PatientHub Portal (extensible)
- **Package layout (`com.patienthub`):**
  - `controller` — HTTP endpoints
  - `service` — business logic
  - `repository` — data access (ready for JPA)
  - `model` — domain/DTO types
  - `config` — application configuration (e.g. CORS)

## Prerequisites

| Application | Requirements |
|-------------|--------------|
| Frontend    | Node.js 20+ and npm |
| Backend     | Java 17+ and Maven 3.9+ |

## Running the frontend

```bash
cd frontend
npm install
npm run dev
```

Open [http://localhost:3000](http://localhost:3000) to view the PatientHub landing page.

### Other frontend commands

```bash
npm run build    # production build
npm run start    # run production server
npm run lint     # ESLint
```

## Running the backend

```bash
cd backend
mvn spring-boot:run
```

The API starts on [http://localhost:8080](http://localhost:8080).

### Health check

```bash
curl http://localhost:8080/health
```

Example response:

```json
{
  "status": "UP",
  "message": "PatientHub API is running"
}
```

### Other backend commands

```bash
mvn test              # run unit tests
mvn package           # build JAR
java -jar target/patienthub-backend-0.0.1-SNAPSHOT.jar
```

Run with the `dev` profile:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

## Independent development

- The **frontend** does not require the backend to run for the landing page.
- The **backend** exposes CORS for `http://localhost:3000` on `/api/**` paths for future integration.
- Use separate terminals, version control workflows, and CI jobs per folder as needed.

## Next steps

- Add REST resources under `/api/` in the backend
- Connect the frontend contact form and portal features to backend APIs
- Add persistence (e.g. Spring Data JPA) in `repository/` and `model/`
