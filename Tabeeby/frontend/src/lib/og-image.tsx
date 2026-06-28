import { ImageResponse } from "next/og";
import { OG_DEFAULT, SITE_BASE_URL } from "@/config/social-links";

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
              width: 80,
              height: 80,
              borderRadius: "50%",
              background: "#ffffff",
              display: "flex",
              alignItems: "center",
              justifyContent: "center",
              padding: 12,
              boxShadow: "0 8px 24px rgba(0,0,0,0.2)",
            }}
          >
            {/* eslint-disable-next-line @next/next/no-img-element */}
            <img
              src={`${SITE_BASE_URL}/tabeeby-icon.png`}
              alt=""
              width={56}
              height={56}
              style={{ objectFit: "contain" }}
            />
          </div>
          <div style={{ display: "flex", flexDirection: "column" }}>
            <span style={{ fontSize: 22, opacity: 0.95 }}>{OG_DEFAULT.siteName}</span>
            <span style={{ fontSize: 18, opacity: 0.8 }}>{OG_DEFAULT.brandTagline}</span>
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
