import { renderOgImage } from "@/lib/og-image";

export const alt = "Dr. Mina Merzek — Consultant Orthopedic Surgeon";
export const size = { width: 1200, height: 630 };
export const contentType = "image/png";

export default function OgImage() {
  return renderOgImage({
    title: "Dr. Mina Merzek",
    subtitle: "Founder · Lead Orthopedic Surgeon · Cairo",
    badge: "Doctor Profile",
  });
}
