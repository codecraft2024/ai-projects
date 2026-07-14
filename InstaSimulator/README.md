# InstaSimulator

Enterprise InstaPay mobile simulator platform.

## Backend modules

| Module | Purpose |
|--------|---------|
| `simulator-common` | DTOs, exceptions, utilities |
| `simulator-config` | Configuration, HTTP clients |
| `simulator-calls` | Health Check, Bind1, Register1 |
| `simulator-scenarios` | Binding scenario (Health → Bind1 → Register1) |
| `simulator-api` | REST controllers |

## Frontend

`simulator-web` — Next.js App Router, TypeScript, Tailwind, shadcn/ui

### Navigation

- **Dashboard** — overview of calls and scenarios
- **Calls** — Health Check, Bind1, Register1
- **Scenarios** — Binding scenario
- **Settings** — backend URL

## Run

```bash
# API
export JAVA_HOME=/path/to/jdk-21
mvn -pl simulator-api -am spring-boot:run

# Web
cd simulator-web && npm install && npm run dev
```

- API: http://localhost:8080  
- UI: http://localhost:3000  

## API endpoints

| Endpoint | Description |
|----------|-------------|
| `GET /health/success` | Health check success |
| `GET /health/failure` | Health check failure |
| `GET /bind1/success` | Bind1 success path |
| `GET /bind1/failure` | Bind1 failure path |
| `GET /register1/success` | Register1 success path |
| `GET /register1/failure` | Register1 failure path |
| `GET /scenarios/binding` | Run binding scenario |
