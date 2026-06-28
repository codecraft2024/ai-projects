"use client";

import { createContext, useContext, useEffect, useMemo, useState } from "react";

export type AppTheme = "sunny" | "candy" | "aurora";

const STORAGE_KEY = "twinzy-theme";
const VALID_THEMES: AppTheme[] = ["sunny", "candy", "aurora"];

interface ThemeContextValue {
  theme: AppTheme;
  setTheme: (theme: AppTheme) => void;
}

const ThemeContext = createContext<ThemeContextValue | null>(null);

export function ThemeProvider({ children }: { children: React.ReactNode }) {
  const [theme, setThemeState] = useState<AppTheme>("sunny");

  useEffect(() => {
    const saved = localStorage.getItem(STORAGE_KEY) as AppTheme | null;
    if (saved && VALID_THEMES.includes(saved)) {
      setThemeState(saved);
    }
  }, []);

  useEffect(() => {
    document.documentElement.setAttribute("data-theme", theme);
    localStorage.setItem(STORAGE_KEY, theme);
  }, [theme]);

  const value = useMemo(
    () => ({
      theme,
      setTheme: setThemeState,
    }),
    [theme],
  );

  return <ThemeContext.Provider value={value}>{children}</ThemeContext.Provider>;
}

export function useTheme() {
  const context = useContext(ThemeContext);
  if (!context) {
    throw new Error("useTheme must be used within ThemeProvider");
  }
  return context;
}
