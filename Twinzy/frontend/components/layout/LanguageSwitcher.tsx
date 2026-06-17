"use client";

import { useLanguage } from "@/lib/i18n/LanguageProvider";
import { Button } from "@/components/ui/button";

export function LanguageSwitcher() {
  const { locale, setLocale } = useLanguage();

  return (
    <div className="flex items-center gap-1 rounded-full border-2 border-foreground/20 bg-card p-1">
      <Button
        size="sm"
        variant={locale === "en" ? "default" : "ghost"}
        className="h-9 min-w-[44px] touch-target rounded-full px-3 text-xs"
        onClick={() => setLocale("en")}
      >
        EN
      </Button>
      <Button
        size="sm"
        variant={locale === "ar" ? "default" : "ghost"}
        className="h-9 min-w-[44px] touch-target rounded-full px-3 text-xs"
        onClick={() => setLocale("ar")}
      >
        عربي
      </Button>
    </div>
  );
}
