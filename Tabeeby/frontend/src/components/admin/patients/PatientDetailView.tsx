"use client";

import { useCallback, useEffect, useMemo, useRef, useState } from "react";
import { useTranslations } from "next-intl";
import { useSearchParams } from "next/navigation";
import { Link, useRouter } from "@/i18n/navigation";
import { Button } from "@/components/ui/Button";
import { ApiError } from "@/services/api.client";
import { PatientForm } from "@/components/admin/patients/PatientForm";
import { PatientQrCode } from "@/components/admin/patients/PatientQrCode";
import {
  StatusBadge,
  inputClassName,
  labelClassName,
  sectionClassName,
} from "@/components/admin/patients/patient-styles";
import {
  addMedicalNote,
  deletePatientFile,
  getFileDownloadHref,
  getPatient,
  updatePatient,
  updatePatientStatus,
  uploadPatientFile,
} from "@/services/patient.service";
import type { CaseStatus, MedicalFileType, PatientDetail } from "@/types/patient";
import { CASE_STATUSES, MEDICAL_FILE_TYPES, patientToForm } from "@/types/patient";
import { cn } from "@/utils/cn";

type Tab = "profile" | "files" | "visits" | "notes";

export function PatientDetailView({ patientId }: { patientId: number }) {
  const t = useTranslations("admin.patients");
  const router = useRouter();
  const searchParams = useSearchParams();
  const initialTab = (searchParams.get("tab") as Tab) || "profile";
  const startEditing = searchParams.get("edit") === "1";
  const startScan = searchParams.get("scan") === "1";

  const scanInputRef = useRef<HTMLInputElement>(null);

  const [patient, setPatient] = useState<PatientDetail | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [tab, setTab] = useState<Tab>(initialTab);
  const [editing, setEditing] = useState(startEditing);
  const [noteText, setNoteText] = useState("");
  const [noteSaving, setNoteSaving] = useState(false);
  const [uploadType, setUploadType] = useState<MedicalFileType>("RADIOLOGY");
  const [uploading, setUploading] = useState(false);
  const [uploadError, setUploadError] = useState<string | null>(null);
  const [uploadSuccess, setUploadSuccess] = useState(false);
  const scanOpenedRef = useRef(false);

  const load = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      setPatient(await getPatient(patientId));
    } catch (err) {
      setError(err instanceof Error ? err.message : t("loadError"));
    } finally {
      setLoading(false);
    }
  }, [patientId, t]);

  useEffect(() => {
    load();
  }, [load]);

  useEffect(() => {
    if (startScan) {
      setTab("files");
    }
  }, [startScan]);

  useEffect(() => {
    if (tab === "files" && startScan && scanInputRef.current && !scanOpenedRef.current) {
      scanOpenedRef.current = true;
      scanInputRef.current.click();
    }
  }, [tab, startScan]);

  const handleUpdate = async (form: ReturnType<typeof patientToForm>) => {
    const updated = await updatePatient(patientId, form);
    setPatient(updated);
    setEditing(false);
  };

  const handleStatusChange = async (caseStatus: CaseStatus) => {
    const updated = await updatePatientStatus(patientId, caseStatus);
    setPatient(updated);
  };

  const handleAddNote = async () => {
    if (!noteText.trim()) return;
    setNoteSaving(true);
    try {
      await addMedicalNote(patientId, noteText.trim());
      setNoteText("");
      await load();
      setTab("notes");
    } finally {
      setNoteSaving(false);
    }
  };

  const handleUpload = async (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (!file) return;
    setUploading(true);
    setUploadError(null);
    setUploadSuccess(false);
    try {
      const uploaded = await uploadPatientFile(patientId, uploadType, file);
      setPatient((prev) =>
        prev ? { ...prev, files: [uploaded, ...prev.files] } : prev,
      );
      setUploadSuccess(true);
      setTab("files");
      if (startScan) {
        router.replace(`/admin/patients/${patientId}?tab=files`, { scroll: false });
      }
    } catch (err) {
      if (err instanceof ApiError && err.status === 401) {
        setUploadError(t("sessionExpired"));
      } else {
        setUploadError(
          err instanceof ApiError ? err.message : err instanceof Error ? err.message : t("uploadError"),
        );
      }
    } finally {
      setUploading(false);
      e.target.value = "";
    }
  };

  const handleDeleteFile = async (fileId: number) => {
    if (!confirm(t("confirmDeleteFile"))) return;
    await deletePatientFile(patientId, fileId);
    await load();
  };

  const tabs: { id: Tab; label: string }[] = useMemo(
    () => [
      { id: "profile", label: t("tabs.profile") },
      { id: "files", label: t("tabs.files") },
      { id: "visits", label: t("tabs.visits") },
      { id: "notes", label: t("tabs.notes") },
    ],
    [t],
  );

  if (loading) {
    return (
      <div className="space-y-4">
        <div className="h-10 w-64 animate-pulse rounded-lg bg-slate-100" />
        <div className="h-64 animate-pulse rounded-2xl bg-slate-100" />
      </div>
    );
  }

  if (error || !patient) {
    return (
      <div className="rounded-lg border border-red-200 bg-red-50 px-4 py-6 text-red-700">
        <p>{error ?? t("notFound")}</p>
        <Button href="/admin/patients" variant="outline" className="mt-4">
          {t("backToList")}
        </Button>
      </div>
    );
  }

  return (
    <div className="space-y-4 sm:space-y-6">
      <div className="flex flex-col gap-4">
        <div>
          <Link
            href="/admin/patients"
            className="text-sm font-medium text-brand hover:underline"
          >
            ← {t("backToList")}
          </Link>
          <h2 className="mt-2 text-xl font-bold text-slate-900 sm:text-2xl">{patient.fullName}</h2>
          <p className="mt-1 font-mono text-xs text-slate-500 sm:text-sm">{patient.medicalRecordNumber}</p>
          <div className="mt-3 flex flex-wrap items-center gap-2 sm:gap-3">
            <StatusBadge status={patient.caseStatus} label={t(`status.${patient.caseStatus}`)} />
            <span className="text-xs text-slate-500 sm:text-sm">
              {t("lastVisitLabel")}: {patient.lastVisitDate ?? "—"}
            </span>
          </div>
        </div>
        <div className="grid gap-2 sm:flex sm:flex-wrap">
          <select
            value={patient.caseStatus}
            onChange={(e) => handleStatusChange(e.target.value as CaseStatus)}
            className={cn(inputClassName, "w-full sm:w-auto sm:min-w-[10rem]")}
            aria-label={t("form.caseStatus")}
          >
            {CASE_STATUSES.map((s) => (
              <option key={s} value={s}>
                {t(`status.${s}`)}
              </option>
            ))}
          </select>
          <Button
            type="button"
            variant={editing ? "outline" : "primary"}
            className="w-full sm:w-auto"
            onClick={() => setEditing((v) => !v)}
          >
            {editing ? t("cancelEdit") : t("editProfile")}
          </Button>
        </div>
      </div>

      <div className="-mx-3 flex gap-1 overflow-x-auto border-b border-slate-200 px-3 pb-0 sm:mx-0 sm:gap-2 sm:px-0">
        {tabs.map((item) => (
          <button
            key={item.id}
            type="button"
            onClick={() => setTab(item.id)}
            className={cn(
              "shrink-0 rounded-t-lg px-3 py-2.5 text-sm font-medium transition sm:px-4",
              tab === item.id
                ? "bg-white text-brand shadow-sm ring-1 ring-[var(--border)]"
                : "text-slate-600 hover:bg-white/70",
            )}
          >
            {item.label}
          </button>
        ))}
      </div>

      {tab === "profile" && (
        <>
          <PatientQrCode
            patientId={patient.id}
            medicalRecordNumber={patient.medicalRecordNumber}
          />
          {editing ? (
            <PatientForm
              initial={patientToForm(patient)}
              onSubmit={handleUpdate}
              submitLabel={t("saveChanges")}
              loadingLabel={t("saving")}
            />
          ) : (
            <ProfileView patient={patient} t={t} />
          )}
        </>
      )}

      {tab === "files" && (
        <section className={sectionClassName}>
          <h3 className="mb-4 text-lg font-semibold text-slate-900">{t("filesTitle")}</h3>
          <div className="mb-6 flex flex-col gap-3 rounded-xl border border-dashed border-slate-200 bg-slate-50/50 p-4 sm:flex-row sm:items-end">
            <div className="flex-1">
              <label className={labelClassName}>{t("fileType")}</label>
              <select
                value={uploadType}
                onChange={(e) => setUploadType(e.target.value as MedicalFileType)}
                className={inputClassName}
              >
                {MEDICAL_FILE_TYPES.map((type) => (
                  <option key={type} value={type}>
                    {t(`fileTypes.${type}`)}
                  </option>
                ))}
              </select>
            </div>
            <div className="flex-1">
              <label className={labelClassName}>{t("chooseFile")}</label>
              <input
                type="file"
                accept="image/*,.pdf,.doc,.docx"
                disabled={uploading}
                onChange={handleUpload}
                className="block w-full text-sm text-slate-600 file:me-3 file:rounded-lg file:border-0 file:bg-brand file:px-4 file:py-2 file:text-sm file:font-semibold file:text-white hover:file:bg-brand-dark"
              />
            </div>
            <div className="flex-1">
              <label className={labelClassName}>{t("scanDocument")}</label>
              <input
                ref={scanInputRef}
                type="file"
                accept="image/*"
                capture="environment"
                disabled={uploading}
                onChange={handleUpload}
                className="block w-full text-sm text-slate-600 file:me-3 file:rounded-lg file:border-0 file:bg-slate-800 file:px-4 file:py-2 file:text-sm file:font-semibold file:text-white hover:file:bg-slate-700"
              />
            </div>
          </div>
          {uploadError && <p className="mb-4 text-sm text-red-600">{uploadError}</p>}
          {uploadSuccess && (
            <p className="mb-4 text-sm text-green-700">{t("uploadSuccess")}</p>
          )}
          {uploading && <p className="mb-4 text-sm text-slate-500">{t("uploading")}</p>}
          {patient.files.length === 0 ? (
            <p className="text-sm text-slate-500">{t("noFiles")}</p>
          ) : (
            <ul className="divide-y divide-slate-100">
              {patient.files.map((file) => (
                <li key={file.id} className="flex flex-col gap-2 py-4 sm:flex-row sm:items-center sm:justify-between">
                  <div>
                    <p className="font-medium text-slate-900">{file.originalFilename}</p>
                    <p className="text-sm text-slate-500">
                      {t(`fileTypes.${file.fileType}`)} · {formatSize(file.fileSize)} ·{" "}
                      {new Date(file.uploadedAt).toLocaleDateString()} · {file.uploadedBy}
                    </p>
                  </div>
                  <div className="flex flex-col gap-2 sm:flex-row">
                    <a
                      href={getFileDownloadHref(file.downloadUrl)}
                      target="_blank"
                      rel="noopener noreferrer"
                      className="inline-flex min-h-[44px] items-center justify-center rounded-lg border border-[var(--border)] px-3 py-2 text-sm font-medium text-slate-700 hover:bg-slate-50"
                    >
                      {t("download")}
                    </a>
                    <button
                      type="button"
                      onClick={() => handleDeleteFile(file.id)}
                      className="inline-flex min-h-[44px] items-center justify-center rounded-lg px-3 py-2 text-sm font-medium text-red-600 hover:bg-red-50"
                    >
                      {t("delete")}
                    </button>
                  </div>
                </li>
              ))}
            </ul>
          )}
        </section>
      )}

      {tab === "visits" && (
        <section className={sectionClassName}>
          <h3 className="mb-4 text-lg font-semibold text-slate-900">{t("visitsTitle")}</h3>
          {patient.visits.length === 0 ? (
            <p className="text-sm text-slate-500">{t("noVisits")}</p>
          ) : (
            <ul className="space-y-4">
              {patient.visits.map((visit) => (
                <li key={visit.id} className="rounded-xl border border-slate-100 bg-slate-50/50 p-4">
                  <div className="flex flex-wrap justify-between gap-2">
                    <p className="font-medium text-slate-900">{visit.visitDate}</p>
                    <span className="text-xs text-slate-500">{visit.createdBy}</span>
                  </div>
                  {visit.diagnosis && (
                    <p className="mt-2 text-sm">
                      <span className="font-medium">{t("form.diagnosis")}:</span> {visit.diagnosis}
                    </p>
                  )}
                  {visit.notes && <p className="mt-2 text-sm text-slate-600">{visit.notes}</p>}
                </li>
              ))}
            </ul>
          )}
        </section>
      )}

      {tab === "notes" && (
        <section className={sectionClassName}>
          <h3 className="mb-4 text-lg font-semibold text-slate-900">{t("notesTitle")}</h3>
          <div className="mb-6 space-y-3">
            <textarea
              rows={3}
              value={noteText}
              onChange={(e) => setNoteText(e.target.value)}
              className={inputClassName}
              placeholder={t("notePlaceholder")}
            />
            <Button
              type="button"
              variant="primary"
              className="w-full sm:w-auto"
              disabled={noteSaving || !noteText.trim()}
              onClick={handleAddNote}
            >
              {noteSaving ? t("saving") : t("addNote")}
            </Button>
          </div>
          {patient.medicalNotes.length === 0 ? (
            <p className="text-sm text-slate-500">{t("noNotes")}</p>
          ) : (
            <ul className="space-y-3">
              {patient.medicalNotes.map((note) => (
                <li key={note.id} className="rounded-xl border border-slate-100 p-4">
                  <p className="text-sm text-slate-800">{note.content}</p>
                  <p className="mt-2 text-xs text-slate-400">
                    {note.createdBy} · {new Date(note.createdAt).toLocaleString()}
                  </p>
                </li>
              ))}
            </ul>
          )}
        </section>
      )}
    </div>
  );
}

