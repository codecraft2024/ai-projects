export type ProfileCategory =
  | "actor_hollywood"
  | "actor_bollywood"
  | "actor_egyptian"
  | "actor_turkish"
  | "actor_korean"
  | "actress_worldwide"
  | "athlete_football"
  | "athlete_basketball"
  | "athlete_tennis"
  | "athlete_mma"
  | "athlete_olympics"
  | "singer"
  | "influencer"
  | "historical"
  | "funny_object";

export type Gender = "male" | "female" | "other" | "unknown";

export interface ProfileImage {
  id: string;
  url: string;
  alt: string;
  isPrimary: boolean;
  width: number;
  height: number;
}

export interface Profile {
  id: string;
  slug: string;
  fullName: string;
  gender: Gender;
  dateOfBirth: string;
  nationality: string;
  country: string;
  city: string;
  occupation: string;
  biography: string;
  heightCm: number | null;
  eyeColor: string;
  hairColor: string;
  ethnicityCategory: string;
  publicFigureCategory: ProfileCategory;
  instagramUrl?: string;
  xUrl?: string;
  websiteUrl?: string;
  images: ProfileImage[];
  tags: string[];
  popularityScore: number;
  isFunnyObject: boolean;
}

export interface FeatureBreakdown {
  faceShape: number;
  eyes: number;
  eyebrows: number;
  nose: number;
  lips: number;
  smile: number;
  forehead: number;
  jawline: number;
  hairline: number;
}

export interface ProfileMatch {
  profile: Profile;
  overallScore: number;
  breakdown: FeatureBreakdown;
  reason: string;
}

export interface SearchResponse {
  funnyMatch: ProfileMatch;
  humanMatches: ProfileMatch[];
  userImageDataUrl?: string;
}

export function computeAge(dateOfBirth: string, referenceDate = new Date()): number {
  const birth = new Date(dateOfBirth);
  let age = referenceDate.getFullYear() - birth.getFullYear();
  const monthDiff = referenceDate.getMonth() - birth.getMonth();
  if (monthDiff < 0 || (monthDiff === 0 && referenceDate.getDate() < birth.getDate())) {
    age -= 1;
  }
  return age;
}
