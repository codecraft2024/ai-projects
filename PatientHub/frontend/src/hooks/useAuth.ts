"use client";

import { useCallback, useSyncExternalStore } from "react";
import type { AuthCredentials, AuthSession } from "@/types/admin";
import {
  subscribeAuth,
  getSession,
  saveSession,
  clearSession,
  validateCredentials,
  initSession,
} from "@/services/auth.service";

if (typeof window !== "undefined") {
  initSession();
}

export function useAuth() {
  const session = useSyncExternalStore(
      subscribeAuth,
      getSession,
      () => null as AuthSession | null
  );

  const login = useCallback((credentials: AuthCredentials): boolean => {
    const isValid = validateCredentials(credentials);

    if (isValid) {
      saveSession(credentials.username); // already notifies internally
    }

    return isValid;
  }, []);

  const logout = useCallback(() => {
    clearSession(); // already notifies internally
  }, []);

  return {
    session,
    isAuthenticated: !!session,
    isLoading: false,
    login,
    logout,
  };
}