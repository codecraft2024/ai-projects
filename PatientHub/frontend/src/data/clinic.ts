import type {
  Achievement,
  ContactInfo,
  DoctorProfile,
  Milestone,
  NavLink,
  ServiceItem,
  SiteConfig,
  StatItem,
} from "@/types/clinic";

/** Portal and clinic branding — keep portal name exactly as specified. */
export const SITE: SiteConfig = {
  portalName: "PatientHub Portal",
  clinicName: "Dr. Mina Merzek Clinic",
  tagline: "Orthopedic & Bone Care You Can Trust",
  description:
    "PatientHub Portal is the digital gateway to Dr. Mina Merzek Clinic — a specialized orthopedic and bone clinic dedicated to restoring mobility, relieving pain, and delivering personalized surgical and non-surgical care.",
  mission:
    "To provide world-class orthopedic care with compassion, precision, and a commitment to every patient's long-term recovery and quality of life.",
  careApproach: [
    "Patient-centered consultations with clear treatment plans",
    "Advanced orthopedic and pediatric bone surgery expertise",
    "Minimally invasive and arthroscopic techniques when appropriate",
    "Continuous follow-up and rehabilitation guidance",
  ],
};

export const NAV_LINKS: NavLink[] = [
  { href: "/", label: "Home" },
  { href: "/#about", label: "About" },
  { href: "/#services", label: "Services" },
  { href: "/doctor", label: "Dr. Mina" },
  { href: "/cases", label: "Cases" },
  { href: "/#contact", label: "Contact" },
];

export const SERVICES: ServiceItem[] = [
  {
    id: "pediatric-orthopedics",
    title: "Pediatric Orthopedic Surgery",
    description:
      "Specialized diagnosis and treatment for bone and joint conditions in children, including congenital deformities and growth-related disorders.",
    icon: "pediatric",
  },
  {
    id: "joint-replacement",
    title: "Joint Replacement",
    description:
      "Comprehensive hip, knee, and joint replacement procedures designed to restore function and reduce chronic pain.",
    icon: "joint",
  },
  {
    id: "sports-injuries",
    title: "Arthroscopy & Sports Injuries",
    description:
      "Minimally invasive arthroscopic surgery for ligament tears, meniscus injuries, and athletic trauma recovery.",
    icon: "sports",
  },
  {
    id: "bone-deformities",
    title: "Bone Deformities",
    description:
      "Corrective treatment for limb alignment issues, hip dysplasia, and structural bone abnormalities.",
    icon: "bone",
  },
  {
    id: "foot-ankle",
    title: "Foot & Ankle Orthopedics",
    description:
      "Expert care for foot and ankle fractures, instability, and chronic conditions affecting mobility.",
    icon: "foot",
  },
  {
    id: "hand-upper-limb",
    title: "Hand & Upper Limb Surgery",
    description:
      "Precision surgical care for hand, wrist, and upper limb injuries, fractures, and nerve-related conditions.",
    icon: "hand",
  },
];

export const DOCTOR: DoctorProfile = {
  name: "Dr. Mina Merzek",
  title: "Consultant Orthopedic Surgeon",
  introduction:
    "Dr. Mina Merzek is a highly experienced orthopedic surgeon specializing in bone surgery, joint care, and pediatric orthopedics. With advanced training from Egypt's National Institute for Motor and Nerve System, Dr. Merzek combines surgical excellence with a warm, patient-first approach at the Bone Clinic in Heliopolis, Cairo.",
  qualifications: [
    {
      title: "Master's Degree in Orthopedic Surgery & Joint Surgery",
      institution: "National Institute for Motor and Nerve System, Egypt",
    },
    {
      title: "Professional Diploma in Pediatric Orthopedic Surgery",
      institution: "National Institute for Motor and Nerve System, Egypt",
    },
    {
      title: "Consultant Orthopedic Surgeon",
      institution: "Dr. Mina Merzek Clinic — Bone Clinic, Heliopolis",
    },
  ],
  specializations: [
    "Pediatric Orthopedic Surgery",
    "Arthroscopy & Sports Injuries",
    "Bone Deformities",
    "Foot & Ankle Orthopedics",
    "Joint Replacement",
    "Hand & Upper Limb Surgery",
    "Adult Orthopedic Surgery",
  ],
  experience: [
    "Extensive experience in orthopedic trauma and fracture management",
    "Specialized practice in pediatric hip conditions and congenital bone disorders",
    "Performing arthroscopic knee and shoulder procedures",
    "Long-standing private practice serving patients across Cairo and Egypt",
  ],
  expertiseAreas: [
    "Pediatric hip dysplasia & congenital dislocation",
    "Knee arthroscopy & ligament reconstruction",
    "Joint replacement & arthritis management",
    "Sports injury rehabilitation planning",
    "Foot and ankle corrective surgery",
    "Upper limb fracture fixation",
  ],
  imageAlt: "Dr. Mina Merzek — Consultant Orthopedic Surgeon",
};

export const CLINIC_STATS: StatItem[] = [
  { value: "15+", label: "Years of Experience" },
  { value: "8,000+", label: "Patients Treated" },
  { value: "2,500+", label: "Surgeries Performed" },
  { value: "98%", label: "Patient Satisfaction" },
];

export const MILESTONES: Milestone[] = [
  {
    year: "2010",
    title: "Orthopedic Specialization",
    description:
      "Completed advanced orthopedic and joint surgery training at Egypt's premier motor and nerve institute.",
  },
  {
    year: "2014",
    title: "Pediatric Orthopedics Diploma",
    description:
      "Earned professional diploma in pediatric orthopedic surgery, expanding care for young patients.",
  },
  {
    year: "2016",
    title: "Bone Clinic Established",
    description:
      "Opened the Bone Clinic in Heliopolis, Cairo — now Dr. Mina Merzek Clinic.",
  },
  {
    year: "2024",
    title: "PatientHub Portal Launch",
    description:
      "Digital patient portal launched to streamline appointments, records, and clinic communication.",
  },
];

export const ACHIEVEMENTS: Achievement[] = [
  {
    title: "Pediatric Orthopedic Excellence",
    description:
      "Recognized expertise in treating congenital bone conditions and pediatric hip disorders across Cairo.",
  },
  {
    title: "Advanced Arthroscopic Surgery",
    description:
      "Minimally invasive joint procedures enabling faster recovery for sports and trauma patients.",
  },
  {
    title: "Comprehensive Bone Care",
    description:
      "Full-spectrum orthopedic services from diagnosis through surgery and long-term follow-up.",
  },
  {
    title: "Trusted Community Practice",
    description:
      "A established Heliopolis clinic known for attentive care and clear patient communication.",
  },
];

export const CONTACT: ContactInfo = {
  clinicName: "Dr. Mina Merzek Clinic",
  address: "70 Abdel Aziz Fahmy Street, Saint Fatima Square",
  city: "Heliopolis, Cairo, Egypt",
  phone: "+201221926646",
  phoneDisplay: "+20 122 192 6646",
  whatsapp: "201221926646",
  whatsappDisplay: "+20 122 192 6646",
  whatsappContactName: "Mina Clinic",
  email: "info@drminamerzek.clinic",
  hours: [
    { days: "Saturday – Thursday", time: "10:00 AM – 8:00 PM" },
    { days: "Friday", time: "Closed" },
    { days: "Emergency", time: "Contact via WhatsApp" },
  ],
};
