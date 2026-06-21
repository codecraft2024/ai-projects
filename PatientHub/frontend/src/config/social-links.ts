/**
 * Central social media configuration for Tabeeby.
 *
 * UPDATE LINKS HERE — all components read from this file.
 * Leave a URL empty string to hide that platform until it is ready.
 *
 * Placeholders (replace when accounts are live):
 *   FACEBOOK_URL   — clinic Facebook page
 *   INSTAGRAM_URL  — clinic Instagram profile
 *   TWITTER_URL    — clinic X (Twitter) profile
 */

import type { SocialPlatformId } from "@/types/social";

/** Production site URL for Open Graph and share links (set in .env.local). */
export const SITE_BASE_URL =
  process.env.NEXT_PUBLIC_SITE_URL ?? "http://localhost:3000";

// ─── Profile / page URLs (update when available) ─────────────────────────────

/** Clinic Facebook page — public page reference */
export const FACEBOOK_URL =
  "https://www.facebook.com/Dr.Mina.Eskarous.Bone.Clinic/";

/** Set your Instagram profile URL when ready, e.g. https://www.instagram.com/yourclinic */
export const INSTAGRAM_URL = "";

/** Set your X (Twitter) profile URL when ready, e.g. https://x.com/yourclinic */
export const TWITTER_URL = "";

// ─── WhatsApp (official wa.me deep link) ─────────────────────────────────────

export const WHATSAPP_PHONE_E164 = "201221926646";
export const WHATSAPP_CONTACT_NAME = "Mina Clinic";
export const WHATSAPP_DEFAULT_MESSAGE = `Hello ${WHATSAPP_CONTACT_NAME}, I would like to inquire about an appointment at Dr. Mina Merzek Clinic.`;

export const WHATSAPP_URL = `https://wa.me/${WHATSAPP_PHONE_E164}?text=${encodeURIComponent(WHATSAPP_DEFAULT_MESSAGE)}`;

/** Resolved URLs map — convenient for imports that expect a single object. */
export const SOCIAL_LINKS = {
  facebook: FACEBOOK_URL,
  instagram: INSTAGRAM_URL,
  twitter: TWITTER_URL,
  whatsapp: WHATSAPP_URL,
} as const;

// ─── Open Graph / preview assets (paths under /public) ───────────────────────

export const OG_IMAGES = {
  /** Default clinic preview — 1200×630 recommended */
  clinic: "/og/clinic-hero.png",
  /** Doctor profile share card */
  doctor: "/og/doctor-profile.png",
  /** Patient cases / gallery share card */
  cases: "/og/patient-cases.png",
  /** Clinic logo (square, for structured data) */
  logo: "/tabeeby-logo.png",
} as const;

export const OG_DEFAULT = {
  siteName: "Tabeeby",
  brandTagline: "Patient & Doctor Hub",
  clinicName: "Dr. Mina Merzek Clinic",
  title: "Tabeeby | Dr. Mina Merzek Clinic",
  description:
    "Orthopedic & bone clinic in Heliopolis, Cairo. Expert care in pediatric orthopedics, joint replacement, arthroscopy, and sports injuries with Dr. Mina Merzek.",
  locale: "en_EG",
} as const;

// ─── Platform registry (add new platforms here) ───────────────────────────────

export type SocialLinkEntry = {
  id: SocialPlatformId;
  label: string;
  href: string;
  /** Show in header/footer/contact icon row */
  showInNav: boolean;
  /** Supports web share dialog / intent URL */
  supportsShare: boolean;
  brandColor: string;
  hoverColor: string;
};

function entry(
  partial: Omit<SocialLinkEntry, "href"> & { href: string },
): SocialLinkEntry | null {
  if (!partial.href.trim()) return null;
  return partial as SocialLinkEntry;
}

/** All configured platforms with resolved URLs — empty URLs are omitted. */
export function getSocialPlatforms(): SocialLinkEntry[] {
  return [
    entry({
      id: "facebook",
      label: "Facebook",
      href: FACEBOOK_URL,
      showInNav: true,
      supportsShare: true,
      brandColor: "#1877F2",
      hoverColor: "#166FE5",
    }),
    entry({
      id: "instagram",
      label: "Instagram",
      href: INSTAGRAM_URL,
      showInNav: true,
      supportsShare: false,
      brandColor: "#E4405F",
      hoverColor: "#D62976",
    }),
    entry({
      id: "twitter",
      label: "X (Twitter)",
      href: TWITTER_URL,
      showInNav: true,
      supportsShare: true,
      brandColor: "#000000",
      hoverColor: "#333333",
    }),
    entry({
      id: "whatsapp",
      label: "WhatsApp",
      href: WHATSAPP_URL,
      showInNav: true,
      supportsShare: true,
      brandColor: "#25D366",
      hoverColor: "#20BD5A",
    }),
  ].filter((p): p is SocialLinkEntry => p !== null);
}

/** Platforms shown in navigation areas (header, footer, contact). */
export function getNavSocialPlatforms(): SocialLinkEntry[] {
  return getSocialPlatforms().filter((p) => p.showInNav);
}

/** Platforms that support share intents. */
export function getShareablePlatforms(): SocialLinkEntry[] {
  return getSocialPlatforms().filter((p) => p.supportsShare);
}

export function getSocialUrl(id: SocialPlatformId): string | undefined {
  return getSocialPlatforms().find((p) => p.id === id)?.href;
}
