"use client";

import { useState } from "react";
import Link from "next/link";
import { NAV_LINKS, SITE } from "@/constants/site";
import { Container } from "@/components/ui/Container";

export function Header() {
  const [menuOpen, setMenuOpen] = useState(false);

  return (
    <header className="sticky top-0 z-50 border-b border-slate-200/80 bg-white/95 backdrop-blur-sm">
      <Container>
        <div className="flex h-16 items-center justify-between lg:h-18">
          <Link href="#home" className="flex items-center gap-2.5">
            <span className="flex h-10 w-10 items-center justify-center rounded-xl bg-teal-600 text-lg font-bold text-white shadow-sm">
              PH
            </span>
            <div className="flex flex-col">
              <span className="text-lg font-bold tracking-tight text-slate-900">
                {SITE.name}
              </span>
              <span className="hidden text-xs text-slate-500 sm:block">
                Portal
              </span>
            </div>
          </Link>

          <nav className="hidden items-center gap-8 md:flex" aria-label="Main">
            {NAV_LINKS.map((link) => (
              <a
                key={link.href}
                href={link.href}
                className="text-sm font-medium text-slate-600 transition-colors hover:text-teal-700"
              >
                {link.label}
              </a>
            ))}
          </nav>

          <div className="hidden items-center gap-3 md:flex">
            <a
              href="#contact"
              className="rounded-lg border border-teal-600 px-4 py-2 text-sm font-semibold text-teal-700 transition-colors hover:bg-teal-50"
            >
              Book Appointment
            </a>
          </div>

          <button
            type="button"
            className="inline-flex items-center justify-center rounded-lg p-2 text-slate-600 hover:bg-slate-100 md:hidden"
            aria-expanded={menuOpen}
            aria-label="Toggle navigation menu"
            onClick={() => setMenuOpen(!menuOpen)}
          >
            <svg className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              {menuOpen ? (
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
              ) : (
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 6h16M4 12h16M4 18h16" />
              )}
            </svg>
          </button>
        </div>

        {menuOpen && (
          <nav
            className="border-t border-slate-100 py-4 md:hidden"
            aria-label="Mobile"
          >
            <ul className="flex flex-col gap-3">
              {NAV_LINKS.map((link) => (
                <li key={link.href}>
                  <a
                    href={link.href}
                    className="block py-2 text-base font-medium text-slate-700"
                    onClick={() => setMenuOpen(false)}
                  >
                    {link.label}
                  </a>
                </li>
              ))}
              <li>
                <a
                  href="#contact"
                  className="mt-2 block rounded-lg bg-teal-600 px-4 py-3 text-center text-sm font-semibold text-white"
                  onClick={() => setMenuOpen(false)}
                >
                  Book Appointment
                </a>
              </li>
            </ul>
          </nav>
        )}
      </Container>
    </header>
  );
}
