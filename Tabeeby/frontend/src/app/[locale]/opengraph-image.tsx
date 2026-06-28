import { OG_DEFAULT } from "@/config/social-links";
import { renderOgImage } from "@/lib/og-image";

export const alt = OG_DEFAULT.title;
export const size = { width: 1200, height: 630 };
export const contentType = "image/png";

export default function OgImage() {
  return renderOgImage({
    title: OG_DEFAULT.clinicName,
    subtitle: "Orthopedic & Bone Clinic · Heliopolis, Cairo",
    badge: "Premium Healthcare",
  });
}
