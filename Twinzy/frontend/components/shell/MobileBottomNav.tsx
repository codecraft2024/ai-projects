"use client";

import Link from "next/link";
import { usePathname } from "next/navigation";
import { Compass, Home, ScanFace } from "lucide-react";
import { useLanguage } from "@/lib/i18n/LanguageProvider";
import { cn } from "@/lib/utils";

export function MobileBottomNav() {
  const pathname = usePathname();
  const { t } = useLanguage();

  const items = [
    { href: "/", label: t("navHome"), icon: Home },
    { href: "/discover", label: t("navDiscover"), icon: Compass },
    { href: "/scan", label: t("navScan"), icon: ScanFace, primary: true },
  ];

  return (
    <nav className="fixed inset-x-0 bottom-0 z-50 border-t border-border/60 bg-card/90 backdrop-blur-xl md:hidden safe-bottom">
      <div className="mx-auto flex max-w-lg items-stretch justify-around px-2 pt-2">
        {items.map(({ href, label, icon: Icon, primary }) => {
          const active = pathname === href || (href !== "/" && pathname.startsWith(href));
          return (
            <Link
              key={href}
              href={href}
              className={cn(
                "flex min-h-[48px] min-w-[72px] flex-1 flex-col items-center justify-center gap-1 rounded-2xl px-2 py-2 text-[11px] font-semibold transition",
                primary
                  ? active
                    ? "btn-glow bg-primary text-primary-foreground"
                    : "bg-primary text-primary-foreground"
                  : active
                    ? "bg-primary/10 text-primary"
                    : "text-muted-foreground",
              )}
            >
              <Icon className="h-5 w-5" />
              <span className="truncate">{label}</span>
            </Link>
          );
        })}
      </div>
    </nav>
  );
}
