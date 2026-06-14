import type { FeatureBreakdown } from "@twinzy/types";

export const FEATURE_WEIGHTS: Record<keyof FeatureBreakdown, number> = {
  faceShape: 0.15,
  eyes: 0.18,
  eyebrows: 0.08,
  nose: 0.14,
  lips: 0.12,
  smile: 0.1,
  forehead: 0.08,
  jawline: 0.1,
  hairline: 0.05,
};

export function computeOverallScore(breakdown: FeatureBreakdown): number {
  const score = (Object.keys(FEATURE_WEIGHTS) as Array<keyof FeatureBreakdown>).reduce(
    (sum, key) => sum + breakdown[key] * FEATURE_WEIGHTS[key],
    0,
  );
  return Math.round(score * 10) / 10;
}

export function cosineSimilarity(a: number[], b: number[]): number {
  if (a.length !== b.length || a.length === 0) return 0;
  let dot = 0;
  let magA = 0;
  let magB = 0;
  for (let i = 0; i < a.length; i++) {
    dot += a[i]! * b[i]!;
    magA += a[i]! * a[i]!;
    magB += b[i]! * b[i]!;
  }
  const magnitude = Math.sqrt(magA) * Math.sqrt(magB);
  return magnitude === 0 ? 0 : dot / magnitude;
}

export function embeddingToPercent(similarity: number): number {
  return Math.round(Math.max(0, Math.min(1, (similarity + 1) / 2)) * 1000) / 10;
}
