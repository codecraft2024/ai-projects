import Image from "next/image";
import type { GalleryItem } from "@/types/social";
import { cn } from "@/utils/cn";

type GalleryCardProps = {
  item: GalleryItem;
  className?: string;
};

export function GalleryCard({ item, className }: GalleryCardProps) {
  return (
    <article
      className={cn(
        "group overflow-hidden rounded-2xl border border-slate-200 bg-white shadow-sm transition hover:shadow-md",
        className,
      )}
    >
      <div className="relative aspect-[4/3] overflow-hidden bg-gradient-to-br from-slate-100 to-blue-50">
        <Image
          src={item.imageSrc}
          alt={item.imageAlt}
          fill
          className="object-cover transition duration-300 group-hover:scale-[1.02]"
          sizes="(max-width: 640px) 100vw, (max-width: 1024px) 50vw, 33vw"
        />
      </div>
      <div className="p-4 sm:p-5">
        <time className="text-xs font-medium text-blue-700">{item.date}</time>
        <h3 className="mt-1 text-base font-semibold text-slate-900 sm:text-lg">
          {item.title}
        </h3>
        <p className="mt-2 text-sm leading-relaxed text-slate-600">{item.caption}</p>
      </div>
    </article>
  );
}
