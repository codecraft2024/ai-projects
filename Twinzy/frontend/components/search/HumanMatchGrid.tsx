"use client";

import { motion } from "framer-motion";
import { SafeImage } from "@/components/ui/SafeImage";
import Link from "next/link";
import type { ProfileMatch } from "@/types/profile";
import { computeAge } from "@/types/profile";
import { useLanguage } from "@/lib/i18n/LanguageProvider";
import { resolveProfileImage } from "@/lib/profile-image";
import { Badge } from "@/components/ui/badge";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { formatPercent, truncate } from "@/lib/utils";

export function HumanMatchGrid({
  matches,
  sessionId,
  userImage,
}: {
  matches: ProfileMatch[];
  sessionId?: string;
  userImage?: string;
}) {
  const { t } = useLanguage();

  return (
    <div className="grid gap-6 sm:grid-cols-2 xl:grid-cols-3">
      {matches.map((match, index) => {
        const image = resolveProfileImage(match.profile);
        const href = sessionId
          ? `/celebrity/${match.profile.slug}?sessionId=${sessionId}`
          : `/celebrity/${match.profile.slug}`;

        if (!image) return null;

        return (
          <motion.div
            key={match.profile.id}
            initial={{ opacity: 0, y: 16 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: index * 0.03, duration: 0.35 }}
          >
            <Card className="shell-card h-full overflow-hidden">
              <div className="flex items-center justify-center gap-2 bg-muted/70 p-3">
                {userImage ? (
                  <div className="relative h-12 w-12 overflow-hidden rounded-full border-2 border-foreground">
                    <SafeImage src={userImage} alt={t("you")} fill className="object-cover" unoptimized />
                  </div>
                ) : null}
                <span className="text-sm font-black text-primary">VS</span>
                <div className="relative h-16 w-16 overflow-hidden rounded-full border-2 border-foreground">
                  <SafeImage src={image.src} alt={image.alt} fill className="object-cover" sizes="64px" />
                </div>
              </div>
              <CardHeader className="space-y-3">
                <div className="flex items-start justify-between gap-3">
                  <div>
                    <CardTitle className="display-title text-lg leading-snug sm:text-xl">{match.profile.fullName}</CardTitle>
                    <CardDescription className="font-medium">
                      {match.profile.occupation} · {match.profile.country}
                    </CardDescription>
                  </div>
                  <Badge variant="secondary">{formatPercent(match.overallScore)}</Badge>
                </div>
              </CardHeader>
              <CardContent className="space-y-4">
                <p className="text-sm font-medium text-muted-foreground">
                  {computeAge(match.profile.dateOfBirth)} · {truncate(match.profile.biography, 120)}
                </p>
                <Link
                  href={href}
                  className="inline-flex h-10 w-full items-center justify-center rounded-full border-2 border-foreground bg-accent px-4 text-sm font-bold transition hover:scale-[1.02]"
                >
                  {t("viewDetails")}
                </Link>
              </CardContent>
            </Card>
          </motion.div>
        );
      })}
    </div>
  );
}
