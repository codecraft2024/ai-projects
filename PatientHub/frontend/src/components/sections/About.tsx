import { useTranslations } from "next-intl";
import { Container } from "@/components/ui/Container";
import { SectionHeading } from "@/components/ui/SectionHeading";

export function About() {
  const t = useTranslations("about");
  const tSite = useTranslations("site");
  const careApproach = t.raw("careApproach") as string[];

  return (
    <section id="about" className="bg-white py-14 sm:py-20">
      <Container>
        <SectionHeading
          eyebrow={t("eyebrow")}
          title={t("title")}
          description={tSite("mission")}
        />
        <div className="mt-12 grid gap-6 sm:grid-cols-2 lg:grid-cols-4">
          {careApproach.map((point, index) => (
            <div key={point} className="card-premium p-6">
              <span className="flex h-10 w-10 items-center justify-center rounded-xl bg-brand text-sm font-bold text-white">
                {String(index + 1).padStart(2, "0")}
              </span>
              <p className="mt-4 text-sm leading-relaxed text-slate-700 sm:text-base">{point}</p>
            </div>
          ))}
        </div>
      </Container>
    </section>
  );
}
