"use client";

import { motion } from "framer-motion";
import { SafeImage } from "@/components/ui/SafeImage";
import type { ProfileMatch } from "@/types/profile";
import { FUNNY_EMOJI } from "@/lib/api-client";
import { useLanguage } from "@/lib/i18n/LanguageProvider";
import { resolveProfileImage } from "@/lib/profile-image";
import { formatPercent } from "@/lib/utils";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";

interface FaceComparisonProps {
  userImage?: string;
  topMatch: ProfileMatch;
}

export function FaceComparison({ userImage, topMatch }: FaceComparisonProps) {
  const { t } = useLanguage();
  const twinImage = resolveProfileImage(topMatch.profile);
  const emoji = FUNNY_EMOJI[topMatch.profile.fullName] ?? "⭐";

  if (!userImage || !twinImage) return null;

  return (
    <motion.div initial={{ scale: 0.95, opacity: 0 }} animate={{ scale: 1, opacity: 1 }}>
      <Card className="shell-card overflow-hidden border-primary/40 bg-gradient-to-br from-secondary/20 via-card to-accent/10">
        <CardHeader className="px-4 pb-2 pt-4 text-center sm:px-6">
          <CardTitle className="display-title text-xl leading-snug sm:text-2xl md:text-3xl">
            {t("looksLike", {
              name: `${topMatch.profile.fullName} ${emoji}`,
              score: formatPercent(topMatch.overallScore),
            })}
          </CardTitle>
        </CardHeader>
        <CardContent className="flex flex-col items-center gap-5 px-4 pb-6 sm:px-6 md:grid md:grid-cols-[1fr_auto_1fr] md:items-center md:gap-6">
          <div className="text-center">
            <p className="mb-2 text-base font-bold sm:mb-3 sm:text-lg">{t("you")} 🤳</p>
            <div className="relative mx-auto h-36 w-36 overflow-hidden rounded-full border-4 border-primary/50 shadow-lg shadow-primary/20 sm:h-48 sm:w-48 md:h-56 md:w-56">
              <SafeImage src={userImage} alt={t("you")} fill className="object-cover" unoptimized />
            </div>
          </div>
          <div className="text-center text-3xl font-black text-primary sm:text-4xl md:text-5xl">VS</div>
          <div className="text-center">
            <p className="mb-2 text-base font-bold sm:mb-3 sm:text-lg">{t("yourTwin")} 🌟</p>
            <div className="relative mx-auto h-36 w-36 overflow-hidden rounded-full border-4 border-accent/50 shadow-lg shadow-accent/20 sm:h-48 sm:w-48 md:h-56 md:w-56">
              <SafeImage src={twinImage.src} alt={twinImage.alt} fill className="object-cover" sizes="(max-width: 768px) 144px, 224px" />
            </div>
            <p className="mt-2 max-w-[16rem] truncate font-semibold sm:mt-3 sm:max-w-none">{topMatch.profile.fullName}</p>
          </div>
        </CardContent>
      </Card>
    </motion.div>
  );
}
