# InstaSimulator Web UI

React page for the **Binding Scenario** (Health Check → Bind1 → Register1).

## Run

Terminal 1 — API:

```bash
cd simulator-api
mvn spring-boot:run
```

Terminal 2 — Web:

```bash
cd simulator-web
npm install
npm run dev
```

Open http://localhost:5173

The Vite dev server proxies `/scenarios` to `http://localhost:8080`.
