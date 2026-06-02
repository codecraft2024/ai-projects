import { DOCTORS } from "@/constants/site";
import { Container } from "@/components/ui/Container";
import { SectionHeading } from "@/components/ui/SectionHeading";

export function MedicalTeam() {
  return (
    <section id="team" className="bg-slate-50 py-16 sm:py-24">
      <Container>
        <SectionHeading
          eyebrow="Medical Team"
          title="Meet our experienced physicians"
          description="Our multidisciplinary team brings decades of combined expertise to every patient interaction."
        />

        <div className="mt-14 grid gap-8 sm:grid-cols-2 lg:grid-cols-4">
          {DOCTORS.map((doctor) => (
            <article
              key={doctor.id}
              className="flex flex-col overflow-hidden rounded-2xl border border-slate-200 bg-white shadow-sm transition hover:shadow-md"
            >
              <div className="flex aspect-[4/3] items-center justify-center bg-gradient-to-br from-teal-100 to-sky-100">
                <span className="flex h-20 w-20 items-center justify-center rounded-full bg-teal-600 text-2xl font-bold text-white shadow-lg">
                  {doctor.initials}
                </span>
              </div>
              <div className="flex flex-1 flex-col p-6">
                <h3 className="text-lg font-semibold text-slate-900">
                  {doctor.name}
                </h3>
                <p className="mt-1 text-sm font-medium text-teal-700">
                  {doctor.role}
                </p>
                <p className="mt-0.5 text-xs uppercase tracking-wide text-slate-500">
                  {doctor.specialty}
                </p>
                <p className="mt-4 flex-1 text-sm leading-relaxed text-slate-600">
                  {doctor.bio}
                </p>
              </div>
            </article>
          ))}
        </div>
      </Container>
    </section>
  );
}
