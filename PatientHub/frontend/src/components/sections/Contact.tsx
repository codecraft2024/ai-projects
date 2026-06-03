"use client";

import { useTranslations } from "next-intl";
import { CONTACT } from "@/data/clinic";
import { Container } from "@/components/ui/Container";
import { SectionHeading } from "@/components/ui/SectionHeading";
import { Button } from "@/components/ui/Button";
import { WhatsAppButton } from "@/components/ui/WhatsAppButton";
import { SocialLinks } from "@/components/social/SocialLinks";
import { WHATSAPP_URL } from "@/utils/whatsapp";

export function Contact() {
  const t = useTranslations("contact");
  const hours = t.raw("hours") as { days: string; time: string }[];

  return (
    <section id="contact" className="bg-muted py-14 sm:py-20">
      <Container>
        <SectionHeading eyebrow={t("eyebrow")} title={t("title")} description={t("description")} />

        <div className="mt-12 grid gap-8 lg:grid-cols-2">
          <div className="space-y-5">
            <ContactCard title={t("addressTitle")}>
              <address className="not-italic text-slate-600">
                <p className="font-medium text-slate-900">{CONTACT.clinicName}</p>
                <p className="mt-2">{t("addressLine1")}</p>
                <p>{t("city")}</p>
              </address>
            </ContactCard>

            <ContactCard title={t("phoneTitle")}>
              <a
                href={`tel:${CONTACT.phone.replace(/\s/g, "")}`}
                className="text-lg font-semibold text-brand hover:underline"
              >
                {CONTACT.phoneDisplay}
              </a>
              <p className="mt-1 text-sm text-slate-500">{t("phoneHint")}</p>
            </ContactCard>

            <ContactCard title={t("whatsappTitle")}>
              <p className="text-slate-600">{CONTACT.whatsappDisplay}</p>
              <WhatsAppButton label={t("whatsappCta")} className="mt-4" />
            </ContactCard>

            <ContactCard title={t("socialTitle")}>
              <p className="text-sm text-slate-600">{t("socialDescription")}</p>
              <SocialLinks variant="contact" className="mt-4" />
            </ContactCard>

            <ContactCard title={t("hoursTitle")}>
              <ul className="space-y-3">
                {hours.map((item) => (
                  <li
                    key={item.days}
                    className="flex flex-col justify-between gap-1 border-b border-[var(--border)] pb-3 last:border-0 sm:flex-row"
                  >
                    <span className="font-medium text-slate-800">{item.days}</span>
                    <span className="text-slate-600">{item.time}</span>
                  </li>
                ))}
              </ul>
            </ContactCard>
          </div>

          <div className="card-premium p-6 sm:p-8">
            <h3 className="text-xl font-bold text-slate-900 sm:text-2xl">{t("formTitle")}</h3>
            <p className="mt-2 text-sm text-slate-600">{t("formDescription")}</p>
            <form
              className="mt-6 space-y-4"
              onSubmit={(e) => {
                e.preventDefault();
                window.open(WHATSAPP_URL, "_blank", "noopener,noreferrer");
              }}
            >
              <div className="grid gap-4 sm:grid-cols-2">
                <FormField label={t("firstName")} name="firstName" required />
                <FormField label={t("lastName")} name="lastName" required />
              </div>
              <FormField label={t("phone")} name="phone" type="tel" required />
              <label className="block">
                <span className="text-sm font-medium text-slate-700">{t("reason")}</span>
                <select
                  name="reason"
                  className="mt-1.5 w-full rounded-xl border border-[var(--border)] px-4 py-3 focus:border-brand focus:outline-none focus:ring-2 focus:ring-brand/20"
                  defaultValue=""
                  required
                >
                  <option value="" disabled>
                    {t("selectService")}
                  </option>
                  <option value="consultation">{t("consultation")}</option>
                  <option value="pediatric">{t("pediatric")}</option>
                  <option value="sports">{t("sports")}</option>
                  <option value="joint">{t("joint")}</option>
                  <option value="follow-up">{t("followUp")}</option>
                </select>
              </label>
              <FormField label={t("message")} name="message" as="textarea" />
              <div className="flex flex-col gap-3 pt-2 sm:flex-row">
                <Button type="submit" variant="primary" size="lg" className="sm:flex-1">
                  {t("submitWhatsApp")}
                </Button>
                <WhatsAppButton label="WhatsApp" size="lg" />
              </div>
              <p className="text-center text-xs text-slate-500">{t("formNote")}</p>
            </form>
          </div>
        </div>
      </Container>
    </section>
  );
}

function ContactCard({
  title,
  children,
}: {
  title: string;
  children: React.ReactNode;
}) {
  return (
    <div className="card-premium p-5 sm:p-6">
      <h3 className="text-base font-semibold text-slate-900 sm:text-lg">{title}</h3>
      <div className="mt-4">{children}</div>
    </div>
  );
}

function FormField({
  label,
  name,
  required,
  type = "text",
  as,
}: {
  label: string;
  name: string;
  required?: boolean;
  type?: string;
  as?: "textarea";
}) {
  const cls =
    "mt-1.5 w-full rounded-xl border border-[var(--border)] px-4 py-3 focus:border-brand focus:outline-none focus:ring-2 focus:ring-brand/20";
  return (
    <label className="block">
      <span className="text-sm font-medium text-slate-700">{label}</span>
      {as === "textarea" ? (
        <textarea name={name} rows={4} className={`${cls} resize-none`} />
      ) : (
        <input type={type} name={name} required={required} className={cls} />
      )}
    </label>
  );
}
