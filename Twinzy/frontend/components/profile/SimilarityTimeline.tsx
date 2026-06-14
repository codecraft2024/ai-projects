import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { formatPercent } from "@/lib/utils";

export function SimilarityTimeline({ overallScore }: { overallScore: number }) {
  const points = [
    { label: "Initial scan", value: Math.max(55, overallScore - 18) },
    { label: "Feature alignment", value: Math.max(60, overallScore - 10) },
    { label: "Embedding match", value: Math.max(65, overallScore - 4) },
    { label: "Final score", value: overallScore },
  ];

  return (
    <Card>
      <CardHeader>
        <CardTitle>Similarity Timeline</CardTitle>
      </CardHeader>
      <CardContent className="space-y-4">
        {points.map((point, index) => (
          <div key={point.label} className="flex items-center gap-4">
            <div className="flex h-10 w-10 items-center justify-center rounded-full bg-primary/15 text-sm font-semibold text-primary">
              {index + 1}
            </div>
            <div className="flex-1">
              <p className="font-medium">{point.label}</p>
              <p className="text-sm text-muted-foreground">{formatPercent(point.value)} confidence</p>
            </div>
          </div>
        ))}
      </CardContent>
    </Card>
  );
}
