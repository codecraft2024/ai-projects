import { setRequestLocale } from "next-intl/server";
import { getTranslations } from "next-intl/server";
import { SiteChrome } from "@/components/layout/SiteChrome";
import { DoctorProfile } from "@/components/sections/DoctorProfile";
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
      <div className="border-b border-[var(--border)] bg-brand-soft py-8">
        <div className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
          <p className="text-sm font-medium text-brand">{t("eyebrow")}</p>
          <h1 className="mt-1 text-2xl font-bold text-slate-900 sm:text-3xl">
            {t("pageTitle")}
          </h1>
        </div>
      </div>
      <DoctorProfile showShare asPage />
    </SiteChrome>
  );
}
