"use client";

import Link from "next/link";
import { ArrowRight, Sparkles } from "lucide-react";
import { useLanguage } from "@/lib/i18n/LanguageProvider";

export default function HomeHero() {
  const { t } = useLanguage();

  return (
    <section className="relative mx-auto max-w-3xl text-center">
      <div className="pointer-events-none absolute inset-x-0 -top-8 mx-auto h-40 w-64 rounded-full bg-primary/20 blur-3xl" />
      <p className="pill mb-6">
        <Sparkles className="mr-1.5 inline h-3.5 w-3.5" />
        {t("heroBadge")}
      </p>
      <h1 className="display-title text-3xl leading-tight sm:text-4xl md:text-5xl">
        <span className="gradient-text">{t("heroTitle")}</span>
      </h1>
      <p className="mx-auto mt-4 max-w-2xl text-sm leading-relaxed text-muted-foreground sm:mt-5 sm:text-base md:text-lg">
        {t("heroSubtitle")}
      </p>
      <div className="mt-8 flex w-full flex-col gap-3 sm:mt-10 sm:flex-row sm:flex-wrap sm:items-center sm:justify-center">
        <Link
          href="/scan"
          className="btn-glow inline-flex h-12 w-full touch-target items-center justify-center gap-2 rounded-full bg-primary px-7 text-sm font-semibold text-primary-foreground transition hover:opacity-90 sm:w-auto"
        >
          {t("heroCta")}
          <ArrowRight className="h-4 w-4" />
        </Link>
        <Link
          href="/discover"
          className="inline-flex h-12 w-full touch-target items-center justify-center gap-2 rounded-full border border-border bg-card/80 px-7 text-sm font-semibold text-foreground backdrop-blur-sm transition hover:bg-muted sm:w-auto"
        >
          {t("navDiscover")}
        </Link>
      </div>
    </section>
  );
}
