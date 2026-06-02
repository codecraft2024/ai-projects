export const SITE = {
  name: "PatientHub",
  tagline: "Your Health, Our Priority",
  description:
    "PatientHub Portal connects you with compassionate care, experienced physicians, and modern healthcare services—all in one trusted clinic experience.",
} as const;

export const NAV_LINKS = [
  { href: "#home", label: "Home" },
  { href: "#services", label: "Services" },
  { href: "#team", label: "Our Team" },
  { href: "#contact", label: "Contact" },
] as const;

export const SERVICES = [
  {
    id: "primary-care",
    title: "Primary Care",
    description:
      "Comprehensive wellness visits, preventive screenings, and chronic disease management tailored to your lifestyle.",
    icon: "stethoscope",
  },
  {
    id: "urgent-care",
    title: "Urgent Care",
    description:
      "Same-day treatment for minor injuries, infections, and acute illnesses without the emergency room wait.",
    icon: "clock",
  },
  {
    id: "diagnostics",
    title: "Diagnostics & Labs",
    description:
      "On-site laboratory testing and imaging coordination for faster, more accurate diagnoses.",
    icon: "microscope",
  },
  {
    id: "telehealth",
    title: "Telehealth",
    description:
      "Secure virtual consultations with your care team from the comfort of home.",
    icon: "video",
  },
  {
    id: "pediatrics",
    title: "Pediatrics",
    description:
      "Child-focused care including immunizations, growth monitoring, and family-centered support.",
    icon: "heart",
  },
  {
    id: "mental-health",
    title: "Mental Health",
    description:
      "Licensed counselors and integrated behavioral health support as part of your whole-person care plan.",
    icon: "mind",
  },
] as const;

export const DOCTORS = [
  {
    id: "dr-chen",
    name: "Dr. Sarah Chen",
    role: "Medical Director",
    specialty: "Internal Medicine",
    bio: "15+ years guiding patients through preventive care and complex chronic conditions.",
    initials: "SC",
  },
  {
    id: "dr-patel",
    name: "Dr. Raj Patel",
    role: "Family Physician",
    specialty: "Family Medicine",
    bio: "Dedicated to building long-term relationships with patients of all ages.",
    initials: "RP",
  },
  {
    id: "dr-martinez",
    name: "Dr. Elena Martinez",
    role: "Pediatrician",
    specialty: "Pediatrics",
    bio: "Passionate about helping children thrive through compassionate, evidence-based care.",
    initials: "EM",
  },
  {
    id: "dr-williams",
    name: "Dr. James Williams",
    role: "Urgent Care Lead",
    specialty: "Emergency Medicine",
    bio: "Expert in rapid assessment and treatment of acute medical needs.",
    initials: "JW",
  },
] as const;

export const CONTACT = {
  address: "1200 Wellness Boulevard, Suite 400",
  city: "San Francisco, CA 94107",
  phone: "(415) 555-0198",
  email: "hello@patienthub.clinic",
  hours: [
    { days: "Monday – Friday", time: "8:00 AM – 6:00 PM" },
    { days: "Saturday", time: "9:00 AM – 2:00 PM" },
    { days: "Sunday", time: "Urgent care only" },
  ],
} as const;
