import { Suspense } from "react";
import SearchResultsClient from "@/components/results/SearchResultsClient";

export default function ResultsPage() {
  return (
    <Suspense fallback={<div className="py-20 text-center text-muted-foreground">Loading...</div>}>
      <SearchResultsClient />
    </Suspense>
  );
}
