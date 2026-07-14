"use client";

import { CatalogGrid } from "@/components/common/CatalogGrid";
import { SCENARIOS } from "@/lib/catalog";

export default function ScenariosPage() {
  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-3xl font-semibold tracking-tight">Scenarios</h1>
        <p className="mt-2 max-w-2xl text-muted-foreground">
          Multi-step business flows composed from simulator calls.
        </p>
      </div>
      <CatalogGrid items={SCENARIOS} />
    </div>
  );
}
