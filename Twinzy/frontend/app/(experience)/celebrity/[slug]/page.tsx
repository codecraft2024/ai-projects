import { computeAge } from "@/types/profile";
import Link from "next/link";
import { notFound } from "next/navigation";
import { FeatureBreakdownPanel } from "@/components/profile/FeatureBreakdownPanel";
import { ProfileGallery } from "@/components/profile/ProfileGallery";
import { RelatedMatches } from "@/components/profile/RelatedMatches";
import { SimilarityTimeline } from "@/components/profile/SimilarityTimeline";
import { JsonLd } from "@/components/seo/JsonLd";
import { Badge } from "@/components/ui/badge";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { SafeImage } from "@/components/ui/SafeImage";
import { fetchProfile, fetchProfileSlugs, getSiteUrl } from "@/lib/api-client";
import { resolveProfileImage } from "@/lib/profile-image";
import { buildProfileMetadata } from "@/lib/seo/metadata";

interface CelebrityPageProps {
  params: Promise<{ slug: string }>;
  searchParams: Promise<{ sessionId?: string }>;
}

export async function generateStaticParams() {
  try {
    const slugs = await fetchProfileSlugs();
    return slugs.map((slug) => ({ slug }));
  } catch {
    return [];
  }
}

export async function generateMetadata({ params }: CelebrityPageProps) {
  const { slug } = await params;
  try {
    const data = await fetchProfile(slug);
    return buildProfileMetadata(data.profile);
  } catch {
    return {};
  }
}

export const revalidate = 86_400;

export default async function CelebrityPage({ params, searchParams }: CelebrityPageProps) {
  const { slug } = await params;
  const { sessionId } = await searchParams;

  let data;
  try {
    data = await fetchProfile(slug, sessionId);
  } catch {
    notFound();
  }

  const { profile, related, match } = data;
  const primaryImage = resolveProfileImage(profile);
  const siteUrl = getSiteUrl();

  if (!primaryImage) {
    notFound();
  }

  return (
    <>
      <JsonLd
        data={{
          "@context": "https://schema.org",
          "@type": "Person",
          name: profile.fullName,
          jobTitle: profile.occupation,
          nationality: profile.nationality,
          birthDate: profile.dateOfBirth,
          image: primaryImage.src,
          url: `${siteUrl}/celebrity/${profile.slug}`,
          sameAs: [profile.instagramUrl, profile.xUrl, profile.websiteUrl].filter(Boolean),
        }}
      />

      <section className="mx-auto max-w-7xl space-y-8 px-4 py-8 sm:space-y-10 sm:px-6 sm:py-12">
        <div className="grid gap-6 sm:gap-8 lg:grid-cols-[320px_1fr]">
          <div className="shell-card relative mx-auto aspect-[4/5] w-full max-w-sm overflow-hidden lg:mx-0 lg:max-w-none">
            <SafeImage src={primaryImage.src} alt={primaryImage.alt} fill className="object-cover" priority sizes="(max-width: 1024px) 100vw, 320px" />
          </div>

          <div className="space-y-5 sm:space-y-6">
            <div className="space-y-2 sm:space-y-3">
              <Badge variant="outline">{profile.publicFigureCategory.replace(/_/g, " ")}</Badge>
              <h1 className="display-title text-2xl leading-tight sm:text-3xl lg:text-4xl">{profile.fullName}</h1>
              <p className="text-base text-muted-foreground sm:text-lg">
                {profile.occupation} · {profile.country} · Age {computeAge(profile.dateOfBirth)}
              </p>
            </div>

            <Card className="shell-card">
              <CardHeader>
                <CardTitle>Biography</CardTitle>
              </CardHeader>
              <CardContent className="space-y-4 text-muted-foreground">
                <p>{profile.biography}</p>
                <div className="flex flex-wrap gap-2">
                  {profile.tags.map((tag) => (
                    <Badge key={tag} variant="secondary">
                      {tag}
                    </Badge>
                  ))}
                </div>
              </CardContent>
            </Card>

            <Card className="shell-card">
              <CardHeader>
                <CardTitle>Details</CardTitle>
              </CardHeader>
              <CardContent className="grid gap-3 text-sm sm:grid-cols-2">
                <p><span className="text-muted-foreground">Born:</span> {profile.dateOfBirth}</p>
                <p><span className="text-muted-foreground">City:</span> {profile.city}</p>
                <p><span className="text-muted-foreground">Eyes:</span> {profile.eyeColor}</p>
                <p><span className="text-muted-foreground">Hair:</span> {profile.hairColor}</p>
                {profile.heightCm ? (
                  <p><span className="text-muted-foreground">Height:</span> {profile.heightCm} cm</p>
                ) : null}
              </CardContent>
            </Card>

            <div className="flex flex-col gap-2 sm:flex-row sm:flex-wrap sm:gap-3">
              {profile.instagramUrl ? (
                <Link href={profile.instagramUrl} className="inline-flex h-12 touch-target items-center justify-center rounded-full border border-border px-4 text-sm hover:bg-muted sm:h-auto">
                  Instagram
                </Link>
              ) : null}
              {profile.xUrl ? (
                <Link href={profile.xUrl} className="inline-flex h-12 touch-target items-center justify-center rounded-full border border-border px-4 text-sm hover:bg-muted sm:h-auto">
                  X / Twitter
                </Link>
              ) : null}
            </div>
          </div>
        </div>

        {match ? (
          <div className="grid gap-6 lg:grid-cols-2">
            <FeatureBreakdownPanel breakdown={match.breakdown} overallScore={match.overallScore} />
            <SimilarityTimeline overallScore={match.overallScore} />
          </div>
        ) : null}

        <div className="space-y-4">
          <h2 className="text-2xl font-bold">Gallery</h2>
          <ProfileGallery profile={profile} />
        </div>

        <RelatedMatches related={related} sessionId={sessionId} />
      </section>
    </>
  );
}
