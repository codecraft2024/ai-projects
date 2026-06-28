import { useTranslations } from "next-intl";
import { Container } from "@/components/ui/Container";
import { SectionHeading } from "@/components/ui/SectionHeading";
import { ServiceIcon } from "@/components/icons/ServiceIcons";
import type { ServiceIconName } from "@/types/clinic";

const SERVICE_KEYS: { id: ServiceIconName; key: string }[] = [
  { id: "pediatric", key: "pediatric" },
  { id: "joint", key: "joint" },
  { id: "sports", key: "sports" },
  { id: "bone", key: "bone" },
  { id: "foot", key: "foot" },
  { id: "hand", key: "hand" },
];

export function Services() {
  const t = useTranslations("services");

  return (
    <section id="services" className="bg-muted py-14 sm:py-20">
      <Container>
        <SectionHeading
          eyebrow={t("eyebrow")}
          title={t("title")}
          description={t("description")}
        />
        <div className="mt-12 grid gap-5 sm:grid-cols-2 lg:grid-cols-3">
          {SERVICE_KEYS.map(({ id, key }) => (
            <article key={key} className="card-premium group p-6">
              <div className="flex h-12 w-12 items-center justify-center rounded-xl bg-brand-soft text-brand transition group-hover:bg-brand group-hover:text-white">
                <ServiceIcon name={id} className="h-6 w-6" />
              </div>
              <h3 className="mt-5 text-lg font-semibold text-slate-900">
                {t(`items.${key}.title`)}
              </h3>
              <p className="mt-2 text-sm leading-relaxed text-slate-600">
                {t(`items.${key}.description`)}
              </p>
            </article>
          ))}
        </div>
      </Container>
    </section>
  );
}
