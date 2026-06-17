import type { MetadataRoute } from "next";
import { fetchProfileSlugs, getSiteUrl } from "@/lib/api-client";

export default async function sitemap(): Promise<MetadataRoute.Sitemap> {
  const siteUrl = getSiteUrl();

  try {
    const slugs = await fetchProfileSlugs();
    const profileEntries = slugs.map((slug) => ({
      url: `${siteUrl}/profile/${slug}`,
      lastModified: new Date(),
      changeFrequency: "weekly" as const,
      priority: 0.7,
    }));

    return [
      {
        url: siteUrl,
        lastModified: new Date(),
        changeFrequency: "daily",
        priority: 1,
      },
      {
        url: `${siteUrl}/search/results`,
        lastModified: new Date(),
        changeFrequency: "daily",
        priority: 0.8,
      },
      ...profileEntries,
    ];
  } catch {
    return [
      {
        url: siteUrl,
        lastModified: new Date(),
        changeFrequency: "daily",
        priority: 1,
      },
    ];
  }
}
