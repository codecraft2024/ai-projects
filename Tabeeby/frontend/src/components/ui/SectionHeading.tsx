import { cn } from "@/utils/cn";

type SectionHeadingProps = {
  eyebrow?: string;
  title: string;
  description?: string;
  align?: "left" | "center";
  light?: boolean;
};

export function SectionHeading({
  eyebrow,
  title,
  description,
  align = "center",
  light = false,
}: SectionHeadingProps) {
  return (
    <div className={cn("max-w-3xl", align === "center" ? "mx-auto text-center" : "text-start")}>
      {eyebrow && (
        <p
          className={cn(
            "mb-2 text-sm font-semibold uppercase tracking-wider",
            light ? "text-accent" : "text-brand",
          )}
        >
          {eyebrow}
        </p>
      )}
      <h2
        className={cn(
          "text-2xl font-bold tracking-tight sm:text-3xl lg:text-4xl",
          light ? "text-white" : "text-slate-900",
        )}
      >
        {title}
      </h2>
      {description && (
        <p
          className={cn(
            "mt-4 text-base leading-relaxed sm:text-lg",
            light ? "text-white/85" : "text-slate-600",
          )}
        >
          {description}
        </p>
      )}
    </div>
  );
}
