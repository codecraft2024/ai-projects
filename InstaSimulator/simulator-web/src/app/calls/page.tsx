"use client";

import { CatalogGrid } from "@/components/common/CatalogGrid";
import { CALLS } from "@/lib/catalog";

export default function CallsPage() {
  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-3xl font-semibold tracking-tight">Calls</h1>
        <p className="mt-2 max-w-2xl text-muted-foreground">
          Execute Health Check, Bind1, and Register1 against the live simulator API.
        </p>
      </div>
      <CatalogGrid items={CALLS} />
    </div>
  );
}
