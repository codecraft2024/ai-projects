# InstaSimulator

Enterprise mobile simulator platform.

## Modules

### Backend (Maven)

- `simulator-common` — shared DTOs and utilities
- `simulator-config` — configuration and HTTP clients
- `simulator-calls` — business logic for simulator calls
- `simulator-api` — thin Spring Boot REST controllers

### Frontend

- `simulator-web` — Next.js App Router UI (TypeScript, Tailwind, shadcn/ui)

## Run

### API

```bash
export JAVA_HOME=/path/to/jdk-21
mvn -pl simulator-api -am spring-boot:run
```

API: http://localhost:8080

Health endpoints:

- `GET /health/success`
- `GET /health/failure`

### Web UI

```bash
cd simulator-web
npm install
npm run dev
```

UI: http://localhost:3000

Set backend URL in `simulator-web/.env.local`:

```
NEXT_PUBLIC_API_BASE_URL=http://localhost:8080
```

## Current scope

- Dashboard with scenario cards
- Health Check scenario page with React Flow runtime visualization
- UI Automation → Health Check
- Light / dark theme
- Expandable navigation for Calls, Business Scenarios, Stress Testing, Settings
