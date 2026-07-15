"use client";

import { API_BASE_URL } from "@/lib/catalog";

export default function SettingsPage() {
  return (
    <div className="space-y-6">
      <header className="space-y-3">
        <p className="text-[11px] font-medium tracking-[0.18em] text-signal uppercase">Config</p>
        <h1 className="font-heading text-4xl font-semibold tracking-tight">Settings</h1>
        <p className="max-w-xl text-sm leading-relaxed text-muted-foreground">
          Workspace configuration for the simulator frontend.
        </p>
      </header>

      <section className="panel-glass max-w-xl rounded-2xl p-5">
        <h2 className="mb-3 font-heading text-base font-semibold tracking-tight">
          Backend connection
        </h2>
        <p className="mb-2 text-sm text-muted-foreground">
          Configured via <code className="font-mono text-xs">NEXT_PUBLIC_API_BASE_URL</code>
        </p>
        <code className="code-surface block px-3 py-2.5 font-mono text-xs">{API_BASE_URL}</code>
      </section>
    </div>
  );
}
