import { getWithTrace } from "@/services/api-client";
import type { Bind1Response, Register1Response } from "@/types";
import type { CallExecution, TracedResult } from "@/types/network";

export function fetchHealthSuccess() {
  return getWithTrace<CallExecution<{ success: boolean; message: string }>>("/health/success");
}

export function fetchHealthFailure() {
  return getWithTrace<CallExecution<{ success: boolean; message: string }>>("/health/failure");
}

export function fetchBind1Success() {
  return getWithTrace<CallExecution<Bind1Response>>("/bind1/success");
}

export function fetchBind1Failure() {
  return getWithTrace<CallExecution<Bind1Response>>("/bind1/failure");
}

export function fetchRegister1Success() {
  return getWithTrace<CallExecution<Register1Response>>("/register1/success");
}

export function fetchRegister1Failure() {
  return getWithTrace<CallExecution<Register1Response>>("/register1/failure");
}

export type { TracedResult };
