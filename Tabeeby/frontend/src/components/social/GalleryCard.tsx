import Image from "next/image";
import type { GalleryItem } from "@/types/social";
import { cn } from "@/utils/cn";

type GalleryCardProps = {
  item: GalleryItem;
  className?: string;
  variant?: "default" | "cases";
};

export function GalleryCard({ item, className, variant = "default" }: GalleryCardProps) {
  const isCases = variant === "cases";

  return (
    <article
      className={cn(
        "group overflow-hidden rounded-2xl border bg-white shadow-sm transition hover:shadow-brand",
        isCases ? "border-brand/15 hover:border-brand/30" : "border-slate-200 hover:shadow-md",
        className,
      )}
    >
      <div
        className={cn(
          "relative aspect-[4/3] overflow-hidden",
          isCases
            ? "bg-gradient-to-br from-brand-soft to-accent-soft/40"
            : "bg-gradient-to-br from-slate-100 to-blue-50",
        )}
      >
        <Image
          src={item.imageSrc}
          alt={item.imageAlt}
          fill
          className="object-cover transition duration-300 group-hover:scale-[1.02]"
          sizes="(max-width: 640px) 100vw, (max-width: 1024px) 50vw, 33vw"
        />
        {isCases && (
          <span className="absolute start-3 top-3 rounded-full bg-accent px-2.5 py-0.5 text-xs font-bold text-[#1a1625]">
            {item.date}
          </span>
        )}
      </div>
      <div className="p-4 sm:p-5">
        {!isCases && (
          <time className="text-xs font-medium text-blue-700">{item.date}</time>
        )}
        <h3
          className={cn(
            "text-base font-semibold text-slate-900 sm:text-lg",
            isCases ? "" : "mt-1",
          )}
        >
          {item.title}
        </h3>
        <p className="mt-2 text-sm leading-relaxed text-slate-600">{item.caption}</p>
      </div>
    </article>
  );
}
