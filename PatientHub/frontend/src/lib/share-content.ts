import type { Locale } from "@/i18n/routing";
import type { ShareContent } from "@/types/social";
import { absoluteUrl } from "@/utils/social";

export function getHomeShare(locale: Locale, title: string, description: string): ShareContent {
  return {
    url: absoluteUrl(`/${locale}`),
    title,
    description,
  };
}

export function getDoctorShare(locale: Locale, title: string, description: string): ShareContent {
  return {
    url: absoluteUrl(`/${locale}/doctor`),
    title,
    description,
  };
}

export function getCasesShare(locale: Locale, title: string, description: string): ShareContent {
  return {
    url: absoluteUrl(`/${locale}/cases`),
    title,
    description,
  };
}
