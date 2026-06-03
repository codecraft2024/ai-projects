import type { Viewport } from "next";
import { NextIntlClientProvider } from "next-intl";
import { getMessages, getTranslations, setRequestLocale } from "next-intl/server";
import { notFound } from "next/navigation";
import { Inter, Cairo } from "next/font/google";
import { routing, type Locale } from "@/i18n/routing";
import { JsonLd } from "@/components/seo/JsonLd";
import { AnalyticsScripts } from "@/components/seo/AnalyticsScripts";
import "../globals.css";

const inter = Inter({
  subsets: ["latin"],
  variable: "--font-inter",
  display: "swap",
});

const cairo = Cairo({
  subsets: ["arabic", "latin"],
  variable: "--font-cairo",
  display: "swap",
});

export const viewport: Viewport = {
  width: "device-width",
  initialScale: 1,
  themeColor: "#6D4AFF",
};

export function generateStaticParams() {
  return routing.locales.map((locale) => ({ locale }));
}

type Props = {
  children: React.ReactNode;
  params: Promise<{ locale: string }>;
};

export default async function LocaleLayout({ children, params }: Props) {
  const { locale } = await params;

  if (!routing.locales.includes(locale as Locale)) {
    notFound();
  }

  setRequestLocale(locale);
  const messages = await getMessages();
  const tSite = await getTranslations({ locale, namespace: "site" });
  const tDoctor = await getTranslations({ locale, namespace: "doctor" });
  const tFaq = await getTranslations({ locale, namespace: "faq" });

  const faqItems = [0, 1, 2].map((i) => ({
    question: tFaq(`items.${i}.question`),
    answer: tFaq(`items.${i}.answer`),
  }));

  const dir = locale === "ar" ? "rtl" : "ltr";

  return (
    <html
      lang={locale}
      dir={dir}
      className={`${inter.variable} ${cairo.variable} scroll-smooth`}
      suppressHydrationWarning
    >
      <head>
        <AnalyticsScripts />
      </head>
      <body className="min-h-screen bg-white text-[var(--foreground)] antialiased">
        <JsonLd
          locale={locale as Locale}
          t={{
            clinicName: tSite("clinicName"),
            description: tSite("description"),
            doctorName: tDoctor("name"),
            doctorTitle: tDoctor("title"),
            faq: faqItems,
          }}
        />
        <NextIntlClientProvider messages={messages}>{children}</NextIntlClientProvider>
      </body>
    </html>
  );
}
