export type Gender = "MALE" | "FEMALE" | "OTHER";

export type CaseStatus =
  | "ACTIVE"
  | "FOLLOW_UP"
  | "DISCHARGED"
  | "UNDER_TREATMENT"
  | "SCHEDULED_SURGERY";

export type MedicalFileType =
  | "RADIOLOGY"
  | "LABORATORY"
  | "MEDICAL_REPORT"
  | "PRESCRIPTION"
  | "SURGERY"
  | "OTHER";

export type PatientSummary = {
  id: number;
  medicalRecordNumber: string;
  fullName: string;
  age: number | null;
  mobileNumber: string;
  diagnosis: string | null;
  surgeryType: string | null;
  lastVisitDate: string | null;
  caseStatus: CaseStatus;
};

export type PatientDetail = {
  id: number;
  medicalRecordNumber: string;
  fullName: string;
  gender: Gender;
  dateOfBirth: string | null;
  age: number | null;
  mobileNumber: string;
  address: string | null;
  emergencyContact: string | null;
  diagnosis: string | null;
  medicalHistory: string | null;
  currentMedications: string | null;
  allergies: string | null;
  surgeryDetails: string | null;
  surgeryType: string | null;
  notes: string | null;
  caseStatus: CaseStatus;
  lastVisitDate: string | null;
  createdAt: string;
  updatedAt: string;
  files: MedicalFile[];
  visits: VisitHistory[];
  medicalNotes: MedicalNote[];
};

export type MedicalFile = {
  id: number;
  fileType: MedicalFileType;
  originalFilename: string;
  contentType: string | null;
  fileSize: number;
  uploadedAt: string;
  uploadedBy: string | null;
  downloadUrl: string;
};

export type VisitHistory = {
  id: number;
  visitDate: string;
  notes: string | null;
  diagnosis: string | null;
  createdBy: string | null;
  createdAt: string;
};

export type MedicalNote = {
  id: number;
  content: string;
  createdBy: string | null;
  createdAt: string;
};

export type PageResponse<T> = {
  content: T[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
  last: boolean;
};

export type PatientSearchFilters = {
  name?: string;
  mobile?: string;
  age?: string;
  caseStatus?: CaseStatus | "";
  diagnosis?: string;
  surgeryType?: string;
  lastVisitFrom?: string;
  lastVisitTo?: string;
  medicalRecordNumber?: string;
};

export type PatientFormData = {
  fullName: string;
  gender: Gender;
  dateOfBirth: string;
  age: string;
  mobileNumber: string;
  address: string;
  emergencyContact: string;
  diagnosis: string;
  medicalHistory: string;
  currentMedications: string;
  allergies: string;
  surgeryDetails: string;
  surgeryType: string;
  notes: string;
  caseStatus: CaseStatus;
  lastVisitDate: string;
};

export const CASE_STATUSES: CaseStatus[] = [
  "ACTIVE",
  "FOLLOW_UP",
  "DISCHARGED",
  "UNDER_TREATMENT",
  "SCHEDULED_SURGERY",
];

export const MEDICAL_FILE_TYPES: MedicalFileType[] = [
  "RADIOLOGY",
  "LABORATORY",
  "MEDICAL_REPORT",
  "PRESCRIPTION",
  "SURGERY",
  "OTHER",
];

export const GENDERS: Gender[] = ["MALE", "FEMALE", "OTHER"];

export function emptyPatientForm(): PatientFormData {
  return {
    fullName: "",
    gender: "MALE",
    dateOfBirth: "",
    age: "",
    mobileNumber: "",
    address: "",
    emergencyContact: "",
    diagnosis: "",
    medicalHistory: "",
    currentMedications: "",
    allergies: "",
    surgeryDetails: "",
    surgeryType: "",
    notes: "",
    caseStatus: "ACTIVE",
    lastVisitDate: new Date().toISOString().slice(0, 10),
  };
}

export function patientToForm(patient: PatientDetail): PatientFormData {
  return {
    fullName: patient.fullName,
    gender: patient.gender,
    dateOfBirth: patient.dateOfBirth ?? "",
    age: patient.age != null ? String(patient.age) : "",
    mobileNumber: patient.mobileNumber,
    address: patient.address ?? "",
    emergencyContact: patient.emergencyContact ?? "",
    diagnosis: patient.diagnosis ?? "",
    medicalHistory: patient.medicalHistory ?? "",
    currentMedications: patient.currentMedications ?? "",
    allergies: patient.allergies ?? "",
    surgeryDetails: patient.surgeryDetails ?? "",
    surgeryType: patient.surgeryType ?? "",
    notes: patient.notes ?? "",
    caseStatus: patient.caseStatus,
    lastVisitDate: patient.lastVisitDate ?? "",
  };
}
