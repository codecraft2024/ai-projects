"use client";

import { useState } from "react";
import { useTranslations } from "next-intl";
import { Button } from "@/components/ui/Button";
import { inputClassName, labelClassName, sectionClassName } from "@/components/admin/patients/patient-styles";
import type { PatientFormData } from "@/types/patient";
import { CASE_STATUSES, GENDERS } from "@/types/patient";

type PatientFormProps = {
  initial: PatientFormData;
  onSubmit: (data: PatientFormData) => Promise<void>;
  submitLabel: string;
  loadingLabel: string;
};

export function PatientForm({ initial, onSubmit, submitLabel, loadingLabel }: PatientFormProps) {
  const t = useTranslations("admin.patients");
  const [form, setForm] = useState<PatientFormData>(initial);
  const [errors, setErrors] = useState<Record<string, string>>({});
  const [submitting, setSubmitting] = useState(false);
  const [submitError, setSubmitError] = useState<string | null>(null);

  const update = (key: keyof PatientFormData, value: string) => {
    setForm((prev) => ({ ...prev, [key]: value }));
    setErrors((prev) => {
      const next = { ...prev };
      delete next[key];
      return next;
    });
  };

  const validate = (): boolean => {
    const next: Record<string, string> = {};
    if (!form.fullName.trim()) next.fullName = t("validation.fullNameRequired");
    if (!form.mobileNumber.trim()) next.mobileNumber = t("validation.mobileRequired");
    if (!form.dateOfBirth && !form.age.trim()) {
      next.dateOfBirth = t("validation.dobOrAgeRequired");
    }
    setErrors(next);
    return Object.keys(next).length === 0;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setSubmitError(null);
    if (!validate()) return;

    setSubmitting(true);
    try {
      await onSubmit(form);
    } catch (err) {
      setSubmitError(err instanceof Error ? err.message : t("saveError"));
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-6">
      {submitError && (
        <div className="rounded-lg border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700">
          {submitError}
        </div>
      )}

      <section className={sectionClassName}>
        <h2 className="mb-4 text-lg font-semibold text-slate-900">{t("sections.basic")}</h2>
        <div className="grid gap-4 sm:grid-cols-2">
          <Field label={t("form.fullName")} error={errors.fullName} required>
            <input
              value={form.fullName}
              onChange={(e) => update("fullName", e.target.value)}
              className={inputClassName}
            />
          </Field>
          <Field label={t("form.gender")} required>
            <select
              value={form.gender}
              onChange={(e) => update("gender", e.target.value)}
              className={inputClassName}
            >
              {GENDERS.map((g) => (
                <option key={g} value={g}>
                  {t(`gender.${g}`)}
                </option>
              ))}
            </select>
          </Field>
          <Field label={t("form.dateOfBirth")} error={errors.dateOfBirth}>
            <input
              type="date"
              value={form.dateOfBirth}
              onChange={(e) => update("dateOfBirth", e.target.value)}
              className={inputClassName}
            />
          </Field>
          <Field label={t("form.age")}>
            <input
              type="number"
              min={0}
              max={120}
              value={form.age}
              onChange={(e) => update("age", e.target.value)}
              className={inputClassName}
              placeholder={t("form.ageHint")}
            />
          </Field>
          <Field label={t("form.mobile")} error={errors.mobileNumber} required>
            <input
              type="tel"
              value={form.mobileNumber}
              onChange={(e) => update("mobileNumber", e.target.value)}
              className={inputClassName}
            />
          </Field>
          <Field label={t("form.emergencyContact")}>
            <input
              value={form.emergencyContact}
              onChange={(e) => update("emergencyContact", e.target.value)}
              className={inputClassName}
            />
          </Field>
          <Field label={t("form.address")} className="sm:col-span-2">
            <input
              value={form.address}
              onChange={(e) => update("address", e.target.value)}
              className={inputClassName}
            />
          </Field>
        </div>
      </section>

      <section className={sectionClassName}>
        <h2 className="mb-4 text-lg font-semibold text-slate-900">{t("sections.medical")}</h2>
        <div className="grid gap-4 sm:grid-cols-2">
          <Field label={t("form.diagnosis")}>
            <input
              value={form.diagnosis}
              onChange={(e) => update("diagnosis", e.target.value)}
              className={inputClassName}
            />
          </Field>
          <Field label={t("form.surgeryType")}>
            <input
              value={form.surgeryType}
              onChange={(e) => update("surgeryType", e.target.value)}
              className={inputClassName}
            />
          </Field>
          <Field label={t("form.caseStatus")}>
            <select
              value={form.caseStatus}
              onChange={(e) => update("caseStatus", e.target.value)}
              className={inputClassName}
            >
              {CASE_STATUSES.map((s) => (
                <option key={s} value={s}>
                  {t(`status.${s}`)}
                </option>
              ))}
            </select>
          </Field>
          <Field label={t("form.lastVisit")}>
            <input
              type="date"
              value={form.lastVisitDate}
              onChange={(e) => update("lastVisitDate", e.target.value)}
              className={inputClassName}
            />
          </Field>
          <Field label={t("form.medicalHistory")} className="sm:col-span-2">
            <textarea
              rows={3}
              value={form.medicalHistory}
              onChange={(e) => update("medicalHistory", e.target.value)}
              className={inputClassName}
            />
          </Field>
          <Field label={t("form.medications")} className="sm:col-span-2">
            <textarea
              rows={2}
              value={form.currentMedications}
              onChange={(e) => update("currentMedications", e.target.value)}
              className={inputClassName}
            />
          </Field>
          <Field label={t("form.allergies")} className="sm:col-span-2">
            <textarea
              rows={2}
              value={form.allergies}
              onChange={(e) => update("allergies", e.target.value)}
              className={inputClassName}
            />
          </Field>
          <Field label={t("form.surgeryDetails")} className="sm:col-span-2">
            <textarea
              rows={3}
              value={form.surgeryDetails}
              onChange={(e) => update("surgeryDetails", e.target.value)}
              className={inputClassName}
            />
          </Field>
          <Field label={t("form.notes")} className="sm:col-span-2">
            <textarea
              rows={3}
              value={form.notes}
              onChange={(e) => update("notes", e.target.value)}
              className={inputClassName}
            />
          </Field>
        </div>
      </section>

      <div className="flex flex-col gap-3 sm:flex-row sm:flex-wrap">
        <Button type="submit" variant="primary" className="w-full sm:w-auto" disabled={submitting}>
          {submitting ? loadingLabel : submitLabel}
        </Button>
        <Button href="/admin/patients" variant="outline" className="w-full sm:w-auto">
          {t("cancel")}
        </Button>
      </div>
    </form>
  );
}

function Field({
  label,
  children,
  error,
  required,
  className,
}: {
  label: string;
  children: React.ReactNode;
  error?: string;
  required?: boolean;
  className?: string;
}) {
  return (
    <div className={className}>
      <label className={labelClassName}>
        {label}
        {required && <span className="text-red-500"> *</span>}
      </label>
      {children}
      {error && <p className="mt-1 text-xs text-red-600">{error}</p>}
    </div>
  );
}
