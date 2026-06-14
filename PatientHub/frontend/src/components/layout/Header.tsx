"use client";

import { useState, useCallback } from "react";
import { useTranslations } from "next-intl";
import { Link } from "@/i18n/navigation";
import { Container } from "@/components/ui/Container";
import { Button } from "@/components/ui/Button";
import { SocialLinks } from "@/components/social/SocialLinks";
import { LanguageSwitcher } from "@/components/layout/LanguageSwitcher";
import { cn } from "@/utils/cn";

const NAV_ITEMS = [
  { href: "/", key: "home" as const },
  { href: "/#about", key: "about" as const },
  { href: "/#services", key: "services" as const },
  { href: "/doctor", key: "doctor" as const },
  { href: "/cases", key: "cases" as const },
  { href: "/#contact", key: "contact" as const },
];

export function Header() {
  const t = useTranslations("nav");
  const tSite = useTranslations("site");
  const [menuOpen, setMenuOpen] = useState(false);
  const closeMenu = useCallback(() => setMenuOpen(false), []);

  return (
    <header className="sticky top-0 z-50 border-b border-[var(--border)] bg-white/95 backdrop-blur-md">
      <Container>
        <div className="flex h-16 items-center justify-between gap-2 sm:h-[4.5rem]">
          <Link href="/" className="flex min-w-0 shrink items-center gap-2 sm:gap-2.5">
            <span className="flex h-9 w-9 shrink-0 items-center justify-center rounded-xl bg-brand-gradient text-sm font-bold text-white shadow-brand sm:h-10 sm:w-10">
              PH
            </span>
            <div className="min-w-0 leading-tight">
              <span className="block truncate text-sm font-bold text-slate-900 sm:text-base">
                {tSite("portalName")}
              </span>
              <span className="hidden truncate text-xs text-brand sm:block">
                {tSite("clinicName")}
              </span>
            </div>
          </Link>

          <nav className="hidden items-center gap-0.5 xl:flex" aria-label="Main">
            {NAV_ITEMS.map((item) => (
              <Link
                key={item.key}
                href={item.href}
                className="rounded-lg px-2.5 py-2 text-sm font-medium text-slate-600 transition hover:bg-brand-soft hover:text-brand"
              >
                {t(item.key)}
              </Link>
            ))}
          </nav>

          <div className="hidden items-center gap-2 lg:flex">
            <LanguageSwitcher />
            <SocialLinks variant="header" />
            <Button href="/#contact" variant="outline" size="sm">
              {t("bookVisit")}
            </Button>
            <Button href="/admin/login" variant="ghost" size="sm">
              {t("admin")}
            </Button>
          </div>

          <div className="flex items-center gap-1 lg:hidden">
            <LanguageSwitcher />
            <button
              type="button"
              className="touch-target inline-flex h-11 w-11 items-center justify-center rounded-xl text-slate-700 hover:bg-muted"
              aria-expanded={menuOpen}
              aria-controls="mobile-nav"
              aria-label={menuOpen ? "Close menu" : "Open menu"}
              onClick={() => setMenuOpen((o) => !o)}
            >
              <svg className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                {menuOpen ? (
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
                ) : (
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 6h16M4 12h16M4 18h16" />
                )}
              </svg>
            </button>
          </div>
        </div>

        <nav
          id="mobile-nav"
          className={cn(
            "overflow-hidden border-t border-[var(--border)] transition-all lg:hidden",
            menuOpen ? "max-h-[40rem] pb-4 opacity-100" : "max-h-0 opacity-0",
          )}
          aria-hidden={!menuOpen}
        >
          <ul className="flex flex-col gap-1 pt-3">
            {NAV_ITEMS.map((item) => (
              <li key={item.key}>
                <Link
                  href={item.href}
                  className="block rounded-lg px-3 py-3 text-base font-medium text-slate-700 hover:bg-brand-soft"
                  onClick={closeMenu}
                >
                  {t(item.key)}
                </Link>
              </li>
            ))}
            <li className="mt-3 space-y-3 border-t border-[var(--border)] pt-4">
              <SocialLinks variant="contact" className="justify-center" />
              <Button href="/#contact" variant="primary" className="w-full" onClick={closeMenu}>
                {t("bookVisit")}
              </Button>
            </li>
          </ul>
        </nav>
      </Container>
    </header>
  );
}
