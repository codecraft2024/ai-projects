import Image from "next/image";
import { cn } from "@/utils/cn";

type LogoVariant = "full" | "icon";

type LogoProps = {
  variant?: LogoVariant;
  className?: string;
  height?: number;
  priority?: boolean;
  /** White circular badge — for purple/dark backgrounds */
  withWhiteBadge?: boolean;
};

const ASPECT: Record<LogoVariant, number> = {
  full: 448 / 373,
  icon: 448 / 216,
};

const SRC: Record<LogoVariant, string> = {
  full: "/tabeeby-logo.png",
  icon: "/tabeeby-icon.png",
};

export function Logo({
  variant = "full",
  className,
  height = 44,
  priority = false,
  withWhiteBadge = false,
}: LogoProps) {
  const width = Math.round(height * ASPECT[variant]);
  const padding = Math.max(6, Math.round(height * 0.12));

  const image = (
    <Image
      src={SRC[variant]}
      alt="Tabeeby — Patient & Doctor Hub"
      width={width}
      height={height}
      className={cn("w-auto object-contain", className)}
      style={{ height, width: "auto", maxWidth: width }}
      priority={priority}
    />
  );

  if (!withWhiteBadge) {
    return image;
  }

  return (
    <span
      className="inline-flex shrink-0 items-center justify-center rounded-full bg-white shadow-md ring-2 ring-white/90"
      style={{ padding, lineHeight: 0 }}
    >
      {image}
    </span>
  );
}
