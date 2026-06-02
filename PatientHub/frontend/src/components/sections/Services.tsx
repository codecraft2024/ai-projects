import { SERVICES } from "@/constants/site";
import { Container } from "@/components/ui/Container";
import { SectionHeading } from "@/components/ui/SectionHeading";
import {
  ServiceIcon,
  type ServiceIconName,
} from "@/components/icons/ServiceIcons";

export function Services() {
  return (
    <section id="services" className="bg-white py-16 sm:py-24">
      <Container>
        <SectionHeading
          eyebrow="Our Services"
          title="Comprehensive healthcare under one roof"
          description="From routine checkups to specialized support, our clinic delivers coordinated care designed around your health goals."
        />

        <div className="mt-14 grid gap-6 sm:grid-cols-2 lg:grid-cols-3">
          {SERVICES.map((service) => (
            <article
              key={service.id}
              className="group rounded-2xl border border-slate-200 bg-slate-50/50 p-6 transition hover:border-teal-200 hover:bg-white hover:shadow-lg hover:shadow-teal-900/5"
            >
              <div className="flex h-12 w-12 items-center justify-center rounded-xl bg-teal-100 text-teal-700 transition group-hover:bg-teal-600 group-hover:text-white">
                <ServiceIcon
                  name={service.icon as ServiceIconName}
                  className="h-6 w-6"
                />
              </div>
              <h3 className="mt-5 text-lg font-semibold text-slate-900">
                {service.title}
              </h3>
              <p className="mt-2 text-sm leading-relaxed text-slate-600">
                {service.description}
              </p>
            </article>
          ))}
        </div>
      </Container>
    </section>
  );
}
