"use client";

import Link from "next/link";
import { useLanguage } from "@/lib/i18n/LanguageProvider";

export default function NotFound() {
  const { t } = useLanguage();

  return (
    <section className="mx-auto max-w-xl px-4 py-24 text-center sm:px-6">
      <h1 className="display-title text-3xl">{t("notFound")}</h1>
      <p className="mt-4 font-medium text-muted-foreground">{t("notFoundHint")}</p>
      <Link href="/" className="mt-8 inline-flex h-10 items-center rounded-full bg-primary px-4 font-bold text-primary-foreground">
        {t("backHome")}
      </Link>
    </section>
  );
}
