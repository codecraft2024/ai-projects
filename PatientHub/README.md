# PatientHub Portal

PatientHub Portal is a monorepo-style project with a **Next.js** frontend and a **Spring Boot** backend. Each application lives in its own folder and can be developed, built, and run independently — or together via **Docker Compose**.

## Project structure

```
PatientHub/
├── frontend/              # Next.js + React + TypeScript + Tailwind CSS
├── backend/               # Java Spring Boot + Maven
├── docker-compose.yml     # Production stack (frontend + backend)
├── .env.example           # Compose environment template
└── README.md
```

### Frontend (`frontend/`)

- **Stack:** Next.js 16 (App Router), React 19, TypeScript, Tailwind CSS 4, next-intl
- **Purpose:** PatientHub Portal for **Dr. Mina Merzek Clinic** (orthopedic & bone clinic, Heliopolis, Cairo)
- **Features:** Responsive landing page, WhatsApp integration, admin login (demo), admin dashboard (mock data)
- **Key folders:**
  - `src/app/` — routes (`/`, `/doctor`, `/cases`, `/admin/login`, `/admin/dashboard`)
  - `src/components/` — layout, sections, admin, UI, icons
  - `src/messages/` — `en.json` / `ar.json` translations
  - `src/i18n/` — routing, navigation, middleware
  - `src/data/` — mock admin data
  - `src/types/` — TypeScript types
  - `src/hooks/` — React hooks (e.g. auth)
  - `src/services/` — client services (e.g. auth)
  - `src/utils/` — helpers (WhatsApp URLs, `cn`)

### Backend (`backend/`)

- **Stack:** Java 17, Spring Boot 3.4, Maven
- **Purpose:** REST API for PatientHub Portal (extensible)
- **Package layout (`com.patienthub`):**
  - `controller` — HTTP endpoints
  - `service` — business logic
  - `repository` — data access (ready for JPA)
  - `model` — domain/DTO types
  - `config` — application configuration (e.g. CORS)

## Architecture

```
┌─────────────────────────────────────────────────────────┐
│  Browser (http://localhost:3000)                        │
└───────────────────────────┬─────────────────────────────┘
                            │
              ┌─────────────▼─────────────┐
              │  frontend (Next.js :3000) │
              │  /api/health              │
              │  /en, /ar, sitemap, SEO   │
              └─────────────┬─────────────┘
                            │ CORS (future /api calls)
              ┌─────────────▼─────────────┐
              │  backend (Spring :8080)   │
              │  GET /health              │
              │  GET /actuator/health     │
              └───────────────────────────┘
```

The frontend is currently self-contained (static/mock data). The backend exposes CORS for browser calls to `/api/**` when API integration is added.

## Prerequisites

| Application | Requirements |
|-------------|--------------|
| Frontend    | Node.js 20+ and npm |
| Backend     | Java 17+ and Maven 3.9+ |
| Docker      | Docker Engine 24+ and Docker Compose v2 |

## Quick start (Docker — recommended)

```bash
cp .env.example .env          # optional — defaults work for local
docker compose up --build -d
```

| Service  | URL |
|----------|-----|
| Frontend | http://localhost:3000/en |
| Backend  | http://localhost:8080/health |

Check status:

```bash
docker compose ps
docker compose logs -f
curl http://localhost:8080/health
curl http://localhost:3000/api/health
```

Stop:

```bash
docker compose down
```

## Running the frontend (local)

```bash
cd frontend
cp .env.example .env.local   # optional
npm install
npm run dev
```

