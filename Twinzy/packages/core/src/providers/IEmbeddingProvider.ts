import type { FeatureBreakdown } from "@twinzy/types";

export interface BoundingBox {
  x: number;
  y: number;
  width: number;
  height: number;
}

export interface LandmarkPoint {
  x: number;
  y: number;
}

export type LandmarkMap = Record<string, LandmarkPoint>;

export interface FaceDetectionResult {
  embedding: number[];
  landmarks?: LandmarkMap;
  boundingBox?: BoundingBox;
}

export interface IFaceAnalysisProvider {
  readonly name: string;
  detectAndEmbed(image: Buffer): Promise<FaceDetectionResult>;
  analyzeFeatures(image: Buffer, landmarks: LandmarkMap): Promise<FeatureBreakdown>;
}

export type ProviderType =
  | "mock"
  | "arcface"
  | "facenet"
  | "deepface"
  | "aws"
  | "azure";
