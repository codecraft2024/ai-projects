"use client";

import Link from "next/link";
import { ArrowRight, Camera, Grid3X3, Sparkles } from "lucide-react";
import HomeHero from "@/components/home/HomeHero";
import { useLanguage } from "@/lib/i18n/LanguageProvider";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";

export default function HomePage() {
  const { t } = useLanguage();

  return (
    <div className="mx-auto max-w-7xl px-4 py-8 sm:px-6 sm:py-12">
      <HomeHero />

      <section className="mt-14 grid gap-5 sm:grid-cols-2 md:grid-cols-3">
        <Card className="shell-card transition hover:-translate-y-0.5 hover:border-primary/30">
          <CardHeader>
            <Sparkles className="h-6 w-6 text-primary" />
            <CardTitle>{t("featureAccurateTitle")}</CardTitle>
          </CardHeader>
          <CardContent className="text-muted-foreground">{t("featureAccurateDesc")}</CardContent>
        </Card>
        <Card className="shell-card transition hover:-translate-y-0.5 hover:border-accent/30">
          <CardHeader>
            <Camera className="h-6 w-6 text-accent" />
            <CardTitle>{t("featureCropTitle")}</CardTitle>
          </CardHeader>
          <CardContent className="text-muted-foreground">{t("featureCropDesc")}</CardContent>
        </Card>
        <Card className="shell-card transition hover:-translate-y-0.5 hover:border-primary/30 sm:col-span-2 md:col-span-1">
          <CardHeader>
            <Grid3X3 className="h-6 w-6 text-primary" />
            <CardTitle>{t("featureDiscoverTitle")}</CardTitle>
          </CardHeader>
          <CardContent className="text-muted-foreground">{t("featureDiscoverDesc")}</CardContent>
        </Card>
      </section>

      <section className="mt-10 text-center">
        <Link
          href="/discover"
          className="inline-flex h-12 touch-target items-center gap-2 rounded-full border border-border bg-card/80 px-6 py-3 font-semibold backdrop-blur-sm transition hover:bg-muted"
        >
          {t("browseAllFaces")}
          <ArrowRight className="h-4 w-4" />
        </Link>
      </section>
    </div>
  );
}
