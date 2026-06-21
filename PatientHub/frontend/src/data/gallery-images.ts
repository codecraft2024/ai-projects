/** Real clinic media sourced from minaeskarous.com and official social profiles. */

export const DOCTOR_PORTRAIT = "/gallery/doctor/dr-mina-portrait.jpg";

export const GALLERY_UPDATE_IMAGES = {
  update1: DOCTOR_PORTRAIT,
  update2: "/gallery/cases/case-pediatric-1.jpg",
  update3: "/gallery/cases/case-surgery-1.jpg",
} as const;

export const GALLERY_CASE_IMAGES = {
  case1: "/gallery/cases/case-hip-dislocation.png",
  case2: "/gallery/cases/case-clubfoot-1.jpg",
  case3: "/gallery/cases/case-surgery-1.jpg",
  case4: "/gallery/cases/case-pediatric-1.jpg",
  case5: "/gallery/cases/case-pediatric-2.jpg",
  case6: "/gallery/clinic/clinic-about.jpg",
} as const;

export const GALLERY_CLINIC_IMAGES = {
  gal1: "/gallery/clinic/clinic-about.jpg",
  gal2: DOCTOR_PORTRAIT,
  gal3: "/gallery/cases/case-surgery-1.jpg",
  gal4: "/gallery/doctor/dr-mina-clinic-page.jpg",
} as const;
