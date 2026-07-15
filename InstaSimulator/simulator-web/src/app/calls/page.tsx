"use client";

import { CatalogGrid } from "@/components/common/CatalogGrid";
import { CALLS } from "@/lib/catalog";

export default function CallsPage() {
  return (
    <div className="space-y-6">
      <header className="space-y-3">
        <p className="text-[11px] font-medium tracking-[0.18em] text-signal uppercase">Library</p>
        <h1 className="font-heading text-4xl font-semibold tracking-tight">Calls</h1>
        <p className="max-w-xl text-sm leading-relaxed text-muted-foreground">
          Execute Health Check, Bind1, and Register1 against the live simulator API.
        </p>
      </header>
      <CatalogGrid items={CALLS} />
    </div>
  );
}
