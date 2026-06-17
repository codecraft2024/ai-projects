import type { FeatureBreakdown } from "@/types/profile";
import { FEATURE_LABELS } from "@/lib/api-client";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { ProgressBar } from "@/components/ui/progress";
import { formatPercent } from "@/lib/utils";

export function FeatureBreakdownPanel({
  breakdown,
  overallScore,
}: {
  breakdown: FeatureBreakdown;
  overallScore: number;
}) {
  const entries = Object.entries(breakdown) as Array<[keyof FeatureBreakdown, number]>;

  return (
    <Card>
      <CardHeader>
        <CardTitle>Facial Feature Breakdown</CardTitle>
      </CardHeader>
      <CardContent className="space-y-4">
        {entries.map(([key, value]) => (
          <ProgressBar key={key} label={`${FEATURE_LABELS[key]} Similarity`} value={value} />
        ))}
        <div className="rounded-lg bg-primary/10 p-4 text-center">
          <p className="text-sm text-muted-foreground">Overall Similarity</p>
          <p className="text-3xl font-bold text-primary">{formatPercent(overallScore)}</p>
        </div>
      </CardContent>
    </Card>
  );
}
