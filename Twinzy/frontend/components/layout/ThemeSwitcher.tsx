"use client";

import { useTheme } from "@/lib/theme/ThemeProvider";
import { useLanguage } from "@/lib/i18n/LanguageProvider";
import { Button } from "@/components/ui/button";

const THEMES = [
  { id: "sunny" as const, emoji: "☀️", labelKey: "themeSunny" as const },
  { id: "candy" as const, emoji: "🍬", labelKey: "themeCandy" as const },
  { id: "aurora" as const, emoji: "🌌", labelKey: "themeAurora" as const },
];

export function ThemeSwitcher() {
  const { theme, setTheme } = useTheme();
  const { t } = useLanguage();

  return (
    <div className="flex items-center gap-1 rounded-full border-2 border-foreground/20 bg-card p-1">
      {THEMES.map((item) => (
        <Button
          key={item.id}
          size="sm"
          variant={theme === item.id ? "default" : "ghost"}
          className="h-8 rounded-full px-3 text-xs"
          onClick={() => setTheme(item.id)}
          title={t(item.labelKey)}
        >
          {item.emoji}
        </Button>
      ))}
    </div>
  );
}
