"use client";

import { useLocale, useTranslations } from "next-intl";
import { Container } from "@/components/ui/Container";
import { SectionHeading } from "@/components/ui/SectionHeading";
import { GalleryCard } from "@/components/social/GalleryCard";
import { ShareButtons } from "@/components/social/ShareButtons";
import { getCasesShare } from "@/lib/share-content";
import { GALLERY_CASE_IMAGES } from "@/data/gallery-images";
import type { GalleryItem } from "@/types/social";
import type { Locale } from "@/i18n/routing";
import { cn } from "@/utils/cn";

const CASE_KEYS = [
  "case1",
  "case2",
  "case3",
  "case4",
  "case5",
  "case6",
] as const;

export function PatientCases({
  showShare = true,
  asPage = false,
}: {
  showShare?: boolean;
  asPage?: boolean;
}) {
  const t = useTranslations("gallery");
  const tCases = useTranslations("cases");
  const tSeo = useTranslations("seo");
  const locale = useLocale() as Locale;

  const items: GalleryItem[] = CASE_KEYS.map((key) => ({
    id: key,
    category: "case",
    title: t(`items.${key}.title`),
    caption: t(`items.${key}.caption`),
    date: "2026",
    imageSrc: GALLERY_CASE_IMAGES[key],
    imageAlt: t(`items.${key}.title`),
  }));

  const share = getCasesShare(locale, tSeo("casesTitle"), tSeo("casesDescription"));

  return (
    <section className="bg-gradient-to-b from-white via-brand-soft/30 to-muted py-14 sm:py-20">
      <Container>
        {!asPage && (
          <SectionHeading
            eyebrow={tCases("eyebrow")}
            title={tCases("title")}
            description={tCases("description")}
          />
        )}
        {showShare && (
          <div className={asPage ? "" : "mt-8"}>
            <ShareButtons content={share} />
          </div>
        )}
        <div className={cn("grid gap-6 sm:grid-cols-2 lg:grid-cols-3", asPage ? "mt-8" : "mt-10")}>
          {items.map((item) => (
            <GalleryCard key={item.id} item={item} variant="cases" />
          ))}
        </div>
      </Container>
    </section>
  );
}
