export type SocialPlatformId = "facebook" | "instagram" | "twitter" | "whatsapp";

export type ShareContent = {
  url: string;
  title: string;
  description: string;
};

export type GalleryItem = {
  id: string;
  title: string;
  caption: string;
  category: "update" | "case" | "gallery";
  date: string;
  /** Path under /public or external URL when available */
  imageSrc: string;
  imageAlt: string;
};

export type ClinicUpdate = GalleryItem & { category: "update" };
export type CaseHighlight = GalleryItem & { category: "case" };
