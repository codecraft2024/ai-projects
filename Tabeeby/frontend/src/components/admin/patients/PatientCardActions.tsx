"use client";

import { useTranslations } from "next-intl";
import { Link } from "@/i18n/navigation";
import { cn } from "@/utils/cn";

type PatientCardActionsProps = {
  patientId: number;
  layout?: "grid" | "row";
  className?: string;
};

export function PatientCardActions({
  patientId,
  layout = "grid",
  className,
}: PatientCardActionsProps) {
  const t = useTranslations("admin.patients.actions");

  const base =
    "inline-flex min-h-[40px] items-center justify-center rounded-xl px-2.5 py-2 text-xs font-semibold transition focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-brand/30 sm:min-h-[44px] sm:px-3 sm:text-sm";

  return (
    <div
      className={cn(
        layout === "grid" ? "grid grid-cols-3 gap-2" : "flex flex-wrap gap-2",
        className,
      )}
    >
      <Link
        href={`/admin/patients/${patientId}?edit=1`}
        className={cn(base, "border-2 border-brand bg-white text-brand hover:bg-brand-soft")}
      >
        {t("edit")}
      </Link>
      <Link
        href={`/admin/patients/${patientId}?tab=files`}
        className={cn(base, "bg-brand text-white shadow-sm hover:bg-brand-dark")}
      >
        {t("upload")}
      </Link>
      <Link
        href={`/admin/patients/${patientId}?tab=files&scan=1`}
        className={cn(base, "border-2 border-slate-300 bg-white text-slate-800 hover:bg-slate-50")}
      >
        {t("scan")}
      </Link>
    </div>
  );
}