function ProfileView({
  patient,
  t,
}: {
  patient: PatientDetail;
  t: ReturnType<typeof useTranslations<"admin.patients">>;
}) {
  const rows: [string, string | null | number][] = [
    [t("form.fullName"), patient.fullName],
    [t("form.gender"), t(`gender.${patient.gender}`)],
    [t("form.dateOfBirth"), patient.dateOfBirth],
    [t("form.age"), patient.age],
    [t("form.mobile"), patient.mobileNumber],
    [t("form.address"), patient.address],
    [t("form.emergencyContact"), patient.emergencyContact],
    [t("form.diagnosis"), patient.diagnosis],
    [t("form.surgeryType"), patient.surgeryType],
    [t("form.medicalHistory"), patient.medicalHistory],
    [t("form.medications"), patient.currentMedications],
    [t("form.allergies"), patient.allergies],
    [t("form.surgeryDetails"), patient.surgeryDetails],
    [t("form.notes"), patient.notes],
  ];

  return (
    <section className={sectionClassName}>
      <dl className="grid gap-4 sm:grid-cols-2">
        {rows.map(([label, value]) => (
          <div key={label} className={typeof value === "string" && value && value.length > 80 ? "sm:col-span-2" : ""}>
            <dt className="text-xs font-semibold uppercase tracking-wide text-slate-500">{label}</dt>
            <dd className="mt-1 text-sm text-slate-900 whitespace-pre-wrap">{value ?? "—"}</dd>
          </div>
        ))}
      </dl>
    </section>
  );
}

function formatSize(bytes: number): string {
  if (bytes < 1024) return `${bytes} B`;
  if (bytes < 1024 * 1024) return `${(bytes / 1024).toFixed(1)} KB`;
  return `${(bytes / (1024 * 1024)).toFixed(1)} MB`;
}
