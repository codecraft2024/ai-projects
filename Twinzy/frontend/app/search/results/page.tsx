import { redirect } from "next/navigation";

export default async function LegacyResultsRedirect({
  searchParams,
}: {
  searchParams: Promise<{ sessionId?: string }>;
}) {
  const { sessionId } = await searchParams;
  redirect(sessionId ? `/results?sessionId=${sessionId}` : "/results");
}
