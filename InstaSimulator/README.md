# Mobile App Simulation Framework

Professional multi-module Java framework that simulates real mobile application behavior through sequenced API calls, business scenarios, stress testing, reporting, and a separate Appium Android UI automation sample.

## Architecture

```
┌─────────────────┐     ┌──────────────────┐     ┌─────────────────┐
│  simulator-api  │────▶│ simulator-engine │────▶│ simulator-calls │
│  simulator-cli  │     │  (sessions,      │     │  (LoginCall,    │
└─────────────────┘     │   executor)      │     │   TransferCall) │
                        └────────┬─────────┘     └────────▲────────┘
                                 │                        │
                        ┌────────▼─────────┐     ┌────────┴────────┐
                        │ business-scenarios│────▶│ simulator-core  │
                        │ (user journeys)  │     │ (ApiCall DSL)   │
                        └──────────────────┘     └─────────────────┘
                                 │
         ┌───────────────────────┼───────────────────────┐
         ▼                       ▼                       ▼
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│ simulator-stress│    │simulator-reports│    │simulator-config │
└─────────────────┘    └─────────────────┘    └─────────────────┘

Shared: simulator-common (HTTP client, context, DTOs, utils)
Independent: automation-sample (Appium 2 / UiAutomator2 POM)
```

**Clean architecture layers**

| Layer | Module | Responsibility |
|-------|--------|----------------|
| API | `simulator-api` | REST controllers |
| CLI | `simulator-cli` | Command-line entry |
| Stress | `simulator-stress` | Virtual users, ramp-up/down |
| Engine | `simulator-engine` | Scenario execution, sessions, metrics |
| Scenarios | `simulator-business-scenarios` | User journeys |
| Calls | `simulator-calls` | One class per API call |
| Core | `simulator-core` | Abstractions, DSL, retry, metrics |
| Config | `simulator-config` | YAML + properties |
| Common | `simulator-common` | HTTP, DTOs, context |
| Reports | `simulator-reports` | HTML / CSV / JSON |
| Automation | `automation-sample` | Appium UI tests |

A built-in **demo backend** (`DemoBackendController`) serves `/api/v1/**` inside `simulator-api`, so you can run end-to-end simulations without an external mobile backend.

---

## Prerequisites

- JDK 21+
- Maven 3.9+
- (Optional) Appium 2 + Android Emulator for UI tests

---

## Build

```bash
mvn clean install
```

Appium tests are skipped by default (`skipAppiumTests=true`).

---

## Run the REST API

```bash
cd simulator-api
mvn spring-boot:run
```

Server starts on **http://localhost:8080**.

### Endpoints

| Method | Path | Description |
|--------|------|-------------|
| `POST` | `/scenario/run` | Execute a business scenario |
| `POST` | `/stress/start` | Start a stress job |
| `POST` | `/stress/stop/{jobId}` | Gracefully stop a stress job |
| `GET` | `/status` | Scenarios, metrics, stress jobs |
| `GET` | `/metrics` | In-memory metrics snapshot |
| `GET` | `/reports` | Generate HTML/CSV/JSON reports |

### Example: run a scenario

```bash
curl -X POST http://localhost:8080/scenario/run \
  -H 'Content-Type: application/json' \
  -d '{"scenarioName":"daily-user"}'
```

### Example: start stress (100 users)

```bash
curl -X POST http://localhost:8080/stress/start \
  -H 'Content-Type: application/json' \
  -d '{
    "scenarioName": "login-scenario",
    "virtualUsers": 100,
    "durationSeconds": 60,
    "rampUpSeconds": 10,
    "thinkTimeMinMs": 200,
    "thinkTimeMaxMs": 1500
  }'
```

Supported user counts: **10 / 100 / 500 / 1000 / 10000** (any positive integer).

---

## Run via CLI

Start the API (demo backend) first, then:

```bash
cd simulator-cli
mvn spring-boot:run -Dspring-boot.run.arguments="list"
mvn spring-boot:run -Dspring-boot.run.arguments="run -s daily-user"
mvn spring-boot:run -Dspring-boot.run.arguments="stress -s login-scenario -u 50 -d 30"
```

---

## Folder explanation

| Folder | Purpose |
|--------|---------|
| `simulator-calls` | Independent API call classes (`execute` / `validate` / retry / logging / timing) |
| `simulator-business-scenarios` | Complete user journeys composed from calls |
| `simulator-stress` | Load testing with virtual users, think time, ramp-up/down |
| `simulator-engine` | Runs scenarios, manages sessions, correlation IDs, metrics |
| `simulator-api` | Spring Boot REST + demo backend |
| `simulator-config` | `application.yml`, scenario YAML, user pools |
| `simulator-common` | Shared DTOs, HTTP client, `SimulationContext`, utils |
| `simulator-reports` | HTML, CSV, JSON reports with percentiles |
| `simulator-cli` | Picocli CLI |
| `automation-sample` | Standalone Appium Page Object sample |

---

## How to add a new Call

1. Create a class under `simulator-calls` extending `AbstractApiCall`.
2. Implement `name()`, `doExecute()`, and `validate()`.
3. Annotate with `@Component`.

