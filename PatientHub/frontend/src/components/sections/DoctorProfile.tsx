"use client";

import { useLocale, useTranslations } from "next-intl";
import { Container } from "@/components/ui/Container";
import { SectionHeading } from "@/components/ui/SectionHeading";
import { ShareButtons } from "@/components/social/ShareButtons";
import { getDoctorShare } from "@/lib/share-content";
import type { Locale } from "@/i18n/routing";

type Qualification = { title: string; institution: string };
type Award = { title: string; description: string };

type DoctorProfileProps = {
  showShare?: boolean;
  asPage?: boolean;
};

export function DoctorProfile({ showShare = false, asPage = false }: DoctorProfileProps) {
  const t = useTranslations("doctor");
  const tSeo = useTranslations("seo");
  const locale = useLocale() as Locale;

  const qualifications = t.raw("qualifications") as Qualification[];
  const specializations = t.raw("specializations") as string[];
  const experience = t.raw("experience") as string[];
  const expertise = t.raw("expertise") as string[];
  const awards = t.raw("awards") as Award[];
  const certifications = t.raw("certifications") as string[];

  const share = getDoctorShare(locale, tSeo("doctorTitle"), tSeo("doctorDescription"));

  return (
    <section id={asPage ? undefined : "doctor"} className="bg-white py-14 sm:py-20">
      <Container>
        <SectionHeading eyebrow={t("eyebrow")} title={t("name")} description={t("introduction")} />

        {showShare && (
          <div className="mt-8">
            <ShareButtons content={share} />
          </div>
        )}

        <div className="mt-12 grid gap-10 lg:grid-cols-12 lg:gap-12">
          <div className="lg:col-span-4">
            <div className="card-premium mx-auto max-w-sm overflow-hidden lg:mx-0">
              <div className="flex aspect-[3/4] flex-col items-center justify-center bg-gradient-to-b from-brand-soft to-white p-8">
                <div className="flex h-32 w-32 items-center justify-center rounded-full bg-brand-gradient text-4xl font-bold text-white shadow-brand">
                  MM
                </div>
                <p className="mt-6 text-lg font-semibold text-slate-900">{t("name")}</p>
                <p className="text-sm font-medium text-brand">{t("title")}</p>
                <p className="mt-4 text-center text-xs text-slate-500">{t("photoPlaceholder")}</p>
              </div>
            </div>
            <p className="mt-4 rounded-xl bg-accent-soft p-4 text-center text-sm text-slate-600">
              {t("videosComingSoon")}
            </p>
          </div>

          <div className="space-y-8 lg:col-span-8">
            <Block title={t("biographyTitle")}>
              <p className="leading-relaxed text-slate-600">{t("biography")}</p>
            </Block>

            <Block title={t("qualificationsTitle")}>
              <ul className="space-y-3">
                {qualifications.map((q) => (
                  <li key={q.title} className="rounded-xl border border-[var(--border)] bg-muted p-4">
                    <p className="font-semibold text-slate-900">{q.title}</p>
                    <p className="mt-1 text-sm text-slate-600">{q.institution}</p>
                  </li>
                ))}
              </ul>
            </Block>

            <Block title={t("awardsTitle")}>
              <div className="grid gap-4 sm:grid-cols-2">
                {awards.map((a) => (
                  <div key={a.title} className="rounded-xl border border-accent/30 bg-accent-soft p-4">
                    <p className="font-semibold text-slate-900">{a.title}</p>
                    <p className="mt-1 text-sm text-slate-600">{a.description}</p>
                  </div>
                ))}
              </div>
            </Block>

            <Block title={t("certificationsTitle")}>
              <ul className="space-y-2">
                {certifications.map((c) => (
                  <li key={c} className="flex gap-2 text-sm text-slate-600 sm:text-base">
                    <span className="text-accent-dark">★</span>
                    {c}
                  </li>
                ))}
              </ul>
            </Block>

            <Block title={t("specializationsTitle")}>
              <div className="flex flex-wrap gap-2">
                {specializations.map((s) => (
                  <span
                    key={s}
                    className="rounded-full bg-brand-soft px-3 py-1.5 text-sm font-medium text-brand ring-1 ring-brand/10"
                  >
                    {s}
                  </span>
                ))}
              </div>
            </Block>

            <div className="grid gap-8 sm:grid-cols-2">
              <Block title={t("experienceTitle")}>
                <ul className="list-inside list-disc space-y-2 text-sm text-slate-600">
                  {experience.map((item) => (
                    <li key={item}>{item}</li>
                  ))}
                </ul>
              </Block>
              <Block title={t("expertiseTitle")}>
                <ul className="space-y-2">
                  {expertise.map((area) => (
                    <li key={area} className="flex gap-2 text-sm text-slate-600">
                      <span className="text-brand">▸</span>
                      {area}
                    </li>
                  ))}
                </ul>
              </Block>
            </div>
          </div>
        </div>
      </Container>
    </section>
  );
}

function Block({ title, children }: { title: string; children: React.ReactNode }) {
  return (
    <div>
      <h3 className="text-lg font-bold text-slate-900">{title}</h3>
      <div className="mt-4">{children}</div>
    </div>
  );
}
