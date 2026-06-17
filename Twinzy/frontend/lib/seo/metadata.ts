import type { Metadata } from "next";
import type { Profile } from "@/types/profile";
import { getSiteUrl } from "@/lib/api-client";

export function buildDefaultMetadata(): Metadata {
  const siteUrl = getSiteUrl();
  return {
    metadataBase: new URL(siteUrl),
    title: {
      default: "Twinzy — Celebrity Lookalike Search",
      template: "%s | Twinzy",
    },
    description:
      "Upload your photo and discover your celebrity lookalikes with rich facial similarity analysis powered by AI.",
    keywords: [
      "celebrity lookalike",
      "face similarity",
      "twin finder",
      "look alike",
      "facial recognition",
    ],
    openGraph: {
      type: "website",
      locale: "en_US",
      url: siteUrl,
      siteName: "Twinzy",
      title: "Twinzy — Celebrity Lookalike Search",
      description:
        "Find your celebrity twin with detailed facial feature breakdown and funny object matches.",
    },
    twitter: {
      card: "summary_large_image",
      title: "Twinzy — Celebrity Lookalike Search",
      description: "Upload a photo and discover who you look like.",
    },
    alternates: {
      canonical: siteUrl,
    },
    robots: {
      index: true,
      follow: true,
    },
  };
}

export function buildProfileMetadata(profile: Profile): Metadata {
  const siteUrl = getSiteUrl();
  const title = `${profile.fullName} — Celebrity Profile`;
  const description = profile.biography.slice(0, 160);
  const primaryImage = profile.images.find((image) => image.isPrimary) ?? profile.images[0];

  return {
    title,
    description,
    openGraph: {
      title,
      description,
      url: `${siteUrl}/profile/${profile.slug}`,
      images: primaryImage ? [{ url: primaryImage.url, alt: primaryImage.alt }] : [],
      type: "profile",
    },
    alternates: {
      canonical: `${siteUrl}/profile/${profile.slug}`,
    },
  };
}
