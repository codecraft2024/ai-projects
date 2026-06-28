"use client";

import { useCallback, useState } from "react";
import Cropper, { type Area } from "react-easy-crop";
import { ZoomIn } from "lucide-react";
import { useLanguage } from "@/lib/i18n/LanguageProvider";
import { blobToFile, getCroppedImage } from "@/lib/crop-image";
import { Button } from "@/components/ui/button";

interface ImageCropModalProps {
  imageSrc: string;
  fileName: string;
  onCancel: () => void;
  onComplete: (file: File, previewUrl: string) => void;
}

export function ImageCropModal({ imageSrc, fileName, onCancel, onComplete }: ImageCropModalProps) {
  const { t } = useLanguage();
  const [crop, setCrop] = useState({ x: 0, y: 0 });
  const [zoom, setZoom] = useState(1.1);
  const [croppedAreaPixels, setCroppedAreaPixels] = useState<Area | null>(null);
  const [isSaving, setIsSaving] = useState(false);

  const onCropComplete = useCallback((_: Area, pixels: Area) => {
    setCroppedAreaPixels(pixels);
  }, []);

  async function handleConfirm() {
    if (!croppedAreaPixels) return;
    setIsSaving(true);
    try {
      const blob = await getCroppedImage(imageSrc, croppedAreaPixels);
      const file = blobToFile(blob, fileName.replace(/\.[^.]+$/, "") + "-cropped.jpg");
      const previewUrl = URL.createObjectURL(blob);
      onComplete(file, previewUrl);
    } finally {
      setIsSaving(false);
    }
  }

  return (
    <div className="fixed inset-0 z-50 flex items-end justify-center bg-black/70 p-0 backdrop-blur-sm sm:items-center sm:p-4">
      <div className="shell-card flex max-h-[100dvh] w-full max-w-2xl flex-col overflow-hidden border-primary bg-card sm:rounded-2xl safe-bottom safe-top">
        <div className="border-b border-border px-4 py-4 sm:px-6">
          <h2 className="display-title text-xl sm:text-2xl">{t("cropTitle")}</h2>
          <p className="mt-1 text-sm font-medium text-muted-foreground">{t("cropSubtitle")}</p>
        </div>

        <div className="relative min-h-[50dvh] flex-1 bg-muted sm:min-h-[20rem] sm:h-80">
          <Cropper
            image={imageSrc}
            crop={crop}
            zoom={zoom}
            aspect={1}
            cropShape="round"
            showGrid
            onCropChange={setCrop}
            onZoomChange={setZoom}
            onCropComplete={onCropComplete}
            onMediaLoaded={() => {
              setCrop({ x: 0, y: 0 });
            }}
          />
        </div>

        <div className="space-y-4 px-4 py-4 sm:px-6">
          <div className="flex items-center gap-3">
            <ZoomIn className="h-4 w-4 shrink-0 text-primary" />
            <input
              type="range"
              min={1}
              max={3}
              step={0.05}
              value={zoom}
              onChange={(event) => setZoom(Number(event.target.value))}
              className="h-10 w-full touch-target accent-primary"
              aria-label={t("cropZoom")}
            />
          </div>

          <div className="flex flex-col-reverse gap-3 sm:flex-row sm:justify-end">
            <Button variant="outline" className="h-12 w-full touch-target sm:w-auto" onClick={onCancel} disabled={isSaving}>
              {t("cropCancel")}
            </Button>
            <Button className="h-12 w-full touch-target sm:w-auto" onClick={handleConfirm} disabled={isSaving || !croppedAreaPixels}>
              {isSaving ? t("cropSaving") : t("cropConfirm")}
            </Button>
          </div>
        </div>
      </div>
    </div>
  );
}
