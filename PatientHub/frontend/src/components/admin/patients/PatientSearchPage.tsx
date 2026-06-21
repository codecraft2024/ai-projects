"use client";

import { useCallback, useEffect, useState } from "react";
import { useTranslations } from "next-intl";
import { Link, useRouter } from "@/i18n/navigation";
import { AdminCard } from "@/components/admin/AdminShell";
import { Button } from "@/components/ui/Button";
import {
  StatusBadge,
  inputClassName,
  labelClassName,
} from "@/components/admin/patients/patient-styles";
import { ApiError } from "@/services/api.client";
import { searchPatients } from "@/services/patient.service";
import type { PatientSearchFilters, PatientSummary } from "@/types/patient";
import { CASE_STATUSES } from "@/types/patient";
import { cn } from "@/utils/cn";

const emptyFilters: PatientSearchFilters = {
  name: "",
  mobile: "",
  age: "",
  caseStatus: "",
  diagnosis: "",
  surgeryType: "",
  lastVisitFrom: "",
  lastVisitTo: "",
  medicalRecordNumber: "",
};

export function PatientSearchPage() {
  const t = useTranslations("admin.patients");
  const router = useRouter();
  const [filters, setFilters] = useState<PatientSearchFilters>(emptyFilters);
  const [applied, setApplied] = useState<PatientSearchFilters>(emptyFilters);
  const [page, setPage] = useState(0);
  const [patients, setPatients] = useState<PatientSummary[]>([]);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const load = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const result = await searchPatients(applied, page, 10);
      setPatients(result.content);
      setTotalPages(result.totalPages);
      setTotalElements(result.totalElements);
    } catch (err) {
      setError(err instanceof ApiError ? err.message : t("loadError"));
      setPatients([]);
    } finally {
      setLoading(false);
    }
  }, [applied, page, t]);

  useEffect(() => {
    load();
  }, [load]);

  const updateFilter = (key: keyof PatientSearchFilters, value: string) => {
    setFilters((prev) => ({ ...prev, [key]: value }));
  };

  const handleSearch = (e: React.FormEvent) => {
    e.preventDefault();
    setPage(0);
    setApplied({ ...filters });
  };

  const handleReset = () => {
    setFilters(emptyFilters);
    setApplied(emptyFilters);
    setPage(0);
  };

  return (
    <div className="space-y-4 sm:space-y-6">
      <div className="flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-end">
        <Button href="/admin/patients/new" variant="primary" className="w-full sm:w-auto">
          {t("addNew")}
        </Button>
      </div>

      <AdminCard title={t("searchTitle")} className="[&>div:first-child]:hidden md:[&>div:first-child]:flex">
        <details className="group md:hidden">
          <summary className="mb-4 cursor-pointer list-none rounded-lg border border-[var(--border)] bg-slate-50 px-4 py-3 text-sm font-medium text-slate-700 marker:content-none [&::-webkit-details-marker]:hidden">
            <span className="flex items-center justify-between">
              {t("searchTitle")}
              <span className="text-xs text-slate-400 group-open:hidden">{t("searchButton")}</span>
              <span className="hidden text-xs text-slate-400 group-open:inline">{t("cancel")}</span>
            </span>
          </summary>
          <SearchFiltersForm
            filters={filters}
            updateFilter={updateFilter}
            onSubmit={handleSearch}
            onReset={handleReset}
            t={t}
          />
        </details>
        <div className="hidden md:block">
          <SearchFiltersForm
            filters={filters}
            updateFilter={updateFilter}
            onSubmit={handleSearch}
            onReset={handleReset}
            t={t}
          />
        </div>
      </AdminCard>

      <AdminCard
        title={t("resultsTitle")}
        action={
          <span className="text-sm text-slate-500">
            {t("totalResults", { count: totalElements })}
          </span>
        }
      >
        {error && (
          <div className="mb-4 rounded-lg border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700">
            {error}
          </div>
        )}

        {loading ? (
          <div className="space-y-3">
            {Array.from({ length: 5 }).map((_, i) => (
              <div key={i} className="h-12 animate-pulse rounded-lg bg-slate-100" />
            ))}
          </div>
        ) : (
          <>
            <div className="space-y-3 md:hidden">
              {patients.length === 0 ? (
                <p className="py-8 text-center text-sm text-slate-500">{t("noResults")}</p>
              ) : (
                patients.map((patient) => (
                  <PatientMobileCard
                    key={patient.id}
                    patient={patient}
                    t={t}
                    onUpload={() => router.push(`/admin/patients/${patient.id}?tab=files`)}
                  />
                ))
              )}
            </div>

            <div className="hidden overflow-x-auto md:block">
              <table className="min-w-full text-start text-sm">
                <thead>
                  <tr className="border-b border-slate-200 text-xs uppercase tracking-wide text-slate-500">
                    <th className="px-4 py-3 sm:px-0">{t("table.id")}</th>
                    <th className="px-4 py-3">{t("table.name")}</th>
                    <th className="hidden px-4 py-3 sm:table-cell">{t("table.age")}</th>
                    <th className="hidden px-4 py-3 md:table-cell">{t("table.mobile")}</th>
                    <th className="px-4 py-3">{t("table.diagnosis")}</th>
                    <th className="hidden px-4 py-3 lg:table-cell">{t("table.surgery")}</th>
                    <th className="hidden px-4 py-3 lg:table-cell">{t("table.lastVisit")}</th>
                    <th className="px-4 py-3">{t("table.status")}</th>
                    <th className="px-4 py-3 sm:pr-0">{t("table.actions")}</th>
                  </tr>
                </thead>
                <tbody>
                  {patients.length === 0 ? (
                    <tr>
                      <td colSpan={9} className="py-10 text-center text-slate-500">
                        {t("noResults")}
                      </td>
                    </tr>
                  ) : (
                    patients.map((patient) => (
                      <tr
                        key={patient.id}
                        className="border-b border-slate-50 hover:bg-slate-50/80"
                      >
                        <td className="px-4 py-3 font-mono text-xs text-slate-500 sm:px-0">
                          {patient.medicalRecordNumber ?? `#${patient.id}`}
                        </td>
                        <td className="px-4 py-3 font-medium text-slate-900">{patient.fullName}</td>
                        <td className="hidden px-4 py-3 sm:table-cell">{patient.age ?? "—"}</td>
                        <td className="hidden px-4 py-3 md:table-cell">{patient.mobileNumber}</td>
                        <td className="max-w-[10rem] truncate px-4 py-3 text-slate-600 lg:max-w-xs">
                          {patient.diagnosis ?? "—"}
                        </td>
                        <td className="hidden max-w-[8rem] truncate px-4 py-3 lg:table-cell">
                          {patient.surgeryType ?? "—"}
                        </td>
                        <td className="hidden px-4 py-3 lg:table-cell">
                          {patient.lastVisitDate ?? "—"}
                        </td>
                        <td className="px-4 py-3">
                          <StatusBadge
                            status={patient.caseStatus}
                            label={t(`status.${patient.caseStatus}`)}
                          />
                        </td>
                        <td className="px-4 py-3 sm:pr-0">
                          <div className="flex flex-wrap gap-1">
                            <ActionLink
                              href={`/admin/patients/${patient.id}`}
                              label={t("actions.view")}
                            />
                            <ActionLink
                              href={`/admin/patients/${patient.id}?edit=1`}
                              label={t("actions.edit")}
                            />
                            <button
                              type="button"
                              onClick={() =>
                                router.push(`/admin/patients/${patient.id}?tab=files`)
                              }
                              className="rounded px-2 py-1 text-xs font-medium text-brand hover:bg-brand-soft"
                            >
                              {t("actions.upload")}
                            </button>
                          </div>
                        </td>
                      </tr>
                    ))
                  )}
                </tbody>
              </table>
            </div>

            {totalPages > 1 && (
              <div className="mt-4 flex flex-col gap-3 border-t border-slate-100 pt-4 sm:flex-row sm:items-center sm:justify-between">
                <p className="text-center text-sm text-slate-500 sm:text-start">
                  {t("pageInfo", { current: page + 1, total: totalPages })}
                </p>
                <div className="grid grid-cols-2 gap-2 sm:flex">
                  <Button
                    type="button"
                    variant="outline"
                    size="sm"
                    className="w-full sm:w-auto"
                    disabled={page === 0}
                    onClick={() => setPage((p) => p - 1)}
                  >
                    {t("prevPage")}
                  </Button>
                  <Button
                    type="button"
                    variant="outline"
                    size="sm"
                    className="w-full sm:w-auto"
                    disabled={page >= totalPages - 1}
                    onClick={() => setPage((p) => p + 1)}
                  >
                    {t("nextPage")}
                  </Button>
                </div>
              </div>
            )}
          </>
        )}
      </AdminCard>
    </div>
  );
}

