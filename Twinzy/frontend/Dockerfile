FROM node:20-alpine AS deps

WORKDIR /app

RUN corepack enable

COPY package.json pnpm-lock.yaml pnpm-workspace.yaml ./
COPY frontend/package.json ./frontend/package.json
RUN pnpm install --frozen-lockfile

FROM node:20-alpine AS build

WORKDIR /app

RUN corepack enable

ARG NEXT_PUBLIC_API_URL=http://localhost:8081
ARG NEXT_PUBLIC_SITE_URL=http://localhost:3001
ENV NEXT_PUBLIC_API_URL=${NEXT_PUBLIC_API_URL}
ENV NEXT_PUBLIC_SITE_URL=${NEXT_PUBLIC_SITE_URL}

COPY --from=deps /app/node_modules ./node_modules
COPY --from=deps /app/frontend/node_modules ./frontend/node_modules
COPY package.json pnpm-lock.yaml pnpm-workspace.yaml ./
COPY frontend ./frontend

RUN pnpm --filter @twinzy/frontend build

FROM node:20-alpine AS runner

WORKDIR /app

RUN corepack enable

ENV NODE_ENV=production
ENV PORT=3001
ENV HOSTNAME=0.0.0.0
ENV NEXT_PUBLIC_API_URL=http://localhost:8081
ENV NEXT_PUBLIC_SITE_URL=http://localhost:3001

COPY --from=build /app/package.json /app/pnpm-lock.yaml /app/pnpm-workspace.yaml ./
COPY --from=build /app/node_modules ./node_modules
COPY --from=build /app/frontend ./frontend

EXPOSE 3001

CMD ["pnpm", "--filter", "@twinzy/frontend", "start"]
