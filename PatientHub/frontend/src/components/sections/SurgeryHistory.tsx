import { useTranslations } from "next-intl";
import { Container } from "@/components/ui/Container";
import { SectionHeading } from "@/components/ui/SectionHeading";

type Milestone = { year: string; title: string; description: string };
type Achievement = { title: string; description: string };

export function SurgeryHistory() {
  const t = useTranslations("experience");

  const milestones = t.raw("milestones") as Milestone[];
  const achievements = t.raw("achievements") as Achievement[];

  const stats = [
    { value: t("stats.experience"), label: t("stats.experienceLabel") },
    { value: t("stats.patients"), label: t("stats.patientsLabel") },
    { value: t("stats.surgeries"), label: t("stats.surgeriesLabel") },
    { value: t("stats.satisfaction"), label: t("stats.satisfactionLabel") },
  ];

  return (
    <section id="experience" className="bg-gradient-to-b from-slate-900 via-[#2d1f6b] to-slate-950 py-14 text-white sm:py-20">
      <Container>
        <SectionHeading
          eyebrow={t("eyebrow")}
          title={t("title")}
          description={t("description")}
          light
        />

        <div className="mt-12 grid grid-cols-2 gap-4 sm:grid-cols-4">
          {stats.map((stat) => (
            <div
              key={stat.label}
              className="rounded-2xl border border-white/10 bg-white/5 p-4 text-center backdrop-blur sm:p-6"
            >
              <p className="text-2xl font-bold text-accent sm:text-3xl">{stat.value}</p>
              <p className="mt-1 text-xs text-brand-light sm:text-sm">{stat.label}</p>
            </div>
          ))}
        </div>

        <div className="mt-14">
          <h3 className="text-center text-xl font-bold sm:text-start">{t("milestonesTitle")}</h3>
          <ol className="relative mt-8 space-y-6 border-s border-brand/40 ps-6 sm:ps-8">
            {milestones.map((m) => (
              <li key={m.year} className="relative">
                <span className="absolute -start-[1.6rem] flex h-8 w-8 items-center justify-center rounded-full bg-brand text-xs font-bold sm:-start-[2.1rem]">
                  {m.year.slice(2)}
                </span>
                <p className="text-sm font-semibold text-brand-light">{m.year}</p>
                <p className="mt-1 text-lg font-semibold">{m.title}</p>
                <p className="mt-1 text-sm text-white/80">{m.description}</p>
              </li>
            ))}
          </ol>
        </div>

        <div className="mt-14 grid gap-5 sm:grid-cols-2">
          {achievements.map((a) => (
            <article key={a.title} className="rounded-2xl border border-white/10 bg-white/5 p-6">
              <h4 className="text-lg font-semibold text-accent">{a.title}</h4>
              <p className="mt-2 text-sm leading-relaxed text-slate-300">{a.description}</p>
            </article>
          ))}
        </div>

        <div className="mt-12 rounded-2xl border border-accent/30 bg-brand/20 p-6 text-center sm:p-8">
          <p className="text-lg font-semibold">{t("highlightsTitle")}</p>
          <p className="mx-auto mt-3 max-w-2xl text-sm leading-relaxed text-white/85 sm:text-base">
            {t("highlights")}
          </p>
        </div>
      </Container>
    </section>
  );
}
