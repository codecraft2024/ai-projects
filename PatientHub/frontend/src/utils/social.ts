import { SITE_BASE_URL } from "@/config/social-links";
import type { ShareContent, SocialPlatformId } from "@/types/social";

/** Absolute URL for Open Graph and sharing. */
export function absoluteUrl(path: string = ""): string {
  const base = SITE_BASE_URL.replace(/\/$/, "");
  if (!path) return base;
  const p = path.startsWith("/") ? path : `/${path}`;
  return `${base}${p}`;
}

export function buildShareUrls(content: ShareContent) {
  const { url, title, description } = content;
  const text = `${title} — ${description}`;

  return {
    facebook: `https://www.facebook.com/sharer/sharer.php?u=${encodeURIComponent(url)}`,
    twitter: `https://twitter.com/intent/tweet?url=${encodeURIComponent(url)}&text=${encodeURIComponent(text)}`,
    whatsapp: `https://wa.me/?text=${encodeURIComponent(`${text}\n${url}`)}`,
    linkedin: `https://www.linkedin.com/sharing/share-offsite/?url=${encodeURIComponent(url)}`,
  } as const;
}

export function getShareUrl(
  platform: SocialPlatformId,
  content: ShareContent,
): string | null {
  const urls = buildShareUrls(content);
  if (platform === "instagram") return null;
  return urls[platform] ?? null;
}

export function getCopyShareText(content: ShareContent): string {
  return `${content.title}\n${content.description}\n${content.url}`;
}
