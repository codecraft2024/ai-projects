import type { MetadataRoute } from "next";
import { OG_DEFAULT } from "@/config/social-links";

export default function manifest(): MetadataRoute.Manifest {
  return {
    name: OG_DEFAULT.siteName,
    short_name: "Tabeeby",
    description: OG_DEFAULT.description,
    start_url: "/en",
    display: "standalone",
    background_color: "#ffffff",
    theme_color: "#4B248B",
    lang: "en",
    icons: [
      {
        src: "/tabeeby-icon.png",
        sizes: "512x512",
        type: "image/png",
      },
    ],
  };
}
