import type { CSSProperties } from "react";
import type { SocialLinkEntry } from "@/config/social-links";
import { getNavSocialPlatforms } from "@/config/social-links";
import { SocialPlatformIcon } from "@/components/icons/SocialIcons";
import { cn } from "@/utils/cn";

type SocialLinksVariant = "header" | "footer" | "contact";

type SocialLinksProps = {
  variant?: SocialLinksVariant;
  className?: string;
  /** Override default platform list */
  platforms?: SocialLinkEntry[];
};

const variantStyles: Record<
  SocialLinksVariant,
  { list: string; button: string; icon: string }
> = {
  header: {
    list: "flex items-center gap-1",
    button:
      "flex h-9 w-9 items-center justify-center rounded-lg text-slate-500 transition hover:bg-slate-100 hover:text-[var(--social-color)]",
    icon: "h-[18px] w-[18px]",
  },
  footer: {
    list: "flex flex-wrap items-center gap-2",
    button:
      "flex h-10 w-10 items-center justify-center rounded-xl bg-slate-800 text-slate-300 transition hover:bg-[var(--social-color)] hover:text-white",
    icon: "h-5 w-5",
  },
  contact: {
    list: "flex flex-wrap items-center gap-3",
    button:
      "flex h-11 w-11 items-center justify-center rounded-xl border border-slate-200 bg-white text-slate-600 shadow-sm transition hover:border-[var(--social-color)] hover:text-[var(--social-color)]",
    icon: "h-5 w-5",
  },
};

export function SocialLinks({
  variant = "header",
  className,
  platforms = getNavSocialPlatforms(),
}: SocialLinksProps) {
  if (platforms.length === 0) return null;

  const styles = variantStyles[variant];

  return (
    <nav aria-label="Social media" className={cn(styles.list, className)}>
      {platforms.map((platform) => (
        <a
          key={platform.id}
          href={platform.href}
          target="_blank"
          rel="noopener noreferrer"
          className={styles.button}
          style={{ "--social-color": platform.brandColor } as CSSProperties}
          title={`Follow us on ${platform.label}`}
          aria-label={`${platform.label} (opens in new tab)`}
        >
          <SocialPlatformIcon platform={platform.id} className={styles.icon} />
        </a>
      ))}
    </nav>
  );
}
