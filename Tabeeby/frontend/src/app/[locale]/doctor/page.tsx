import { setRequestLocale } from "next-intl/server";
import { getTranslations } from "next-intl/server";
import { SiteChrome } from "@/components/layout/SiteChrome";
import { DoctorProfile } from "@/components/sections/DoctorProfile";
import { PremiumPageHero } from "@/components/ui/PremiumPageHero";
import { routing, type Locale } from "@/i18n/routing";
import { getDoctorMetadata } from "@/lib/metadata";

type Props = { params: Promise<{ locale: string }> };

export function generateStaticParams() {
  return routing.locales.map((locale) => ({ locale }));
}

export async function generateMetadata({ params }: Props) {
  const { locale } = await params;
  const t = await getTranslations({ locale, namespace: "seo" });
  return getDoctorMetadata(locale as Locale, (key) => t(key));
}

export default async function DoctorPage({ params }: Props) {
  const { locale } = await params;
  setRequestLocale(locale);
  const t = await getTranslations({ locale, namespace: "doctor" });

  return (
    <SiteChrome>
      <PremiumPageHero
        variant="doctor"
        eyebrow={t("eyebrow")}
        title={t("pageTitle")}
        description={t("introduction")}
      />
      <DoctorProfile showShare asPage />
    </SiteChrome>
  );
}
