"use client";

import { Check, Copy } from "lucide-react";
import { useState } from "react";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Collapsible, CollapsibleContent, CollapsibleTrigger } from "@/components/ui/collapsible";
import { prettyJson } from "@/lib/format";

export function JsonViewer({
  title,
  value,
  emptyMessage = "No payload yet",
}: {
  title: string;
  value: unknown;
  emptyMessage?: string;
}) {
  const [open, setOpen] = useState(true);
  const [copied, setCopied] = useState(false);
  const text = value == null ? "" : prettyJson(value);

  async function onCopy() {
    if (!text) return;
    await navigator.clipboard.writeText(text);
    setCopied(true);
    setTimeout(() => setCopied(false), 1200);
  }

  return (
    <Card className="rounded-2xl">
      <Collapsible open={open} onOpenChange={setOpen}>
        <CardHeader className="flex flex-row items-center justify-between gap-3 space-y-0 pb-3">
          <CardTitle className="text-base">{title}</CardTitle>
          <div className="flex items-center gap-2">
            <Button variant="outline" size="sm" className="rounded-lg" onClick={onCopy} disabled={!text}>
              {copied ? <Check className="size-3.5" /> : <Copy className="size-3.5" />}
              {copied ? "Copied" : "Copy"}
            </Button>
            <CollapsibleTrigger className="inline-flex h-7 items-center rounded-lg px-2.5 text-[0.8rem] hover:bg-muted">
              {open ? "Collapse" : "Expand"}
            </CollapsibleTrigger>
          </div>
        </CardHeader>
        <CollapsibleContent>
          <CardContent>
            <pre className="max-h-80 overflow-auto rounded-xl bg-muted/60 p-4 font-mono text-xs leading-relaxed">
              {text || emptyMessage}
            </pre>
          </CardContent>
        </CollapsibleContent>
      </Collapsible>
    </Card>
  );
}
