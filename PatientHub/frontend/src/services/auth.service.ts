import { apiJson } from "@/services/api.client";
import type { AuthCredentials, AuthSession } from "@/types/admin";

const STORAGE_KEY = "session";

let session: AuthSession | null = null;
const listeners = new Set<() => void>();

export function getSession() {
  return session;
}

export function subscribeAuth(listener: () => void) {
  listeners.add(listener);

  return () => {
    listeners.delete(listener);
  };
}

function emitChange() {
  for (const l of listeners) l();
}

export function initSession() {
  if (typeof window === "undefined") return;

  const raw = localStorage.getItem(STORAGE_KEY);
  session = raw ? (JSON.parse(raw) as AuthSession) : null;
}

export function saveSession(username: string, token: string) {
  session = {
    username,
    token,
    loggedInAt: new Date().toISOString(),
  };
  localStorage.setItem(STORAGE_KEY, JSON.stringify(session));
  emitChange();
}

export function clearSession() {
  const token = session?.token;
  session = null;
  localStorage.removeItem(STORAGE_KEY);
  emitChange();

  if (token) {
    void apiJson("/api/auth/logout", {
      method: "POST",
      headers: { Authorization: `Bearer ${token}` },
    }).catch(() => undefined);
  }
}

export async function loginWithApi(credentials: AuthCredentials): Promise<AuthSession> {
  const response = await apiJson<{ username: string; token: string }>("/api/auth/login", {
    method: "POST",
    body: JSON.stringify(credentials),
  });

  saveSession(response.username, response.token);
  return session!;
}
