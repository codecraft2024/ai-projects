"use client";

import { motion } from "framer-motion";
import { SafeImage } from "@/components/ui/SafeImage";
import type { ProfileMatch } from "@/types/profile";
import { FUNNY_EMOJI } from "@/lib/api-client";
import { useLanguage } from "@/lib/i18n/LanguageProvider";
import { formatPercent } from "@/lib/utils";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";

interface FaceComparisonProps {
  userImage?: string;
  topMatch: ProfileMatch;
}

export function FaceComparison({ userImage, topMatch }: FaceComparisonProps) {
  const { t } = useLanguage();
  const twinImage =
    topMatch.profile.images.find((image) => image.isPrimary) ?? topMatch.profile.images[0];
  const emoji = FUNNY_EMOJI[topMatch.profile.fullName] ?? "⭐";

  if (!userImage) return null;

  return (
    <motion.div initial={{ scale: 0.95, opacity: 0 }} animate={{ scale: 1, opacity: 1 }}>
      <Card className="shell-card overflow-hidden border-primary/40 bg-gradient-to-br from-secondary/20 via-card to-accent/10">
        <CardHeader className="text-center">
          <CardTitle className="display-title text-3xl">
            {t("looksLike", {
              name: `${topMatch.profile.fullName} ${emoji}`,
              score: formatPercent(topMatch.overallScore),
            })}
          </CardTitle>
        </CardHeader>
        <CardContent className="grid items-center gap-6 md:grid-cols-[1fr_auto_1fr]">
          <div className="text-center">
            <p className="mb-3 text-lg font-bold">{t("you")} 🤳</p>
            <div className="relative mx-auto h-56 w-56 overflow-hidden rounded-full border-4 border-primary/50 shadow-lg shadow-primary/20">
              <SafeImage src={userImage} alt={t("you")} fill className="object-cover" unoptimized />
            </div>
          </div>
          <div className="text-center text-5xl font-black text-primary">VS</div>
          <div className="text-center">
            <p className="mb-3 text-lg font-bold">{t("yourTwin")} 🌟</p>
            <div className="relative mx-auto h-56 w-56 overflow-hidden rounded-full border-4 border-accent/50 shadow-lg shadow-accent/20">
              {twinImage ? (
                <SafeImage src={twinImage.url} alt={topMatch.profile.fullName} fill className="object-cover" sizes="224px" />
              ) : null}
            </div>
            <p className="mt-3 font-semibold">{topMatch.profile.fullName}</p>
          </div>
        </CardContent>
      </Card>
    </motion.div>
  );
}
