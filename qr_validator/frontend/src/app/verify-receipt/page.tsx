"use client";

import { useEffect, useMemo, useState } from "react";
import Link from "next/link";
import { useSearchParams } from "next/navigation";

type ReceiptPayload = {
  transactionId: string;
  amount: string;
  currency: string;
  from: string;
  to: string;
  timestamp: string;
  status: string;
  signature: string;
};

type VerifyResponse = {
  valid: boolean;
  message: string;
  payload: ReceiptPayload;
};

const defaultPayload: ReceiptPayload = {
  transactionId: "TXN123456789",
  amount: "48000",
  currency: "EGP",
  from: "masked-from",
  to: "masked-to",
  timestamp: "2026-05-08T14:33:00",
  status: "SUCCESS",
  signature: "digital-signature",
};

const payloadKeys: Array<keyof ReceiptPayload> = [
  "transactionId",
  "amount",
  "currency",
  "from",
  "to",
  "timestamp",
  "status",
  "signature",
];

function mapQueryToPayload(params: URLSearchParams): ReceiptPayload {
  const queryData: Partial<ReceiptPayload> = {};

  for (const [rawKey, value] of params.entries()) {
    const key = rawKey.replace(/^\?+/, "") as keyof ReceiptPayload;
    if (payloadKeys.includes(key)) {
      queryData[key] = value;
    }
  }

  return { ...defaultPayload, ...queryData };
}

