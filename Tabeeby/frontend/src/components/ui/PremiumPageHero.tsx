import { Container } from "@/components/ui/Container";
import { cn } from "@/utils/cn";

type PremiumPageHeroProps = {
  eyebrow: string;
  title: string;
  description?: string;
  variant?: "doctor" | "cases";
};

export function PremiumPageHero({
  eyebrow,
  title,
  description,
  variant = "doctor",
}: PremiumPageHeroProps) {
  return (
    <section
      className={cn(
        "relative overflow-hidden py-12 text-white sm:py-16",
        variant === "doctor"
          ? "bg-gradient-to-br from-[#1a1625] via-[#2d1f6b] to-brand"
          : "bg-gradient-to-br from-brand via-[#5a3de6] to-[#1a1625]",
      )}
    >
      <div
        className="pointer-events-none absolute inset-0 opacity-35"
        aria-hidden
        style={{
          backgroundImage:
            variant === "cases"
              ? "radial-gradient(circle at 15% 85%, #FFC107 0%, transparent 45%)"
              : "radial-gradient(circle at 85% 15%, #FFC107 0%, transparent 40%)",
        }}
      />
      <Container className="relative">
        <p className="text-sm font-semibold uppercase tracking-widest text-accent">{eyebrow}</p>
        <h1 className="mt-3 max-w-3xl text-3xl font-bold tracking-tight sm:text-4xl lg:text-5xl">
          {title}
        </h1>
        {description && (
          <p className="mt-4 max-w-2xl text-base leading-relaxed text-white/90 sm:text-lg">
            {description}
          </p>
        )}
        <div
          className={cn(
            "mt-6 h-1 w-16 rounded-full",
            variant === "cases" ? "bg-accent" : "bg-white/80",
          )}
          aria-hidden
        />
      </Container>
    </section>
  );
}
