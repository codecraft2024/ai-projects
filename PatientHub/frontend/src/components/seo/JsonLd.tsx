import { CONTACT } from "@/data/clinic";
import { SITE_BASE_URL, FACEBOOK_URL, WHATSAPP_URL } from "@/config/social-links";
import type { Locale } from "@/i18n/routing";

type JsonLdProps = {
  locale: Locale;
  t: {
    clinicName: string;
    description: string;
    doctorName: string;
    doctorTitle: string;
    faq: { question: string; answer: string }[];
  };
};

export function JsonLd({ locale, t }: JsonLdProps) {
  const base = SITE_BASE_URL.replace(/\/$/, "");
  const clinicUrl = `${base}/${locale}`;

  const medicalClinic = {
    "@context": "https://schema.org",
    "@type": "MedicalClinic",
    "@id": `${clinicUrl}#clinic`,
    name: t.clinicName,
    description: t.description,
    url: clinicUrl,
    telephone: CONTACT.phone,
    email: CONTACT.email,
    image: `${base}/og/clinic-hero.png`,
    logo: `${base}/og/clinic-logo.png`,
    address: {
      "@type": "PostalAddress",
      streetAddress: CONTACT.address,
      addressLocality: "Heliopolis",
      addressRegion: "Cairo",
      addressCountry: "EG",
    },
    geo: {
      "@type": "GeoCoordinates",
      latitude: 30.0876,
      longitude: 31.3249,
    },
    openingHoursSpecification: [
      {
        "@type": "OpeningHoursSpecification",
        dayOfWeek: ["Saturday", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday"],
        opens: "10:00",
        closes: "20:00",
      },
    ],
    sameAs: [FACEBOOK_URL, WHATSAPP_URL].filter(Boolean),
    medicalSpecialty: "Orthopedic",
  };

  const physician = {
    "@context": "https://schema.org",
    "@type": "Physician",
    "@id": `${clinicUrl}#physician`,
    name: t.doctorName,
    jobTitle: t.doctorTitle,
    medicalSpecialty: "Orthopedic Surgery",
    worksFor: { "@id": `${clinicUrl}#clinic` },
    url: `${base}/${locale}/doctor`,
    image: `${base}/og/doctor-profile.png`,
  };

  const organization = {
    "@context": "https://schema.org",
    "@type": "Organization",
    name: t.clinicName,
    url: clinicUrl,
    logo: `${base}/og/clinic-logo.png`,
    founder: { "@id": `${clinicUrl}#physician` },
  };

  const localBusiness = {
    "@context": "https://schema.org",
    "@type": "LocalBusiness",
    name: t.clinicName,
    url: clinicUrl,
    telephone: CONTACT.phone,
    address: medicalClinic.address,
  };

  const faqPage = {
    "@context": "https://schema.org",
    "@type": "FAQPage",
    mainEntity: t.faq.map((item) => ({
      "@type": "Question",
      name: item.question,
      acceptedAnswer: {
        "@type": "Answer",
        text: item.answer,
      },
    })),
  };

  const graph = {
    "@context": "https://schema.org",
    "@graph": [medicalClinic, physician, organization, localBusiness, faqPage],
  };

  return (
    <script
      type="application/ld+json"
      dangerouslySetInnerHTML={{ __html: JSON.stringify(graph) }}
    />
  );
}