Open [http://localhost:3000/en](http://localhost:3000/en) (English) or [http://localhost:3000/ar](http://localhost:3000/ar) (Arabic — RTL).

**Admin demo login:** `/en/admin/login` or `/ar/admin/login` — username `test`, password `test`

### Languages & branding

- **Locales:** `/en` and `/ar` with `hreflang`, localized metadata, and `sitemap.xml` entries
- **Fonts:** Inter (English), Cairo (Arabic)
- **Brand colors:** Purple `#6D4AFF`, accent `#FFC107`
- **Language switcher:** Client-side navigation + cookie/localStorage persistence
- **SEO:** JSON-LD (MedicalClinic, Physician, FAQ, LocalBusiness), `robots.txt`, `manifest.webmanifest`, dynamic OG images
- **Analytics (optional):** Set `NEXT_PUBLIC_GA_ID` and/or `NEXT_PUBLIC_GTM_ID` in `frontend/.env.local`

**WhatsApp:** Links open chat with Mina Clinic at +20 122 192 6646.

### Social media configuration

All social URLs are centralized in `frontend/src/config/social-links.ts`:

| Constant | Purpose |
|----------|---------|
| `FACEBOOK_URL` | Clinic Facebook page |
| `INSTAGRAM_URL` | Instagram profile (empty = hidden) |
| `TWITTER_URL` | X (Twitter) profile (empty = hidden) |
| `WHATSAPP_URL` | Official `wa.me` deep link |

Set `NEXT_PUBLIC_SITE_URL` in `frontend/.env.local` for correct Open Graph previews when sharing (see `.env.example`).

**Pages with sharing:** `/`, `/doctor`, `/cases`

### Other frontend commands

```bash
npm run build    # production build
npm run start    # run production server
npm run lint     # ESLint
```

## Running the backend (local)

```bash
cd backend
cp .env.example .env           # optional
mvn spring-boot:run
```

The API starts on [http://localhost:8080](http://localhost:8080).

### Health check

```bash
curl http://localhost:8080/health
curl http://localhost:8080/actuator/health
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

Production profile (used in Docker):

```bash
SPRING_PROFILES_ACTIVE=prod mvn spring-boot:run
```

## Docker commands

### Build images individually

```bash
docker build -t patienthub-backend ./backend
docker build -t patienthub-frontend \
  --build-arg NEXT_PUBLIC_SITE_URL=http://localhost:3000 \
  --build-arg NEXT_PUBLIC_API_URL=http://localhost:8080 \
  ./frontend
```

### Push to Docker Hub

```bash
export DOCKERHUB_USER=yourusername

docker tag patienthub-backend  $DOCKERHUB_USER/patienthub-backend:latest
docker tag patienthub-frontend $DOCKERHUB_USER/patienthub-frontend:latest

docker login
docker push $DOCKERHUB_USER/patienthub-backend:latest
docker push $DOCKERHUB_USER/patienthub-frontend:latest
```

On a VPS, pull and run:

```bash
docker pull $DOCKERHUB_USER/patienthub-backend:latest
docker pull $DOCKERHUB_USER/patienthub-frontend:latest
# Update docker-compose.yml image: fields or use compose with pre-built images
FRONTEND_PUBLIC_URL=https://your-domain.com \
BACKEND_PUBLIC_URL=https://api.your-domain.com \
docker compose up -d
```

## Deploy steps (VPS / Cloud)

1. **Provision** a Linux VM (2 GB+ RAM) with Docker and Compose installed.
2. **Clone** the repo or pull pre-built images from Docker Hub.
3. **Configure** `.env`:
   - `FRONTEND_PUBLIC_URL=https://your-domain.com`
   - `BACKEND_PUBLIC_URL=https://api.your-domain.com` (or same host with reverse proxy)
4. **Build & start:** `docker compose up --build -d`
5. **Reverse proxy** (recommended): put Nginx or Caddy in front:
   - `your-domain.com` → frontend `:3000`
   - `api.your-domain.com` → backend `:8080`
6. **TLS:** use Let's Encrypt (Certbot or Caddy auto-HTTPS).
7. **Verify:** `curl https://api.your-domain.com/health` and open the site in a browser.

## Independent development

- The **frontend** does not require the backend to run for the landing page.
- The **backend** exposes CORS for the frontend origin on `/api/**` paths for future integration.
- Use separate terminals, version control workflows, and CI jobs per folder as needed.

## Next steps

- Add REST resources under `/api/` in the backend
- Connect the frontend contact form and portal features to backend APIs
- Add persistence (e.g. Spring Data JPA) in `repository/` and `model/`
