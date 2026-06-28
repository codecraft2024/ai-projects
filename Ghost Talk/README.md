# Ghost Talk

Anonymous chatting Android application built with Kotlin, MVVM, and Clean Architecture.

## Tech Stack

| Layer | Technology |
|-------|------------|
| Language | Kotlin |
| UI | XML Layouts + ViewBinding |
| Architecture | MVVM + Clean Architecture |
| DI | Hilt |
| Navigation | Navigation Component |
| Networking | Retrofit + OkHttp |
| Local Storage | Room + DataStore |
| Async | Coroutines + Flow |
| Design | Material Design 3 (Dark) |
| Min SDK | 26 |
| Target SDK | 35 |

## Project Structure

```
app/src/main/java/com/ghosttalk/
├── core/
│   ├── encryption/     # E2E encryption abstractions (Signal-ready)
│   ├── session/        # Session management via DataStore
│   └── utils/          # Ghost ID, avatars, validators
├── data/
│   ├── local/          # Room entities, DAOs, database
│   ├── remote/         # Retrofit APIs, DTOs, FakeBackendService
│   └── repository/     # Repository implementations
├── domain/
│   ├── model/          # Domain models
│   ├── repository/     # Repository interfaces
│   └── usecase/        # Single-responsibility use cases
├── presentation/
│   ├── splash/
│   ├── onboarding/
│   ├── auth/
│   ├── home/
│   ├── chat/
│   ├── profile/
│   └── settings/
├── di/                 # Hilt modules
└── navigation/         # Nav graphs (main + home nested)
```

## Screens

1. **Splash** — Routes to onboarding, login, or home based on session
2. **Onboarding** — 3-page intro to anonymous chat
3. **Login** — Mobile or anonymous nickname entry
4. **Mobile Login** — OTP flow (test OTP: `123456`)
5. **Nickname Selection** — Pick ghost name + avatar
6. **Home** — Bottom navigation container
7. **Chat List** — Conversations with unread counts
8. **User Discovery** — Find and start chats with ghosts
9. **Chat Details** — Messaging with status, typing, online indicators
10. **Profile** — Ghost identity display
11. **Settings** — App settings + logout

## Architecture

```
Presentation (Fragments + ViewModels)
        ↓ Use Cases
Domain (Models + Repository Interfaces)
        ↓
Data (Repositories → Room + Retrofit/Fake Backend)
        ↓
Core (Session, Encryption abstractions)
```

### MVVM Flow Example

```
ChatDetailFragment → ChatDetailViewModel → SendMessageUseCase → ChatRepository → FakeChatApi
```

## Authentication

Auth is abstracted via `AuthRepository` and `AuthProvider` interface for future provider integration (Firebase, OAuth, etc.).

- **Mobile**: Phone + OTP (mocked; use `123456`)
- **Anonymous**: Nickname + avatar selection

Session stored in **DataStore** (no PII persisted beyond ghost persona).

## Fake Backend

`FakeBackendService` simulates API latency and returns mock users/chats. Retrofit interfaces are bound to fake implementations in `ApiModule`. Swap bindings to real API implementations when backend is ready.

## End-to-End Encryption (Future)

Abstractions are in `core/encryption/`:

| Interface | Purpose |
|-----------|---------|
| `EncryptionProvider` | Encrypt/decrypt message content |
| `KeyManager` | Public/private key pair management |
| `SecureSessionManager` | Signal-style session establishment |
| `DeviceRegistration` | Pre-key upload to server |
| `MessageEncryptionLayer` | Orchestrates full encrypt/decrypt pipeline |

Current implementations are **NoOp** pass-throughs. To integrate Signal Protocol:

1. Replace `NoOpEncryptionProvider` with Signal-backed implementation
2. Store private keys in Android Keystore via `KeyManager`
3. Implement X3DH + Double Ratchet in `SecureSessionManager`
4. Upload pre-keys via `DeviceRegistration` on login
5. Encrypt in `ChatRepositoryImpl.sendMessage()` (already wired)

## Security Considerations

- `android:allowBackup="false"` — prevents backup of session/DB
- Data extraction rules exclude prefs and database from cloud backup
- `usesCleartextTraffic="false"` — HTTPS only in production
- No real phone numbers or PII stored locally
- Session tokens in DataStore (migrate to EncryptedSharedPreferences for production)
- ProGuard rules for Retrofit/Gson/Room
- Private keys must use Android Keystore when E2E is implemented

## Getting Started

### Prerequisites

- Android Studio Ladybug or newer
- JDK 17
- Android SDK 35

### Build & Run

```bash
./gradlew assembleDebug
```

Open in Android Studio and run on emulator or device (API 26+).

### Test Login

| Method | Credentials |
|--------|-------------|
| Mobile | Any valid phone + OTP `123456` |
| Anonymous | Any nickname (3–20 chars) + avatar |

## Gradle Modules

- `:app` — Single application module

## Key Dependencies

Defined in `gradle/libs.versions.toml` — Hilt, Room, Retrofit, Navigation, Material 3, Coroutines, DataStore.

## License

Private project — all rights reserved.
