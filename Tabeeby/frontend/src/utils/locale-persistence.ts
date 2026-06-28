import { LOCALE_STORAGE_KEY, type Locale } from "@/i18n/routing";

/** Persist language choice in localStorage and cookies for return visits. */
export function persistLocalePreference(locale: Locale): void {
  if (typeof window === "undefined") return;

  try {
    localStorage.setItem(LOCALE_STORAGE_KEY, locale);
  } catch {
    /* storage unavailable */
  }

  const maxAge = "31536000";
  const base = `path=/;max-age=${maxAge};SameSite=Lax`;
  document.cookie = `NEXT_LOCALE=${locale};${base}`;
  document.cookie = `${LOCALE_STORAGE_KEY}=${locale};${base}`;
}
