import type { CallMeta, CatalogItem } from "@/types";

export const API_BASE_URL =
  process.env.NEXT_PUBLIC_API_BASE_URL ?? "http://localhost:8080";

export const CALLS: CatalogItem[] = [
  {
    id: "health-check",
    name: "Health Check",
    description: "Verify backend availability and service readiness.",
    href: "/calls/health-check",
    status: "ready",
  },
  {
    id: "bind1",
    name: "Bind1",
    description: "Device bind against InstaPay staging.",
    href: "/calls/bind1",
    status: "ready",
  },
  {
    id: "register1",
    name: "Register1",
    description: "Customer register / DEVCHNG on staging.",
    href: "/calls/register1",
    status: "ready",
  },
];

export const SCENARIOS: CatalogItem[] = [
  {
    id: "binding",
    name: "Binding Scenario",
    description: "Health Check → Bind1 → Register1 end-to-end flow.",
    href: "/scenarios/binding",
    status: "ready",
  },
];

export const CALL_META: Record<string, CallMeta> = {
  "health-check": {
    id: "health-check",
    name: "Health Check",
    description: "Local readiness probe returning success or failure payload.",
    method: "GET",
    successPath: "/health/success",
    failurePath: "/health/failure",
  },
  bind1: {
    id: "bind1",
    name: "Bind1",
    description: "Binds device to InstaPay. Success requires encString and returns code 00000.",
    method: "GET",
    successPath: "/bind1/success",
    failurePath: "/bind1/failure",
  },
  register1: {
    id: "register1",
    name: "Register1",
    description: "Registers customer on staging. Business success is code 00000.",
    method: "GET",
    successPath: "/register1/success",
    failurePath: "/register1/failure",
  },
};

export const BINDING_SCENARIO = {
  name: "Binding Scenario",
  description:
    "Runs Health Check, Bind1, and Register1 in sequence. The scenario fails if any step returns a non-success business result.",
  steps: ["health-check", "bind1", "register1"],
};
