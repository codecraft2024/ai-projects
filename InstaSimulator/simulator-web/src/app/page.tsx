"use client";

import { CatalogGrid } from "@/components/common/CatalogGrid";
import { CALLS, SCENARIOS } from "@/lib/catalog";

export default function DashboardPage() {
  return (
    <div className="space-y-10">
      <div>
        <h1 className="text-3xl font-semibold tracking-tight">Dashboard</h1>
        <p className="mt-2 max-w-2xl text-muted-foreground">
          InstaPay simulator — run individual calls or the full binding scenario.
        </p>
      </div>

      <section className="space-y-4">
        <h2 className="text-lg font-medium">Calls</h2>
        <CatalogGrid items={CALLS} />
      </section>

      <section className="space-y-4">
        <h2 className="text-lg font-medium">Scenarios</h2>
        <CatalogGrid items={SCENARIOS} />
      </section>
    </div>
  );
}
