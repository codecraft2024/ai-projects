"use client";

import { useCallback, useSyncExternalStore } from "react";
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

export function useAuth() {
  const session = useSyncExternalStore(
    subscribeAuth,
    getSession,
    () => null as AuthSession | null,
  );

  const login = useCallback(async (credentials: AuthCredentials): Promise<boolean> => {
    clearSession();
    try {
      await loginWithApi(credentials);
      return true;
    } catch {
      return false;
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
