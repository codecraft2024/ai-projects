"use client";

import { useLocale } from "next-intl";
import { usePathname, useRouter } from "@/i18n/navigation";
import { routing, type Locale } from "@/i18n/routing";
import { persistLocalePreference } from "@/utils/locale-persistence";
import { cn } from "@/utils/cn";

export function LanguageSwitcher({ className }: { className?: string }) {
  const locale = useLocale() as Locale;
  const router = useRouter();
  const pathname = usePathname();

  const switchLocale = (next: Locale) => {
    if (next === locale) return;

    persistLocalePreference(next);
    router.replace(pathname, { locale: next });
  };

  return (
    <div
      className={cn(
        "inline-flex rounded-xl border border-[var(--border)] bg-white p-1 shadow-sm",
        className,
      )}
      role="group"
      aria-label="Language"
    >
      {routing.locales.map((l) => (
        <button
          key={l}
          type="button"
          onClick={() => switchLocale(l)}
          className={cn(
            "touch-target rounded-lg px-3 py-1.5 text-xs font-semibold transition sm:text-sm",
            locale === l
              ? "bg-brand text-white shadow-sm"
              : "text-slate-600 hover:bg-brand-soft hover:text-brand",
          )}
          aria-pressed={locale === l}
        >
          {l === "ar" ? "العربية" : "EN"}
        </button>
      ))}
    </div>
  );
}
