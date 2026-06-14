"use client";

import { useMutation } from "@tanstack/react-query";
import { Camera, Loader2, Upload } from "lucide-react";
import Image from "next/image";
import { useRouter } from "next/navigation";
import { useRef, useState } from "react";
import { ImageCropModal } from "@/components/search/ImageCropModal";
import { searchByImage } from "@/lib/api-client";
import { useLanguage } from "@/lib/i18n/LanguageProvider";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";

const SEARCH_SESSION_KEY = "twinzy-search-session";

export function ImageUploadForm() {
  const router = useRouter();
  const { t } = useLanguage();
  const inputRef = useRef<HTMLInputElement>(null);
  const [preview, setPreview] = useState<string | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [cropSource, setCropSource] = useState<{ src: string; fileName: string } | null>(null);

  const mutation = useMutation({
    mutationFn: searchByImage,
    onSuccess: (data) => {
      const payload = {
        sessionId: data.sessionId,
        funnyMatch: data.funnyMatch,
        humanMatches: data.humanMatches,
        userImageDataUrl: data.userImageDataUrl ?? preview,
      };
      sessionStorage.setItem(SEARCH_SESSION_KEY, JSON.stringify(payload));
      router.push(`/results?sessionId=${data.sessionId}`);
    },
    onError: (err: Error) => {
      setError(err.message);
    },
  });

  function openCropper(file: File) {
    setError(null);
    const objectUrl = URL.createObjectURL(file);
    setCropSource({ src: objectUrl, fileName: file.name });
  }

  function handleFile(file: File | null) {
    if (!file) return;
    if (!file.type.startsWith("image/")) {
      setError(t("uploadInvalidType"));
      return;
    }
    openCropper(file);
  }

  function handleCropComplete(file: File, previewUrl: string) {
    if (cropSource?.src) {
      URL.revokeObjectURL(cropSource.src);
    }
    setCropSource(null);
    setPreview(previewUrl);
    mutation.mutate(file);
  }

  function handleCropCancel() {
    if (cropSource?.src) {
      URL.revokeObjectURL(cropSource.src);
    }
    setCropSource(null);
    if (inputRef.current) {
      inputRef.current.value = "";
    }
  }

  return (
    <>
      {cropSource ? (
        <ImageCropModal
          imageSrc={cropSource.src}
          fileName={cropSource.fileName}
          onCancel={handleCropCancel}
          onComplete={handleCropComplete}
        />
      ) : null}

      <Card className="shell-card mx-auto max-w-2xl border-primary/40">
        <CardHeader className="text-center">
          <CardTitle className="display-title text-3xl">{t("uploadTitle")}</CardTitle>
          <CardDescription className="text-base font-medium">{t("uploadSubtitle")}</CardDescription>
        </CardHeader>
        <CardContent className="space-y-6">
          <div
            className="flex min-h-64 cursor-pointer flex-col items-center justify-center rounded-3xl border-4 border-dashed border-primary/50 bg-muted/60 p-8 transition hover:scale-[1.01] hover:bg-muted"
            onClick={() => !mutation.isPending && inputRef.current?.click()}
            onDragOver={(event) => event.preventDefault()}
            onDrop={(event) => {
              event.preventDefault();
              handleFile(event.dataTransfer.files[0] ?? null);
            }}
          >
            {preview ? (
              <div className="relative h-48 w-48 overflow-hidden rounded-full border-4 border-foreground shadow-[6px_6px_0_0_hsl(var(--primary))]">
                <Image src={preview} alt="Preview" fill className="object-cover" unoptimized />
              </div>
            ) : (
              <>
                <Upload className="mb-4 h-12 w-12 text-primary" />
                <p className="text-lg font-bold">{t("uploadDrop")}</p>
                <p className="mt-2 text-sm font-medium text-muted-foreground">{t("uploadHint")}</p>
              </>
            )}
          </div>

          <input
            ref={inputRef}
            type="file"
            accept="image/*"
            className="hidden"
            onChange={(event) => handleFile(event.target.files?.[0] ?? null)}
          />

          <div className="flex flex-col gap-3 sm:flex-row sm:justify-center">
            <Button
              size="lg"
              onClick={() => inputRef.current?.click()}
              disabled={mutation.isPending || Boolean(cropSource)}
            >
              {mutation.isPending ? (
                <>
                  <Loader2 className="h-4 w-4 animate-spin" />
                  {t("uploadAnalyzing")}
                </>
              ) : (
                <>
                  <Camera className="h-4 w-4" />
                  {t("uploadChoose")}
                </>
              )}
            </Button>
          </div>

          {error ? <p className="text-center text-sm font-semibold text-red-600">{error}</p> : null}
        </CardContent>
      </Card>
    </>
  );
}

export { SEARCH_SESSION_KEY };
