import type { MetadataRoute } from "next";
import { SITE_BASE_URL } from "@/config/social-links";

export default function robots(): MetadataRoute.Robots {
  const base = SITE_BASE_URL.replace(/\/$/, "");

  return {
    rules: {
      userAgent: "*",
      allow: "/",
      disallow: ["/admin/", "/*/admin/"],
    },
    sitemap: `${base}/sitemap.xml`,
    host: base,
  };
}
