# Twinzy

Celebrity lookalike search — upload a selfie, get your top 12 matches plus a funny twin.

## Project layout

```
Twinzy/
├── backend/     # Spring Boot API (Java 17)
├── frontend/    # Next.js 15 app
└── database/    # PostgreSQL, migrations, seed catalogs
```

## Quick start (local)

**Requirements:** Node 20+, Java 17+, Maven, Docker

```bash
pnpm install
pnpm db:up        # Start Postgres
pnpm seed:small   # Seed 500 profiles (first time)
pnpm dev          # backend :8081 + frontend :3001
```

- Frontend: http://localhost:3001
- Backend: http://localhost:8081
- Postgres: `localhost:5433` (user/pass/db: `twinzy`)

## Quick start (Docker)

```bash
pnpm docker:build
pnpm docker:up
pnpm docker:seed  # Seed 500 profiles (first time)
```

- Frontend: http://localhost:3001
- Backend: http://localhost:8081
- Postgres: `localhost:5433`

## Database

```bash
pnpm db:up        # Start PostgreSQL
pnpm db:down      # Stop PostgreSQL
pnpm seed:small   # Seed 500 profiles (dev)
pnpm seed         # Seed 20,000 profiles (full, ~30–60 min)
```

See [database/README.md](database/README.md) for schema and connection details.

## Features

- Multi-signal face matching (pixel embedding + features + traits)
- Top **12** celebrity matches per search
- 74 curated celebrities + Wikimedia portrait bulk seed
- EN/AR language switch, image crop, share buttons
- Paginated discover gallery
