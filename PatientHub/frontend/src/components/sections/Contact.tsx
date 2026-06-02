"use client";

import { CONTACT } from "@/constants/site";
import { Container } from "@/components/ui/Container";
import { SectionHeading } from "@/components/ui/SectionHeading";

export function Contact() {
  return (
    <section id="contact" className="bg-white py-16 sm:py-24">
      <Container>
        <SectionHeading
          eyebrow="Contact Us"
          title="We're here to help"
          description="Reach out to schedule an appointment, ask a question, or learn more about our services."
        />

        <div className="mt-14 grid gap-10 lg:grid-cols-2">
          <div className="space-y-8">
            <div className="rounded-2xl border border-slate-200 bg-slate-50 p-6">
              <h3 className="flex items-center gap-2 text-lg font-semibold text-slate-900">
                <LocationIcon />
                Visit Us
              </h3>
              <address className="mt-4 not-italic text-slate-600">
                <p>{CONTACT.address}</p>
                <p>{CONTACT.city}</p>
              </address>
            </div>

            <div className="rounded-2xl border border-slate-200 bg-slate-50 p-6">
              <h3 className="flex items-center gap-2 text-lg font-semibold text-slate-900">
                <PhoneIcon />
                Get in Touch
              </h3>
              <ul className="mt-4 space-y-2 text-slate-600">
                <li>
                  <a
                    href={`tel:${CONTACT.phone.replace(/\D/g, "")}`}
                    className="font-medium text-teal-700 hover:underline"
                  >
                    {CONTACT.phone}
                  </a>
                </li>
                <li>
                  <a
                    href={`mailto:${CONTACT.email}`}
                    className="font-medium text-teal-700 hover:underline"
                  >
                    {CONTACT.email}
                  </a>
                </li>
              </ul>
            </div>

            <div className="rounded-2xl border border-slate-200 bg-slate-50 p-6">
              <h3 className="flex items-center gap-2 text-lg font-semibold text-slate-900">
                <ClockIcon />
                Office Hours
              </h3>
              <ul className="mt-4 space-y-3">
                {CONTACT.hours.map((item) => (
                  <li
                    key={item.days}
                    className="flex justify-between gap-4 text-sm text-slate-600"
                  >
                    <span className="font-medium text-slate-800">{item.days}</span>
                    <span>{item.time}</span>
                  </li>
                ))}
              </ul>
            </div>
          </div>

          <div className="rounded-2xl border border-slate-200 bg-gradient-to-br from-teal-600 to-teal-800 p-8 text-white shadow-xl lg:p-10">
            <h3 className="text-2xl font-bold">Request an appointment</h3>
            <p className="mt-2 text-teal-100">
              Fill out the form below and our team will contact you within one
              business day.
            </p>
            <form className="mt-8 space-y-5" onSubmit={(e) => e.preventDefault()}>
              <div className="grid gap-5 sm:grid-cols-2">
                <label className="block">
                  <span className="text-sm font-medium text-teal-50">First name</span>
                  <input
                    type="text"
                    name="firstName"
                    required
                    className="mt-1.5 w-full rounded-lg border-0 bg-white/10 px-4 py-2.5 text-white placeholder:text-teal-200/70 focus:ring-2 focus:ring-white/50"
                    placeholder="Jane"
                  />
                </label>
                <label className="block">
                  <span className="text-sm font-medium text-teal-50">Last name</span>
                  <input
                    type="text"
                    name="lastName"
                    required
                    className="mt-1.5 w-full rounded-lg border-0 bg-white/10 px-4 py-2.5 text-white placeholder:text-teal-200/70 focus:ring-2 focus:ring-white/50"
                    placeholder="Doe"
                  />
                </label>
              </div>
              <label className="block">
                <span className="text-sm font-medium text-teal-50">Email</span>
                <input
                  type="email"
                  name="email"
                  required
                  className="mt-1.5 w-full rounded-lg border-0 bg-white/10 px-4 py-2.5 text-white placeholder:text-teal-200/70 focus:ring-2 focus:ring-white/50"
                  placeholder="you@example.com"
                />
              </label>
              <label className="block">
                <span className="text-sm font-medium text-teal-50">Message</span>
                <textarea
                  name="message"
                  rows={4}
                  className="mt-1.5 w-full resize-none rounded-lg border-0 bg-white/10 px-4 py-2.5 text-white placeholder:text-teal-200/70 focus:ring-2 focus:ring-white/50"
                  placeholder="How can we help you?"
                />
              </label>
              <button
                type="submit"
                className="w-full rounded-xl bg-white py-3.5 text-base font-semibold text-teal-800 transition hover:bg-teal-50"
              >
                Submit Request
              </button>
              <p className="text-center text-xs text-teal-200">
                This form is for demonstration. Connect to the PatientHub API for
                production submissions.
              </p>
            </form>
          </div>
        </div>
      </Container>
    </section>
  );
}

function LocationIcon() {
  return (
    <svg className="h-5 w-5 text-teal-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z" />
      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 11a3 3 0 11-6 0 3 3 0 016 0z" />
    </svg>
  );
}

function PhoneIcon() {
  return (
    <svg className="h-5 w-5 text-teal-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M3 5a2 2 0 012-2h3.28a1 1 0 01.948.684l1.498 4.493a1 1 0 01-.502 1.21l-2.257 1.13a11.042 11.042 0 005.516 5.516l1.13-2.257a1 1 0 011.21-.502l4.493 1.498a1 1 0 01.684.949V19a2 2 0 01-2 2h-1C9.716 21 3 14.284 3 6V5z" />
    </svg>
  );
}

function ClockIcon() {
  return (
    <svg className="h-5 w-5 text-teal-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
    </svg>
  );
}
