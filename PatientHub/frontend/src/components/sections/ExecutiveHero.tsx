"use client";

import { useTranslations } from "next-intl";
import { Container } from "@/components/ui/Container";
import { Button } from "@/components/ui/Button";

export function ExecutiveHero() {
  const t = useTranslations("executive");
  const tSite = useTranslations("site");

  const stats = [
    { value: "15+", label: t("stats.experience") },
    { value: "8,000+", label: t("stats.patients") },
    { value: "2,500+", label: t("stats.surgeries") },
    { value: "98%", label: t("stats.satisfaction") },
  ];

  return (
    <section className="relative overflow-hidden bg-brand-gradient py-14 text-white sm:py-20">
      <div
        className="pointer-events-none absolute inset-0 opacity-30"
        aria-hidden
        style={{
          backgroundImage:
            "radial-gradient(circle at 80% 20%, #FFC107 0%, transparent 40%)",
        }}
      />
      <Container className="relative">
        <div className="grid items-center gap-10 lg:grid-cols-12 lg:gap-14">
          <div className="lg:col-span-7">
            <p className="text-sm font-semibold uppercase tracking-widest text-accent">
              {t("eyebrow")}
            </p>
            <h2 className="mt-3 text-3xl font-bold tracking-tight sm:text-4xl lg:text-5xl">
              {t("title")}
            </h2>
            <p className="mt-2 text-lg font-medium text-brand-light">{t("subtitle")}</p>
            <p className="mt-4 text-base leading-relaxed text-white/90 sm:text-lg">
              {t("bio")}
            </p>
            <ul className="mt-6 flex flex-wrap gap-2 text-sm">
              <li className="rounded-full bg-white/15 px-3 py-1">{tSite("roles.founder")}</li>
              <li className="rounded-full bg-white/15 px-3 py-1">{tSite("roles.leadSurgeon")}</li>
              <li className="rounded-full bg-accent/90 px-3 py-1 font-medium text-[#1a1625]">
                {tSite("roles.faceOfClinic")}
              </li>
            </ul>
            <div className="mt-8 flex flex-col gap-3 sm:flex-row">
              <Button href="/doctor" variant="secondary" size="lg" className="!bg-white !text-brand">
                {t("ctaProfile")}
              </Button>
              <Button
                href="/cases"
                variant="outline"
                size="lg"
                className="!border-white/40 !text-white hover:!bg-white/10"
              >
                {t("ctaCases")}
              </Button>
            </div>
          </div>

          <div className="lg:col-span-5">
            <div className="mx-auto max-w-sm overflow-hidden rounded-3xl border border-white/20 bg-white/10 p-8 backdrop-blur-sm lg:mx-0 lg:ms-auto">
              <div className="mx-auto flex h-36 w-36 items-center justify-center rounded-full bg-accent text-5xl font-bold text-[#1a1625] shadow-lg">
                MM
              </div>
              <p className="mt-6 text-center text-xl font-bold">{t("title")}</p>
              <p className="text-center text-sm text-brand-light">{tSite("roles.leadSurgeon")}</p>
              <dl className="mt-8 grid grid-cols-2 gap-4 border-t border-white/20 pt-6">
                {stats.map((s) => (
                  <div key={s.label} className="text-center">
                    <dt className="text-2xl font-bold text-accent">{s.value}</dt>
                    <dd className="mt-0.5 text-xs text-white/80">{s.label}</dd>
                  </div>
                ))}
              </dl>
            </div>
          </div>
        </div>
      </Container>
    </section>
  );
}
