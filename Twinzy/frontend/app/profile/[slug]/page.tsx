import { redirect } from "next/navigation";

export default async function LegacyProfileRedirect({
  params,
  searchParams,
}: {
  params: Promise<{ slug: string }>;
  searchParams: Promise<{ sessionId?: string }>;
}) {
  const { slug } = await params;
  const { sessionId } = await searchParams;
  redirect(sessionId ? `/celebrity/${slug}?sessionId=${sessionId}` : `/celebrity/${slug}`);
}
