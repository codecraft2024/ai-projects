"use client";

import dynamic from "next/dynamic";

const FloatingWhatsApp = dynamic(
  () =>
    import("@/components/ui/FloatingWhatsApp").then((m) => ({
      default: m.FloatingWhatsApp,
    })),
  { ssr: false },
);

export function HomePageExtras() {
  return <FloatingWhatsApp />;
}
