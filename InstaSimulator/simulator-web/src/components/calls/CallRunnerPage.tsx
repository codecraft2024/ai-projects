"use client";

import { useMutation } from "@tanstack/react-query";
import { motion } from "framer-motion";
import { Play, Zap } from "lucide-react";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { JsonViewer } from "@/components/runtime/JsonViewer";
import { MetricsBar } from "@/components/runtime/MetricsBar";
import { CALL_META } from "@/lib/catalog";
import { byteSize } from "@/lib/format";
import {
  fetchBind1Failure,
  fetchBind1Success,
  fetchHealthFailure,
  fetchHealthSuccess,
  fetchRegister1Failure,
  fetchRegister1Success,
} from "@/services/calls.service";
import type { CallMeta } from "@/types";

function isSuccessResponse(callId: string, response: unknown): boolean {
  if (!response || typeof response !== "object") return false;
  if ("success" in response) return Boolean((response as { success: boolean }).success);
  if ("code" in response) return (response as { code: string }).code === "00000";
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

  const mutation = useMutation<{ data: unknown; status: number }, Error, "success" | "failure">({
    mutationFn: (mode) => RUNNERS[callId][mode](),
  });

  const payload = mutation.data?.data;
  const responseBody =
    payload && typeof payload === "object" && "response" in payload
      ? (payload as { response: unknown }).response
      : payload;
  const requestBody =
    payload && typeof payload === "object" && "request" in payload
      ? (payload as { request: unknown }).request
      : { method: meta.method, path: meta.successPath };

  const ok = responseBody ? isSuccessResponse(callId, responseBody) : false;

  return (
    <div className="space-y-6">
      <motion.div initial={{ opacity: 0, y: 8 }} animate={{ opacity: 1, y: 0 }} className="space-y-3">
        <div className="flex flex-wrap items-center gap-2">
          <h1 className="text-3xl font-semibold tracking-tight">{meta.name}</h1>
          <Badge variant="outline" className="rounded-full font-mono text-xs">
            {meta.method}
          </Badge>
        </div>
        <p className="max-w-3xl text-muted-foreground">{meta.description}</p>
        <div className="flex flex-wrap gap-2 font-mono text-xs text-muted-foreground">
          <code className="rounded-lg bg-muted px-2 py-1">{meta.successPath}</code>
          <code className="rounded-lg bg-muted px-2 py-1">{meta.failurePath}</code>
        </div>
      </motion.div>

      <div className="flex flex-wrap gap-3">
        <Button className="rounded-xl" disabled={mutation.isPending} onClick={() => mutation.mutate("success")}>
          <Play className="size-4" />
          Run Success
        </Button>
        <Button
          variant="outline"
          className="rounded-xl"
          disabled={mutation.isPending}
          onClick={() => mutation.mutate("failure")}
        >
          <Zap className="size-4" />
          Run Failure
        </Button>
      </div>

      <MetricsBar
        metrics={
          mutation.data
            ? {
                durationMs:
                  payload && typeof payload === "object" && "durationMs" in payload
                    ? Number((payload as { durationMs: number }).durationMs)
                    : 0,
                requestSize: byteSize(requestBody),
                responseSize: byteSize(responseBody),
                httpStatus: mutation.data.status,
                success: ok,
              }
            : null
        }
      />

      <div className="grid gap-4 lg:grid-cols-2">
        <JsonViewer title="Request" value={requestBody} />
        <JsonViewer title="Response" value={responseBody ?? payload} />
      </div>

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
