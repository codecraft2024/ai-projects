"use client";

import { useState, useEffect, FormEvent } from "react";
import { useTranslations } from "next-intl";
import { Link, useRouter } from "@/i18n/navigation";
import { useAuth } from "@/hooks/useAuth";
import { Button } from "@/components/ui/Button";
import { Logo } from "@/components/ui/Logo";
import { LanguageSwitcher } from "@/components/layout/LanguageSwitcher";

export default function AdminLoginPage() {
  const t = useTranslations("admin");
  const router = useRouter();
  const { login, isAuthenticated } = useAuth();
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState<string | null>(null);
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    if (isAuthenticated) {
      router.replace("/admin/patients");
    }
  }, [isAuthenticated, router]);

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    setError(null);
    setSubmitting(true);

    const success = await login({ username, password });
    if (success) {
      router.push("/admin/patients");
    } else {
      setError(t("invalidCredentials"));
      setSubmitting(false);
    }
  };

  return (
    <div className="flex min-h-screen flex-col bg-gradient-to-br from-[#1a1625] via-[#2d1f6b] to-slate-900">
      <div className="absolute end-4 top-4">
        <LanguageSwitcher />
      </div>
      <div className="flex flex-1 items-center justify-center px-4 py-12">
        <div className="w-full max-w-md">
          <div className="mb-8 text-center">
            <Link href="/" className="inline-flex justify-center">
              <Logo height={72} priority />
            </Link>
            <p className="mt-4 text-brand-light">{t("signIn")}</p>
          </div>

          <div className="card-premium p-6 sm:p-8">
            <p className="text-sm text-slate-500">{t("demoNote")}</p>
            <form
              onSubmit={handleSubmit}
              className="mt-6 space-y-4"
              autoComplete="off"
            >
              {error && (
                <div role="alert" className="rounded-lg border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-800">
                  {error}
                </div>
              )}
              <label className="block">
                <span className="text-sm font-medium text-slate-700">{t("username")}</span>
                <input
                  type="text"
                  name="username"
                  value={username}
                  onChange={(e) => setUsername(e.target.value)}
                  required
                  autoComplete="off"
                  className="mt-1.5 w-full rounded-xl border border-[var(--border)] px-4 py-3 focus:border-brand focus:ring-2 focus:ring-brand/20"
                />
              </label>
              <label className="block">
                <span className="text-sm font-medium text-slate-700">{t("password")}</span>
                <input
                  type="password"
                  name="password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  required
                  autoComplete="off"
                  className="mt-1.5 w-full rounded-xl border border-[var(--border)] px-4 py-3 focus:border-brand focus:ring-2 focus:ring-brand/20"
                />
              </label>
              <Button type="submit" variant="primary" size="lg" className="w-full" disabled={submitting}>
                {submitting ? t("signingIn") : t("signInButton")}
              </Button>
            </form>
            <p className="mt-6 text-center text-xs text-slate-400">
              {t("demoCredentials")}: admin / admin
            </p>
          </div>

          <p className="mt-6 text-center">
            <Link href="/" className="text-sm text-brand-light hover:text-white">
              ← {t("backToSite")}
            </Link>
          </p>
        </div>
      </div>
    </div>
  );
}
