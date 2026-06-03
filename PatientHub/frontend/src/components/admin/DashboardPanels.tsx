"use client";

import { useMemo, useState } from "react";
import { useTranslations } from "next-intl";
import {
  DASHBOARD_STATS,
  MOCK_APPOINTMENTS,
  MOCK_MEDICAL_RECORDS,
  MOCK_PATIENTS,
  MOCK_PATIENT_HISTORY,
  RECENT_ACTIVITIES,
} from "@/data/admin-mock";
import { AdminCard } from "@/components/admin/AdminShell";
import type { Patient, PatientStatus } from "@/types/admin";
import { cn } from "@/utils/cn";

const statusStyles: Record<PatientStatus, string> = {
  active: "bg-green-100 text-green-800",
  "follow-up": "bg-amber-100 text-amber-800",
  discharged: "bg-slate-100 text-slate-600",
};

export function DashboardStatsGrid() {
  return (
    <div className="grid gap-4 sm:grid-cols-2 xl:grid-cols-4">
      {DASHBOARD_STATS.map((stat) => (
        <div
          key={stat.id}
          className="rounded-2xl border border-slate-200 bg-white p-5 shadow-sm"
        >
          <p className="text-sm font-medium text-slate-500">{stat.label}</p>
          <p className="mt-2 text-3xl font-bold text-slate-900">{stat.value}</p>
          <p
            className={cn(
              "mt-1 text-xs font-medium",
              stat.trend === "up" && "text-green-600",
              stat.trend === "down" && "text-amber-600",
              stat.trend === "neutral" && "text-slate-500",
            )}
          >
            {stat.change}
          </p>
        </div>
      ))}
    </div>
  );
}

export function PatientSearchPanel() {
  const t = useTranslations("admin");
  const [query, setQuery] = useState("");
  const filtered = useMemo(() => {
    const q = query.trim().toLowerCase();
    if (!q) return MOCK_PATIENTS;
    return MOCK_PATIENTS.filter(
      (p) =>
        p.name.toLowerCase().includes(q) ||
        p.id.toLowerCase().includes(q) ||
        p.condition.toLowerCase().includes(q),
    );
  }, [query]);

  return (
    <AdminCard
      title={t("searchPatients")}
      action={
        <input
          type="search"
          placeholder={t("searchPlaceholder")}
          value={query}
          onChange={(e) => setQuery(e.target.value)}
          className="w-full max-w-xs rounded-lg border border-[var(--border)] px-3 py-2 text-sm focus:border-brand focus:outline-none focus:ring-2 focus:ring-brand/20 sm:w-56"
          aria-label={t("searchPatients")}
        />
      }
    >
      <PatientsTable patients={filtered} compact />
    </AdminCard>
  );
}

export function PatientsListPanel() {
  const t = useTranslations("admin");
  return (
    <AdminCard title={t("patientsList")}>
      <PatientsTable patients={MOCK_PATIENTS} />
    </AdminCard>
  );
}

function PatientsTable({
  patients,
  compact = false,
}: {
  patients: Patient[];
  compact?: boolean;
}) {
  const t = useTranslations("admin");
  return (
    <div className="overflow-x-auto -mx-4 sm:mx-0">
      <table className="min-w-full text-start text-sm">
        <thead>
          <tr className="border-b border-slate-200 text-xs uppercase tracking-wide text-slate-500">
            <th className="px-4 py-3 sm:px-0">ID</th>
            <th className="px-4 py-3">Name</th>
            {!compact && <th className="hidden px-4 py-3 sm:table-cell">Age</th>}
            <th className="px-4 py-3">Condition</th>
            <th className="hidden px-4 py-3 md:table-cell">Last Visit</th>
            <th className="px-4 py-3 sm:pr-0">Status</th>
          </tr>
        </thead>
        <tbody>
          {patients.length === 0 ? (
            <tr>
              <td colSpan={6} className="py-8 text-center text-slate-500">
                {t("noPatients")}
              </td>
            </tr>
          ) : (
            patients.map((p) => (
              <tr key={p.id} className="border-b border-slate-50 hover:bg-slate-50/80">
                <td className="px-4 py-3 font-mono text-xs text-slate-500 sm:px-0">{p.id}</td>
                <td className="px-4 py-3 font-medium text-slate-900">{p.name}</td>
                {!compact && (
                  <td className="hidden px-4 py-3 sm:table-cell">{p.age}</td>
                )}
                <td className="max-w-[8rem] truncate px-4 py-3 text-slate-600 sm:max-w-none">
                  {p.condition}
                </td>
                <td className="hidden px-4 py-3 md:table-cell">{p.lastVisit}</td>
                <td className="px-4 py-3 sm:pr-0">
                  <span
                    className={cn(
                      "inline-block rounded-full px-2.5 py-0.5 text-xs font-medium capitalize",
                      statusStyles[p.status],
                    )}
                  >
                    {p.status.replace("-", " ")}
                  </span>
                </td>
              </tr>
            ))
          )}
        </tbody>
      </table>
    </div>
  );
}

