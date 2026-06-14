"use client";

import { motion } from "framer-motion";
import Link from "next/link";
import type { ProfileMatch } from "@/types/profile";
import { FUNNY_EMOJI } from "@/lib/api-client";
import { useLanguage } from "@/lib/i18n/LanguageProvider";
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
  const image = match.profile.images.find((item) => item.isPrimary) ?? match.profile.images[0];

  return (
    <motion.div initial={{ opacity: 0, y: 24 }} animate={{ opacity: 1, y: 0 }} transition={{ duration: 0.5 }}>
      <Card className="shell-card overflow-hidden border-secondary/40 bg-gradient-to-br from-secondary/30 via-card to-accent/20">
        <CardHeader>
          <Badge className="w-fit">{t("funnyMatchBadge")}</Badge>
          <CardTitle className="display-title text-3xl">
            {t("funnyMatchTitle", {
              score: formatPercent(match.overallScore),
              name: match.profile.fullName,
              emoji,
            })}
          </CardTitle>
          <CardDescription className="text-base font-medium">{match.reason || t("funnyMatchReason")}</CardDescription>
        </CardHeader>
        <CardContent className="grid gap-6 md:grid-cols-[220px_1fr]">
          <div className="flex items-center justify-center gap-4">
            {userImage ? (
              <div className="relative h-28 w-28 overflow-hidden rounded-full border-4 border-primary/40">
                <SafeImage src={userImage} alt="You" fill className="object-cover" unoptimized />
              </div>
            ) : null}
            {image ? (
              <div className="relative h-36 w-36 overflow-hidden rounded-2xl border-4 border-accent/40">
                <SafeImage src={image.url} alt={image.alt} fill className="object-cover" sizes="144px" priority />
              </div>
            ) : null}
          </div>
          <div className="flex flex-col justify-center gap-4">
            <p className="text-lg font-medium text-muted-foreground">{match.reason}</p>
            <Link href={`/celebrity/${match.profile.slug}`} className="text-sm font-bold text-primary hover:underline">
              {t("viewDetails")} →
            </Link>
          </div>
        </CardContent>
      </Card>
    </motion.div>
  );
}
