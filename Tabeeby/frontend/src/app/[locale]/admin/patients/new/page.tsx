"use client";

import { useTranslations } from "next-intl";
import { useRouter } from "@/i18n/navigation";
import { AdminAuthGuard } from "@/components/admin/AdminAuthGuard";
import { AdminShell } from "@/components/admin/AdminShell";
import { PatientForm } from "@/components/admin/patients/PatientForm";
import { createPatient } from "@/services/patient.service";
import { emptyPatientForm } from "@/types/patient";

export default function NewPatientPage() {
  const t = useTranslations("admin.patients");
  const router = useRouter();

  return (
    <AdminAuthGuard>
      <AdminShell title={t("createTitle")} subtitle={t("createSubtitle")} activeNav="patients">
        <PatientForm
          initial={emptyPatientForm()}
          submitLabel={t("createPatient")}
          loadingLabel={t("creating")}
          onSubmit={async (form) => {
            const patient = await createPatient(form);
            router.push(`/admin/patients/${patient.id}`);
          }}
        />
      </AdminShell>
    </AdminAuthGuard>
  );
}
