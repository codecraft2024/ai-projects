"use client";

import { useMutation } from "@tanstack/react-query";
import { motion } from "framer-motion";
import { Loader2, Play, Zap } from "lucide-react";
import { Button } from "@/components/ui/button";
import { HttpSection, TimingSection } from "@/components/runtime/CallHttpPanel";
import { CALL_META } from "@/lib/catalog";
import {
  fetchBind1Failure,
  fetchBind1Success,
  fetchHealthFailure,
  fetchHealthSuccess,
  fetchRegister1Failure,
  fetchRegister1Success,
} from "@/services/calls.service";
import type { CallMeta } from "@/types";
import { isCallExecution } from "@/types/network";
import type { HttpExchange, TracedResult } from "@/types/network";
import { cn } from "@/lib/utils";

function businessSuccess(callId: string, execution: unknown): boolean {
  if (!execution || typeof execution !== "object") return false;
  if (isCallExecution(execution)) {
    const res = execution.response as Record<string, unknown>;
    if ("success" in res) return Boolean(res.success);
    if ("code" in res) return res.code === "00000";
  }
  if ("success" in (execution as object)) return Boolean((execution as { success: boolean }).success);
  if ("code" in (execution as object)) return (execution as { code: string }).code === "00000";
  return callId === "health-check";
}

const RUNNERS = {
  "health-check": { success: fetchHealthSuccess, failure: fetchHealthFailure },
  bind1: { success: fetchBind1Success, failure: fetchBind1Failure },
  register1: { success: fetchRegister1Success, failure: fetchRegister1Failure },
} as const;

type CallId = keyof typeof RUNNERS;

export function CallRunnerPage({ callId }: { callId: CallId }) {
  const meta: CallMeta = CALL_META[callId];

  const mutation = useMutation<TracedResult<unknown>, Error, "success" | "failure">({
    mutationFn: (mode) => RUNNERS[callId][mode](),
  });

  const execution = isCallExecution(mutation.data?.data) ? mutation.data.data : null;
  const businessBody = execution?.response ?? mutation.data?.data;
  const ok = businessBody ? businessSuccess(callId, businessBody) : false;
  const exchange: HttpExchange | null = execution?.networkExchange ?? null;
  const loading = mutation.isPending;

  return (
    <div className="space-y-6">
      <motion.header
        initial={{ opacity: 0, y: 8 }}
        animate={{ opacity: 1, y: 0 }}
        className="space-y-4"
      >
        <div className="flex flex-wrap items-end justify-between gap-4">
          <div className="space-y-2">
            <p className="text-[11px] font-medium tracking-[0.18em] text-signal uppercase">Call</p>
            <div className="flex flex-wrap items-center gap-3">
              <h1 className="font-heading text-4xl font-semibold tracking-tight">{meta.name}</h1>
              <span className="rounded-md border border-border/70 bg-muted/40 px-2 py-0.5 font-mono text-[11px]">
                {meta.method}
              </span>
              {mutation.data && !loading && (
                <span
                  className={cn(
                    "rounded-md px-2 py-0.5 text-[11px] font-semibold tracking-wide uppercase",
                    ok
                      ? "bg-emerald-500/12 text-emerald-700 dark:text-emerald-300"
                      : "bg-red-500/12 text-red-700 dark:text-red-300",
                  )}
                >
                  {ok ? "Success" : "Failure"}
                </span>
              )}
            </div>
            <p className="max-w-2xl text-sm leading-relaxed text-muted-foreground">
              {meta.description}
            </p>
          </div>
        </div>

        <div className="flex flex-wrap gap-2 font-mono text-[11px] text-muted-foreground">
          <code className="rounded-lg border border-border/60 bg-muted/30 px-2.5 py-1">
            {meta.successPath}
          </code>
          <code className="rounded-lg border border-border/60 bg-muted/30 px-2.5 py-1">
            {meta.failurePath}
          </code>
        </div>
      </motion.header>

      <div className="flex flex-wrap gap-3">
        <Button
          className="rounded-xl px-5"
          disabled={loading}
          onClick={() => mutation.mutate("success")}
        >
          {loading && mutation.variables === "success" ? (
            <Loader2 className="size-4 animate-spin" />
          ) : (
            <Play className="size-4" />
          )}
          {loading && mutation.variables === "success" ? "Running…" : "Run Success"}
        </Button>
        <Button
          variant="outline"
          className="rounded-xl border-border/70 px-5"
          disabled={loading}
          onClick={() => mutation.mutate("failure")}
        >
          {loading && mutation.variables === "failure" ? (
            <Loader2 className="size-4 animate-spin" />
          ) : (
            <Zap className="size-4" />
          )}
          {loading && mutation.variables === "failure" ? "Running…" : "Run Failure"}
        </Button>
      </div>

      <TimingSection
        loading={loading}
        sentAt={exchange?.sentAt}
        receivedAt={exchange?.receivedAt}
        durationMs={exchange?.durationMs}
      />

      <HttpSection exchange={loading ? null : exchange} loading={loading} />

      {mutation.isError && (
        <div className="rounded-2xl border border-red-500/30 bg-red-500/8 p-4 text-sm text-red-700 dark:text-red-300">
          {(mutation.error as Error).message}
        </div>
      )}
    </div>
  );
}
