import type { Metadata } from "next";
import { OG_DEFAULT, SITE_BASE_URL } from "@/config/social-links";
import { routing, type Locale } from "@/i18n/routing";

type PageMetaInput = {
  locale: Locale;
  title: string;
  description: string;
  path: string;
  keywords?: string;
};

function localePath(locale: Locale, path: string): string {
  const p = path.startsWith("/") ? path : `/${path}`;
  return p === "/" ? `/${locale}` : `/${locale}${p}`;
}

export function createPageMetadata({
  locale,
  title,
  description,
  path,
  keywords,
}: PageMetaInput): Metadata {
  const pageUrl = `${SITE_BASE_URL.replace(/\/$/, "")}${localePath(locale, path)}`;
  const fullTitle = title.includes(OG_DEFAULT.siteName)
    ? title
    : `${title} | ${OG_DEFAULT.siteName}`;

  const languages = Object.fromEntries(
    routing.locales.map((l) => [l, `${SITE_BASE_URL.replace(/\/$/, "")}${localePath(l, path)}`]),
  );
  languages["x-default"] = `${SITE_BASE_URL.replace(/\/$/, "")}${localePath(routing.defaultLocale, path)}`;

  return {
    title: fullTitle,
    description,
    keywords: keywords?.split(",").map((k) => k.trim()),
    metadataBase: new URL(SITE_BASE_URL),
    alternates: {
      canonical: pageUrl,
      languages,
    },
    openGraph: {
      type: "website",
      locale: locale === "ar" ? "ar_EG" : "en_EG",
      alternateLocale: locale === "ar" ? ["en_EG"] : ["ar_EG"],
      url: pageUrl,
      siteName: OG_DEFAULT.siteName,
      title: fullTitle,
      description,
    },
    twitter: {
      card: "summary_large_image",
      title: fullTitle,
      description,
    },
  };
}

export function getHomeMetadata(locale: Locale, t: (key: string) => string): Metadata {
  return createPageMetadata({
    locale,
    title: t("homeTitle"),
    description: t("homeDescription"),
    path: "/",
    keywords: t("keywords"),
  });
}

export function getDoctorMetadata(locale: Locale, t: (key: string) => string): Metadata {
  return createPageMetadata({
    locale,
    title: t("doctorTitle"),
    description: t("doctorDescription"),
    path: "/doctor",
    keywords: t("keywords"),
  });
}

export function getCasesMetadata(locale: Locale, t: (key: string) => string): Metadata {
  return createPageMetadata({
    locale,
    title: t("casesTitle"),
    description: t("casesDescription"),
    path: "/cases",
    keywords: t("keywords"),
  });
}
