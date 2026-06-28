import { setRequestLocale } from "next-intl/server";
import { SiteChrome } from "@/components/layout/SiteChrome";
import { Hero } from "@/components/sections/Hero";
import { ExecutiveHero } from "@/components/sections/ExecutiveHero";
import { About } from "@/components/sections/About";
import { Services } from "@/components/sections/Services";
import { DoctorProfile } from "@/components/sections/DoctorProfile";
import { SurgeryHistory } from "@/components/sections/SurgeryHistory";
import { FaqSection } from "@/components/sections/FaqSection";
import {
  LatestClinicUpdates,
  CaseHighlightsSection,
  ClinicGallerySection,
} from "@/components/sections/ClinicSocialGallery";
import { Contact } from "@/components/sections/Contact";
import { routing, type Locale } from "@/i18n/routing";
import { getHomeMetadata } from "@/lib/metadata";
import { getTranslations } from "next-intl/server";

type Props = { params: Promise<{ locale: string }> };

export function generateStaticParams() {
  return routing.locales.map((locale) => ({ locale }));
}

export async function generateMetadata({ params }: Props) {
  const { locale } = await params;
  const t = await getTranslations({ locale, namespace: "seo" });
  return getHomeMetadata(locale as Locale, (key) => t(key));
}

export default async function HomePage({ params }: Props) {
  const { locale } = await params;
  setRequestLocale(locale);

  return (
    <SiteChrome>
      <Hero />
      <ExecutiveHero />
      <About />
      <Services />
      <DoctorProfile />
      <SurgeryHistory />
      <FaqSection />
      <LatestClinicUpdates />
      <CaseHighlightsSection />
      <ClinicGallerySection />
      <Contact />
    </SiteChrome>
  );
}
