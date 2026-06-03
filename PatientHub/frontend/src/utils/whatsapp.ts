/**
 * WhatsApp helpers — URLs are defined in @/config/social-links.ts
 */
export {
  WHATSAPP_URL,
  WHATSAPP_DEFAULT_MESSAGE as DEFAULT_WHATSAPP_MESSAGE,
  WHATSAPP_PHONE_E164,
} from "@/config/social-links";

import { WHATSAPP_PHONE_E164 } from "@/config/social-links";

/** Builds an official WhatsApp deep link (wa.me). */
export function buildWhatsAppUrl(message?: string): string {
  const base = `https://wa.me/${WHATSAPP_PHONE_E164}`;
  if (!message) return base;
  return `${base}?text=${encodeURIComponent(message)}`;
}
