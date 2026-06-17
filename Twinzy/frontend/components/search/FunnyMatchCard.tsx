"use client";

import { motion } from "framer-motion";
import Link from "next/link";
import type { ProfileMatch } from "@/types/profile";
import { FUNNY_EMOJI } from "@/lib/api-client";
import { useLanguage } from "@/lib/i18n/LanguageProvider";
import { resolveProfileImage } from "@/lib/profile-image";
import { SafeImage } from "@/components/ui/SafeImage";
import { Badge } from "@/components/ui/badge";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { formatPercent } from "@/lib/utils";

export function FunnyMatchCard({
  match,
  userImage,
}: {
  match: ProfileMatch;
  userImage?: string;
}) {
  const { t } = useLanguage();
  const emoji = FUNNY_EMOJI[match.profile.fullName] ?? "✨";
  const image = resolveProfileImage(match.profile);
  if (!image) return null;

  return (
    <motion.div initial={{ opacity: 0, y: 24 }} animate={{ opacity: 1, y: 0 }} transition={{ duration: 0.5 }}>
      <Card className="shell-card overflow-hidden border-secondary/40 bg-gradient-to-br from-secondary/30 via-card to-accent/20">
        <CardHeader className="px-4 sm:px-6">
          <Badge className="w-fit">{t("funnyMatchBadge")}</Badge>
          <CardTitle className="display-title text-xl leading-snug sm:text-2xl md:text-3xl">
            {t("funnyMatchTitle", {
              score: formatPercent(match.overallScore),
              name: match.profile.fullName,
              emoji,
            })}
          </CardTitle>
          <CardDescription className="text-sm font-medium sm:text-base">{match.reason || t("funnyMatchReason")}</CardDescription>
        </CardHeader>
        <CardContent className="flex flex-col gap-5 px-4 pb-6 sm:px-6 md:grid md:grid-cols-[220px_1fr] md:gap-6">
          <div className="flex items-center justify-center gap-3 sm:gap-4">
            {userImage ? (
              <div className="relative h-24 w-24 shrink-0 overflow-hidden rounded-full border-4 border-primary/40 sm:h-28 sm:w-28">
                <SafeImage src={userImage} alt="You" fill className="object-cover" unoptimized />
              </div>
            ) : null}
            <div className="relative h-28 w-28 shrink-0 overflow-hidden rounded-2xl border-4 border-accent/40 sm:h-36 sm:w-36">
              <SafeImage src={image.src} alt={image.alt} fill className="object-cover" sizes="(max-width: 640px) 112px, 144px" priority />
            </div>
          </div>
          <div className="flex flex-col justify-center gap-3 sm:gap-4">
            <p className="text-base font-medium text-muted-foreground sm:text-lg">{match.reason}</p>
            <Link href={`/celebrity/${match.profile.slug}`} className="touch-target inline-flex items-center text-sm font-bold text-primary hover:underline">
              {t("viewDetails")} →
            </Link>
          </div>
        </CardContent>
      </Card>
    </motion.div>
  );
}
