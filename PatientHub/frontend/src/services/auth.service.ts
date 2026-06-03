type Session = { username: string };

let session: Session | null = null;
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

  const raw = localStorage.getItem("session");
  session = raw ? JSON.parse(raw) : null;
}

export function saveSession(username: string) {
  session = { username };
  localStorage.setItem("session", JSON.stringify(session));
  emitChange();
}

export function clearSession() {
  session = null;
  localStorage.removeItem("session");
  emitChange();
}

export function validateCredentials(credentials: {
  username: string;
  password: string;
}) {
  return credentials.username.length > 0 && credentials.password.length > 0;
}