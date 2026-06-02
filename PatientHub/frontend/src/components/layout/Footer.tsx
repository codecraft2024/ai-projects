import Link from "next/link";
import { CONTACT, NAV_LINKS, SITE } from "@/constants/site";
import { Container } from "@/components/ui/Container";

export function Footer() {
  const year = new Date().getFullYear();

  return (
    <footer className="border-t border-slate-200 bg-slate-900 text-slate-300">
      <Container className="py-12 lg:py-16">
        <div className="grid gap-10 md:grid-cols-2 lg:grid-cols-4">
          <div className="lg:col-span-1">
            <Link href="#home" className="flex items-center gap-2.5">
              <span className="flex h-10 w-10 items-center justify-center rounded-xl bg-teal-500 text-lg font-bold text-white">
                PH
              </span>
              <span className="text-lg font-bold text-white">{SITE.name}</span>
            </Link>
            <p className="mt-4 text-sm leading-relaxed text-slate-400">
              {SITE.description}
            </p>
          </div>

          <div>
            <h3 className="text-sm font-semibold uppercase tracking-wider text-white">
              Quick Links
            </h3>
            <ul className="mt-4 space-y-2">
              {NAV_LINKS.map((link) => (
                <li key={link.href}>
                  <a
                    href={link.href}
                    className="text-sm text-slate-400 transition-colors hover:text-teal-400"
                  >
                    {link.label}
                  </a>
                </li>
              ))}
            </ul>
          </div>

          <div>
            <h3 className="text-sm font-semibold uppercase tracking-wider text-white">
              Contact
            </h3>
            <ul className="mt-4 space-y-2 text-sm text-slate-400">
              <li>{CONTACT.address}</li>
              <li>{CONTACT.city}</li>
              <li>
                <a href={`tel:${CONTACT.phone}`} className="hover:text-teal-400">
                  {CONTACT.phone}
                </a>
              </li>
              <li>
                <a href={`mailto:${CONTACT.email}`} className="hover:text-teal-400">
                  {CONTACT.email}
                </a>
              </li>
            </ul>
          </div>

          <div>
            <h3 className="text-sm font-semibold uppercase tracking-wider text-white">
              Hours
            </h3>
            <ul className="mt-4 space-y-2 text-sm text-slate-400">
              {CONTACT.hours.map((item) => (
                <li key={item.days}>
                  <span className="font-medium text-slate-300">{item.days}:</span>{" "}
                  {item.time}
                </li>
              ))}
            </ul>
          </div>
        </div>

        <div className="mt-12 flex flex-col items-center justify-between gap-4 border-t border-slate-800 pt-8 sm:flex-row">
          <p className="text-sm text-slate-500">
            © {year} {SITE.name} Portal. All rights reserved.
          </p>
          <p className="text-xs text-slate-600">
            For medical emergencies, call 911.
          </p>
        </div>
      </Container>
    </footer>
  );
}
