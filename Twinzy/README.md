# Twinzy

Celebrity lookalike search powered by rich profile similarity — not name matching.

## Stack

- **Frontend:** Next.js 15, TypeScript, Tailwind CSS, Framer Motion, React Query (`frontend/`)
- **Backend:** Java 17, Spring Boot 3 (`backend/`)
- **Data:** JSON profile dataset with vector similarity search (`data/profiles.json`)

## Project Structure

```
Twinzy/
├── frontend/           # Next.js UI
├── backend/            # Spring Boot REST API
├── data/               # Generated profile dataset
└── scripts/seed/       # Demo data generator
```

## Getting Started

### 1. Install dependencies and seed data

```bash
pnpm install
pnpm seed:small   # fast demo dataset (210 profiles)
# pnpm seed       # full dataset (2100 profiles)
```

### 2. Run both services

**Option A — single command (recommended):**

```bash
pnpm dev
```

**Option B — separate terminals:**

```bash
# Terminal 1 — Spring Boot API on :8081
cd backend && mvn spring-boot:run

# Terminal 2 — Next.js UI on :3001
cd frontend && pnpm dev
```

Open [http://localhost:3001](http://localhost:3001), upload a photo, and browse your lookalike matches.

### Environment

Frontend (`frontend/.env.local`):

```
NEXT_PUBLIC_API_URL=http://localhost:8081
NEXT_PUBLIC_SITE_URL=http://localhost:3001
```

Backend (`backend/src/main/resources/application.yml`):

- `twinzy.data-path` — path to `data/profiles.json` (default: `../data/profiles.json`)
- `twinzy.cors.allowed-origins` — frontend origins

## API Endpoints (Spring Boot)

- `GET /api/health` — service health and profile count
- `POST /api/search` — upload image (`multipart/form-data`, field: `image`)
- `GET /api/search?sessionId=` — retrieve search session
- `GET /api/profiles/slugs` — all profile slugs (for sitemap/SSG)
- `GET /api/profiles/{slug}` — profile details with optional `sessionId` for similarity breakdown

## License

Proprietary
