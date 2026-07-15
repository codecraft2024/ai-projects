"use client";

import { useQuery, useQueryClient } from "@tanstack/react-query";
import { Moon, RefreshCw, Sun } from "lucide-react";
import { useTheme } from "next-themes";
import { Button } from "@/components/ui/button";
import { API_BASE_URL } from "@/lib/catalog";
import { fetchBackendStatus } from "@/services/status.service";
import { cn } from "@/lib/utils";

export function TopNav() {
  const { theme, setTheme } = useTheme();
  const queryClient = useQueryClient();
  const statusQuery = useQuery({
    queryKey: ["backend-status"],
    queryFn: fetchBackendStatus,
    refetchInterval: 15_000,
  });

  const status = statusQuery.data ?? "checking";

  return (
    <header className="flex h-14 items-center justify-between border-b border-border/70 bg-surface/70 px-6 backdrop-blur-md md:px-9">
      <div className="flex items-center gap-3">
        <div
          className={cn(
            "inline-flex items-center gap-2 rounded-lg border px-2.5 py-1 text-xs font-medium",
            status === "online" && "border-emerald-500/30 bg-emerald-500/10 text-emerald-700 dark:text-emerald-300",
            status === "offline" && "border-red-500/30 bg-red-500/10 text-red-700 dark:text-red-300",
            status === "checking" && "border-amber-500/30 bg-amber-500/10 text-amber-700 dark:text-amber-300",
          )}
        >
          <span
            className={cn(
              "size-1.5 rounded-full",
              status === "online" && "bg-emerald-500",
              status === "offline" && "bg-red-500",
              status === "checking" && "animate-pulse bg-amber-400",
            )}
          />
          {status === "checking" ? "Checking" : status === "online" ? "Online" : "Offline"}
        </div>
        <div className="hidden rounded-lg border border-border/60 bg-muted/40 px-2.5 py-1 font-mono text-[11px] text-muted-foreground md:block">
          {API_BASE_URL}
        </div>
      </div>

      <div className="flex items-center gap-2">
        <Button
          variant="outline"
          size="icon"
          className="rounded-xl border-border/70"
          onClick={() => queryClient.invalidateQueries()}
          aria-label="Refresh"
        >
          <RefreshCw className={cn("size-4", statusQuery.isFetching && "animate-spin")} />
        </Button>
        <Button
          variant="outline"
          size="icon"
          className="relative rounded-xl border-border/70"
          onClick={() => setTheme(theme === "dark" ? "light" : "dark")}
          aria-label="Toggle theme"
        >
          <Sun className="size-4 scale-100 rotate-0 transition-all dark:scale-0 dark:-rotate-90" />
          <Moon className="absolute size-4 scale-0 rotate-90 transition-all dark:scale-100 dark:rotate-0" />
        </Button>
      </div>
    </header>
  );
}
