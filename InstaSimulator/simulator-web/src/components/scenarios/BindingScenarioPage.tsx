"use client";

import { useMutation } from "@tanstack/react-query";
import { motion } from "framer-motion";
import { GitBranch, Play } from "lucide-react";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { JsonViewer } from "@/components/runtime/JsonViewer";
import { BINDING_SCENARIO } from "@/lib/catalog";
import { runBindingScenario } from "@/services/scenario.service";
import { cn } from "@/lib/utils";

export function BindingScenarioPage() {
  const mutation = useMutation({
    mutationFn: runBindingScenario,
  });

  const result = mutation.data?.data;

  return (
    <div className="space-y-6">
      <motion.div initial={{ opacity: 0, y: 8 }} animate={{ opacity: 1, y: 0 }} className="space-y-3">
        <div className="flex flex-wrap items-center gap-2">
          <GitBranch className="size-7 text-violet-500" />
          <h1 className="text-3xl font-semibold tracking-tight">{BINDING_SCENARIO.name}</h1>
          {result && (
            <Badge
              className={cn(
                "rounded-full",
                result.success
                  ? "bg-emerald-500/15 text-emerald-700 dark:text-emerald-300"
                  : "bg-red-500/15 text-red-700 dark:text-red-300",
              )}
            >
              {result.status}
            </Badge>
          )}
        </div>
        <p className="max-w-3xl text-muted-foreground">{BINDING_SCENARIO.description}</p>
        <div className="flex flex-wrap gap-2">
          {BINDING_SCENARIO.steps.map((step, i) => (
            <Badge key={step} variant="outline" className="rounded-full font-mono text-xs">
              {i + 1}. {step}
            </Badge>
          ))}
        </div>
      </motion.div>

      <Button
        className="rounded-xl"
        disabled={mutation.isPending}
        onClick={() => mutation.mutate()}
      >
        <Play className="size-4" />
        {mutation.isPending ? "Running…" : "Run Binding Scenario"}
      </Button>

      {result && (
        <>
          <Card className="rounded-2xl">
            <CardHeader>
              <CardTitle className="text-base">
                Step outcomes · {result.durationMs} ms · HTTP {mutation.data?.status}
              </CardTitle>
            </CardHeader>
            <CardContent className="space-y-3">
              {result.steps.map((step) => (
                <div
                  key={step.stepName}
                  className={cn(
                    "flex items-center justify-between rounded-xl border px-4 py-3",
                    step.success
                      ? "border-emerald-500/30 bg-emerald-500/5"
                      : "border-red-500/30 bg-red-500/5",
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
            </CardContent>
          </Card>

          <JsonViewer title="Scenario Result" value={result} />
        </>
      )}

      {mutation.isError && (
        <Card className="rounded-2xl border-red-500/40">
          <CardContent className="p-4 text-sm text-red-600 dark:text-red-400">
            {(mutation.error as Error).message}
          </CardContent>
        </Card>
      )}
    </div>
  );
}
