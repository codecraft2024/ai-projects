"use client";

import { useCallback, useSyncExternalStore } from "react";
import { ApiError } from "@/services/api.client";
import type { AuthCredentials, AuthSession } from "@/types/admin";
import {
  subscribeAuth,
  getSession,
  clearSession,
  initSession,
  loginWithApi,
} from "@/services/auth.service";

if (typeof window !== "undefined") {
  initSession();
}

export type LoginResult = "success" | "invalid" | "network";

export type LoginResponse =
  | { result: "success" }
  | { result: "invalid" }
  | { result: "network"; message?: string };

export function useAuth() {
  const session = useSyncExternalStore(
    subscribeAuth,
    getSession,
    () => null as AuthSession | null,
  );

  const login = useCallback(async (credentials: AuthCredentials): Promise<LoginResponse> => {
    clearSession();
    try {
      await loginWithApi({
        username: credentials.username.trim(),
        password: credentials.password.trim(),
      });
      return { result: "success" };
    } catch (err) {
      if (err instanceof ApiError && err.status === 401) {
        return { result: "invalid" };
      }
      if (err instanceof ApiError) {
        return { result: "network", message: err.message };
      }
      return { result: "network" };
    }
  }, []);

  const logout = useCallback(() => {
    clearSession();
  }, []);

  return {
    session,
    isAuthenticated: !!session?.token,
    isLoading: false,
    login,
    logout,
  };
}
