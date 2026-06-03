"use client";

import { useEffect } from "react";
import { useTranslations } from "next-intl";
import { useRouter } from "@/i18n/navigation";
import dynamic from "next/dynamic";
import { useAuth } from "@/hooks/useAuth";
import { AdminShell } from "@/components/admin/AdminShell";
import { DashboardStatsGrid } from "@/components/admin/DashboardPanels";

const PatientSearchPanel = dynamic(
  () => import("@/components/admin/DashboardPanels").then((m) => ({ default: m.PatientSearchPanel })),
  { loading: () => <PanelSkeleton /> },
);
const PatientsListPanel = dynamic(
  () => import("@/components/admin/DashboardPanels").then((m) => ({ default: m.PatientsListPanel })),
  { loading: () => <PanelSkeleton /> },
);
const AppointmentsPanel = dynamic(
  () => import("@/components/admin/DashboardPanels").then((m) => ({ default: m.AppointmentsPanel })),
  { loading: () => <PanelSkeleton /> },
);
const PatientHistoryPanel = dynamic(
  () => import("@/components/admin/DashboardPanels").then((m) => ({ default: m.PatientHistoryPanel })),
  { loading: () => <PanelSkeleton /> },
);
const MedicalRecordsPanel = dynamic(
  () => import("@/components/admin/DashboardPanels").then((m) => ({ default: m.MedicalRecordsPanel })),
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
  const { isAuthenticated } = useAuth();
  const router = useRouter();

  useEffect(() => {
    if (!isAuthenticated) {
      router.replace("/admin/login");
    }
  }, [isAuthenticated, router]);

  if (!isAuthenticated) {
    return (
      <div className="flex min-h-screen items-center justify-center bg-muted">
        <p className="text-slate-600">{t("loading")}</p>
      </div>
    );
  }

  return (
    <AdminShell title={t("dashboard")} subtitle={t("dashboardSubtitle")}>
      <div className="space-y-6">
        <DashboardStatsGrid />
        <PatientSearchPanel />
        <div className="grid gap-6 xl:grid-cols-2">
          <PatientsListPanel />
          <AppointmentsPanel />
        </div>
        <div className="grid gap-6 xl:grid-cols-2">
          <PatientHistoryPanel />
          <MedicalRecordsPanel />
        </div>
        <RecentActivitiesPanel />
      </div>
    </AdminShell>
  );
}
