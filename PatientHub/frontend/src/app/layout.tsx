import type { Metadata, Viewport } from "next";
import type { ReactNode } from "react";
import { SITE_BASE_URL, OG_DEFAULT } from "@/config/social-links";

export const metadata: Metadata = {
  metadataBase: new URL(SITE_BASE_URL),
  applicationName: OG_DEFAULT.siteName,
  referrer: "origin-when-cross-origin",
  formatDetection: {
    telephone: true,
    email: true,
  },
};

export const viewport: Viewport = {
  width: "device-width",
  initialScale: 1,
  themeColor: "#4B248B",
};

/** Root layout — locale-specific html/body in [locale]/layout.tsx */
export default function RootLayout({ children }: { children: ReactNode }) {
  return children;
}
