import axios from "axios";
import { API_BASE_URL } from "@/lib/catalog";

export const apiClient = axios.create({
  baseURL: API_BASE_URL,
  timeout: 30_000,
  headers: {
    Accept: "application/json",
  },
});
