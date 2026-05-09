"use client";

import Image from "next/image";
import Link from "next/link";
import { useState } from "react";

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

const samplePayload: ReceiptPayload = {
  transactionId: "TXN123456789",
  amount: "48000",
  currency: "EGP",
  from: "masked-from",
  to: "masked-to",
  timestamp: "2026-05-08T14:33:00",
  status: "SUCCESS",
  signature: "digital-signature",
};

export default function Home() {
  const [uploadName, setUploadName] = useState("");
  const [uploadPreview, setUploadPreview] = useState("");
  const [isUploading, setIsUploading] = useState(false);

  const onUploadReceipt = async (event: React.ChangeEvent<HTMLInputElement>) => {
    const file = event.target.files?.[0];
    if (!file) return;

    const randomDelayMs = Math.floor(Math.random() * 3000) + 2000;
    setUploadName(file.name);
    setUploadPreview(URL.createObjectURL(file));
    setIsUploading(true);

    await new Promise((resolve) => {
      window.setTimeout(resolve, randomDelayMs);
    });

    const query = new URLSearchParams(samplePayload).toString();
    window.location.href = `/verify-receipt?${query}`;
  };

  return (
    <div className="min-h-screen bg-gradient-to-b from-[#f7f9ff] to-[#eef3fb] text-[#1d2d45]">
      <main className="animate-fade-slide-in mx-auto flex min-h-screen w-full max-w-5xl flex-col gap-6 px-4 py-8 sm:px-8">
        <header className="animate-fade-slide-in rounded-3xl bg-white p-6 shadow-[0_18px_45px_rgba(19,46,91,0.09)]">
          <div className="flex flex-wrap items-center justify-between gap-4 border-b border-[#edf2fb] pb-4">
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
            <div className="rounded-full border border-[#dce4f1] bg-[#f8faff] px-4 py-1.5 text-xs font-semibold text-[#304b73]">
              Trusted Network
            </div>
          </div>
          <h1 className="mt-4 text-3xl font-bold sm:text-4xl">Receipt Verification Portal</h1>
          <p className="mt-3 text-sm text-[#6d7d98] sm:text-base">
            Upload transaction receipt or follow scanning tutorial to verify QR payment receipts.
          </p>
        </header>

        <section className="grid gap-5 lg:grid-cols-2">
          <article className="animate-fade-slide-in rounded-3xl border border-[#e8edf7] bg-white p-6 shadow-[0_10px_28px_rgba(19,46,91,0.06)]">
            <h2 className="text-lg font-semibold text-[#203658]">Upload Transaction Receipt</h2>
            <p className="mt-2 text-sm text-[#6d7d98]">
              Upload a receipt image to start verification. The app redirects to result view.
            </p>
            <input
              className="mt-4 w-full rounded-xl border border-[#dbe3f0] bg-[#f8faff] p-3 text-sm disabled:opacity-70"
              type="file"
              accept="image/*"
              onChange={onUploadReceipt}
              disabled={isUploading}
            />
            {uploadName ? <p className="mt-3 text-sm font-medium text-[#2a9d55]">Uploaded: {uploadName}</p> : null}
            {isUploading ? (
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
            {uploadPreview ? (
              <div className="relative mt-4 h-64 w-full overflow-hidden rounded-2xl border border-[#edf2fb] bg-[#f8faff]">
                <Image
                  src={uploadPreview}
                  alt="Uploaded transaction receipt preview"
                  fill
                  className="object-contain"
                  unoptimized
                />
              </div>
            ) : null}
          </article>

          <article className="animate-fade-slide-in rounded-3xl border border-[#e8edf7] bg-white p-6 shadow-[0_10px_28px_rgba(19,46,91,0.06)]">
            <h2 className="text-lg font-semibold text-[#203658]">Tutorial: Scan QR from Transaction Screen</h2>
            <ol className="mt-3 list-decimal space-y-2 pl-5 text-sm text-[#5c6f8d]">
              <li>Open your transaction details screen in the payment app.</li>
              <li>Tap the QR icon or Scan to verify.</li>
              <li>Allow camera permission and point to the receipt QR code.</li>
              <li>You will be redirected to this website with receipt parameters.</li>
              <li>The result page validates and displays transaction status instantly.</li>
            </ol>
            <div className="relative mt-4 h-60 overflow-hidden rounded-2xl border border-[#edf2fb] bg-[#f8faff]">
              <Image src="/reference-transaction-screen.png" alt="Transaction details example screen" fill className="object-contain" />
            </div>
          </article>
        </section>

        <footer className="animate-fade-slide-in rounded-3xl border border-[#e8edf7] bg-white px-6 py-5 text-center shadow-[0_10px_30px_rgba(19,46,91,0.06)]">
          <p className="text-sm font-semibold text-[#35527a]">Powered by Secure Network</p>
          <p className="mt-1 text-xs text-[#8d9bb3]">Customer Receipt Validation Portal</p>
        </footer>
      </main>
    </div>
  );
}
