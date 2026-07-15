"use client";

import { useMutation } from "@tanstack/react-query";
import { motion } from "framer-motion";
import { GitBranch, Loader2, Play } from "lucide-react";
import { Button } from "@/components/ui/button";
import { TimingSection } from "@/components/runtime/CallHttpPanel";
import { BINDING_SCENARIO } from "@/lib/catalog";
import { runBindingScenario } from "@/services/scenario.service";
import { cn } from "@/lib/utils";

export function BindingScenarioPage() {
  const mutation = useMutation({
    mutationFn: runBindingScenario,
  });

  const result = mutation.data?.data;
  const loading = mutation.isPending;

  return (
    <div className="space-y-6">
      <motion.header
        initial={{ opacity: 0, y: 8 }}
        animate={{ opacity: 1, y: 0 }}
        className="space-y-4"
      >
        <p className="text-[11px] font-medium tracking-[0.18em] text-signal uppercase">Scenario</p>
        <div className="flex flex-wrap items-center gap-3">
          <GitBranch className="size-6 text-signal" />
          <h1 className="font-heading text-4xl font-semibold tracking-tight">
            {BINDING_SCENARIO.name}
          </h1>
          {result && !loading && (
            <span
              className={cn(
                "rounded-md px-2 py-0.5 text-[11px] font-semibold tracking-wide uppercase",
                result.success
                  ? "bg-emerald-500/12 text-emerald-700 dark:text-emerald-300"
                  : "bg-red-500/12 text-red-700 dark:text-red-300",
              )}
            >
              {result.status}
            </span>
          )}
        </div>
        <p className="max-w-2xl text-sm leading-relaxed text-muted-foreground">
          {BINDING_SCENARIO.description}
        </p>
        <div className="flex flex-wrap gap-2">
          {BINDING_SCENARIO.steps.map((step, i) => (
            <span
              key={step}
              className="rounded-md border border-border/60 bg-muted/30 px-2.5 py-1 font-mono text-[11px] text-muted-foreground"
            >
              {i + 1}. {step}
            </span>
          ))}
        </div>
      </motion.header>

      <Button className="rounded-xl px-5" disabled={loading} onClick={() => mutation.mutate()}>
        {loading ? <Loader2 className="size-4 animate-spin" /> : <Play className="size-4" />}
        {loading ? "Running…" : "Run Binding Scenario"}
      </Button>

      <TimingSection
        loading={loading}
        sentAt={result?.startedAt}
        receivedAt={result?.finishedAt}
        durationMs={result?.durationMs}
      />

      {(result || loading) && (
        <section className="panel-glass rounded-2xl p-5">
          <h2 className="mb-4 font-heading text-base font-semibold tracking-tight">Steps</h2>
          {loading && !result ? (
            <div className="flex items-center gap-2 py-8 text-sm text-muted-foreground">
              <Loader2 className="size-4 animate-spin text-signal" />
              Executing steps…
            </div>
          ) : (
            <div className="space-y-2">
              {result?.steps.map((step) => (
                <div
                  key={step.stepName}
                  className={cn(
                    "flex items-center justify-between rounded-xl border px-4 py-3",
                    step.success
                      ? "border-emerald-500/25 bg-emerald-500/6"
                      : "border-red-500/25 bg-red-500/6",
                  )}
                >
                  <div>
                    <p className="font-medium">{step.stepName}</p>
                    <p className="text-xs text-muted-foreground">{step.message}</p>
                  </div>
                  <div className="text-right font-mono text-xs">
                    <p>{step.status}</p>
                    <p className="text-muted-foreground">{step.statusCode}</p>
                  </div>
                </div>
              ))}
            </div>
          )}
        </section>
      )}

      {mutation.isError && (
        <div className="rounded-2xl border border-red-500/30 bg-red-500/8 p-4 text-sm text-red-700 dark:text-red-300">
          {(mutation.error as Error).message}
        </div>
      )}
    </div>
  );
}
