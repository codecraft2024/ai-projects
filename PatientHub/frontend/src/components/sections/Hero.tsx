"use client";

import { useLocale, useTranslations } from "next-intl";
import { Container } from "@/components/ui/Container";
import { Button } from "@/components/ui/Button";
import { WhatsAppButton } from "@/components/ui/WhatsAppButton";
import { ShareButtons } from "@/components/social/ShareButtons";
import { getHomeShare } from "@/lib/share-content";
import type { Locale } from "@/i18n/routing";

export function Hero() {
  const t = useTranslations("hero");
  const tSite = useTranslations("site");
  const tAbout = useTranslations("about");
  const tExp = useTranslations("experience.stats");
  const locale = useLocale() as Locale;
  const tSeo = useTranslations("seo");

  const stats = [
    { value: tExp("experience"), label: tExp("experienceLabel") },
    { value: tExp("patients"), label: tExp("patientsLabel") },
    { value: tExp("surgeries"), label: tExp("surgeriesLabel") },
    { value: tExp("satisfaction"), label: tExp("satisfactionLabel") },
  ];

  const share = getHomeShare(locale, tSeo("homeTitle"), tSeo("homeDescription"));

  return (
    <section
      id="home"
      className="relative overflow-hidden bg-gradient-to-br from-brand-soft via-white to-accent-soft"
    >
      <Container className="relative py-12 sm:py-16 lg:py-20">
        <div className="grid items-center gap-10 lg:grid-cols-2 lg:gap-14">
          <div>
            <p className="inline-flex items-center gap-2 rounded-full border border-brand/20 bg-brand-soft px-4 py-1.5 text-xs font-semibold text-brand sm:text-sm">
              <span className="h-2 w-2 rounded-full bg-brand" />
              {t("badge")}
            </p>
            <h1 className="mt-5 text-3xl font-bold leading-tight tracking-tight text-slate-900 sm:text-4xl lg:text-5xl">
              {tSite("clinicName")}
            </h1>
            <p className="mt-2 text-lg font-semibold text-brand sm:text-xl">{tSite("tagline")}</p>
            <p className="mt-1 text-sm text-slate-500">
              {t("viaPortal")} — {tSite("portalName")}
            </p>
            <p className="mt-5 text-base leading-relaxed text-slate-600 sm:text-lg">
              {tSite("description")}
            </p>
            <div className="mt-8 flex flex-col gap-3 sm:flex-row">
              <Button href="/#contact" variant="primary" size="lg" className="w-full sm:w-auto">
                {t("ctaAppointment")}
              </Button>
              <WhatsAppButton label={t("ctaWhatsApp")} size="lg" className="w-full sm:w-auto" />
            </div>
            <div className="mt-8">
              <ShareButtons content={share} compact />
            </div>
            <dl className="mt-8 grid grid-cols-2 gap-4 border-t border-[var(--border)] pt-8 sm:grid-cols-4">
              {stats.map((s) => (
                <div key={s.label}>
                  <dt className="text-xl font-bold text-brand sm:text-2xl">{s.value}</dt>
                  <dd className="mt-0.5 text-xs text-slate-600 sm:text-sm">{s.label}</dd>
                </div>
              ))}
            </dl>
          </div>

          <div className="overflow-hidden rounded-3xl bg-brand-gradient p-6 text-white shadow-brand sm:p-8 lg:p-10">
            <p className="text-xs font-semibold uppercase tracking-widest text-accent">
              {t("accepting")}
            </p>
            <p className="mt-4 text-2xl font-semibold leading-snug sm:text-3xl">{t("premiumTitle")}</p>
            <ul className="mt-8 space-y-3 text-sm text-white/90 sm:text-base">
              {(tAbout.raw("careApproach") as string[]).map((item) => (
                <li key={item} className="flex gap-3">
                  <span className="flex h-6 w-6 shrink-0 items-center justify-center rounded-md bg-accent text-xs font-bold text-[#1a1625]">
                    ✓
                  </span>
                  {item}
                </li>
              ))}
            </ul>
          </div>
        </div>
      </Container>
    </section>
  );
}
