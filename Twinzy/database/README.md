# Twinzy Database

PostgreSQL stores all profile data, embeddings, and images metadata.

## Quick start

```bash
# From project root
pnpm db:up          # Start Postgres on localhost:5433
pnpm seed:small     # Seed 500 profiles locally (dev)
pnpm seed           # Seed 20,000 profiles locally (full)

# Or with Docker
pnpm docker:up
pnpm docker:seed
```

## Connection

| Setting  | Value        |
|----------|--------------|
| Host     | `localhost`  |
| Port     | `5433`       |
| Database | `twinzy`     |
| User     | `twinzy`     |
| Password | `twinzy`     |

Copy `.env.example` to `.env` if you need local overrides.

## Folder layout

```
database/
├── docker-compose.yml   # Local Postgres 16
├── generated/           # Previously generated JSON data, kept for reference
├── legacy/              # Old root folders moved here to keep root clean
├── migrations/          # Flyway SQL schema
│   └── V1__init_schema.sql
└── seed/                # Curated seed catalogs (JSON)
    ├── celebrities.json
    └── funny-objects.json
```

## How seeding works

1. Flyway applies migrations on backend startup.
2. `pnpm seed` runs the Java seed profile (`DatasetSeedService`).
3. Seed reads catalogs from `database/seed/`, fetches Wikimedia portraits, analyzes images, and writes to Postgres.

## Stop database

```bash
pnpm db:down
```
