"use client";

import Link from "next/link";
import { Sparkles } from "lucide-react";
import { LanguageSwitcher } from "@/components/layout/LanguageSwitcher";
import { MobileBottomNav } from "@/components/shell/MobileBottomNav";
import { useLanguage } from "@/lib/i18n/LanguageProvider";

export function AppShell({ children }: { children: React.ReactNode }) {
  const { t } = useLanguage();

  return (
    <div className="min-h-dvh">
      <header className="sticky top-0 z-40 border-b border-border/60 bg-card/80 backdrop-blur-xl safe-top">
        <div className="mx-auto flex max-w-6xl items-center justify-between gap-3 px-4 py-3 sm:px-6 sm:py-4">
          <Link href="/" className="flex min-w-0 items-center gap-2.5">
            <span className="flex h-9 w-9 shrink-0 items-center justify-center rounded-xl bg-gradient-to-br from-primary to-accent text-primary-foreground shadow-sm">
              <Sparkles className="h-5 w-5" />
            </span>
            <span className="display-title truncate text-lg sm:text-xl">{t("brand")}</span>
          </Link>

          <nav className="hidden items-center gap-1 text-sm font-medium md:flex md:gap-2">
            <Link href="/" className="rounded-full px-4 py-2 text-muted-foreground transition hover:bg-muted hover:text-foreground">
              {t("navHome")}
            </Link>
            <Link href="/discover" className="rounded-full px-4 py-2 text-muted-foreground transition hover:bg-muted hover:text-foreground">
              {t("navDiscover")}
            </Link>
            <Link
              href="/scan"
              className="btn-glow rounded-full bg-primary px-5 py-2 text-primary-foreground transition hover:opacity-90"
            >
              {t("navScan")}
            </Link>
            <LanguageSwitcher />
          </nav>

          <div className="md:hidden">
            <LanguageSwitcher />
          </div>
        </div>
      </header>

      <main className="pb-24 md:pb-16">{children}</main>

      <footer className="hidden border-t border-border/60 bg-card/50 py-8 text-center text-sm text-muted-foreground md:block">
        {t("footer")}
      </footer>

      <MobileBottomNav />
    </div>
  );
}
