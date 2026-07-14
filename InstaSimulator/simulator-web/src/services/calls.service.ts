import { apiClient } from "@/services/api-client";
import type { Bind1Response, CallExecution, HealthCheckResponse, Register1Response } from "@/types";

export async function fetchHealthSuccess() {
  const res = await apiClient.get<HealthCheckResponse>("/health/success");
  return { data: res.data, status: res.status };
}

export async function fetchHealthFailure() {
  const res = await apiClient.get<HealthCheckResponse>("/health/failure");
  return { data: res.data, status: res.status };
}

export async function fetchBind1Success() {
  const res = await apiClient.get<CallExecution<Bind1Response>>("/bind1/success");
  return { data: res.data, status: res.status };
}

export async function fetchBind1Failure() {
  const res = await apiClient.get<CallExecution<Bind1Response>>("/bind1/failure");
  return { data: res.data, status: res.status };
}

export async function fetchRegister1Success() {
  const res = await apiClient.get<CallExecution<Register1Response>>("/register1/success");
  return { data: res.data, status: res.status };
}

export async function fetchRegister1Failure() {
  const res = await apiClient.get<CallExecution<Register1Response>>("/register1/failure");
  return { data: res.data, status: res.status };
}