```java
@Component
public class GetCardsCall extends AbstractApiCall {

    public GetCardsCall(SimulatorHttpClient httpClient, SimulatorProperties properties) {
        super(httpClient, properties);
    }

    @Override
    public String name() {
        return "get-cards";
    }

    @Override
    protected HttpResponse doExecute(SimulationContext context) {
        return httpClient.get(url("/api/v1/cards"), defaultHeaders(context));
    }

    @Override
    public void validate(SimulationContext context, CallResult result) {
        requireSuccess(result, "Get cards failed");
        context.put("cards", result.getResponseBody());
    }
}
```

The call is auto-discovered by Spring and can be injected into scenarios.

---

## How to create a Scenario

**Option A — Spring component**

```java
@Component
public class CardsScenario implements BusinessScenario {
    private final List<ApiCall> steps;

    public CardsScenario(LoginCall login, GetCardsCall cards, LogoutCall logout) {
        this.steps = List.of(login, cards, logout);
    }

    @Override public String name() { return "cards"; }
    @Override public String description() { return "Login → cards → logout"; }
    @Override public List<ApiCall> steps() { return steps; }
}
```

**Option B — Fluent DSL**

```java
BusinessScenario scenario = ScenarioBuilder.create("quick-transfer")
    .description("Minimal transfer flow")
    .step(new LoginCall(...))
    .step(new TransferCall(...))
    .step(new LogoutCall(...))
    .build();
```

---

## Simulation context

Every call receives a `SimulationContext` holding:

- User ID, tokens, session ID, correlation ID  
- Accounts / balances / profile  
- Current scenario name  
- Arbitrary shared attributes (`context.put` / `context.get`)

---

## Stress testing

`StressRunner` supports:

- Virtual users via virtual threads  
- Random think time  
- Ramp-up / ramp-down  
- Loop execution  
- Live statistics (avg, min, max, p50/p90/p95/p99)  
- Graceful stop  

---

## Reports

`GET /reports` (or CLI after a run) writes to `reports/`:

- `report-*.html` — summary dashboard  
- `report-*.csv` — tabular results  
- `report-*.json` — machine-readable  

Includes average duration, percentiles, and failure details.

---

## Configuration

Primary file: `simulator-api/src/main/resources/application.yml`

```yaml
simulator:
  base-url: http://localhost:8080
  timeouts:
    connect-ms: 5000
    read-ms: 30000
  retry:
    max-attempts: 3
    backoff-ms: 500
```

Scenario YAML: `simulator-config/src/main/resources/scenarios/`  
User pool: `simulator-config/src/main/resources/users/user-pool.yml`

Point `simulator.base-url` at your real mobile backend when ready; keep or remove the demo controller as needed.

---

## How to run Appium tests

1. Start an Android emulator.  
2. Install your APK (or set `appium.app` in `automation-sample/src/main/resources/appium.properties`).  
3. Start Appium 2:

```bash
appium --base-path /
```

4. Run tests:

```bash
cd automation-sample
mvn test -DskipAppiumTests=false
```

Sample pages: `LoginPage`, `HomePage`, `TransferPage`, `SettingsPage`, `BasePage`  
Sample test: `LoginTest` — launches app, enters credentials, verifies home screen.

Desired capabilities sample: `automation-sample/src/main/resources/desired-capabilities.json`

---

## Built-in scenarios

| Name | Flow |
|------|------|
| `login-scenario` | Login → profile → logout |
| `daily-user` | Login → profile → accounts → balance → transactions → logout |
| `transfer-money` | Login → accounts → balance → transfer → balance → logout |
| `power-user` | Extended multi-feature journey |
| `bill-payment` | Login → accounts → bill pay → logout |
| `donation` | Login → accounts → donate → logout |
| `cash-out` | Login → accounts → balance → cash-out → logout |
| `registration` | First-time login journey |

---

## Best practices

1. **One call = one class** — keep HTTP + validation local to the call.  
2. **Scenarios only compose calls** — no raw HTTP in scenarios.  
3. **Pass state via `SimulationContext`** — never use static mutable state.  
4. **Prefer composition** — inject calls; avoid deep inheritance beyond `AbstractApiCall`.  
5. **Log correlation IDs** — already included on every call.  
6. **Tune think time** — match real mobile user pauses in stress tests.  
7. **Keep automation-sample independent** — do not couple UI tests to the simulator engine.  
8. **Point to real backends** via `simulator.base-url` for production-like simulation.

---

## Import into IntelliJ IDEA

1. **File → Open** the repository root (`pom.xml`).  
2. Trust the Maven project and wait for indexing.  
3. Use the `SimulatorApiApplication` run configuration (or `mvn spring-boot:run` in `simulator-api`).

---

## Tech stack

Java 21 · Spring Boot 3.3 · Maven · Lombok · Jackson · Spring Validation · Spring Retry · Spring Scheduling · SLF4J/Logback · Apache HttpClient 5 · JUnit 5 · Mockito · Picocli · Appium 2 · TestNG · UiAutomator2
