"use client";

import { useQuery } from "@tanstack/react-query";
import Link from "next/link";
import { useSearchParams } from "next/navigation";
import { useEffect, useState } from "react";
import type { SearchResponse } from "@/types/profile";
import { FaceComparison } from "@/components/search/FaceComparison";
import { FunnyMatchCard } from "@/components/search/FunnyMatchCard";
import { HumanMatchGrid } from "@/components/search/HumanMatchGrid";
import { SEARCH_SESSION_KEY } from "@/components/search/ImageUploadForm";
import { ShareButtons } from "@/components/share/ShareButtons";
import { Button } from "@/components/ui/button";
import { getApiBaseUrl } from "@/lib/api-client";
import { useLanguage } from "@/lib/i18n/LanguageProvider";

async function fetchSession(sessionId: string): Promise<SearchResponse & { sessionId: string }> {
  const response = await fetch(`${getApiBaseUrl()}/api/search?sessionId=${sessionId}`);
  if (!response.ok) {
    throw new Error("Search session not found");
  }
  return response.json();
}

export default function SearchResultsClient() {
  const { t } = useLanguage();
  const searchParams = useSearchParams();
  const sessionId = searchParams.get("sessionId");
  const [localData, setLocalData] = useState<(SearchResponse & { sessionId?: string }) | null>(null);

  useEffect(() => {
    const raw = sessionStorage.getItem(SEARCH_SESSION_KEY);
    if (raw) {
      try {
        setLocalData(JSON.parse(raw));
      } catch {
        setLocalData(null);
      }
    }
  }, []);

  const query = useQuery({
    queryKey: ["search-session", sessionId],
    queryFn: () => fetchSession(sessionId!),
    enabled: Boolean(sessionId) && !localData,
  });

  const data = localData ?? query.data;
  const topMatch = data?.humanMatches[0];
  const userImage = data?.userImageDataUrl;

  if (!sessionId && !localData) {
    return (
      <section className="mx-auto max-w-3xl px-4 py-20 text-center sm:px-6">
        <h1 className="funny-title text-3xl">{t("resultsEmpty")}</h1>
        <p className="mt-4 font-medium text-muted-foreground">{t("resultsEmptyHint")}</p>
        <Button className="mt-8" onClick={() => (window.location.href = "/")}>
          {t("resultsStart")}
        </Button>
      </section>
    );
  }

  if (query.isLoading && !data) {
    return (
      <section className="mx-auto max-w-7xl px-4 py-20 text-center sm:px-6">
        <p className="font-medium text-muted-foreground">{t("resultsLoading")}</p>
      </section>
    );
  }

  if (!data || !topMatch) {
    return (
      <section className="mx-auto max-w-3xl px-4 py-20 text-center sm:px-6">
        <h1 className="funny-title text-3xl">{t("resultsExpired")}</h1>
        <p className="mt-4 font-medium text-muted-foreground">{t("resultsExpiredHint")}</p>
        <Link href="/" className="mt-8 inline-flex h-10 items-center rounded-full bg-primary px-4 font-bold text-primary-foreground">
          {t("resultsUpload")}
        </Link>
      </section>
    );
  }

  return (
    <section className="mx-auto max-w-7xl space-y-12 px-4 py-12 sm:px-6">
      <div>
        <h1 className="funny-title text-3xl">{t("resultsTitle")}</h1>
        <p className="mt-2 font-medium text-muted-foreground">{t("resultsSubtitle")}</p>
      </div>

      <FaceComparison userImage={userImage} topMatch={topMatch} />

      <ShareButtons
        sessionId={data.sessionId ?? sessionId ?? undefined}
        topMatchName={topMatch.profile.fullName}
        topMatchScore={topMatch.overallScore}
      />

      <FunnyMatchCard match={data.funnyMatch} userImage={userImage} />

      <div className="space-y-6">
        <h2 className="funny-title text-2xl">{t("humanMatchesTitle")}</h2>
        <HumanMatchGrid
          matches={data.humanMatches}
          sessionId={data.sessionId ?? sessionId ?? undefined}
          userImage={userImage}
        />
      </div>
    </section>
  );
}