function SearchFiltersForm({
  filters,
  updateFilter,
  onSubmit,
  onReset,
  t,
}: {
  filters: PatientSearchFilters;
  updateFilter: (key: keyof PatientSearchFilters, value: string) => void;
  onSubmit: (e: React.FormEvent) => void;
  onReset: () => void;
  t: ReturnType<typeof useTranslations<"admin.patients">>;
}) {
  return (
    <form onSubmit={onSubmit} className="space-y-4">
      <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-4">
        <FilterField label={t("filters.name")}>
          <input
            type="text"
            value={filters.name}
            onChange={(e) => updateFilter("name", e.target.value)}
            className={inputClassName}
            placeholder={t("filters.namePlaceholder")}
          />
        </FilterField>
        <FilterField label={t("filters.mobile")}>
          <input
            type="tel"
            value={filters.mobile}
            onChange={(e) => updateFilter("mobile", e.target.value)}
            className={inputClassName}
          />
        </FilterField>
        <FilterField label={t("filters.age")}>
          <input
            type="number"
            min={0}
            max={120}
            value={filters.age}
            onChange={(e) => updateFilter("age", e.target.value)}
            className={inputClassName}
          />
        </FilterField>
        <FilterField label={t("filters.mrn")}>
          <input
            type="text"
            value={filters.medicalRecordNumber}
            onChange={(e) => updateFilter("medicalRecordNumber", e.target.value)}
            className={inputClassName}
          />
        </FilterField>
        <FilterField label={t("filters.caseStatus")}>
          <select
            value={filters.caseStatus}
            onChange={(e) => updateFilter("caseStatus", e.target.value)}
            className={inputClassName}
          >
            <option value="">{t("filters.allStatuses")}</option>
            {CASE_STATUSES.map((status) => (
              <option key={status} value={status}>
                {t(`status.${status}`)}
              </option>
            ))}
          </select>
        </FilterField>
        <FilterField label={t("filters.diagnosis")}>
          <input
            type="text"
            value={filters.diagnosis}
            onChange={(e) => updateFilter("diagnosis", e.target.value)}
            className={inputClassName}
          />
        </FilterField>
        <FilterField label={t("filters.surgeryType")}>
          <input
            type="text"
            value={filters.surgeryType}
            onChange={(e) => updateFilter("surgeryType", e.target.value)}
            className={inputClassName}
          />
        </FilterField>
        <FilterField label={t("filters.lastVisitFrom")}>
          <input
            type="date"
            value={filters.lastVisitFrom}
            onChange={(e) => updateFilter("lastVisitFrom", e.target.value)}
            className={inputClassName}
          />
        </FilterField>
        <FilterField label={t("filters.lastVisitTo")}>
          <input
            type="date"
            value={filters.lastVisitTo}
            onChange={(e) => updateFilter("lastVisitTo", e.target.value)}
            className={inputClassName}
          />
        </FilterField>
      </div>
      <div className="grid grid-cols-2 gap-2 sm:flex sm:flex-wrap">
        <Button type="submit" variant="primary" className="w-full sm:w-auto">
          {t("searchButton")}
        </Button>
        <Button type="button" variant="outline" onClick={onReset} className="w-full sm:w-auto">
          {t("resetButton")}
        </Button>
      </div>
    </form>
  );
}

