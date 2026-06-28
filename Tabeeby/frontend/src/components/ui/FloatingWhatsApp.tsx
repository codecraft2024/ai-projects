import { WHATSAPP_URL } from "@/utils/whatsapp";
import { CONTACT } from "@/data/clinic";
import { WhatsAppIcon } from "@/components/icons/WhatsAppIcon";

/** Fixed FAB for quick WhatsApp access on all viewport sizes. */
export function FloatingWhatsApp() {
  return (
    <a
      href={WHATSAPP_URL}
      target="_blank"
      rel="noopener noreferrer"
      className="fixed bottom-5 right-4 z-50 flex h-14 w-14 items-center justify-center rounded-full bg-[#25D366] text-white shadow-xl shadow-green-900/30 transition hover:scale-105 hover:bg-[#20BD5A] focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-green-400 focus-visible:ring-offset-2 sm:bottom-6 sm:right-6 sm:h-16 sm:w-16"
      aria-label={`Chat with ${CONTACT.whatsappContactName} on WhatsApp`}
    >
      <WhatsAppIcon className="h-7 w-7 sm:h-8 sm:w-8" />
    </a>
  );
}
