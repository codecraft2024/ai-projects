# Mobile Simulator Framework

Reusable simulator foundation with **Calls** and first **Scenario**.

## Modules

| Module | Responsibility |
|--------|----------------|
| `simulator-common` | Shared DTOs, exceptions, constants, utilities |
| `simulator-config` | Properties, Jackson, RestTemplate |
| `simulator-calls` | HealthCheck, Bind1, Register1 services/clients |
| `simulator-scenarios` | Binding scenario (Health → Bind1 → Register1) |
| `simulator-api` | Thin REST controllers |
| `simulator-ui` | JavaFX screens |

## Calls

| Method | Path | Notes |
|--------|------|-------|
| `GET` | `/health/success` | Local healthy response |
| `GET` | `/health/failure` | Local failure response |
| `GET` | `/bind1/success` | Live InstaPay bind1 |
| `GET` | `/bind1/failure` | Live bind1 without `encString` |
| `GET` | `/register1/success` | Live InstaPay register1 |
| `GET` | `/register1/failure` | Live register1 without `appPin` (`code=100`) |

## Scenarios

| Method | Path | Steps |
|--------|------|-------|
| `GET` | `/scenarios/binding` | Health Check → Bind1 → Register1 |

## Run

```bash
export JAVA_HOME=/path/to/jdk-21
mvn clean install

cd simulator-api && mvn spring-boot:run
cd simulator-ui && mvn javafx:run
```
