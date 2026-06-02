import { SITE } from "@/constants/site";
import { Container } from "@/components/ui/Container";

export function Hero() {
  return (
    <section
      id="home"
      className="relative overflow-hidden bg-gradient-to-br from-teal-50 via-white to-sky-50"
    >
      <div
        className="pointer-events-none absolute inset-0 opacity-40"
        aria-hidden
        style={{
          backgroundImage:
            "radial-gradient(circle at 20% 50%, rgb(20 184 166 / 0.15) 0%, transparent 50%), radial-gradient(circle at 80% 20%, rgb(14 165 233 / 0.12) 0%, transparent 40%)",
        }}
      />
      <Container className="relative py-16 sm:py-20 lg:py-28">
        <div className="grid items-center gap-12 lg:grid-cols-2 lg:gap-16">
          <div>
            <p className="inline-flex items-center gap-2 rounded-full bg-teal-100 px-4 py-1.5 text-sm font-medium text-teal-800">
              <span className="h-2 w-2 rounded-full bg-teal-500" />
              Accepting new patients
            </p>
            <h1 className="mt-6 text-4xl font-bold tracking-tight text-slate-900 sm:text-5xl lg:text-6xl">
              Welcome to{" "}
              <span className="text-teal-700">PatientHub</span> Clinic
            </h1>
            <p className="mt-2 text-xl font-medium text-teal-700">{SITE.tagline}</p>
            <p className="mt-6 text-lg leading-relaxed text-slate-600">
              {SITE.description}
            </p>
            <div className="mt-10 flex flex-col gap-4 sm:flex-row">
              <a
                href="#contact"
                className="inline-flex items-center justify-center rounded-xl bg-teal-600 px-8 py-3.5 text-base font-semibold text-white shadow-lg shadow-teal-600/25 transition hover:bg-teal-700"
              >
                Schedule a Visit
              </a>
              <a
                href="#services"
                className="inline-flex items-center justify-center rounded-xl border border-slate-300 bg-white px-8 py-3.5 text-base font-semibold text-slate-700 transition hover:border-teal-300 hover:bg-teal-50"
              >
                Explore Services
              </a>
            </div>
            <dl className="mt-12 grid grid-cols-3 gap-6 border-t border-slate-200 pt-10">
              <div>
                <dt className="text-2xl font-bold text-teal-700">25+</dt>
                <dd className="mt-1 text-sm text-slate-600">Years of care</dd>
              </div>
              <div>
                <dt className="text-2xl font-bold text-teal-700">12k+</dt>
                <dd className="mt-1 text-sm text-slate-600">Patients served</dd>
              </div>
              <div>
                <dt className="text-2xl font-bold text-teal-700">4.9</dt>
                <dd className="mt-1 text-sm text-slate-600">Patient rating</dd>
              </div>
            </dl>
          </div>

          <div className="relative hidden lg:block">
            <div className="aspect-[4/5] overflow-hidden rounded-3xl bg-gradient-to-br from-teal-600 to-teal-800 shadow-2xl shadow-teal-900/20">
              <div className="flex h-full flex-col justify-between p-10 text-white">
                <div>
                  <p className="text-sm font-medium uppercase tracking-wider text-teal-100">
                    PatientHub Clinic
                  </p>
                  <p className="mt-4 text-3xl font-semibold leading-snug">
                    Compassionate care, when you need it most.
                  </p>
                </div>
                <ul className="space-y-4 text-teal-50">
                  <li className="flex items-center gap-3">
                    <span className="flex h-8 w-8 items-center justify-center rounded-lg bg-white/20 text-sm font-bold">
                      ✓
                    </span>
                    Board-certified physicians
                  </li>
                  <li className="flex items-center gap-3">
                    <span className="flex h-8 w-8 items-center justify-center rounded-lg bg-white/20 text-sm font-bold">
                      ✓
                    </span>
                    Same-day appointments available
                  </li>
                  <li className="flex items-center gap-3">
                    <span className="flex h-8 w-8 items-center justify-center rounded-lg bg-white/20 text-sm font-bold">
                      ✓
                    </span>
                    Integrated patient portal
                  </li>
                </ul>
              </div>
            </div>
          </div>
        </div>
      </Container>
    </section>
  );
}
