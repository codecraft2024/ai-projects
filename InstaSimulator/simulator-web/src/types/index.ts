export type ItemStatus = "ready" | "coming-soon";

export interface CatalogItem {
  id: string;
  name: string;
  description: string;
  href: string;
  status: ItemStatus;
}

export interface HealthCheckResponse {
  success: boolean;
  message: string;
}

export interface Bind1Response {
  code: string;
  result: string;
  data?: unknown;
}

export interface Register1Response {
  code: string;
  result: string;
  data?: unknown;
}

export interface CallExecution<T = unknown> {
  request: unknown;
  response: T;
  startedAt: string;
  finishedAt: string;
  durationMs: number;
  timeline?: unknown[];
}

export interface ScenarioStepResult {
  stepName: string;
  status: string;
  success: boolean;
  statusCode: string;
  message: string;
}

export interface ScenarioResult {
  scenarioName: string;
  status: string;
  success: boolean;
  startedAt: string;
  finishedAt: string;
  durationMs: number;
  steps: ScenarioStepResult[];
}

export interface CallMeta {
  id: string;
  name: string;
  description: string;
  method: string;
  successPath: string;
  failurePath: string;
}

export interface ExecutionMetrics {
  durationMs: number;
  requestSize: number;
  responseSize: number;
  httpStatus: number;
  success: boolean;
}
