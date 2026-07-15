import { getWithTrace } from "@/services/api-client";
import type { ScenarioResult } from "@/types";
import type { TracedResult } from "@/types/network";

export function runBindingScenario(): Promise<TracedResult<ScenarioResult>> {
  return getWithTrace<ScenarioResult>("/scenarios/binding");
}
