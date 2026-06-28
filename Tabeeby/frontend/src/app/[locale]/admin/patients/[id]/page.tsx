"use client";

import { use } from "react";
import { useTranslations } from "next-intl";
import { AdminAuthGuard } from "@/components/admin/AdminAuthGuard";
import { AdminShell } from "@/components/admin/AdminShell";
import { PatientDetailView } from "@/components/admin/patients/PatientDetailView";

type Props = { params: Promise<{ id: string }> };

export default function PatientDetailPage({ params }: Props) {
  const { id } = use(params);
  const t = useTranslations("admin.patients");
  const patientId = Number(id);

  if (Number.isNaN(patientId)) {
    return (
      <AdminAuthGuard>
        <AdminShell title={t("title")} activeNav="patients">
          <p className="text-red-600">{t("invalidId")}</p>
        </AdminShell>
      </AdminAuthGuard>
    );
  }

  return (
    <AdminAuthGuard>
      <AdminShell title={t("detailTitle")} activeNav="patients">
        <PatientDetailView patientId={patientId} />
      </AdminShell>
    </AdminAuthGuard>
  );
}
