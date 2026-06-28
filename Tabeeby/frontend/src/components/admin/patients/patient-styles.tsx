import type { CaseStatus } from "@/types/patient";
import { cn } from "@/utils/cn";

export const statusStyles: Record<CaseStatus, string> = {
  ACTIVE: "bg-green-100 text-green-800",
  FOLLOW_UP: "bg-amber-100 text-amber-800",
  DISCHARGED: "bg-slate-100 text-slate-600",
  UNDER_TREATMENT: "bg-blue-100 text-blue-800",
  SCHEDULED_SURGERY: "bg-purple-100 text-purple-800",
};

export function StatusBadge({
  status,
  label,
}: {
  status: CaseStatus;
  label: string;
}) {
  return (
    <span
      className={cn(
        "inline-block rounded-full px-2.5 py-0.5 text-xs font-medium whitespace-nowrap",
        statusStyles[status],
      )}
    >
      {label}
    </span>
  );
}

export const inputClassName =
  "w-full min-h-[44px] rounded-lg border border-[var(--border)] px-3 py-2.5 text-base focus:border-brand focus:outline-none focus:ring-2 focus:ring-brand/20 sm:py-2 sm:text-sm";

export const labelClassName = "mb-1.5 block text-sm font-medium text-slate-700";

export const sectionClassName =
  "rounded-2xl border border-[var(--border)] bg-white p-4 shadow-sm sm:p-6";
