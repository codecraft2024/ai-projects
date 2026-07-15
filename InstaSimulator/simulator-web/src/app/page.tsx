"use client";

import { CatalogGrid } from "@/components/common/CatalogGrid";
import { CALLS, SCENARIOS } from "@/lib/catalog";

export default function DashboardPage() {
  return (
    <div className="space-y-10">
      <header className="space-y-3">
        <p className="text-[11px] font-medium tracking-[0.18em] text-signal uppercase">Workspace</p>
        <h1 className="font-heading text-4xl font-semibold tracking-tight">Dashboard</h1>
        <p className="max-w-xl text-sm leading-relaxed text-muted-foreground">
          Run individual InstaPay calls or the binding scenario against staging.
        </p>
      </header>

      <section className="space-y-4">
        <h2 className="font-heading text-lg font-semibold tracking-tight">Calls</h2>
        <CatalogGrid items={CALLS} />
      </section>

      <section className="space-y-4">
        <h2 className="font-heading text-lg font-semibold tracking-tight">Scenarios</h2>
        <CatalogGrid items={SCENARIOS} />
      </section>
    </div>
  );
}
