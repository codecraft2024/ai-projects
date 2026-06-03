import { ImageResponse } from "next/og";
import { OG_DEFAULT } from "@/config/social-links";

export const ogSize = { width: 1200, height: 630 };

type OgImageOptions = {
  title: string;
  subtitle: string;
  badge?: string;
};

export function renderOgImage({ title, subtitle, badge }: OgImageOptions) {
  return new ImageResponse(
    (
      <div
        style={{
          height: "100%",
          width: "100%",
          display: "flex",
          flexDirection: "column",
          justifyContent: "space-between",
          padding: 64,
          background: "linear-gradient(135deg, #6D4AFF 0%, #5A3AD9 45%, #1a1625 100%)",
          color: "white",
          fontFamily: "system-ui, sans-serif",
        }}
      >
        <div style={{ display: "flex", alignItems: "center", gap: 20 }}>
          <div
            style={{
              width: 72,
              height: 72,
              borderRadius: 16,
              background: "#FFC107",
              color: "#1a1625",
              display: "flex",
              alignItems: "center",
              justifyContent: "center",
              fontSize: 28,
              fontWeight: 700,
            }}
          >
            PH
          </div>
          <div style={{ display: "flex", flexDirection: "column" }}>
            <span style={{ fontSize: 22, opacity: 0.95 }}>{OG_DEFAULT.siteName}</span>
            <span style={{ fontSize: 18, opacity: 0.8 }}>{OG_DEFAULT.clinicName}</span>
          </div>
        </div>
        <div style={{ display: "flex", flexDirection: "column", gap: 16 }}>
          {badge && (
            <span
              style={{
                fontSize: 18,
                textTransform: "uppercase",
                letterSpacing: 2,
                color: "#FFC107",
              }}
            >
              {badge}
            </span>
          )}
          <span style={{ fontSize: 52, fontWeight: 700, lineHeight: 1.15, maxWidth: 900 }}>
            {title}
          </span>
          <span style={{ fontSize: 26, opacity: 0.9, maxWidth: 800, lineHeight: 1.4 }}>
            {subtitle}
          </span>
        </div>
        <span style={{ fontSize: 18, opacity: 0.65 }}>
          Heliopolis, Cairo · Orthopedic & Bone Clinic
        </span>
      </div>
    ),
    ogSize,
  );
}
