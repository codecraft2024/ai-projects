import type { IFaceAnalysisProvider, ProviderType } from "./IEmbeddingProvider";
import { MockEmbeddingProvider } from "./MockEmbeddingProvider";

class UnimplementedProvider implements IFaceAnalysisProvider {
  constructor(readonly name: ProviderType) {}

  async detectAndEmbed(): Promise<never> {
    throw new Error(`Provider "${this.name}" is not yet implemented. Set FACE_PROVIDER=mock for development.`);
  }

  async analyzeFeatures(): Promise<never> {
    throw new Error(`Provider "${this.name}" is not yet implemented. Set FACE_PROVIDER=mock for development.`);
  }
}

export class EmbeddingProviderFactory {
  static create(type: ProviderType = "mock"): IFaceAnalysisProvider {
    switch (type) {
      case "arcface":
      case "facenet":
      case "deepface":
      case "aws":
      case "azure":
        return new UnimplementedProvider(type);
      case "mock":
      default:
        return new MockEmbeddingProvider();
    }
  }

  static fromEnv(): IFaceAnalysisProvider {
    const type = (process.env.FACE_PROVIDER ?? "mock") as ProviderType;
    return EmbeddingProviderFactory.create(type);
  }
}
