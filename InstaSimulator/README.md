# Mobile Simulator Framework

Minimal reusable simulator foundation. Only **Calls / Health Check** is implemented.

## Modules

| Module | Responsibility |
|--------|----------------|
| `simulator-common` | Shared DTOs, exceptions, constants, utilities |
| `simulator-config` | Properties, Jackson, RestTemplate — no business logic |
| `simulator-calls` | Reusable `HealthCheckService` + `HealthCheckClient` |
| `simulator-api` | Thin REST controllers only |
| `simulator-ui` | JavaFX Health Check screen |

```
UI / Tests / future Scenarios & Stress
                │
                ▼
        HealthCheckService   (simulator-calls)
                │
                ▼
        HealthCheckController (simulator-api)
```

## Endpoints

| Method | Path | Body |
|--------|------|------|
| `GET` | `/health/success` | `{"success":true,"message":"Service is healthy"}` |
| `GET` | `/health/failure` | `{"success":false,"message":"Service unavailable"}` |

Set `simulator.health-check.failure-throws=true` to throw on failure instead.

## Run

```bash
export JAVA_HOME=/path/to/jdk-21
mvn clean install

# API
cd simulator-api && mvn spring-boot:run

# UI (calls HealthCheckService in-process)
cd simulator-ui && mvn javafx:run
```

## Future

`simulator-scenarios` and `simulator-stress` can depend on `simulator-calls` without changing this layout.
