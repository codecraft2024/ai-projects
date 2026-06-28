import type { MetadataRoute } from "next";
import { SITE_BASE_URL } from "@/config/social-links";
import { routing } from "@/i18n/routing";

const paths = ["", "/doctor", "/cases"] as const;

export default function sitemap(): MetadataRoute.Sitemap {
  const base = SITE_BASE_URL.replace(/\/$/, "");
  const now = new Date();

  return routing.locales.flatMap((locale) =>
    paths.map((path) => {
      const url = path ? `${base}/${locale}${path}` : `${base}/${locale}`;
      const languages = Object.fromEntries(
        routing.locales.map((l) => [
          l,
          path ? `${base}/${l}${path}` : `${base}/${l}`,
        ]),
      );
      languages["x-default"] = path
        ? `${base}/${routing.defaultLocale}${path}`
        : `${base}/${routing.defaultLocale}`;

      return {
        url,
        lastModified: now,
        changeFrequency: path === "" ? "weekly" : "monthly",
        priority: path === "" ? 1 : 0.8,
        alternates: { languages },
      };
    }),
  );
}
