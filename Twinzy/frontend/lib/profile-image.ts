import type { Profile } from "@/types/profile";

export function normalizeImageUrl(url?: string | null): string {
  if (!url) return "";
  return url.trim();
}

export function hasValidImageUrl(url?: string | null): boolean {
  const normalized = normalizeImageUrl(url);
  return normalized.startsWith("http://") || normalized.startsWith("https://") || normalized.startsWith("/");
}

export function resolveProfileImage(profile: Pick<Profile, "images" | "fullName">): { src: string; alt: string } | null {
  const primary = profile.images.find((image) => image.isPrimary) ?? profile.images[0];
  const src = normalizeImageUrl(primary?.url);
  if (!hasValidImageUrl(src)) {
    return null;
  }
  return { src, alt: primary?.alt || profile.fullName };
}
