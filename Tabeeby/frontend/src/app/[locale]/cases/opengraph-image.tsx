import { renderOgImage } from "@/lib/og-image";

export const alt = "Patient Cases — Dr. Mina Merzek Clinic";
export const size = { width: 1200, height: 630 };
export const contentType = "image/png";

export default function OgImage() {
  return renderOgImage({
    title: "Patient Cases & Highlights",
    subtitle: "Orthopedic treatment outcomes and success stories",
    badge: "Case Library",
  });
}
