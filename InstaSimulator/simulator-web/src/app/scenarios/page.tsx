"use client";

import { CatalogGrid } from "@/components/common/CatalogGrid";
import { SCENARIOS } from "@/lib/catalog";

export default function ScenariosPage() {
  return (
    <div className="space-y-6">
      <header className="space-y-3">
        <p className="text-[11px] font-medium tracking-[0.18em] text-signal uppercase">Flows</p>
        <h1 className="font-heading text-4xl font-semibold tracking-tight">Scenarios</h1>
        <p className="max-w-xl text-sm leading-relaxed text-muted-foreground">
          Multi-step business flows composed from simulator calls.
        </p>
      </header>
      <CatalogGrid items={SCENARIOS} />
    </div>
  );
}
