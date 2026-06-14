"use client";

import { SafeImage } from "@/components/ui/SafeImage";
import type { Profile } from "@/types/profile";

export function ProfileGallery({ profile }: { profile: Profile }) {
  return (
    <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
      {profile.images.map((image) => (
        <div key={image.id} className="shell-card relative aspect-[4/5] overflow-hidden">
          <SafeImage
            src={image.url}
            alt={image.alt}
            fill
            className="object-cover"
            sizes="(max-width: 768px) 100vw, 33vw"
          />
        </div>
      ))}
    </div>
  );
}
