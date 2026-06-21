import type { CaseHighlight, ClinicUpdate, GalleryItem } from "@/types/social";

/**
 * Gallery & social content placeholders.
 * Replace imageSrc with real assets from the clinic Facebook page when available.
 * Recommended: add images to /public/gallery/ and update paths below.
 */

export const CLINIC_UPDATES: ClinicUpdate[] = [
  {
    id: "update-1",
    category: "update",
    title: "Welcome to Tabeeby",
    caption:
      "Our new digital portal makes it easier to book visits and stay connected with the clinic.",
    date: "2026-06-01",
    imageSrc: "/gallery/placeholder.svg",
    imageAlt: "Tabeeby launch announcement",
  },
  {
    id: "update-2",
    category: "update",
    title: "Pediatric Orthopedic Care",
    caption:
      "Specialized care for children with bone and joint conditions — compassionate, expert treatment.",
    date: "2026-05-15",
    imageSrc: "/gallery/placeholder.svg",
    imageAlt: "Pediatric orthopedic care at the clinic",
  },
  {
    id: "update-3",
    category: "update",
    title: "Sports Injury & Arthroscopy",
    caption:
      "Minimally invasive arthroscopic procedures for faster recovery from athletic injuries.",
    date: "2026-05-01",
    imageSrc: "/gallery/placeholder.svg",
    imageAlt: "Sports injury and arthroscopy services",
  },
];

export const CASE_HIGHLIGHTS: CaseHighlight[] = [
  {
    id: "case-1",
    category: "case",
    title: "Pediatric Hip Dysplasia",
    caption:
      "Successful management of developmental hip dysplasia with a tailored pediatric treatment plan.",
    date: "2026-04",
    imageSrc: "/gallery/placeholder.svg",
    imageAlt: "Pediatric hip dysplasia case highlight",
  },
  {
    id: "case-2",
    category: "case",
    title: "Knee Arthroscopy Recovery",
    caption:
      "ACL reconstruction and rehabilitation — restoring mobility for an active patient.",
    date: "2026-03",
    imageSrc: "/gallery/placeholder.svg",
    imageAlt: "Knee arthroscopy recovery case",
  },
  {
    id: "case-3",
    category: "case",
    title: "Joint Replacement Outcome",
    caption:
      "Total knee replacement with excellent post-operative function and pain relief.",
    date: "2026-02",
    imageSrc: "/gallery/placeholder.svg",
    imageAlt: "Joint replacement outcome case",
  },
];

export const CLINIC_GALLERY: GalleryItem[] = [
  {
    id: "gal-1",
    category: "gallery",
    title: "Bone Clinic — Heliopolis",
    caption: "Dr. Mina Merzek Clinic, Saint Fatima Square, Heliopolis, Cairo.",
    date: "2026",
    imageSrc: "/gallery/placeholder.svg",
    imageAlt: "Clinic exterior placeholder",
  },
  {
    id: "gal-2",
    category: "gallery",
    title: "Consultation Room",
    caption: "Private, comfortable consultations with clear treatment planning.",
    date: "2026",
    imageSrc: "/gallery/placeholder.svg",
    imageAlt: "Consultation room placeholder",
  },
  {
    id: "gal-3",
    category: "gallery",
    title: "Orthopedic Excellence",
    caption: "Modern facilities supporting advanced bone and joint care.",
    date: "2026",
    imageSrc: "/gallery/placeholder.svg",
    imageAlt: "Clinic facility placeholder",
  },
  {
    id: "gal-4",
    category: "gallery",
    title: "Patient-Centered Care",
    caption: "Every visit focused on your recovery, comfort, and long-term health.",
    date: "2026",
    imageSrc: "/gallery/placeholder.svg",
    imageAlt: "Patient care placeholder",
  },
];

export const ALL_CASES: CaseHighlight[] = [
  ...CASE_HIGHLIGHTS,
  {
    id: "case-4",
    category: "case",
    title: "Ankle Fracture Fixation",
    caption: "Stable fracture fixation with structured rehabilitation protocol.",
    date: "2026-01",
    imageSrc: "/gallery/placeholder.svg",
    imageAlt: "Ankle fracture case",
  },
  {
    id: "case-5",
    category: "case",
    title: "Congenital Foot Correction",
    caption: "Corrective pediatric procedure with ongoing growth monitoring.",
    date: "2025-12",
    imageSrc: "/gallery/placeholder.svg",
    imageAlt: "Foot correction case",
  },
  {
    id: "case-6",
    category: "case",
    title: "Shoulder Arthroscopy",
    caption: "Minimally invasive shoulder repair for rotator cuff injury.",
    date: "2025-11",
    imageSrc: "/gallery/placeholder.svg",
    imageAlt: "Shoulder arthroscopy case",
  },
];
