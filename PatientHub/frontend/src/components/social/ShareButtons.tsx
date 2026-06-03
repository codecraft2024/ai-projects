"use client";

import { useState, useCallback } from "react";
import { useTranslations } from "next-intl";
import { getShareablePlatforms, getSocialPlatforms } from "@/config/social-links";
import { SocialPlatformIcon, ShareIcon } from "@/components/icons/SocialIcons";
import { buildShareUrls, getCopyShareText } from "@/utils/social";
import type { ShareContent } from "@/types/social";
import { cn } from "@/utils/cn";

type ShareButtonsProps = {
  content: ShareContent;
  className?: string;
  compact?: boolean;
};

export function ShareButtons({ content, className, compact = false }: ShareButtonsProps) {
  const t = useTranslations("share");
  const [copied, setCopied] = useState(false);
  const shareable = getShareablePlatforms();
  const hasInstagram = getSocialPlatforms().some((p) => p.id === "instagram");

  const handleShare = useCallback((href: string) => {
    window.open(href, "_blank", "noopener,noreferrer,width=600,height=500");
  }, []);

  const handleInstagramCopy = useCallback(async () => {
    try {
      await navigator.clipboard.writeText(getCopyShareText(content));
      setCopied(true);
      setTimeout(() => setCopied(false), 2500);
    } catch {
      /* clipboard unavailable */
    }
  }, [content]);

  const urls = buildShareUrls(content);

  return (
    <div
      className={cn(
        "card-premium rounded-2xl p-4 sm:p-5",
        className,
      )}
    >
      <div className="flex flex-wrap items-center gap-3">
        <span
          className={cn(
            "flex items-center gap-2 font-semibold text-slate-800",
            compact ? "text-sm" : "text-base",
          )}
        >
          <ShareIcon className="h-5 w-5 text-brand" />
          {t("label")}
        </span>
        <div className="flex flex-wrap gap-2">
          {shareable.map((platform) => {
            const shareUrl =
              platform.id === "facebook"
                ? urls.facebook
                : platform.id === "twitter"
                  ? urls.twitter
                  : platform.id === "whatsapp"
                    ? urls.whatsapp
                    : null;

            if (!shareUrl) return null;

            return (
              <button
                key={platform.id}
                type="button"
                onClick={() => handleShare(shareUrl)}
                className="inline-flex items-center gap-2 rounded-xl px-3 py-2 text-sm font-medium text-white transition hover:opacity-90"
                style={{ backgroundColor: platform.brandColor }}
                aria-label={platform.label}
              >
                <SocialPlatformIcon platform={platform.id} className="h-4 w-4" />
                {!compact && <span>{platform.label}</span>}
              </button>
            );
          })}
          {hasInstagram && (
            <button
              type="button"
              onClick={handleInstagramCopy}
              className="inline-flex items-center gap-2 rounded-xl bg-gradient-to-r from-[#833AB4] via-[#E4405F] to-[#FCAF45] px-3 py-2 text-sm font-medium text-white transition hover:opacity-90"
            >
              <SocialPlatformIcon platform="instagram" className="h-4 w-4" />
              {!compact && <span>{copied ? t("copied") : "Instagram"}</span>}
            </button>
          )}
        </div>
      </div>
      {copied && compact && (
        <p className="mt-2 text-xs text-green-700">{t("instagramHint")}</p>
      )}
    </div>
  );
}
