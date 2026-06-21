export type NavLink = {
  href: string;
  label: string;
};

export type ServiceItem = {
  id: string;
  title: string;
  description: string;
  icon: ServiceIconName;
};

export type ServiceIconName =
  | "bone"
  | "joint"
  | "pediatric"
  | "sports"
  | "foot"
  | "hand";

export type WorkingHour = {
  days: string;
  time: string;
};

export type ContactInfo = {
  clinicName: string;
  address: string;
  city: string;
  phone: string;
  phoneDisplay: string;
  phoneSecondary?: string;
  phoneSecondaryDisplay?: string;
  whatsapp: string;
  whatsappDisplay: string;
  whatsappContactName: string;
  email: string;
  hours: WorkingHour[];
};

export type Qualification = {
  title: string;
  institution: string;
};

export type DoctorProfile = {
  name: string;
  title: string;
  introduction: string;
  qualifications: Qualification[];
  specializations: string[];
  experience: string[];
  expertiseAreas: string[];
  imageAlt: string;
};

export type StatItem = {
  value: string;
  label: string;
};

export type Milestone = {
  year: string;
  title: string;
  description: string;
};

export type Achievement = {
  title: string;
  description: string;
};

export type SiteConfig = {
  portalName: string;
  clinicName: string;
  tagline: string;
  description: string;
  mission: string;
  careApproach: string[];
};
