# PatientHub Portal

PatientHub Portal is a monorepo-style project with a **Next.js** frontend and a **Spring Boot** backend. Each application lives in its own folder and can be developed, built, and run independently.

## Project structure

```
PatientHub/
├── frontend/          # Next.js + React + TypeScript + Tailwind CSS
├── backend/           # Java Spring Boot + Maven
└── README.md
```

### Frontend (`frontend/`)

- **Stack:** Next.js (App Router), React, TypeScript, ESLint, Tailwind CSS
- **Purpose:** PatientHub Portal for **Dr. Mina Merzek Clinic** (orthopedic & bone clinic, Heliopolis, Cairo)
- **Features:** Responsive landing page, WhatsApp integration, admin login (demo), admin dashboard (mock data)
- **Key folders:**
  - `src/app/` — routes (`/`, `/admin/login`, `/admin/dashboard`)
  - `src/components/` — layout, sections, admin, UI, icons
  - `src/messages/` — `en.json` / `ar.json` translations
  - `src/i18n/` — routing, navigation, middleware
  - `src/data/` — mock admin data
  - `src/types/` — TypeScript types
  - `src/hooks/` — React hooks (e.g. auth)
  - `src/services/` — client services (e.g. auth)
  - `src/utils/` — helpers (WhatsApp URLs, `cn`)

### Backend (`backend/`)

- **Stack:** Java 21, Spring Boot 3, Maven
- **Purpose:** REST API for PatientHub Portal (extensible)
- **Package layout (`com.patienthub`):**
  - `controller` — HTTP endpoints
  - `service` — business logic
  - `repository` — data access (ready for JPA)
  - `model` — domain/DTO types
  - `config` — application configuration (e.g. CORS)

## Prerequisites

| Application | Requirements |
|-------------|--------------|
| Frontend    | Node.js 20+ and npm |
| Backend     | Java 17+ and Maven 3.9+ |

## Running the frontend

```bash
cd frontend
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
- **SEO:** JSON-LD (MedicalClinic, Physician, FAQ, LocalBusiness), `robots.txt`, dynamic OG images
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

## Running the backend

```bash
cd backend
mvn spring-boot:run
```

The API starts on [http://localhost:8080](http://localhost:8080).

### Health check

```bash
curl http://localhost:8080/health
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

## Independent development

- The **frontend** does not require the backend to run for the landing page.
- The **backend** exposes CORS for `http://localhost:3000` on `/api/**` paths for future integration.
- Use separate terminals, version control workflows, and CI jobs per folder as needed.

## Next steps

- Add REST resources under `/api/` in the backend
- Connect the frontend contact form and portal features to backend APIs
- Add persistence (e.g. Spring Data JPA) in `repository/` and `model/`
