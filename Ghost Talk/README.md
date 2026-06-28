# Ghost Talk

Anonymous, device-bound messaging platform with a Spring Boot backend and Kotlin Android client.

## Project layout

```
Ghost Talk/
├── app/                 # Android client (open this folder in Android Studio)
├── backend/             # Spring Boot API
├── docker-compose.yml   # PostgreSQL + backend
└── README.md
```

## Quick start

### Backend + database

```bash
docker compose up -d
```

API: `http://localhost:8080/api/v1`  
Swagger: `http://localhost:8080/api/v1/docs/swagger`

Build the server:

```bash
cd backend
./gradlew build
```

### Android app

Open the **`app/`** directory in Android Studio (not the repo root).

```bash
cd app
./gradlew assembleDebug
```

Debug builds use `http://10.0.2.2:8080/api/v1/` (emulator → host machine).

### Auth flow

| Action | Description |
|--------|-------------|
| **Create New Identity** | Register anonymous username + avatar (one per device fingerprint) |
| **Return as Ghost** | Login using device fingerprint hash |

## Architecture

```
┌─────────────────────┐     REST + WebSocket (STOMP)     ┌──────────────────────┐
│   app/ (Android)    │ ◄──────────────────────────────► │  backend/ (Spring)   │
│   MVVM + Room       │                                  │  Java 21 + Postgres  │
└─────────────────────┘                                  └──────────────────────┘
```

## Backend modules

| Module | Description |
|--------|-------------|
| `auth` | JWT + refresh tokens, device registration |
| `device` | Fingerprint hash storage, abuse prevention |
| `conversation` | 1:1 and group chats, archive/pin/mute |
| `message` | Send/edit/delete, reactions, read receipts |
| `websocket` | STOMP real-time events |
| `admin` | Block devices, ban users, stats |

## Android highlights

| Layer | Key components |
|-------|----------------|
| `core/device` | `DeviceFingerprintManager` — SHA-256 hashed fingerprint |
| `core/network` | `AuthInterceptor`, `TokenAuthenticator` |
| `data/remote` | Retrofit APIs matching backend |
| `data/local` | Room cache + pending message queue |

## Environment variables

| Variable | Default |
|----------|---------|
| `DATABASE_URL` | `jdbc:postgresql://localhost:5432/ghosttalk` |
| `DATABASE_USER` | `ghosttalk` |
| `DATABASE_PASSWORD` | `ghosttalk` |
| `JWT_SECRET` | dev secret (change in production) |
| `SERVER_PORT` | `8080` |
