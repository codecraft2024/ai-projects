"use client";

import { useTranslations } from "next-intl";
import dynamic from "next/dynamic";
import { AdminAuthGuard } from "@/components/admin/AdminAuthGuard";
import { AdminShell } from "@/components/admin/AdminShell";
import { Button } from "@/components/ui/Button";
import { DashboardStatsGrid } from "@/components/admin/DashboardPanels";

const AppointmentsPanel = dynamic(
  () => import("@/components/admin/DashboardPanels").then((m) => ({ default: m.AppointmentsPanel })),
  { loading: () => <PanelSkeleton /> },
);
const RecentActivitiesPanel = dynamic(
  () => import("@/components/admin/DashboardPanels").then((m) => ({ default: m.RecentActivitiesPanel })),
  { loading: () => <PanelSkeleton /> },
);

function PanelSkeleton() {
  return <div className="h-48 animate-pulse rounded-2xl border border-[var(--border)] bg-white" />;
}

export default function AdminDashboardPage() {
  const t = useTranslations("admin");

  return (
    <AdminAuthGuard>
      <AdminShell title={t("dashboard")} subtitle={t("dashboardSubtitle")} activeNav="dashboard">
        <div className="space-y-6">
          <div className="flex flex-col gap-4 rounded-2xl border border-brand/20 bg-brand-soft p-4 sm:flex-row sm:items-center sm:justify-between sm:p-5">
            <div>
              <h2 className="text-base font-semibold text-slate-900 sm:text-lg">{t("patientsHubTitle")}</h2>
              <p className="mt-1 text-sm text-slate-600">{t("patientsHubDesc")}</p>
            </div>
            <Button href="/admin/patients" variant="primary" className="w-full sm:w-auto">
              {t("openPatients")}
            </Button>
          </div>
          <DashboardStatsGrid />
          <div className="grid gap-6 xl:grid-cols-2">
            <AppointmentsPanel />
            <RecentActivitiesPanel />
          </div>
        </div>
      </AdminShell>
    </AdminAuthGuard>
  );
}
