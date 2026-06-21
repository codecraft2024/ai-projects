import { apiJson, fileDownloadUrl, getApiBase, getAuthHeaders } from "@/services/api.client";
import type {
  CaseStatus,
  MedicalFile,
  MedicalFileType,
  MedicalNote,
  PageResponse,
  PatientDetail,
  PatientFormData,
  PatientSearchFilters,
  PatientSummary,
  VisitHistory,
} from "@/types/patient";

function buildQuery(filters: PatientSearchFilters, page: number, size: number): string {
  const params = new URLSearchParams();
  params.set("page", String(page));
  params.set("size", String(size));

  if (filters.name?.trim()) params.set("name", filters.name.trim());
  if (filters.mobile?.trim()) params.set("mobile", filters.mobile.trim());
  if (filters.age?.trim()) params.set("age", filters.age.trim());
  if (filters.caseStatus) params.set("caseStatus", filters.caseStatus);
  if (filters.diagnosis?.trim()) params.set("diagnosis", filters.diagnosis.trim());
  if (filters.surgeryType?.trim()) params.set("surgeryType", filters.surgeryType.trim());
  if (filters.lastVisitFrom) params.set("lastVisitFrom", filters.lastVisitFrom);
  if (filters.lastVisitTo) params.set("lastVisitTo", filters.lastVisitTo);
  if (filters.medicalRecordNumber?.trim()) {
    params.set("medicalRecordNumber", filters.medicalRecordNumber.trim());
  }

  return params.toString();
}

function formToPayload(form: PatientFormData) {
  return {
    fullName: form.fullName,
    gender: form.gender,
    dateOfBirth: form.dateOfBirth || null,
    age: form.age ? Number(form.age) : null,
    mobileNumber: form.mobileNumber,
    address: form.address || null,
    emergencyContact: form.emergencyContact || null,
    diagnosis: form.diagnosis || null,
    medicalHistory: form.medicalHistory || null,
    currentMedications: form.currentMedications || null,
    allergies: form.allergies || null,
    surgeryDetails: form.surgeryDetails || null,
    surgeryType: form.surgeryType || null,
    notes: form.notes || null,
    caseStatus: form.caseStatus,
    lastVisitDate: form.lastVisitDate || null,
  };
}

export async function searchPatients(
  filters: PatientSearchFilters,
  page = 0,
  size = 10,
): Promise<PageResponse<PatientSummary>> {
  return apiJson(`/api/patients?${buildQuery(filters, page, size)}`);
}

export async function getPatient(id: number): Promise<PatientDetail> {
  return apiJson(`/api/patients/${id}`);
}

export async function createPatient(form: PatientFormData): Promise<PatientDetail> {
  return apiJson("/api/patients", {
    method: "POST",
    body: JSON.stringify(formToPayload(form)),
  });
}

export async function updatePatient(id: number, form: PatientFormData): Promise<PatientDetail> {
  return apiJson(`/api/patients/${id}`, {
    method: "PUT",
    body: JSON.stringify(formToPayload(form)),
  });
}

export async function updatePatientStatus(
  id: number,
  caseStatus: CaseStatus,
): Promise<PatientDetail> {
  return apiJson(`/api/patients/${id}/status`, {
    method: "PATCH",
    body: JSON.stringify({ caseStatus }),
  });
}

export async function addMedicalNote(id: number, content: string): Promise<MedicalNote> {
  return apiJson(`/api/patients/${id}/notes`, {
    method: "POST",
    body: JSON.stringify({ content }),
  });
}

export async function addVisit(
  id: number,
  data: { visitDate?: string; notes?: string; diagnosis?: string },
): Promise<VisitHistory> {
  return apiJson(`/api/patients/${id}/visits`, {
    method: "POST",
    body: JSON.stringify(data),
  });
}

export async function listPatientFiles(id: number): Promise<MedicalFile[]> {
  return apiJson(`/api/patients/${id}/files`);
}

export async function uploadPatientFile(
  id: number,
  fileType: MedicalFileType,
  file: File,
): Promise<MedicalFile> {
  const formData = new FormData();
  formData.append("fileType", fileType);
  formData.append("file", file);

  const headers = new Headers(getAuthHeaders());
  const res = await fetch(`${getApiBase()}/api/patients/${id}/files`, {
    method: "POST",
    headers,
    body: formData,
  });

  if (!res.ok) {
    const body = (await res.json().catch(() => null)) as { message?: string } | null;
    throw new Error(body?.message ?? res.statusText);
  }

  return res.json();
}

export async function deletePatientFile(id: number, fileId: number): Promise<void> {
  await apiJson(`/api/patients/${id}/files/${fileId}`, { method: "DELETE" });
}

export function getFileDownloadHref(downloadUrl: string): string {
  return fileDownloadUrl(downloadUrl);
}
