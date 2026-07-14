"use client";

import { API_BASE_URL } from "@/lib/catalog";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";

export default function SettingsPage() {
  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-3xl font-semibold tracking-tight">Settings</h1>
        <p className="mt-2 text-muted-foreground">Workspace configuration for the simulator frontend.</p>
      </div>
      <Card className="max-w-xl rounded-2xl">
        <CardHeader>
          <CardTitle className="text-base">Backend Connection</CardTitle>
        </CardHeader>
        <CardContent className="space-y-2 text-sm">
          <p className="text-muted-foreground">Configured via NEXT_PUBLIC_API_BASE_URL</p>
          <code className="block rounded-xl bg-muted px-3 py-2 font-mono text-xs">{API_BASE_URL}</code>
        </CardContent>
      </Card>
    </div>
  );
}
