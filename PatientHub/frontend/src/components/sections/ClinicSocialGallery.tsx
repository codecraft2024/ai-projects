import { useTranslations } from "next-intl";
import { FACEBOOK_URL } from "@/config/social-links";
import { Container } from "@/components/ui/Container";
import { SectionHeading } from "@/components/ui/SectionHeading";
import { GalleryCard } from "@/components/social/GalleryCard";
import { Button } from "@/components/ui/Button";
import type { GalleryItem } from "@/types/social";

function buildItems(
  tg: (key: string) => string,
  keys: string[],
  category: GalleryItem["category"],
): GalleryItem[] {
  return keys.map((key) => ({
    id: `${category}-${key}`,
    category,
    title: tg(`items.${key}.title`),
    caption: tg(`items.${key}.caption`),
    date: "2026",
    imageSrc: "/gallery/placeholder.svg",
    imageAlt: tg(`items.${key}.title`),
  }));
}

export function LatestClinicUpdates() {
  const t = useTranslations("gallery");
  const items = buildItems(t, ["update1", "update2", "update3"], "update");

  return (
    <section id="updates" className="bg-white py-14 sm:py-20">
      <Container>
        <SectionHeading
          eyebrow={t("updates.eyebrow")}
          title={t("updates.title")}
          description={t("updates.description")}
        />
        <div className="mt-10 grid gap-6 sm:grid-cols-2 lg:grid-cols-3">
          {items.map((item) => (
            <GalleryCard key={item.id} item={item} />
          ))}
        </div>
        {FACEBOOK_URL && (
          <p className="mt-8 text-center text-sm text-slate-600">
            {t("updates.facebookMore")}{" "}
            <a
              href={FACEBOOK_URL}
              target="_blank"
              rel="noopener noreferrer"
              className="font-semibold text-brand hover:underline"
            >
              Facebook
            </a>
          </p>
        )}
      </Container>
    </section>
  );
}

export function CaseHighlightsSection() {
  const t = useTranslations("gallery");
  const items = buildItems(t, ["case1", "case2", "case3"], "case");

  return (
    <section id="case-highlights" className="bg-muted py-14 sm:py-20">
      <Container>
        <SectionHeading
          eyebrow={t("cases.eyebrow")}
          title={t("cases.title")}
          description={t("cases.description")}
        />
        <div className="mt-10 grid gap-6 sm:grid-cols-2 lg:grid-cols-3">
          {items.map((item) => (
            <GalleryCard key={item.id} item={item} />
          ))}
        </div>
        <div className="mt-10 text-center">
          <Button href="/cases" variant="primary" size="lg">
            {t("cases.cta")}
          </Button>
        </div>
      </Container>
    </section>
  );
}

export function ClinicGallerySection() {
  const t = useTranslations("gallery");
  const items = buildItems(t, ["gal1", "gal2", "gal3", "gal4"], "gallery");

  return (
    <section id="gallery" className="bg-white py-14 sm:py-20">
      <Container>
        <SectionHeading
          eyebrow={t("clinic.eyebrow")}
          title={t("clinic.title")}
          description={t("clinic.description")}
        />
        <div className="mt-10 grid grid-cols-2 gap-5 lg:grid-cols-4">
          {items.map((item) => (
            <GalleryCard key={item.id} item={item} />
          ))}
        </div>
      </Container>
    </section>
  );
}
