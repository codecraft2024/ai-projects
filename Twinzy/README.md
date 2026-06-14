# Twinzy

Celebrity lookalike search powered by rich profile similarity — not name matching.

## Stack

- **Frontend:** Next.js 15, TypeScript, App Router, Tailwind CSS, Shadcn UI, Framer Motion, React Query
- **Domain:** Provider-agnostic similarity engine (strategy pattern for ArcFace, FaceNet, DeepFace, AWS Rekognition, Azure Face API)
- **Data:** PostgreSQL + pgvector for embedding search at scale

## Monorepo Structure

```
Twinzy/
├── apps/web/           # Next.js frontend (SSR/ISR, SEO)
├── packages/
│   ├── types/          # Shared TypeScript types
│   └── core/           # Domain logic — similarity, providers
└── scripts/seed/       # Demo data generation
```

## Profile Dataset

Profiles include UUID, demographics, biography, 5–20 images, face embeddings, tags, and popularity score across categories: actors, actresses, athletes, singers, influencers, historical figures, and funny objects.

## Getting Started

```bash
pnpm install
pnpm dev
```

## License

Proprietary
