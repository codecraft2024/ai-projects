"use client";

import { useQuery, useQueryClient } from "@tanstack/react-query";
import { Moon, RefreshCw, Sun } from "lucide-react";
import { useTheme } from "next-themes";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
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
    <header className="flex h-16 items-center justify-between border-b border-border bg-background/80 px-6 backdrop-blur">
      <div className="flex items-center gap-3">
        <Badge
          variant="outline"
          className={cn(
            "gap-2 rounded-full px-3 py-1",
            status === "online" && "border-emerald-500/40 text-emerald-600 dark:text-emerald-400",
            status === "offline" && "border-red-500/40 text-red-600 dark:text-red-400",
          )}
        >
          <span
            className={cn(
              "size-2 rounded-full",
              status === "online" && "bg-emerald-500",
              status === "offline" && "bg-red-500",
              status === "checking" && "animate-pulse bg-amber-400",
            )}
          />
          Simulator {status === "checking" ? "Checking" : status === "online" ? "Online" : "Offline"}
        </Badge>
        <div className="hidden rounded-full border border-border bg-muted/50 px-3 py-1 font-mono text-xs text-muted-foreground md:block">
          {API_BASE_URL}
        </div>
      </div>

      <div className="flex items-center gap-2">
        <Button
          variant="outline"
          size="icon"
          className="rounded-full"
          onClick={() => queryClient.invalidateQueries()}
          aria-label="Refresh"
        >
          <RefreshCw className={cn("size-4", statusQuery.isFetching && "animate-spin")} />
        </Button>
        <Button
          variant="outline"
          size="icon"
          className="rounded-full"
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
