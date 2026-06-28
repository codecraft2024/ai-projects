"use client";

import { ImageUploadForm } from "@/components/search/ImageUploadForm";
import { useLanguage } from "@/lib/i18n/LanguageProvider";

export default function ScanPage() {
  const { t } = useLanguage();

  return (
    <div className="mx-auto max-w-4xl px-4 py-8 sm:px-6 sm:py-12">
      <div className="mb-8 text-center sm:mb-10">
        <p className="pill mb-4">{t("scanBadge")}</p>
        <h1 className="display-title text-2xl sm:text-4xl">{t("scanTitle")}</h1>
        <p className="mt-4 text-muted-foreground">{t("scanSubtitle")}</p>
      </div>
      <ImageUploadForm />
    </div>
  );
}
