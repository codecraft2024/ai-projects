"use client";

import { useTranslations } from "next-intl";
import { AdminAuthGuard } from "@/components/admin/AdminAuthGuard";
import { AdminShell } from "@/components/admin/AdminShell";
import { PatientSearchPage } from "@/components/admin/patients/PatientSearchPage";

export default function PatientsPage() {
  const t = useTranslations("admin.patients");

  return (
    <AdminAuthGuard>
      <AdminShell title={t("title")} subtitle={t("subtitle")} activeNav="patients">
        <PatientSearchPage />
      </AdminShell>
    </AdminAuthGuard>
  );
}
