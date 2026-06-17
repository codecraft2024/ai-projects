import type { FeatureBreakdown, Profile, ProfileMatch, SearchResponse } from "@/types/profile";

export function getApiBaseUrl(): string {
  return process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8081";
}

export function getSiteUrl(): string {
  return process.env.NEXT_PUBLIC_SITE_URL ?? "http://localhost:3001";
}

export interface SearchApiResponse extends SearchResponse {
  sessionId: string;
}

export interface ProfileDetailResponse {
  profile: Profile;
  related: Array<{
    profile: Profile;
    score: number;
  }>;
  match?: ProfileMatch;
}

export async function searchByImage(file: File): Promise<SearchApiResponse> {
  const formData = new FormData();
  formData.append("image", file);

  const response = await fetch(`${getApiBaseUrl()}/api/search`, {
    method: "POST",
    body: formData,
  });

  if (!response.ok) {
    const error = await response.json().catch(() => ({ message: "Search failed" }));
    throw new Error(error.message ?? "Search failed");
  }

  return response.json();
}

export interface ProfileSummary {
  profile: Profile;
  primaryImageUrl: string;
}

export interface ProfilePage {
  items: ProfileSummary[];
  page: number;
  size: number;
  total: number;
  totalPages: number;
}

export async function fetchProfilesPage(
  page = 0,
  size = 48,
  funnyOnly?: boolean,
): Promise<ProfilePage> {
  const params = new URLSearchParams({
    page: String(page),
    size: String(size),
  });
  if (funnyOnly !== undefined) params.set("funnyOnly", String(funnyOnly));

  const response = await fetch(`${getApiBaseUrl()}/api/profiles?${params.toString()}`, {
    cache: "no-store",
  });
  if (!response.ok) throw new Error("Failed to load profiles");
  return response.json();
}

export async function fetchProfileCount(): Promise<{ total: number; humans: number; funny: number }> {
  const response = await fetch(`${getApiBaseUrl()}/api/profiles/count`, { cache: "no-store" });
  if (!response.ok) throw new Error("Failed to load profile count");
  return response.json();
}

export async function fetchAllProfiles(funnyOnly?: boolean): Promise<ProfileSummary[]> {
  const first = await fetchProfilesPage(0, 200, funnyOnly);
  if (first.totalPages <= 1) return first.items;

  const all = [...first.items];
  for (let page = 1; page < first.totalPages; page++) {
    const next = await fetchProfilesPage(page, 200, funnyOnly);
    all.push(...next.items);
  }
  return all;
}

export async function fetchProfile(
  slug: string,
  sessionId?: string,
): Promise<ProfileDetailResponse> {
  const params = new URLSearchParams();
  if (sessionId) params.set("sessionId", sessionId);

  const response = await fetch(
    `${getApiBaseUrl()}/api/profiles/${slug}${params.size ? `?${params.toString()}` : ""}`,
    { cache: "no-store" },
  );

  if (!response.ok) {
    throw new Error("Profile not found");
  }

  return response.json();
}

export async function fetchProfileSlugs(): Promise<string[]> {
  const response = await fetch(`${getApiBaseUrl()}/api/profiles/slugs`, {
    next: { revalidate: 86_400 },
  });

  if (!response.ok) {
    return [];
  }

  return response.json();
}

export async function checkHealth(): Promise<{ ok: boolean; profiles: number }> {
  const response = await fetch(`${getApiBaseUrl()}/api/health`, { cache: "no-store" });
  if (!response.ok) throw new Error("API unavailable");
  return response.json();
}

export const FEATURE_LABELS: Record<keyof FeatureBreakdown, string> = {
  faceShape: "Face Shape",
  eyes: "Eyes",
  eyebrows: "Eyebrows",
  nose: "Nose",
  lips: "Lips",
  smile: "Smile",
  forehead: "Forehead",
  jawline: "Jawline",
  hairline: "Hairline",
};

export const FUNNY_EMOJI: Record<string, string> = {
  Potato: "🥔",
  Tomato: "🍅",
  Onion: "🧅",
  Carrot: "🥕",
  Pumpkin: "🎃",
  Banana: "🍌",
  Broccoli: "🥦",
  Eggplant: "🍆",
  Avocado: "🥑",
  Panda: "🐼",
  Koala: "🐨",
  Cat: "🐱",
  Duck: "🦆",
  Owl: "🦉",
  Sloth: "🦥",
  "Golden Retriever": "🐕",
};