export function AppointmentsPanel() {
  const t = useTranslations("admin");
  return (
    <AdminCard title={t("appointments")}>
      <ul className="divide-y divide-slate-100">
        {MOCK_APPOINTMENTS.map((a) => (
          <li
            key={a.id}
            className="flex flex-col gap-2 py-4 first:pt-0 last:pb-0 sm:flex-row sm:items-center sm:justify-between"
          >
            <div>
              <p className="font-medium text-slate-900">{a.patientName}</p>
              <p className="text-sm text-slate-500">
                {a.date} · {a.time} · {a.type}
              </p>
            </div>
            <span
              className={cn(
                "w-fit rounded-full px-3 py-1 text-xs font-semibold capitalize",
                a.status === "confirmed" && "bg-green-100 text-green-800",
                a.status === "pending" && "bg-amber-100 text-amber-800",
                a.status === "completed" && "bg-blue-100 text-blue-800",
                a.status === "cancelled" && "bg-slate-100 text-slate-600",
              )}
            >
              {a.status}
            </span>
          </li>
        ))}
      </ul>
    </AdminCard>
  );
}

export function PatientHistoryPanel() {
  const t = useTranslations("admin");
  return (
    <AdminCard title={t("patientHistory")}>
      <ul className="space-y-4">
        {MOCK_PATIENT_HISTORY.map((h) => (
          <li key={h.id} className="rounded-xl border border-slate-100 bg-slate-50/50 p-4">
            <div className="flex flex-wrap items-start justify-between gap-2">
              <p className="font-medium text-slate-900">{h.patientName}</p>
              <span className="text-xs text-slate-500">{h.date}</span>
            </div>
            <p className="mt-1 text-sm font-medium text-blue-800">{h.event}</p>
            <p className="mt-2 text-sm text-slate-600">{h.notes}</p>
          </li>
        ))}
      </ul>
    </AdminCard>
  );
}

export function MedicalRecordsPanel() {
  const t = useTranslations("admin");
  return (
    <AdminCard title={t("medicalRecords")}>
      <div className="space-y-4">
        {MOCK_MEDICAL_RECORDS.map((r) => (
          <div
            key={r.id}
            className="rounded-xl border border-slate-200 p-4 transition hover:border-blue-200"
          >
            <div className="flex flex-wrap justify-between gap-2">
              <p className="font-semibold text-slate-900">{r.patientName}</p>
              <span className="font-mono text-xs text-slate-400">{r.id}</span>
            </div>
            <p className="mt-2 text-sm">
              <span className="font-medium text-slate-700">Diagnosis:</span> {r.diagnosis}
            </p>
            <p className="mt-1 text-sm text-slate-600">
              <span className="font-medium text-slate-700">Treatment:</span> {r.treatment}
            </p>
            <p className="mt-2 text-xs text-slate-400">{r.date}</p>
          </div>
        ))}
      </div>
    </AdminCard>
  );
}

export function RecentActivitiesPanel() {
  const t = useTranslations("admin");
  return (
    <AdminCard title={t("recentActivities")}>
      <ul className="space-y-3">
        {RECENT_ACTIVITIES.map((a) => (
          <li key={a.id} className="flex gap-3 border-b border-slate-50 pb-3 last:border-0">
            <span className="mt-1.5 h-2 w-2 shrink-0 rounded-full bg-blue-600" />
            <div>
              <p className="text-sm font-medium text-slate-900">{a.action}</p>
              <p className="text-sm text-slate-600">{a.detail}</p>
              <p className="mt-0.5 text-xs text-slate-400">{a.timestamp}</p>
            </div>
          </li>
        ))}
      </ul>
    </AdminCard>
  );
}
