"use client";

import { useInfiniteQuery, useQuery } from "@tanstack/react-query";
import Link from "next/link";
import { useState } from "react";
import { fetchProfileCount, fetchProfilesPage } from "@/lib/api-client";
import { useLanguage } from "@/lib/i18n/LanguageProvider";
import { hasValidImageUrl, normalizeImageUrl, resolveProfileImage } from "@/lib/profile-image";
import { SafeImage } from "@/components/ui/SafeImage";
import { Button } from "@/components/ui/button";

const PAGE_SIZE = 48;

export default function DiscoverPage() {
  const { t } = useLanguage();
  const [showFunny, setShowFunny] = useState(false);

  const countQuery = useQuery({
    queryKey: ["profiles-count"],
    queryFn: fetchProfileCount,
  });

  const humansQuery = useInfiniteQuery({
    queryKey: ["profiles-page", false],
    queryFn: ({ pageParam }) => fetchProfilesPage(pageParam, PAGE_SIZE, false),
    initialPageParam: 0,
    getNextPageParam: (lastPage) =>
      lastPage.page + 1 < lastPage.totalPages ? lastPage.page + 1 : undefined,
    enabled: !showFunny,
  });

  const funnyQuery = useInfiniteQuery({
    queryKey: ["profiles-page", true],
    queryFn: ({ pageParam }) => fetchProfilesPage(pageParam, PAGE_SIZE, true),
    initialPageParam: 0,
    getNextPageParam: (lastPage) =>
      lastPage.page + 1 < lastPage.totalPages ? lastPage.page + 1 : undefined,
    enabled: showFunny,
  });

  const activeQuery = showFunny ? funnyQuery : humansQuery;
  const items = activeQuery.data?.pages.flatMap((page) => page.items) ?? [];
  const total = showFunny ? countQuery.data?.funny : countQuery.data?.humans;

  return (
    <div className="mx-auto max-w-7xl px-4 py-8 sm:px-6 sm:py-12">
      <div className="mb-8 sm:mb-10">
        <p className="pill mb-3">{t("discoverBadge")}</p>
        <h1 className="display-title text-2xl sm:text-4xl">{t("discoverTitle")}</h1>
        <p className="mt-3 text-sm text-muted-foreground sm:text-base">
          {t("discoverSubtitle")} {countQuery.data ? `(${countQuery.data.total.toLocaleString()} total)` : ""}
        </p>
      </div>

      <div className="mb-6 flex flex-col gap-2 sm:mb-8 sm:flex-row sm:flex-wrap sm:gap-3">
        <Button
          className="h-12 w-full touch-target sm:w-auto"
          variant={showFunny ? "outline" : "default"}
          onClick={() => setShowFunny(false)}
        >
          {t("humanMatchesTitle")} {countQuery.data ? `(${countQuery.data.humans.toLocaleString()})` : ""}
        </Button>
        <Button
          className="h-12 w-full touch-target sm:w-auto"
          variant={showFunny ? "default" : "outline"}
          onClick={() => setShowFunny(true)}
        >
          {t("funnyGalleryTitle")} {countQuery.data ? `(${countQuery.data.funny})` : ""}
        </Button>
      </div>

      {activeQuery.isLoading ? <p className="text-muted-foreground">{t("resultsLoading")}</p> : null}

      <div className="grid grid-cols-2 gap-3 sm:gap-4 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-6">
        {items.map(({ profile, primaryImageUrl }) => {
          const resolvedImage =
            resolveProfileImage(profile) ??
            (hasValidImageUrl(primaryImageUrl) ? { src: normalizeImageUrl(primaryImageUrl), alt: profile.fullName } : null);
          if (!resolvedImage) return null;
          return (
            <Link
              key={profile.id}
              href={`/celebrity/${profile.slug}`}
              className="shell-card group overflow-hidden transition hover:-translate-y-1 hover:border-primary/40"
            >
              <div className="relative aspect-[4/5] bg-muted">
                <SafeImage
                  src={resolvedImage.src}
                  alt={resolvedImage.alt}
                  fill
                  className="object-cover transition group-hover:scale-105"
                  sizes="(max-width: 768px) 50vw, 16vw"
                />
              </div>
              <div className="p-3">
                <p className="truncate font-bold group-hover:text-primary">{profile.fullName}</p>
                <p className="truncate text-xs text-muted-foreground">
                  {profile.occupation} · {profile.country}
                </p>
              </div>
            </Link>
          );
        })}
      </div>

      {activeQuery.hasNextPage ? (
        <div className="mt-8 text-center sm:mt-10">
          <Button
            className="h-12 w-full touch-target sm:w-auto"
            onClick={() => activeQuery.fetchNextPage()}
            disabled={activeQuery.isFetchingNextPage}
          >
            {activeQuery.isFetchingNextPage
              ? t("resultsLoading")
              : `${t("browseAllFaces")} (${items.length}${total ? ` / ${total.toLocaleString()}` : ""})`}
          </Button>
        </div>
      ) : null}
    </div>
  );
}
