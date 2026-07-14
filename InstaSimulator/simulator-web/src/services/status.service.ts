import { apiClient } from "@/services/api-client";

export type BackendStatus = "online" | "offline" | "checking";

export async function fetchBackendStatus(): Promise<BackendStatus> {
  try {
    await apiClient.get("/actuator/health", { timeout: 4000 });
    return "online";
  } catch {
    try {
      await apiClient.get("/health/success", { timeout: 4000 });
      return "online";
    } catch {
      return "offline";
    }
  }
}
