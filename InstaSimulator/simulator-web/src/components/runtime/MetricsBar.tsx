"use client";

import { Card, CardContent } from "@/components/ui/card";
import type { ExecutionMetrics } from "@/types";
import { cn } from "@/lib/utils";

export function MetricsBar({ metrics }: { metrics: ExecutionMetrics | null }) {
  const items = [
    { label: "Duration", value: metrics ? `${metrics.durationMs} ms` : "—" },
    { label: "Request Size", value: metrics ? `${metrics.requestSize} B` : "—" },
    { label: "Response Size", value: metrics ? `${metrics.responseSize} B` : "—" },
    { label: "HTTP Status", value: metrics ? String(metrics.httpStatus) : "—" },
    {
      label: "Result",
      value: metrics ? (metrics.success ? "Success" : "Failure") : "—",
      tone: metrics ? (metrics.success ? "ok" : "bad") : undefined,
    },
  ] as const;

  return (
    <div className="grid gap-3 sm:grid-cols-2 lg:grid-cols-5">
      {items.map((item) => (
        <Card key={item.label} className="rounded-2xl">
          <CardContent className="p-4">
            <p className="text-xs tracking-wide text-muted-foreground uppercase">{item.label}</p>
            <p
              className={cn(
                "mt-1 text-lg font-semibold tracking-tight",
                "tone" in item && item.tone === "ok" && "text-emerald-600 dark:text-emerald-400",
                "tone" in item && item.tone === "bad" && "text-red-600 dark:text-red-400",
              )}
            >
              {item.value}
            </p>
          </CardContent>
        </Card>
      ))}
    </div>
  );
}
