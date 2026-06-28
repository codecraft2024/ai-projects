import { setRequestLocale } from "next-intl/server";
import { getTranslations } from "next-intl/server";
import { SiteChrome } from "@/components/layout/SiteChrome";
import { PatientCases } from "@/components/sections/PatientCases";
import { PremiumPageHero } from "@/components/ui/PremiumPageHero";
import { routing, type Locale } from "@/i18n/routing";
import { getCasesMetadata } from "@/lib/metadata";

type Props = { params: Promise<{ locale: string }> };

export function generateStaticParams() {
  return routing.locales.map((locale) => ({ locale }));
}

export async function generateMetadata({ params }: Props) {
  const { locale } = await params;
  const t = await getTranslations({ locale, namespace: "seo" });
  return getCasesMetadata(locale as Locale, (key) => t(key));
}

export default async function CasesPage({ params }: Props) {
  const { locale } = await params;
  setRequestLocale(locale);
  const t = await getTranslations({ locale, namespace: "cases" });

  return (
    <SiteChrome>
      <PremiumPageHero
        variant="cases"
        eyebrow={t("eyebrow")}
        title={t("pageTitle")}
        description={t("description")}
      />
      <PatientCases showShare asPage />
    </SiteChrome>
  );
}
