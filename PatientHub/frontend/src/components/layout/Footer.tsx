"use client";

import { useTranslations } from "next-intl";
import { Link } from "@/i18n/navigation";
import { CONTACT } from "@/data/clinic";
import { Container } from "@/components/ui/Container";
import { Logo } from "@/components/ui/Logo";
import { SocialLinks } from "@/components/social/SocialLinks";

const NAV_ITEMS = [
  { href: "/", key: "home" as const },
  { href: "/#about", key: "about" as const },
  { href: "/doctor", key: "doctor" as const },
  { href: "/cases", key: "cases" as const },
  { href: "/#contact", key: "contact" as const },
];

export function Footer() {
  const t = useTranslations("footer");
  const tNav = useTranslations("nav");
  const tSite = useTranslations("site");
  const tContact = useTranslations("contact");
  const year = new Date().getFullYear();

  const hours = tContact.raw("hours") as { days: string; time: string }[];

  return (
    <footer className="border-t border-slate-800 bg-slate-950 text-slate-300">
      <Container className="py-12 sm:py-16">
        <div className="grid gap-10 sm:grid-cols-2 lg:grid-cols-4">
          <div className="sm:col-span-2 lg:col-span-1">
            <Link href="/" className="inline-flex items-center gap-3">
              <Logo variant="icon" height={44} withWhiteBadge className="shrink-0" />
              <div className="min-w-0">
                <span className="block font-bold text-white">{tSite("portalName")}</span>
                <span className="text-xs text-brand-light">{tSite("brandTagline")}</span>
              </div>
            </Link>
            <p className="mt-4 max-w-sm text-sm leading-relaxed text-slate-400">
              {tSite("description")}
            </p>
            <div className="mt-6">
              <p className="mb-3 text-xs font-semibold uppercase tracking-wider text-slate-500">
                {t("followUs")}
              </p>
              <SocialLinks variant="footer" />
            </div>
          </div>

          <div>
            <h3 className="text-sm font-semibold uppercase tracking-wider text-white">
              {t("quickLinks")}
            </h3>
            <ul className="mt-4 space-y-2">
              {NAV_ITEMS.map((item) => (
                <li key={item.key}>
                  <Link
                    href={item.href}
                    className="text-sm text-slate-400 transition hover:text-brand-light"
                  >
                    {tNav(item.key)}
                  </Link>
                </li>
              ))}
              <li>
                <Link
                  href="/admin/login"
                  className="text-sm text-slate-400 transition hover:text-brand-light"
                >
                  {tNav("adminLogin")}
                </Link>
              </li>
            </ul>
          </div>

          <div>
            <h3 className="text-sm font-semibold uppercase tracking-wider text-white">
              {t("contact")}
            </h3>
            <ul className="mt-4 space-y-2 text-sm text-slate-400">
              <li>{tContact("addressLine1")}</li>
              <li>{tContact("city")}</li>
              <li>
                <a href={`tel:${CONTACT.phone}`} className="hover:text-brand-light">
                  {CONTACT.phoneDisplay}
                </a>
              </li>
            </ul>
          </div>

          <div>
            <h3 className="text-sm font-semibold uppercase tracking-wider text-white">
              {t("hours")}
            </h3>
            <ul className="mt-4 space-y-2 text-sm text-slate-400">
              {hours.map((item) => (
                <li key={item.days}>
                  <span className="font-medium text-slate-300">{item.days}:</span> {item.time}
                </li>
              ))}
            </ul>
          </div>
        </div>

        <div className="mt-12 flex flex-col items-center justify-between gap-3 border-t border-slate-800 pt-8 text-center sm:flex-row sm:text-start">
          <p className="text-sm text-slate-500">
            © {year} {tSite("portalName")} · {tSite("clinicName")}. {t("rights")}
          </p>
          <p className="text-xs text-slate-600">{t("tagline")}</p>
        </div>
      </Container>
    </footer>
  );
}
