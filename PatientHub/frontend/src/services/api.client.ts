export class ApiError extends Error {
  status: number;

  constructor(status: number, message: string) {
    super(message);
    this.name = "ApiError";
    this.status = status;
  }
}

function isNgrokHost(hostname: string): boolean {
  return hostname.includes("ngrok") || hostname.endsWith(".ngrok-free.app") || hostname.endsWith(".ngrok-free.dev");
}

export function getApiBase(): string {
  // Browser: same-origin proxy (works with ngrok, LAN IP, mobile testing)
  if (typeof window !== "undefined") {
    return `${window.location.origin}/api-proxy`;
  }
  const internal = process.env.BACKEND_INTERNAL_URL;
  if (internal) {
    return internal.replace(/\/$/, "");
  }
  return (process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8080").replace(/\/$/, "");
}

export function getAuthHeaders(): HeadersInit {
  const headers: Record<string, string> = {};
  if (typeof window !== "undefined") {
    if (isNgrokHost(window.location.hostname)) {
      headers["ngrok-skip-browser-warning"] = "true";
    }
    const raw = localStorage.getItem("session");
    if (raw) {
      try {
        const session = JSON.parse(raw) as { token?: string; username?: string };
        if (session.token) {
          headers.Authorization = `Bearer ${session.token}`;
        }
        if (session.username) {
          headers["X-Admin-User"] = session.username;
        }
      } catch {
        // ignore invalid session payload
      }
    }
  }
  return headers;
}

/** @deprecated Use getAuthHeaders() */
export function apiHeaders(adminUser?: string): HeadersInit {
  const headers = getAuthHeaders();
  if (adminUser && !("X-Admin-User" in headers)) {
    (headers as Record<string, string>)["X-Admin-User"] = adminUser;
  }
  return headers;
}

export async function apiJson<T>(path: string, options: RequestInit = {}): Promise<T> {
  const headers = new Headers(options.headers);
  if (!headers.has("Content-Type") && options.body && typeof options.body === "string") {
    headers.set("Content-Type", "application/json");
  }
  for (const [key, value] of Object.entries(getAuthHeaders())) {
    if (!headers.has(key)) {
      headers.set(key, value);
    }
  }

  let res: Response;
  try {
    res = await fetch(`${getApiBase()}${path}`, {
      ...options,
      headers,
      credentials: "same-origin",
      cache: "no-store",
    });
  } catch {
    throw new ApiError(0, "Network request failed");
  }

  const contentType = res.headers.get("content-type") ?? "";

  if (!res.ok) {
    const body = contentType.includes("application/json")
      ? ((await res.json().catch(() => null)) as { message?: string } | null)
      : null;
    const fallback =
      contentType.includes("text/html")
        ? "Server returned an unexpected response. If using ngrok, open the site once in the browser first, then retry."
        : res.statusText;
    throw new ApiError(res.status, body?.message ?? fallback);
  }

  if (res.status === 204) {
    return undefined as T;
  }

  if (!contentType.includes("application/json")) {
    const onNgrok = typeof window !== "undefined" && isNgrokHost(window.location.hostname);
    throw new ApiError(
      502,
      onNgrok
        ? "Unexpected response. Open the ngrok link, tap Visit Site, then try login again."
        : "Server returned an unexpected response.",
    );
  }

  try {
    return (await res.json()) as T;
  } catch {
    throw new ApiError(502, "Invalid response from server");
  }
}

export function fileDownloadUrl(path: string): string {
  return `${getApiBase()}${path}`;
}
