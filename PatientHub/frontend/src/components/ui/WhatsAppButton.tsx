import { WHATSAPP_URL } from "@/utils/whatsapp";
import { Button } from "@/components/ui/Button";
import { WhatsAppIcon } from "@/components/icons/WhatsAppIcon";
import { cn } from "@/utils/cn";

type WhatsAppButtonProps = {
  label?: string;
  showIcon?: boolean;
  size?: "sm" | "md" | "lg";
  className?: string;
  fullWidth?: boolean;
};

export function WhatsAppButton({
  label = "WhatsApp",
  showIcon = true,
  size = "md",
  className,
  fullWidth,
}: WhatsAppButtonProps) {
  return (
    <Button
      href={WHATSAPP_URL}
      variant="whatsapp"
      size={size}
      className={cn(fullWidth && "w-full", className)}
      aria-label={`Contact ${label} on WhatsApp`}
    >
      {showIcon && <WhatsAppIcon className="h-5 w-5 shrink-0" />}
      {label}
    </Button>
  );
}
