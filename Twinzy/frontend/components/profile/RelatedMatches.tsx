import Link from "next/link";
import type { Profile } from "@/types/profile";
import { SafeImage } from "@/components/ui/SafeImage";
import { resolveProfileImage } from "@/lib/profile-image";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { formatPercent } from "@/lib/utils";

export function RelatedMatches({
  related,
  sessionId,
}: {
  related: Array<{ profile: Profile; score: number }>;
  sessionId?: string;
}) {
  if (!related.length) return null;

  return (
    <Card className="shell-card">
      <CardHeader>
        <CardTitle>Related Matches</CardTitle>
      </CardHeader>
      <CardContent className="grid grid-cols-2 gap-3 sm:gap-4 lg:grid-cols-3">
        {related.map(({ profile, score }) => {
          const image = resolveProfileImage(profile);
          const href = sessionId
            ? `/celebrity/${profile.slug}?sessionId=${sessionId}`
            : `/celebrity/${profile.slug}`;

          if (!image) return null;

          return (
            <Link
              key={profile.id}
              href={href}
              className="group overflow-hidden rounded-xl border border-border transition hover:border-primary/40"
            >
              <div className="relative h-40">
                <SafeImage src={image.src} alt={image.alt} fill className="object-cover" sizes="240px" />
              </div>
              <div className="space-y-1 p-4">
                <p className="font-medium group-hover:text-primary">{profile.fullName}</p>
                <p className="text-sm text-muted-foreground">{formatPercent(score * 50 + 50)} match</p>
              </div>
            </Link>
          );
        })}
      </CardContent>
    </Card>
  );
}
