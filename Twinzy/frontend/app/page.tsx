"use client";

import { Sparkles, Users, Zap } from "lucide-react";
import { JsonLd } from "@/components/seo/JsonLd";
import { ImageUploadForm } from "@/components/search/ImageUploadForm";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { useLanguage } from "@/lib/i18n/LanguageProvider";
import { getSiteUrl } from "@/lib/api-client";

export default function HomePage() {
  const { t } = useLanguage();
  const siteUrl = getSiteUrl();

  return (
    <>
      <JsonLd
        data={{
          "@context": "https://schema.org",
          "@type": "WebApplication",
          name: "Twinzy",
          url: siteUrl,
          applicationCategory: "EntertainmentApplication",
          description: "Celebrity lookalike search with real photos and funny matches.",
        }}
      />

      <section className="mx-auto max-w-7xl px-4 py-16 sm:px-6">
        <div className="mx-auto max-w-3xl text-center">
          <p className="mb-4 text-sm font-black uppercase tracking-[0.2em] text-primary">
            {t("heroBadge")}
          </p>
          <h1 className="funny-title text-4xl sm:text-6xl">{t("heroTitle")}</h1>
          <p className="mt-6 text-lg font-medium text-muted-foreground">{t("heroSubtitle")}</p>
        </div>

        <div className="mt-12">
          <ImageUploadForm />
        </div>

        <div className="mt-16 grid gap-6 md:grid-cols-3">
          <Card className="funny-card">
            <CardHeader>
              <Sparkles className="h-6 w-6 text-primary" />
              <CardTitle className="funny-title">{t("featureFunnyTitle")}</CardTitle>
            </CardHeader>
            <CardContent className="font-medium text-muted-foreground">{t("featureFunnyDesc")}</CardContent>
          </Card>
          <Card className="funny-card">
            <CardHeader>
              <Users className="h-6 w-6 text-primary" />
              <CardTitle className="funny-title">{t("featureRealTitle")}</CardTitle>
            </CardHeader>
            <CardContent className="font-medium text-muted-foreground">{t("featureRealDesc")}</CardContent>
          </Card>
          <Card className="funny-card">
            <CardHeader>
              <Zap className="h-6 w-6 text-primary" />
              <CardTitle className="funny-title">{t("featureProviderTitle")}</CardTitle>
            </CardHeader>
            <CardContent className="font-medium text-muted-foreground">{t("featureProviderDesc")}</CardContent>
          </Card>
        </div>
      </section>
    </>
  );
}
