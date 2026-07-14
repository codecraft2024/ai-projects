import { apiClient } from "@/services/api-client";
import type { ScenarioResult } from "@/types";

export async function runBindingScenario(): Promise<{
  data: ScenarioResult;
  status: number;
}> {
  const res = await apiClient.get<ScenarioResult>("/scenarios/binding");
  return { data: res.data, status: res.status };
}
