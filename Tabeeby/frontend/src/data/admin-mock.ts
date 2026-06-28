import type {
  ActivityItem,
  Appointment,
  DashboardStat,
  MedicalRecord,
  Patient,
  PatientHistoryEntry,
} from "@/types/admin";

export const DASHBOARD_STATS: DashboardStat[] = [
  {
    id: "patients",
    label: "Total Patients",
    value: "1,248",
    change: "+12 this month",
    trend: "up",
  },
  {
    id: "appointments",
    label: "Today's Appointments",
    value: "18",
    change: "3 pending confirmation",
    trend: "neutral",
  },
  {
    id: "surgeries",
    label: "Surgeries This Month",
    value: "42",
    change: "+8% vs last month",
    trend: "up",
  },
  {
    id: "follow-ups",
    label: "Follow-ups Due",
    value: "27",
    change: "5 overdue",
    trend: "down",
  },
];

export const MOCK_PATIENTS: Patient[] = [
  {
    id: "P-001",
    name: "Ahmed Hassan",
    age: 34,
    phone: "+20 100 234 5678",
    condition: "ACL Reconstruction",
    lastVisit: "2026-05-28",
    status: "follow-up",
  },
  {
    id: "P-002",
    name: "Fatima El-Sayed",
    age: 8,
    phone: "+20 101 345 6789",
    condition: "Pediatric Hip Dysplasia",
    lastVisit: "2026-05-30",
    status: "active",
  },
  {
    id: "P-003",
    name: "Omar Khalil",
    age: 52,
    phone: "+20 102 456 7890",
    condition: "Knee Replacement",
    lastVisit: "2026-05-15",
    status: "active",
  },
  {
    id: "P-004",
    name: "Nour Ibrahim",
    age: 28,
    phone: "+20 103 567 8901",
    condition: "Ankle Fracture",
    lastVisit: "2026-05-20",
    status: "discharged",
  },
  {
    id: "P-005",
    name: "Youssef Mahmoud",
    age: 45,
    phone: "+20 104 678 9012",
    condition: "Shoulder Arthroscopy",
    lastVisit: "2026-06-01",
    status: "follow-up",
  },
  {
    id: "P-006",
    name: "Mariam Farouk",
    age: 12,
    phone: "+20 105 789 0123",
    condition: "Congenital Foot Deformity",
    lastVisit: "2026-05-25",
    status: "active",
  },
];

export const MOCK_APPOINTMENTS: Appointment[] = [
  {
    id: "A-101",
    patientName: "Ahmed Hassan",
    date: "2026-06-02",
    time: "10:00 AM",
    type: "Follow-up",
    status: "confirmed",
  },
  {
    id: "A-102",
    patientName: "Fatima El-Sayed",
    date: "2026-06-02",
    time: "11:30 AM",
    type: "Consultation",
    status: "confirmed",
  },
  {
    id: "A-103",
    patientName: "Omar Khalil",
    date: "2026-06-02",
    time: "2:00 PM",
    type: "Post-op Review",
    status: "pending",
  },
  {
    id: "A-104",
    patientName: "Nour Ibrahim",
    date: "2026-06-03",
    time: "9:00 AM",
    type: "X-ray Review",
    status: "confirmed",
  },
  {
    id: "A-105",
    patientName: "Youssef Mahmoud",
    date: "2026-06-03",
    time: "3:30 PM",
    type: "Physiotherapy Plan",
    status: "pending",
  },
];

export const MOCK_MEDICAL_RECORDS: MedicalRecord[] = [
  {
    id: "MR-001",
    patientId: "P-001",
    patientName: "Ahmed Hassan",
    diagnosis: "Complete ACL tear — right knee",
    treatment: "Arthroscopic ACL reconstruction scheduled",
    date: "2026-05-28",
  },
  {
    id: "MR-002",
    patientId: "P-002",
    patientName: "Fatima El-Sayed",
    diagnosis: "Developmental hip dysplasia",
    treatment: "Pavlik harness protocol + monitoring",
    date: "2026-05-30",
  },
  {
    id: "MR-003",
    patientId: "P-003",
    patientName: "Omar Khalil",
    diagnosis: "Severe osteoarthritis — left knee",
    treatment: "Total knee replacement — post-op week 3",
    date: "2026-05-15",
  },
];

export const MOCK_PATIENT_HISTORY: PatientHistoryEntry[] = [
  {
    id: "H-001",
    patientId: "P-001",
    patientName: "Ahmed Hassan",
    event: "MRI Review",
    date: "2026-05-20",
    notes: "Confirmed complete ACL rupture. Surgery recommended.",
  },
  {
    id: "H-002",
    patientId: "P-002",
    patientName: "Fatima El-Sayed",
    event: "Initial Consultation",
    date: "2026-04-10",
    notes: "Hip ultrasound showed dysplasia. Treatment plan initiated.",
  },
  {
    id: "H-003",
    patientId: "P-003",
    patientName: "Omar Khalil",
    event: "Surgery",
    date: "2026-04-22",
    notes: "Total knee replacement completed successfully.",
  },
];

export const RECENT_ACTIVITIES: ActivityItem[] = [
  {
    id: "ACT-1",
    action: "New appointment booked",
    detail: "Fatima El-Sayed — Consultation on Jun 2",
    timestamp: "2 hours ago",
  },
  {
    id: "ACT-2",
    action: "Medical record updated",
    detail: "Ahmed Hassan — ACL reconstruction notes",
    timestamp: "4 hours ago",
  },
  {
    id: "ACT-3",
    action: "Follow-up reminder sent",
    detail: "Omar Khalil — Post-op review due",
    timestamp: "Yesterday",
  },
  {
    id: "ACT-4",
    action: "New patient registered",
    detail: "Mariam Farouk — Pediatric consultation",
    timestamp: "Yesterday",
  },
  {
    id: "ACT-5",
    action: "Surgery completed",
    detail: "Youssef Mahmoud — Shoulder arthroscopy",
    timestamp: "2 days ago",
  },
];
