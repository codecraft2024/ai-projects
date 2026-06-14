"use client";

import Link from "next/link";
import { LanguageSwitcher } from "@/components/layout/LanguageSwitcher";
import { ThemeSwitcher } from "@/components/layout/ThemeSwitcher";
import { useLanguage } from "@/lib/i18n/LanguageProvider";

export function SiteHeader() {
  const { t } = useLanguage();

  return (
    <header className="border-b-4 border-foreground/20 bg-card/90 backdrop-blur">
      <div className="mx-auto flex max-w-7xl items-center justify-between px-4 py-4 sm:px-6">
        <Link href="/" className="text-2xl font-black tracking-tight text-primary">
          {t("brand")}
        </Link>
        <nav className="flex items-center gap-3 text-sm font-semibold sm:gap-5">
          <Link href="/" className="hover:text-primary">
            {t("navHome")}
          </Link>
          <Link href="/search/results" className="hover:text-primary">
            {t("navResults")}
          </Link>
          <LanguageSwitcher />
          <ThemeSwitcher />
        </nav>
      </div>
    </header>
  );
}

export function SiteFooter() {
  const { t } = useLanguage();

  return (
    <footer className="border-t-4 border-foreground/20 bg-card py-8 text-center text-sm font-medium">
      {t("footer")}
    </footer>
  );
}
