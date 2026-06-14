"use client";

import { useTranslations } from "next-intl";
import { Link, useRouter } from "@/i18n/navigation";
import { useAuth } from "@/hooks/useAuth";
import { LanguageSwitcher } from "@/components/layout/LanguageSwitcher";
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

  const handleLogout = () => {
    logout();
    router.push("/admin/login");
  };

  return (
    <div className="min-h-screen bg-muted">
      <header className="border-b border-[var(--border)] bg-white shadow-sm">
        <div className="mx-auto flex h-14 max-w-7xl flex-wrap items-center justify-between gap-3 px-4 sm:h-16 sm:px-6 lg:px-8">
          <div className="flex min-w-0 items-center gap-3">
            <Link href="/" className="flex shrink-0 items-center gap-2">
              <span className="flex h-9 w-9 items-center justify-center rounded-lg bg-brand text-sm font-bold text-white">
                PH
              </span>
              <span className="hidden truncate text-sm font-bold text-slate-900 sm:block">
                {tSite("portalName")}
              </span>
            </Link>
            <span className="text-slate-300">|</span>
            <span className="truncate text-sm font-medium text-slate-600">{t("adminLabel")}</span>
          </div>
          <div className="flex items-center gap-2 sm:gap-4">
            <LanguageSwitcher />
            {session && (
              <span className="hidden text-sm text-slate-500 sm:inline">{session.username}</span>
            )}
            <Link
              href="/"
              className="rounded-lg px-3 py-2 text-sm font-medium text-slate-600 hover:bg-muted"
            >
              {t("viewSite")}
            </Link>
            <button
              type="button"
              onClick={handleLogout}
              className="rounded-lg bg-slate-900 px-3 py-2 text-sm font-semibold text-white hover:bg-slate-800"
            >
              {t("logout")}
            </button>
          </div>
        </div>
      </header>

      <main className="mx-auto max-w-7xl px-4 py-6 sm:px-6 sm:py-8 lg:px-8">
        <nav className="mb-6 flex flex-wrap gap-2 border-b border-[var(--border)] pb-4">
          <NavLink href="/admin/dashboard" active={activeNav === "dashboard"}>
            {t("nav.dashboard")}
          </NavLink>
          <NavLink href="/admin/patients" active={activeNav === "patients"}>
            {t("nav.patients")}
          </NavLink>
        </nav>
        <div className="mb-6 sm:mb-8">
          <h1 className="text-2xl font-bold text-slate-900 sm:text-3xl">{title}</h1>
          {subtitle && <p className="mt-1 text-slate-600">{subtitle}</p>}
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
        "rounded-lg px-4 py-2 text-sm font-medium transition",
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
      <div className="flex flex-col gap-3 border-b border-[var(--border)] px-4 py-4 sm:flex-row sm:items-center sm:justify-between sm:px-6">
        <h2 className="text-lg font-semibold text-slate-900">{title}</h2>
        {action}
      </div>
      <div className="overflow-x-auto p-4 sm:p-6">{children}</div>
    </section>
  );
}
