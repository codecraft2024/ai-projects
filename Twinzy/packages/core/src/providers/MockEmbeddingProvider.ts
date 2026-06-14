import type { FeatureBreakdown } from "@twinzy/types";
import type {
  FaceDetectionResult,
  IFaceAnalysisProvider,
  LandmarkMap,
} from "./IEmbeddingProvider";

function hashBuffer(buffer: Buffer): number {
  let hash = 0;
  for (let i = 0; i < buffer.length; i++) {
    hash = (hash * 31 + buffer[i]!) % 2147483647;
  }
  return hash;
}

function seededVector(seed: number, dimensions = 512): number[] {
  const vector: number[] = [];
  let state = seed;
  for (let i = 0; i < dimensions; i++) {
    state = (state * 1664525 + 1013904223) % 2147483647;
    vector.push((state / 2147483647) * 2 - 1);
  }
  const magnitude = Math.sqrt(vector.reduce((sum, v) => sum + v * v, 0));
  return vector.map((v) => v / magnitude);
}

function seededBreakdown(seed: number): FeatureBreakdown {
  const next = (offset: number) => 70 + ((seed + offset) % 31);
  return {
    faceShape: next(1),
    eyes: next(2),
    eyebrows: next(3),
    nose: next(4),
    lips: next(5),
    smile: next(6),
    forehead: next(7),
    jawline: next(8),
    hairline: next(9),
  };
}

export class MockEmbeddingProvider implements IFaceAnalysisProvider {
  readonly name = "mock";

  async detectAndEmbed(image: Buffer): Promise<FaceDetectionResult> {
    const seed = hashBuffer(image);
    return {
      embedding: seededVector(seed),
      landmarks: this.mockLandmarks(seed),
    };
  }

  async analyzeFeatures(_image: Buffer, landmarks: LandmarkMap): Promise<FeatureBreakdown> {
    const seed = Object.values(landmarks).reduce((sum, p) => sum + p.x + p.y, 0);
    return seededBreakdown(Math.floor(seed * 1000) % 10000);
  }

  private mockLandmarks(seed: number): LandmarkMap {
    const offset = (n: number) => (seed % 100) / 1000 + n * 0.01;
    return {
      leftEye: { x: 0.35 + offset(1), y: 0.4 },
      rightEye: { x: 0.65 + offset(2), y: 0.4 },
      nose: { x: 0.5, y: 0.55 + offset(3) },
      mouthLeft: { x: 0.4, y: 0.7 },
      mouthRight: { x: 0.6, y: 0.7 },
    };
  }
}
