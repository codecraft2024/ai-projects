"use client";

import { Facebook, MessageCircle } from "lucide-react";
import { useLanguage } from "@/lib/i18n/LanguageProvider";
import { getSiteUrl } from "@/lib/api-client";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";

interface ShareButtonsProps {
  sessionId?: string;
  topMatchName: string;
  topMatchScore: number;
}

export function ShareButtons({ sessionId, topMatchName, topMatchScore }: ShareButtonsProps) {
  const { t } = useLanguage();
  const shareUrl = sessionId
    ? `${getSiteUrl()}/results?sessionId=${sessionId}`
    : getSiteUrl();
  const shareText = t("shareText", {
    score: topMatchScore.toFixed(1),
    name: topMatchName,
  });
  const encodedUrl = encodeURIComponent(shareUrl);
  const encodedText = encodeURIComponent(`${shareText} ${shareUrl}`);

  return (
    <Card className="border-4 border-dashed border-primary/40 bg-secondary/40">
      <CardHeader>
        <CardTitle className="display-title">{t("shareTitle")}</CardTitle>
      </CardHeader>
      <CardContent className="flex flex-wrap gap-3">
        <a
          href={`https://www.facebook.com/sharer/sharer.php?u=${encodedUrl}&quote=${encodedText}`}
          target="_blank"
          rel="noopener noreferrer"
          className="inline-flex h-10 items-center gap-2 rounded-full bg-[#1877F2] px-4 text-sm font-bold text-white hover:opacity-90"
        >
          <Facebook className="h-4 w-4" />
          {t("shareFacebook")}
        </a>
        <a
          href={`https://wa.me/?text=${encodedText}`}
          target="_blank"
          rel="noopener noreferrer"
          className="inline-flex h-10 items-center gap-2 rounded-full bg-[#25D366] px-4 text-sm font-bold text-white hover:opacity-90"
        >
          <MessageCircle className="h-4 w-4" />
          {t("shareWhatsapp")}
        </a>
      </CardContent>
    </Card>
  );
}