export default function VerifyReceiptPage() {
  const searchParams = useSearchParams();
  const [loading, setLoading] = useState(true);
  const [result, setResult] = useState<VerifyResponse | null>(null);
  const [error, setError] = useState("");

  const payloadFromLink = useMemo(() => mapQueryToPayload(searchParams), [searchParams]);

  useEffect(() => {
    let active = true;

    async function verifyFromLink() {
      setLoading(true);
      setError("");
      setResult(null);

      try {
        const randomDelayMs = Math.floor(Math.random() * 3000) + 2000;
        const query = new URLSearchParams(payloadFromLink).toString();
        const response = await fetch(`https://localhost:8080/verify-receipt?${query}`);

        if (!response.ok) {
          throw new Error(`Validation request failed with status ${response.status}`);
        }

        const data = (await response.json()) as VerifyResponse;
        await new Promise((resolve) => {
          window.setTimeout(resolve, randomDelayMs);
        });

        if (active) {
          setResult(data);
        }
      } catch {
        if (active) {
          setError("Could not verify this transaction right now. Please check backend service and try again.");
        }
      } finally {
        if (active) {
          setLoading(false);
        }
      }
    }

    void verifyFromLink();
    return () => {
      active = false;
    };
  }, [payloadFromLink]);

  return (
    <div className="min-h-screen bg-gradient-to-b from-[#f7f9ff] to-[#eef3fb] px-4 py-8 text-[#1d2d45] sm:px-8">
      <main className="animate-fade-slide-in mx-auto flex w-full max-w-3xl flex-col gap-6">
        <header className="animate-fade-slide-in rounded-3xl bg-white p-6 shadow-[0_18px_45px_rgba(19,46,91,0.09)]">
          <div className="flex items-center justify-between border-b border-[#edf2fb] pb-4">
            <Link href="/" className="flex items-center gap-3 rounded-xl transition hover:opacity-90">
              <div className="relative h-11 w-11">
                <div className="absolute left-0 top-0 h-7 w-7 rounded-full bg-[#1f6bff]" />
                <div className="absolute right-0 top-1 h-7 w-7 rounded-full bg-[#18d7cf]" />
                <div className="absolute bottom-0 left-2 h-7 w-7 rounded-full bg-[#ff7a22]" />
                <div className="absolute inset-0 grid place-items-center text-xs font-extrabold text-white">V</div>
              </div>
              <div>
                <p className="text-lg font-extrabold tracking-tight text-[#216dff]">VeriPay</p>
                <p className="text-[10px] font-semibold uppercase tracking-[0.14em] text-[#8d9bb3]">
                  Secure Payment Check
                </p>
              </div>
            </Link>
            <span className="rounded-full border border-[#dce4f1] bg-[#f8faff] px-4 py-1.5 text-xs font-semibold text-[#304b73]">
              Trusted Network
            </span>
          </div>
          <h1 className="mt-4 text-3xl font-bold">QR Transaction Verification</h1>
          <p className="mt-2 text-sm text-[#6d7d98]">
            Redirect transaction is validated automatically and shown below.
          </p>
        </header>

        <section className="animate-fade-slide-in rounded-3xl border border-[#e8edf7] bg-white p-6 shadow-[0_10px_28px_rgba(19,46,91,0.06)]">
          <h2 className="text-lg font-semibold text-[#203658]">Result</h2>
          {loading ? (
            <div className="mt-3 flex items-center gap-2 text-sm text-[#216dff]">
              <span className="inline-block h-4 w-4 animate-spin rounded-full border-2 border-[#216dff] border-t-transparent" />
              <span className="font-medium">Waiting to verify</span>
              <span className="flex items-center gap-1">
                <span className="h-1.5 w-1.5 animate-bounce rounded-full bg-[#216dff]" />
                <span className="h-1.5 w-1.5 animate-bounce rounded-full bg-[#216dff]" style={{ animationDelay: "120ms" }} />
                <span className="h-1.5 w-1.5 animate-bounce rounded-full bg-[#216dff]" style={{ animationDelay: "240ms" }} />
              </span>
            </div>
          ) : null}
          {error ? <p className="mt-3 text-sm font-medium text-[#c93d2e]">{error}</p> : null}
          {result ? (
            <div className="mt-3 space-y-3">
              <div className={`rounded-2xl border p-4 ${result.valid ? "border-[#d9f0e1] bg-[#f2fcf6]" : "border-[#f8d7d2] bg-[#fff5f3]"}`}>
                <p className={`text-base font-semibold ${result.valid ? "text-[#1f9d55]" : "text-[#c93d2e]"}`}>
                  {result.valid ? "Verified Successfully" : "Verification Failed"}
                </p>
                <p className="mt-1 text-sm text-[#5e7190]">{result.message}</p>
              </div>
              <div className="grid gap-3 sm:grid-cols-2">
                <div className="rounded-xl bg-[#f8faff] p-3 text-sm"><span className="text-[#8d9bb3]">Transaction ID:</span> {result.payload.transactionId}</div>
                <div className="rounded-xl bg-[#f8faff] p-3 text-sm"><span className="text-[#8d9bb3]">Amount:</span> {result.payload.amount} {result.payload.currency}</div>
                <div className="rounded-xl bg-[#f8faff] p-3 text-sm"><span className="text-[#8d9bb3]">From:</span> {result.payload.from}</div>
                <div className="rounded-xl bg-[#f8faff] p-3 text-sm"><span className="text-[#8d9bb3]">To:</span> {result.payload.to}</div>
                <div className="rounded-xl bg-[#f8faff] p-3 text-sm"><span className="text-[#8d9bb3]">Time:</span> {result.payload.timestamp}</div>
                <div className="rounded-xl bg-[#f8faff] p-3 text-sm"><span className="text-[#8d9bb3]">Status:</span> {result.payload.status}</div>
              </div>
            </div>
          ) : null}
        </section>

        <footer className="animate-fade-slide-in rounded-3xl border border-[#e8edf7] bg-white px-6 py-5 text-center shadow-[0_10px_30px_rgba(19,46,91,0.06)]">
          <p className="text-sm font-semibold text-[#35527a]">Powered by Secure Network</p>
          <p className="mt-1 text-xs text-[#8d9bb3]">Customer Receipt Validation Portal</p>
        </footer>
      </main>
    </div>
  );
}
