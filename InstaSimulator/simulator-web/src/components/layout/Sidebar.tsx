"use client";

import Link from "next/link";
import { usePathname } from "next/navigation";
import { Activity, GitBranch, LayoutDashboard, PhoneCall, Settings } from "lucide-react";
import { cn } from "@/lib/utils";

const NAV = [
  { href: "/", label: "Dashboard", icon: LayoutDashboard },
  { href: "/calls", label: "Calls", icon: PhoneCall },
  { href: "/scenarios", label: "Scenarios", icon: GitBranch },
  { href: "/settings", label: "Settings", icon: Settings },
];

export function Sidebar() {
  const pathname = usePathname();

  return (
    <aside className="relative flex h-full w-64 shrink-0 flex-col overflow-hidden border-r border-sidebar-border bg-sidebar text-sidebar-foreground">
      <div
        aria-hidden
        className="pointer-events-none absolute inset-0 bg-[radial-gradient(ellipse_at_top,oklch(0.4_0.08_195_/_0.35),transparent_55%)]"
      />

      <div className="relative flex items-center gap-3 border-b border-sidebar-border px-5 py-6">
        <div className="flex size-10 items-center justify-center rounded-xl bg-signal text-signal-foreground">
          <Activity className="size-5" />
        </div>
        <div>
          <p className="font-heading text-base font-bold tracking-tight">InstaSimulator</p>
          <p className="text-[11px] tracking-[0.14em] text-sidebar-foreground/55 uppercase">
            QA console
          </p>
        </div>
      </div>

      <nav className="relative flex flex-1 flex-col gap-1 p-3">
        {NAV.map((item) => {
          const active =
            item.href === "/"
              ? pathname === "/"
              : pathname === item.href || pathname.startsWith(`${item.href}/`);
          const Icon = item.icon;
          return (
            <Link
              key={item.href}
              href={item.href}
              className={cn(
                "group flex items-center gap-3 rounded-xl px-3 py-2.5 text-sm font-medium transition-colors duration-200",
                active
                  ? "bg-sidebar-accent text-sidebar-accent-foreground"
                  : "text-sidebar-foreground/65 hover:bg-sidebar-accent/70 hover:text-sidebar-accent-foreground",
              )}
            >
              <Icon
                className={cn(
                  "size-4 transition-colors",
                  active ? "text-signal" : "text-sidebar-foreground/50 group-hover:text-signal",
                )}
              />
              {item.label}
            </Link>
          );
        })}
      </nav>

      <div className="relative border-t border-sidebar-border p-4 font-mono text-[10px] tracking-wide text-sidebar-foreground/45 uppercase">
        Health · Bind1 · Register1
      </div>
    </aside>
  );
}
