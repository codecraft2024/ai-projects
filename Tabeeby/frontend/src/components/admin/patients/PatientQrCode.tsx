"use client";

import { useLocale, useTranslations } from "next-intl";
import QRCode from "react-qr-code";

type PatientQrCodeProps = {
  patientId: number;
  medicalRecordNumber: string | null;
};

export function PatientQrCode({ patientId, medicalRecordNumber }: PatientQrCodeProps) {
  const t = useTranslations("admin.patients");
  const locale = useLocale();

  const payload =
    typeof window !== "undefined"
      ? `${window.location.origin}/${locale}/admin/patients/${patientId}`
      : `tabeeby:patient:${patientId}`;

  const displayId = medicalRecordNumber ?? `#${patientId}`;

  return (
    <section className="rounded-2xl border border-[var(--border)] bg-white p-4 shadow-sm sm:p-6">
      <h3 className="text-sm font-semibold uppercase tracking-wide text-slate-500">{t("qrTitle")}</h3>
      <p className="mt-1 text-xs text-slate-500">{t("qrHint")}</p>
      <div className="mt-4 flex flex-col items-center gap-3 sm:flex-row sm:items-start sm:gap-6">
        <div className="rounded-xl border border-slate-100 bg-white p-3 shadow-sm">
          <QRCode value={payload} size={140} level="M" />
        </div>
        <div className="text-center sm:text-start">
          <p className="font-mono text-lg font-bold text-slate-900">{displayId}</p>
          <p className="mt-1 text-sm text-slate-600">
            {t("qrPatientId")}: {patientId}
          </p>
        </div>
      </div>
    </section>
  );
}
