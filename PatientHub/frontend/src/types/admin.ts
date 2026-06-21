export type PatientStatus = "active" | "follow-up" | "discharged";

export type Patient = {
  id: string;
  name: string;
  age: number;
  phone: string;
  condition: string;
  lastVisit: string;
  status: PatientStatus;
};

export type AppointmentStatus = "confirmed" | "pending" | "completed" | "cancelled";

export type Appointment = {
  id: string;
  patientName: string;
  date: string;
  time: string;
  type: string;
  status: AppointmentStatus;
};

export type MedicalRecord = {
  id: string;
  patientId: string;
  patientName: string;
  diagnosis: string;
  treatment: string;
  date: string;
};

export type PatientHistoryEntry = {
  id: string;
  patientId: string;
  patientName: string;
  event: string;
  date: string;
  notes: string;
};

export type DashboardStat = {
  id: string;
  label: string;
  value: string;
  change: string;
  trend: "up" | "down" | "neutral";
};

export type ActivityItem = {
  id: string;
  action: string;
  detail: string;
  timestamp: string;
};

export type AuthCredentials = {
  username: string;
  password: string;
};

export type AuthSession = {
  username: string;
  token: string;
  loggedInAt: string;
};
