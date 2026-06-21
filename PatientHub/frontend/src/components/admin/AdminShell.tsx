"use client";

import { useState } from "react";
import { useTranslations } from "next-intl";
import { Link, useRouter } from "@/i18n/navigation";
import { useAuth } from "@/hooks/useAuth";
import { LanguageSwitcher } from "@/components/layout/LanguageSwitcher";
import { Logo } from "@/components/ui/Logo";
import { cn } from "@/utils/cn";

type AdminShellProps = {
  children: React.ReactNode;
  title: string;
  subtitle?: string;
  activeNav?: "dashboard" | "patients";
};

export function AdminShell({ children, title, subtitle, activeNav }: AdminShellProps) {
  const t = useTranslations("admin");
  const tSite = useTranslations("site");
  const { session, logout } = useAuth();
  const router = useRouter();
  const [menuOpen, setMenuOpen] = useState(false);

  const handleLogout = () => {
    logout();
    router.push("/admin/login");
  };

  return (
    <div className="min-h-screen bg-muted">
      <header className="border-b border-[var(--border)] bg-white shadow-sm">
        <div className="mx-auto max-w-7xl px-3 sm:px-6 lg:px-8">
          <div className="flex h-14 items-center justify-between gap-2 sm:h-16 sm:gap-3">
            <div className="flex min-w-0 items-center gap-2 sm:gap-3">
              <Link href="/" className="flex shrink-0 items-center gap-2">
                <Logo variant="icon" height={34} className="shrink-0" />
                <span className="hidden truncate text-sm font-bold text-slate-900 md:block">
                  {tSite("portalName")}
                </span>
              </Link>
              <span className="hidden text-slate-300 sm:inline">|</span>
              <span className="truncate text-xs font-medium text-slate-600 sm:text-sm">
                {t("adminLabel")}
              </span>
            </div>

            <div className="flex items-center gap-1.5 sm:gap-3">
              <LanguageSwitcher />
              {session && (
                <span className="hidden max-w-[6rem] truncate text-sm text-slate-500 lg:inline xl:max-w-none">
                  {session.username}
                </span>
              )}
              <Link
                href="/"
                className="hidden rounded-lg px-3 py-2 text-sm font-medium text-slate-600 hover:bg-muted md:inline-flex"
              >
                {t("viewSite")}
              </Link>
              <button
                type="button"
                onClick={handleLogout}
                className="hidden rounded-lg bg-slate-900 px-3 py-2 text-sm font-semibold text-white hover:bg-slate-800 sm:inline-flex"
              >
                {t("logout")}
              </button>
              <button
                type="button"
                className="inline-flex h-10 w-10 items-center justify-center rounded-xl text-slate-700 hover:bg-muted sm:hidden"
                aria-expanded={menuOpen}
                aria-controls="admin-mobile-menu"
                aria-label={menuOpen ? "Close menu" : "Open menu"}
                onClick={() => setMenuOpen((open) => !open)}
              >
                <svg className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
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
            id="admin-mobile-menu"
            className={cn(
              "overflow-hidden border-t border-[var(--border)] transition-all sm:hidden",
              menuOpen ? "max-h-48 pb-3 pt-2 opacity-100" : "max-h-0 opacity-0",
            )}
            aria-hidden={!menuOpen}
          >
            <div className="flex flex-col gap-1">
              {session && (
                <p className="px-2 py-1 text-xs text-slate-500">{session.username}</p>
              )}
              <Link
                href="/"
                className="rounded-lg px-3 py-2.5 text-sm font-medium text-slate-700 hover:bg-muted"
                onClick={() => setMenuOpen(false)}
              >
                {t("viewSite")}
              </Link>
              <button
                type="button"
                onClick={() => {
                  setMenuOpen(false);
                  handleLogout();
                }}
                className="rounded-lg bg-slate-900 px-3 py-2.5 text-start text-sm font-semibold text-white hover:bg-slate-800"
              >
                {t("logout")}
              </button>
            </div>
          </nav>
        </div>
      </header>

      <main className="mx-auto max-w-7xl px-3 py-4 sm:px-6 sm:py-8 lg:px-8">
        <nav className="-mx-3 mb-5 flex gap-2 overflow-x-auto border-b border-[var(--border)] px-3 pb-3 sm:mx-0 sm:mb-6 sm:px-0 sm:pb-4">
          <NavLink href="/admin/dashboard" active={activeNav === "dashboard"}>
            {t("nav.dashboard")}
          </NavLink>
          <NavLink href="/admin/patients" active={activeNav === "patients"}>
            {t("nav.patients")}
          </NavLink>
        </nav>
        <div className="mb-5 sm:mb-8">
          <h1 className="text-xl font-bold text-slate-900 sm:text-3xl">{title}</h1>
          {subtitle && <p className="mt-1 text-sm text-slate-600 sm:text-base">{subtitle}</p>}
        </div>
        {children}
      </main>
    </div>
  );
}

function NavLink({
  href,
  active,
  children,
}: {
  href: string;
  active?: boolean;
  children: React.ReactNode;
}) {
  return (
    <Link
      href={href}
      className={cn(
        "shrink-0 rounded-lg px-3 py-2 text-sm font-medium transition sm:px-4",
        active
          ? "bg-brand text-white shadow-sm"
          : "text-slate-600 hover:bg-white hover:text-brand",
      )}
    >
      {children}
    </Link>
  );
}

export function AdminCard({
  title,
  children,
  className,
  action,
}: {
  title: string;
  children: React.ReactNode;
  className?: string;
  action?: React.ReactNode;
}) {
  return (
    <section
      className={cn(
        "overflow-hidden rounded-2xl border border-[var(--border)] bg-white shadow-sm",
        className,
      )}
    >
      <div className="flex flex-col gap-3 border-b border-[var(--border)] px-3 py-3 sm:flex-row sm:items-center sm:justify-between sm:px-6 sm:py-4">
        <h2 className="text-base font-semibold text-slate-900 sm:text-lg">{title}</h2>
        {action && <div className="shrink-0">{action}</div>}
      </div>
      <div className="overflow-x-auto p-3 sm:p-6">{children}</div>
    </section>
  );
}
