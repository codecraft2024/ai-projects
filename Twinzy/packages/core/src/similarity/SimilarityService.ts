import type { FeatureBreakdown, Profile, ProfileMatch, SearchResponse } from "@twinzy/types";
import type { IFaceAnalysisProvider } from "../providers/IEmbeddingProvider";
import {
  computeOverallScore,
  cosineSimilarity,
  embeddingToPercent,
} from "./FeatureWeights";

export interface ProfileRepository {
  findTopK(
    embedding: number[],
    options: { limit: number; funnyOnly: boolean },
  ): Promise<Array<{ profile: Profile; storedFeatures: FeatureBreakdown }>>;
}

export class SimilarityService {
  constructor(
    private readonly provider: IFaceAnalysisProvider,
    private readonly profileRepo: ProfileRepository,
  ) {}

  async search(image: Buffer): Promise<SearchResponse> {
    const { embedding, landmarks } = await this.provider.detectAndEmbed(image);
    const userFeatures = await this.provider.analyzeFeatures(image, landmarks!);

    const [funnyResult] = await this.profileRepo.findTopK(embedding, {
      limit: 1,
      funnyOnly: true,
    });

    const humanResults = await this.profileRepo.findTopK(embedding, {
      limit: 50,
      funnyOnly: false,
    });

    if (!funnyResult) {
      throw new Error("No funny object profiles found in dataset.");
    }

    return {
      funnyMatch: this.buildMatch(funnyResult.profile, funnyResult.storedFeatures, userFeatures, embedding),
      humanMatches: humanResults
        .map((result) => this.buildMatch(result.profile, result.storedFeatures, userFeatures, embedding))
        .sort((a, b) => b.overallScore - a.overallScore),
    };
  }

  private buildMatch(
    profile: Profile,
    storedFeatures: FeatureBreakdown,
    userFeatures: FeatureBreakdown,
    userEmbedding: number[],
  ): ProfileMatch {
    const embeddingScore = embeddingToPercent(
      cosineSimilarity(userEmbedding, profile.faceEmbedding),
    );
    const breakdown = this.blendFeatures(userFeatures, storedFeatures);
    const overallScore = computeOverallScore(breakdown);
    const blendedScore = Math.round((overallScore * 0.6 + embeddingScore * 0.4) * 10) / 10;

    return {
      profile,
      overallScore: blendedScore,
      breakdown,
      reason: this.buildReason(profile, blendedScore),
    };
  }

  private blendFeatures(user: FeatureBreakdown, stored: FeatureBreakdown): FeatureBreakdown {
    const keys = Object.keys(user) as Array<keyof FeatureBreakdown>;
    return keys.reduce((acc, key) => {
      acc[key] = Math.round((user[key] * 0.4 + stored[key] * 0.6) * 10) / 10;
      return acc;
    }, {} as FeatureBreakdown);
  }

  private buildReason(profile: Profile, score: number): string {
    if (profile.isFunnyObject) {
      return `Your face shape and proportions resemble this ${profile.fullName.toLowerCase()}.`;
    }
    return `You are ${score}% similar to ${profile.fullName} based on facial structure and feature alignment.`;
  }
}