function PatientMobileCard({
  patient,
  t,
  onUpload,
}: {
  patient: PatientSummary;
  t: ReturnType<typeof useTranslations<"admin.patients">>;
  onUpload: () => void;
}) {
  return (
    <article className="rounded-xl border border-[var(--border)] bg-white p-4 shadow-sm">
      <div className="flex items-start justify-between gap-3">
        <div className="min-w-0">
          <p className="truncate font-semibold text-slate-900">{patient.fullName}</p>
          <p className="mt-0.5 font-mono text-xs text-slate-500">
            {patient.medicalRecordNumber ?? `#${patient.id}`}
          </p>
        </div>
        <StatusBadge status={patient.caseStatus} label={t(`status.${patient.caseStatus}`)} />
      </div>
      <dl className="mt-3 grid grid-cols-2 gap-x-3 gap-y-2 text-sm">
        <div>
          <dt className="text-xs text-slate-500">{t("table.age")}</dt>
          <dd className="font-medium text-slate-800">{patient.age ?? "—"}</dd>
        </div>
        <div>
          <dt className="text-xs text-slate-500">{t("table.mobile")}</dt>
          <dd className="truncate font-medium text-slate-800">{patient.mobileNumber}</dd>
        </div>
        <div className="col-span-2">
          <dt className="text-xs text-slate-500">{t("table.diagnosis")}</dt>
          <dd className="text-slate-800">{patient.diagnosis ?? "—"}</dd>
        </div>
        {patient.lastVisitDate && (
          <div className="col-span-2">
            <dt className="text-xs text-slate-500">{t("table.lastVisit")}</dt>
            <dd className="text-slate-800">{patient.lastVisitDate}</dd>
          </div>
        )}
      </dl>
      <div className="mt-4 grid grid-cols-3 gap-2 border-t border-slate-100 pt-3">
        <ActionLink href={`/admin/patients/${patient.id}`} label={t("actions.view")} className="justify-center" />
        <ActionLink
          href={`/admin/patients/${patient.id}?edit=1`}
          label={t("actions.edit")}
          className="justify-center"
        />
        <button
          type="button"
          onClick={onUpload}
          className="rounded-lg px-2 py-2 text-center text-xs font-medium text-brand hover:bg-brand-soft"
        >
          {t("actions.upload")}
        </button>
      </div>
    </article>
  );
}

function FilterField({ label, children }: { label: string; children: React.ReactNode }) {
  return (
    <div>
      <label className={labelClassName}>{label}</label>
      {children}
    </div>
  );
}

function ActionLink({
  href,
  label,
  className,
}: {
  href: string;
  label: string;
  className?: string;
}) {
  return (
    <Link
      href={href}
      className={cn(
        "inline-flex items-center rounded-lg px-2 py-2 text-xs font-medium text-slate-700 hover:bg-slate-100",
        className,
      )}
    >
      {label}
    </Link>
  );
}
