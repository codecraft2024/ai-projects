import axios, { type AxiosError } from "axios";
import { API_BASE_URL } from "@/lib/catalog";
import { buildTraceFromAxios, type TracedResult } from "@/types/network";

export const apiClient = axios.create({
  baseURL: API_BASE_URL,
  timeout: 120_000,
  headers: {
    Accept: "application/json",
  },
});

export async function getWithTrace<T>(path: string): Promise<TracedResult<T>> {
  const sentAt = new Date();
  try {
    const response = await apiClient.get<T>(path);
    const receivedAt = new Date();
    return {
      data: response.data,
      httpStatus: response.status,
      trace: buildTraceFromAxios(response.config, response, sentAt, receivedAt),
    };
  } catch (error) {
    if (axios.isAxiosError(error) && error.response) {
      const receivedAt = new Date();
      const axiosError = error as AxiosError<T>;
      return {
        data: axiosError.response!.data,
        httpStatus: axiosError.response!.status,
        trace: buildTraceFromAxios(axiosError.config!, axiosError.response!, sentAt, receivedAt),
      };
    }
    throw error;
  }
}
